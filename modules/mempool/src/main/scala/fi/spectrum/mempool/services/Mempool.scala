package fi.spectrum.mempool.services

import cats.Monad
import cats.effect.Ref
import cats.effect.kernel.Sync
import cats.syntax.show._
import cats.syntax.traverse._
import derevo.derive
import fi.spectrum.core.domain.address._
import fi.spectrum.core.domain.analytics.Processed
import fi.spectrum.core.domain.order.OrderStatus
import fi.spectrum.core.domain.order.OrderStatus._
import fi.spectrum.core.domain.pool.Pool
import fi.spectrum.core.domain.{Address, BoxId}
import fi.spectrum.mempool.cache.RedisCache
import fi.spectrum.mempool.config.MempoolConfig
import fi.spectrum.mempool.v1.models.AddressResponse
import io.circe.parser.decode
import io.circe.syntax._
import org.ergoplatform.ErgoAddressEncoder
import tofu.higherKind.Mid
import tofu.higherKind.derived.representableK
import tofu.logging.Logging
import tofu.logging.derivation.loggable
import tofu.syntax.logging._
import tofu.syntax.monadic._

@derive(representableK)
trait Mempool[F[_]] {

  def getOrder(id: List[BoxId]): F[List[Processed.Any]]

  def getPool(id: List[BoxId]): F[Option[Pool]]

  def put(pool: Option[Pool], order: List[Processed.Any]): F[Unit]

  def del(pool: Option[Pool], processed: List[Processed.Any]): F[Unit]

  def getOrders(addresses: List[Address]): F[List[AddressResponse]]
}

object Mempool {

  @derive(loggable)
  final case class MempoolKey(address: String, orderId: String, orderType: String) {
    def redisKey = s"${address}_${orderId}_$orderType"
  }

  object MempoolKey {

    def apply(order: Processed.Any): MempoolKey =
      MempoolKey(order.order.redeemer.show, order.order.id.value, mapToMempool(order.state.status).entryName)
  }

  private def redisKey(processed: Processed.Any): String =
    s"${processed.order.redeemer.show}_${processed.order.id}_${mapToMempool(processed.state.status).entryName}"

  def make[F[_]: Sync: MempoolConfig.Has](implicit
    redis: RedisCache[F],
    e: ErgoAddressEncoder,
    logs: Logging.Make[F]
  ): F[Mempool[F]] =
    for {
      ref  <- Ref.of[F, Map[MempoolKey, Processed.Any]](Map.empty)
      ref2 <- Ref.of[F, Map[String, Pool]](Map.empty)
      implicit0(logging: Logging[F]) = logs.forService[Mempool[F]]
      config <- MempoolConfig.access
      keys   <- redis.keys("*")
      (ordersK, poolsK) =
        keys.toSet[String].foldLeft(List.empty[MempoolKey], List.empty[String]) { case ((acc1, acc2), next) =>
          next.split("_").toList match {
            case address :: orderId :: orderType :: Nil => (MempoolKey(address, orderId, orderType) :: acc1) -> acc2
            case head :: Nil                            => acc1                                              -> (head :: acc2)
            case _                                      => acc1                                              -> acc2
          }
        }
      orders <- ordersK
                  .traverse(k => redis.get(k.redisKey))
                  .map(_.flatten)
                  .map(_.flatMap(str => decode[Processed.Any](str).toOption))
      pools <- poolsK.traverse(redis.get).map(_.flatten).map(_.flatMap(str => decode[Pool](str).toOption))
      _     <- ref.set(orders.map(o => MempoolKey(o) -> o).toMap)
      _     <- ref2.set(pools.map(o => o.box.boxId.value -> o).toMap)
    } yield new Tracing[F] attach new Live[F](config, ref, ref2)

