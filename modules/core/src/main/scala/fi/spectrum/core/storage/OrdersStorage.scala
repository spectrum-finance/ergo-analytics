package fi.spectrum.core.storage

import cats.Monad
import cats.effect.MonadCancel
import cats.syntax.traverse._
import fi.spectrum.core.domain.BoxId
import fi.spectrum.core.domain.analytics.Processed
import fi.spectrum.core.domain.order.OrderId
import fi.spectrum.core.domain.pool.Pool
import io.circe.parser.decode
import io.circe.syntax._
import io.github.oskin1.rocksdb.scodec.TxRocksDB
import scodec.Codec
import scodec.codecs.{int32, utf8, variableSizeBits}
import tofu.higherKind.{Mid, RepresentableK}
import tofu.logging.Logging
import tofu.syntax.logging._
import tofu.syntax.monadic._

trait OrdersStorage[F[_]] {
  def insertOrder(order: Processed.Any): F[Unit]

  def getOrders(ids: List[BoxId]): F[List[Processed.Any]]

  def deleteOrder(orderId: OrderId): F[Unit]

  def insertPool(pool: Pool): F[Unit]

  def getPool(id: List[BoxId]): F[Option[Pool]]

  def deletePool(id: BoxId): F[Unit]

  def insertPoolAndOrder(pool: Option[Pool], orders: List[Processed.Any]): F[Unit]

  def deletePoolAndOrder(pool: Option[Pool], orders: List[Processed.Any]): F[Unit]
}

object OrdersStorage {

  implicit def representableK: RepresentableK[OrdersStorage] =
    tofu.higherKind.derived.genRepresentableK

  def make[F[_]: MonadCancel[*[_], Throwable]](implicit
    rocks: TxRocksDB[F],
    logs: Logging.Make[F]
  ): OrdersStorage[F] =
    logs
      .forService[OrdersStorage[F]]
      .map(implicit __ => new Tracing[F] attach new RocksDBStorage[F])

  final private class RocksDBStorage[F[_]: MonadCancel[*[_], Throwable]: Logging](implicit rocks: TxRocksDB[F])
    extends OrdersStorage[F] {

    implicit val scodec: Codec[String] = variableSizeBits(int32, utf8)

    def deletePoolAndOrder(pool: Option[Pool], orders: List[Processed.Any]): F[Unit] =
      rocks.beginTransaction.use { tx =>
        pool.traverse(p => deletePool(p.box.boxId)) >> orders.traverse(o => deleteOrder(o.order.id)) >> tx.commit
      }

    def insertPoolAndOrder(pool: Option[Pool], orders: List[Processed.Any]): F[Unit] =
      rocks.beginTransaction.use { tx =>
        pool.traverse(insertPool) >> orders.traverse(insertOrder) >> tx.commit
      }

    def insertOrder(order: Processed.Any): F[Unit] =
      rocks.put(order.order.box.boxId, order.asJson.noSpaces)

    def getOrders(ids: List[BoxId]): F[List[Processed.Any]] =
      ids
        .traverse(rocks.get[BoxId, String])
        .map(_.flatten.flatMap(decode[Processed.Any](_).toOption))

    def deleteOrder(orderId: OrderId): F[Unit] =
      rocks.delete(orderId)

    def insertPool(pool: Pool): F[Unit] =
      rocks.put(mkPoolKey(pool.box.boxId), pool.asJson.noSpaces)

    //todo: Optimisation. Try to get only the first one, if it doesnt exist, try other ones
    def getPool(ids: List[BoxId]): F[Option[Pool]] =
      ids
        .map(mkPoolKey)
        .traverse(rocks.get[String, String])
        .map(_.flatten)
        .flatTap { r =>
          r.traverse(p => info"Parse pool: $p")

        }
        .map(_.map(decode[Pool](_)))
        .map(_.flatMap(_.toOption).headOption)

    def deletePool(id: BoxId): F[Unit] =
      rocks.delete(mkPoolKey(id))

    private def mkPoolKey(id: BoxId): String = s"pool.key.$id"
  }

  final private class Tracing[F[_]: Monad: Logging] extends OrdersStorage[Mid[F, *]] {

    def insertOrder(order: Processed.Any): Mid[F, Unit] =
      for {
        _ <- trace"insertOrder(${order.order.id})"
        _ <- _
        _ <- trace"insertOrder(${order.order.id}) -> finish"
      } yield ()

    def getOrders(ids: List[BoxId]): Mid[F, List[Processed.Any]] =
      for {
        _ <- trace"getOrder($ids)"
        r <- _
        _ <- trace"getOrder($ids) -> ${r.map(_.order.id)}"
      } yield r

    def deleteOrder(orderId: OrderId): Mid[F, Unit] =
      for {
        _ <- trace"deleteOrder($orderId)"
        _ <- _
        _ <- trace"deleteOrder($orderId) -> finish"
      } yield ()

    def insertPool(pool: Pool): Mid[F, Unit] =
      for {
        _ <- info"insertPool(${pool.poolId}, ${pool.box.boxId})"
        _ <- _
        _ <- info"insertPool(${pool.poolId}, ${pool.box.boxId}) -> finish"
      } yield ()

    def getPool(id: List[BoxId]): Mid[F, Option[Pool]] =
      for {
        _ <- info"getPool($id)"
        r <- _
        _ <- info"getPool($id) -> ${r.map(_.poolId)}"
      } yield r

    def deletePool(id: BoxId): Mid[F, Unit] =
      for {
        _ <- info"deletePool($id)"
        _ <- _
        _ <- info"deletePool($id) -> finish"
      } yield ()

    def insertPoolAndOrder(pool: Option[Pool], orders: List[Processed.Any]): Mid[F, Unit] =
      for {
        _ <- info"insertPoolAndOrder(${pool.map(_.box.boxId)}, ${orders.map(_.order.id)})"
        _ <- _
        _ <- info"insertPoolAndOrder(${pool.map(_.box.boxId)}, ${orders.map(_.order.id)}) -> finish"
      } yield ()

    def deletePoolAndOrder(pool: Option[Pool], orders: List[Processed.Any]): Mid[F, Unit] =
      for {
        _ <- info"deletePoolAndOrder(${pool.map(_.box.boxId)}, ${orders.map(_.order.id)})"
        _ <- _
        _ <- info"deletePoolAndOrder(${pool.map(_.box.boxId)}, ${orders.map(_.order.id)}) -> finish"
      } yield ()
  }
}
