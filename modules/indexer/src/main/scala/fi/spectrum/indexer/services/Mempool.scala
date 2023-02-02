package fi.spectrum.indexer.services

import cats.Monad
import cats.effect.Ref
import cats.effect.kernel.Sync
import cats.syntax.show._
import cats.syntax.traverse._
import derevo.derive
import fi.spectrum.core.domain.BoxId
import fi.spectrum.core.domain.analytics.Processed
import fi.spectrum.core.domain.order.OrderStatus
import fi.spectrum.core.domain.order.OrderStatus._
import fi.spectrum.core.domain.pool.Pool
import fi.spectrum.indexer.config.MempoolConfig
import fi.spectrum.indexer.db.local.storage.RedisCache
import io.circe.parser.decode
import io.circe.syntax._
import tofu.higherKind.Mid
import tofu.higherKind.derived.representableK
import tofu.logging.Logging
import tofu.syntax.logging._
import tofu.syntax.monadic._

@derive(representableK)
trait Mempool[F[_]] {

  def getOrder(id: List[BoxId]): F[Option[Processed.Any]]

  def getPool(id: List[BoxId]): F[Option[Pool]]

  def put(pool: Option[Pool], order: Option[Processed.Any]): F[Unit]

  def del(pool: Option[Pool], order: Option[Processed.Any]): F[Unit]
}

object Mempool {

  final case class MempoolKey(address: String, orderId: String, orderType: String)

  def make[F[_]: Sync: MempoolConfig.Has](implicit
    redis: RedisCache[F],
    logs: Logging.Make[F]
  ): F[Mempool[F]] =
    for {
      ref <- Ref.of[F, Set[MempoolKey]](Set.empty)
      implicit0(logging: Logging[F]) = logs.forService[Mempool[F]]
      config <- MempoolConfig.access
      keys   <- redis.keys("*")
      elems = keys.toSet[String].flatMap { str =>
                str.split("_").toList match {
                  case address :: orderId :: orderType :: Nil => Some(MempoolKey(address, orderId, orderType))
                  case _                                      => None
                }
              }
      _ <- ref.set(elems)
    } yield new Tracing[F] attach new Live[F](config, ref)

  final private class Live[F[_]: Monad](config: MempoolConfig, keys: Ref[F, Set[MempoolKey]])(implicit
    redis: RedisCache[F]
  ) extends Mempool[F] {

    def del(pool: Option[Pool], order: Option[Processed.Any]): F[Unit] = {
      def delPool  = pool.map(pool => redis.del(poolKey(pool.box.boxId.value)))
      def delOrder = order.map(order => redis.del(orderKey(order)))
      if (delPool.isEmpty && delOrder.isEmpty) unit
      else
        redis.transact_(List(delPool.sequence.void, delOrder.sequence.void)) >>
        order.traverse(order => keys.update(_ - mkMempoolKey(order))).void
    }

    def put(pool: Option[Pool], order: Option[Processed.Any]): F[Unit] = {
      def putPool  = pool.map(pool => redis.setEx(poolKey(pool.box.boxId.value), pool.asJson.noSpaces, config.ttl))
      def putOrder = order.map(order => redis.setEx(orderKey(order), order.asJson.noSpaces, config.ttl))
      if (putPool.isEmpty && putOrder.isEmpty) unit
      else
        redis.transact_(List(putPool.sequence.void, putOrder.sequence.void)) >> order
          .traverse(order => keys.update(_ + mkMempoolKey(order)))
          .void
    }

    def getOrder(id: List[BoxId]): F[Option[Processed.Any]] =
      keys.get
        .map(_.find { key =>
          id.contains(BoxId(key.orderId)) && key.orderType == mapToMempool(OrderStatus.WaitingRegistration).entryName
        })
        .flatMap(_.flatTraverse(key => redis.get(mkKey(key))))
        .map(_.flatMap(str => decode[Processed.Any](str).toOption))

    def getPool(id: List[BoxId]): F[Option[Pool]] =
      id.map(id => poolKey(id.value))
        .traverse(k =>
          redis
            .get(k)
            .map(_.flatMap(decode[Pool](_).toOption))
        )
        .map(_.flatten.headOption)

    private def orderKey(processed: Processed.Any): String =
      s"${processed.order.redeemer.show}_${processed.order.id}_${mapToMempool(processed.state.status).entryName}"

    private def mkKey(key: MempoolKey): String =
      s"${key.address}_${key.orderId}_${key.orderType}"

    private def mkMempoolKey(order: Processed.Any): MempoolKey =
      MempoolKey(order.order.redeemer.show, order.order.id.value, mapToMempool(order.state.status).entryName)

    private def poolKey(poolBoxId: String): String =
      s"pool_$poolBoxId"
  }

  final private class Tracing[F[_]: Monad: Logging] extends Mempool[Mid[F, *]] {

    def getOrder(id: List[BoxId]): Mid[F, Option[Processed.Any]] =
      for {
        _ <- info"getOrder($id)"
        r <- _
        _ <- info"getOrder($id) -> ${r.map(_.order.id)}"
      } yield r

    def getPool(id: List[BoxId]): Mid[F, Option[Pool]] =
      for {
        _ <- info"getPool($id)"
        r <- _
        _ <- info"getPool($id) -> ${r.map(_.box.boxId)}"
      } yield r

    def put(pool: Option[Pool], order: Option[Processed.Any]): Mid[F, Unit] =
      for {
        _ <- info"put(${pool.map(_.box.boxId)}, ${order.map(_.order.id)})"
        r <- _
        _ <- info"put(${pool.map(_.box.boxId)}, ${order.map(_.order.id)}) -> finish"
      } yield r

    def del(pool: Option[Pool], order: Option[Processed.Any]): Mid[F, Unit] =
      for {
        _ <- info"del(${pool.map(_.box.boxId)}, ${order.map(_.order.id)})"
        r <- _
        _ <- info"del(${pool.map(_.box.boxId)}, ${order.map(_.order.id)}) -> finish"
      } yield r
  }

}