  final private class Live[F[_]: Monad: Logging](
    config: MempoolConfig,
    orders: Ref[F, Map[MempoolKey, Processed.Any]],
    pools: Ref[F, Map[String, Pool]]
  )(implicit
    redis: RedisCache[F],
    e: ErgoAddressEncoder
  ) extends Mempool[F] {

    def getOrders(addresses: List[Address]): F[List[AddressResponse]] =
      orders.get.map { orders =>
        addresses
          .map(a => a -> formRedeemer(a))
          .collect { case (a, Some(r)) => a -> r }
          .map { case (a, r) =>
            AddressResponse(a, orders.filter(_._1.address == r.show).values.toList)
          }
      }

    def del(pool: Option[Pool], processed: List[Processed.Any]): F[Unit] = {
      def delPool: Option[(String, String)] = pool.map(pool => mkDelScript(1) -> poolKey(pool.box.boxId.value))
      val scripts = (delPool, processed) match {
        case (Some(value), x :: Nil)       => List(value, mkDelScript(2) -> redisKey(x))
        case (Some(value), x :: xs :: Nil) => List(value, mkDelScript(2) -> redisKey(x), mkDelScript(3) -> redisKey(xs))
        case (None, x :: Nil)              => List(mkDelScript(1) -> redisKey(x))
        case (None, x :: xs :: Nil)        => List(mkDelScript(1) -> redisKey(x), mkDelScript(2) -> redisKey(xs))
        case (Some(value), _)              => List(value)
        case _                             => List.empty
      }

      val script = scripts.map(_._1).fold("")(_ ++ _)
      for {
        _ <- trace"Logging script del: $script, ${scripts.map(_._2)}, List.empty"
        _ <- Monad[F].whenA(script.nonEmpty)(redis.eval(script, scripts.map(_._2), List.empty))
        _ <- processed.traverse(order => orders.update(_ - MempoolKey(order)))
        _ <- pool.traverse(pool => pools.update(_ - poolKey(pool.box.boxId.value)))
      } yield ()
    }

    def put(pool: Option[Pool], order: List[Processed.Any]): F[Unit] = {
      final case class ExKey(script: String, key: String, values: List[String])
      object ExKey {
        def apply(i1: Int, i2: Int, i3: Int, order: Processed.Any): ExKey =
          ExKey(mkExScript(i1, i2, i3), redisKey(order), List(order.asJson.noSpaces, s"${config.ttl.toSeconds}"))

        def apply(i1: Int, i2: Int, i3: Int, pool: Pool): ExKey =
          ExKey(
            mkExScript(i1, i2, i3),
            poolKey(pool.box.boxId.value),
            List(pool.asJson.noSpaces, s"${config.ttl.toSeconds}")
          )
      }

      val elems = (pool.map(pool => ExKey(1, 1, 2, pool)), order) match {
        case (Some(p), x :: Nil)       => List(p, ExKey(2, 3, 4, x))
        case (Some(p), x :: xs :: Nil) => List(p, ExKey(2, 3, 4, x), ExKey(3, 5, 6, xs))
        case (None, x :: Nil)          => List(ExKey(1, 1, 2, x))
        case (None, x :: xs :: Nil)    => List(ExKey(1, 1, 2, x), ExKey(2, 3, 4, xs))
        case (Some(p), Nil)            => List(p)
        case _                         => List.empty
      }

      val script = elems.map(_.script).fold("")(_ ++ _)

      for {
        _ <- trace"Logging script put: $script, ${elems.map(_.key)}, ${elems.flatMap(_.values)}"
        _ <- Monad[F].whenA(script.nonEmpty)(redis.eval(script, elems.map(_.key), elems.flatMap(_.values)))
        _ <- order.traverse(order => orders.update(_ ++ Map(MempoolKey(order) -> order)))
        _ <- pool.traverse(pool => pools.update(_ ++ Map(poolKey(pool.box.boxId.value) -> pool)))
      } yield ()
    }

    def getOrder(id: List[BoxId]): F[List[Processed.Any]] =
      orders.get.map(_.filter { case (key, _) =>
        id.contains(BoxId(key.orderId)) && key.orderType == mapToMempool(OrderStatus.WaitingRegistration).entryName
      }.values.toList)

    def getPool(id: List[BoxId]): F[Option[Pool]] =
      pools.get.map { pools =>
        id.map(id => poolKey(id.value))
          .flatMap(pools.get)
          .headOption
      }

    private def poolKey(poolBoxId: String): String =
      s"pool_$poolBoxId"

    private def mkDelScript(index: Int) = s"""redis.call('del', KEYS[$index]);"""

    private def mkExScript(indexKey: Int, indexValue: Int, indexEx: Int) =
      s"""redis.call('set', KEYS[$indexKey], ARGV[$indexValue], "EX", ARGV[$indexEx]);"""

  }

  final private class Tracing[F[_]: Monad: Logging] extends Mempool[Mid[F, *]] {

    def getOrder(id: List[BoxId]): Mid[F, List[Processed.Any]] =
      for {
        _ <- info"getOrderById($id)"
        r <- _
        _ <- info"getOrderById($id) -> ${r.map(_.order.id)}"
      } yield r

    def getPool(id: List[BoxId]): Mid[F, Option[Pool]] =
      for {
        _ <- info"getPool($id)"
        r <- _
        _ <- info"getPool($id) -> ${r.map(_.box.boxId)}"
      } yield r

    def put(pool: Option[Pool], order: List[Processed.Any]): Mid[F, Unit] =
      for {
        _ <- info"put(${pool.map(_.box.boxId)}, ${order.map(_.order.id)})"
        r <- _
        _ <- info"put(${pool.map(_.box.boxId)}, ${order.map(_.order.id)}) -> finish"
      } yield r

    def del(pool: Option[Pool], processed: List[Processed.Any]): Mid[F, Unit] =
      for {
        _ <- info"del(${pool.map(_.box.boxId)}, ${processed.map(_.order.id)})"
        r <- _
        _ <- info"del(${pool.map(_.box.boxId)}, ${processed.map(_.order.id)}) -> finish"
      } yield r

    def getOrders(addresses: List[Address]): Mid[F, List[AddressResponse]] =
      for {
        _ <- info"getOrders($addresses)"
        r <- _
        _ <- info"getOrders($addresses) -> ${r.map(s => s"${s.address} -> ${s.orders.map(_.order.id)}")}"
      } yield r
  }

}
