package fi.spectrum.mempool.services

import cats.Monad
import cats.effect.Ref
import cats.effect.kernel.Sync
import cats.syntax.show._
import cats.syntax.traverse._
import cats.syntax.option._
import derevo.derive
import fi.spectrum.core.domain.{Address, BoxId}
import fi.spectrum.core.domain.analytics.Processed
import fi.spectrum.core.domain.order.OrderStatus
import fi.spectrum.core.domain.order.OrderStatus._
import fi.spectrum.core.domain.pool.Pool
import fi.spectrum.mempool.cache.RedisCache
import fi.spectrum.mempool.config.MempoolConfig
import fi.spectrum.mempool.v1.models.AddressResponse
import io.circe.syntax._
import tofu.higherKind.Mid
import tofu.higherKind.derived.representableK
import tofu.logging.Logging
import tofu.syntax.logging._
import tofu.syntax.monadic._
import io.circe.parser.decode
import org.ergoplatform.ErgoAddressEncoder
import fi.spectrum.core.domain.address._
import tofu.logging.derivation.loggable

@derive(representableK)
trait Mempool[F[_]] {

  def getOrder(id: List[BoxId]): F[List[Processed.Any]]

  def getPool(id: List[BoxId]): F[Option[Pool]]

  def put(pool: Option[Pool], order: List[Processed.Any]): F[Unit]

  def del(pool: Option[Pool], order: List[Processed.Any]): F[Unit]

  def getOrders(addresses: List[Address]): F[List[AddressResponse]]
}

object Mempool {

  @derive(loggable)
  final case class MempoolKey(address: String, orderId: String, orderType: String) {
    def redisKey = s"${address}_${orderId}_$orderType"
  }

  private def orderKey(processed: Processed.Any): String =
    s"${processed.order.redeemer.show}_${processed.order.id}_${mapToMempool(processed.state.status).entryName}"

  private def mkMempoolKey(order: Processed.Any): MempoolKey =
    MempoolKey(order.order.redeemer.show, order.order.id.value, mapToMempool(order.state.status).entryName)

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
      _     <- ref.set(orders.map(o => mkMempoolKey(o) -> o).toMap)
      _     <- ref2.set(pools.map(o => o.box.boxId.value -> o).toMap)
    } yield new Tracing[F] attach new Live[F](config, ref, ref2)

  final private class Live[F[_]: Monad](
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

    def del(pool: Option[Pool], order: List[Processed.Any]): F[Unit] = {
      def delPool = pool.map(pool => redis.del(poolKey(pool.box.boxId.value)))
      val (delOrder1, delOrder2) = order match {
        case x :: Nil       => (redis.del(orderKey(x)).some, none)
        case x :: xs :: Nil => (redis.del(orderKey(x)).some, redis.del(orderKey(xs)).some)
        case _              => (none, none)
      }
      order.map(order => redis.del(orderKey(order)))
      if (delPool.isEmpty && delOrder1.isEmpty && delOrder2.isEmpty) unit
      else {
        for {
          _ <- redis.transact_(List(delPool.sequence.void, delOrder1.sequence.void, delOrder2.sequence.void))
          _ <- order.traverse(order => orders.update(_ - mkMempoolKey(order)))
          _ <- pool.traverse(pool => pools.update(_ - poolKey(pool.box.boxId.value)))
        } yield ()
      }
    }

    def put(pool: Option[Pool], order: List[Processed.Any]): F[Unit] = {
      def putPool = pool.map(pool => redis.setEx(poolKey(pool.box.boxId.value), pool.asJson.noSpaces, config.ttl))

      val (putOrder1, putOrder2) = order match {
        case x :: Nil => (redis.setEx(orderKey(x), x.asJson.noSpaces, config.ttl).some, none)
        case x :: xs :: Nil =>
          (
            redis.setEx(orderKey(x), x.asJson.noSpaces, config.ttl).some,
            redis.setEx(orderKey(xs), xs.asJson.noSpaces, config.ttl).some
          )
        case _ => (none, none)
      }
      if (putPool.isEmpty && putOrder1.isEmpty && putOrder2.isEmpty) unit
      else
        for {
          _ <- redis.transact_(List(putPool.sequence.void, putOrder1.sequence.void, putOrder2.sequence.void))
          _ <- order.traverse(order => orders.update(_ ++ Map(mkMempoolKey(order) -> order)))
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

    def del(pool: Option[Pool], order: List[Processed.Any]): Mid[F, Unit] =
      for {
        _ <- info"del(${pool.map(_.box.boxId)}, ${order.map(_.order.id)})"
        r <- _
        _ <- info"del(${pool.map(_.box.boxId)}, ${order.map(_.order.id)}) -> finish"
      } yield r

    def getOrders(addresses: List[Address]): Mid[F, List[AddressResponse]] =
      for {
        _ <- info"getOrders($addresses)"
        r <- _
        _ <- info"getOrders($addresses) -> ${r.map(s => s"${s.address} -> ${s.orders.map(_.order.id)}")}"
      } yield r
  }

}
