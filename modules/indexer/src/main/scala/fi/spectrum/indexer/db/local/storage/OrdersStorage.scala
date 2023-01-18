package fi.spectrum.indexer.db.local.storage

import cats.Monad
import cats.effect.kernel.Resource
import cats.syntax.traverse._
import derevo.derive
import fi.spectrum.core.domain.BoxId
import fi.spectrum.core.domain.analytics.Processed
import fi.spectrum.core.domain.order.OrderId
import fi.spectrum.core.domain.pool.Pool
import io.circe.parser.decode
import io.circe.syntax._
import io.github.oskin1.rocksdb.scodec.{Transaction, TxRocksDB}
import scodec.Codec
import scodec.codecs.{int32, utf8, variableSizeBits}
import tofu.higherKind.Mid
import tofu.higherKind.derived.representableK
import tofu.logging.Logging
import tofu.syntax.logging._
import tofu.syntax.monadic._

@derive(representableK)
trait OrdersStorage[F[_]] {
  def insertOrder(order: Processed.Any): F[Unit]

  def getOrder(id: List[BoxId]): F[Option[Processed.Any]]

  def deleteOrder(orderId: OrderId): F[Unit]

  def insertPool(pool: Pool): F[Unit]

  def getPool(id: List[BoxId]): F[Option[Pool]]

  def deletePool(id: BoxId): F[Unit]
}

trait OrderStorageTransactional[F[_]] extends OrdersStorage[F] {
  def runTransaction: Resource[F, Transaction[F]]
}

object OrdersStorage {

  def make[F[_]: Monad](implicit rocks: TxRocksDB[F], logs: Logging.Make[F]): OrderStorageTransactional[F] =
    logs
      .forService[OrderStorageTransactional[F]]
      .map(implicit __ =>
        new Transactional[F](
          new Tracing[F] attach new RocksDBStorage[F]
        )
      )

  final private class Transactional[F[_]: Monad](storage: OrdersStorage[F])(implicit rocks: TxRocksDB[F])
    extends OrderStorageTransactional[F] {
    def runTransaction: Resource[F, Transaction[F]] = rocks.beginTransaction

    def insertOrder(order: Processed.Any): F[Unit] = storage.insertOrder(order)

    def getOrder(id: List[BoxId]): F[Option[Processed.Any]] = storage.getOrder(id)

    def deleteOrder(orderId: OrderId): F[Unit] = storage.deleteOrder(orderId)

    def insertPool(pool: Pool): F[Unit] = storage.insertPool(pool)

    def getPool(id: List[BoxId]): F[Option[Pool]] = storage.getPool(id)

    def deletePool(id: BoxId): F[Unit] = storage.deletePool(id)
  }

  final private class RocksDBStorage[F[_]: Monad](implicit rocks: TxRocksDB[F]) extends OrdersStorage[F] {

    implicit val scodec: Codec[String] = variableSizeBits(int32, utf8)

    def insertOrder(order: Processed.Any): F[Unit] =
      rocks.put(order.order.box.boxId, order.asJson.noSpaces)

    def getOrder(ids: List[BoxId]): F[Option[Processed.Any]] =
      ids
        .traverse(rocks.get[BoxId, String])
        .map(_.flatten.map(decode[Processed.Any](_).toOption).collectFirst { case Some(v) => v })

    def deleteOrder(orderId: OrderId): F[Unit] =
      rocks.delete(orderId)

    def insertPool(pool: Pool): F[Unit] =
      rocks.put(mkPoolKey(pool.box.boxId), pool.asJson.noSpaces)

    def getPool(ids: List[BoxId]): F[Option[Pool]] =
      ids
        .map(mkPoolKey)
        .traverse(rocks.get[String, String])
        .map(_.flatten.map(decode[Pool](_).toOption).collectFirst { case Some(v) => v })

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

    def getOrder(id: List[BoxId]): Mid[F, Option[Processed.Any]] =
      for {
        _ <- trace"getOrder($id)"
        r <- _
        _ <- trace"getOrder($id) -> $r"
      } yield r

    def deleteOrder(orderId: OrderId): Mid[F, Unit] =
      for {
        _ <- trace"deleteOrder($orderId)"
        _ <- _
        _ <- trace"deleteOrder($orderId) -> finish"
      } yield ()

    def insertPool(pool: Pool): Mid[F, Unit] =
      for {
        _ <- trace"insertPool(${pool.poolId})"
        _ <- _
        _ <- trace"insertPool(${pool.poolId}) -> finish"
      } yield ()

    def getPool(id: List[BoxId]): Mid[F, Option[Pool]] =
      for {
        _ <- trace"getPool($id)"
        r <- _
        _ <- trace"getPool($id) -> $r"
      } yield r

    def deletePool(id: BoxId): Mid[F, Unit] =
      for {
        _ <- trace"deletePool($id)"
        _ <- _
        _ <- trace"deletePool($id) -> finish"
      } yield ()
  }
}
