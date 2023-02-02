package fi.spectrum.indexer.services

import cats.Monad
import cats.effect.MonadCancel
import cats.syntax.option._
import cats.syntax.traverse._
import derevo.derive
import fi.spectrum.core.domain.analytics.Processed
import fi.spectrum.core.domain.pool.Pool
import fi.spectrum.core.domain.transaction.Transaction
import fi.spectrum.graphite.Metrics
import fi.spectrum.indexer.db.local.storage.OrderStorageTransactional
import fi.spectrum.parser.PoolParser
import fi.spectrum.parser.evaluation.ProcessedOrderParser
import fi.spectrum.streaming.domain.MempoolEvent
import fi.spectrum.streaming.domain.MempoolEvent.{MempoolApply, MempoolUnapply}
import tofu.higherKind.Mid
import tofu.higherKind.derived.representableK
import tofu.logging.Logging
import tofu.syntax.foption._
import tofu.syntax.logging._
import tofu.syntax.monadic._
import tofu.syntax.time.now.millis
import tofu.time.Clock

@derive(representableK)
trait MempoolTx[F[_]] {
  def process(event: MempoolEvent): F[Unit]
}

object MempoolTx {

  def make[F[_]: MonadCancel[*[_], Throwable]: Clock](implicit
    orderParser: ProcessedOrderParser[F],
    poolsParser: PoolParser,
    storage: OrderStorageTransactional[F],
    mempool: Mempool[F],
    metrics: Metrics[F],
    logs: Logging.Make[F]
  ): MempoolTx[F] =
    logs.forService[MempoolTx[F]].map(implicit __ => new Tracing[F] attach (new MetricsMid[F] attach new Live[F]))

  final private class Live[F[_]: MonadCancel[*[_], Throwable]: Logging: Clock](implicit
    orderParser: ProcessedOrderParser[F],
    poolsParser: PoolParser,
    storage: OrderStorageTransactional[F],
    mempool: Mempool[F]
  ) extends MempoolTx[F] {

    def process(event: MempoolEvent): F[Unit] =
      for {
        now   <- millis
        order <- processOrder(event.transaction, now)
        pool  <- processPool(event.transaction, now)
        _ <-
          event match {
            case MempoolApply(_)   => mempool.put(pool, order)
            case MempoolUnapply(_) => mempool.del(pool, order)
          }
      } yield ()

    private def processOrder(tx: Transaction, timestamp: Long): F[Option[Processed.Any]] =
      orderParser
        .registered(tx, timestamp)
        .orElseF {
          val ids = tx.inputs.toList
          def getState: F[(Option[Processed.Any], Option[Pool])] = for {
            order <- mempool.getOrder(ids).orElseF(storage.getOrder(ids))
            pool  <- mempool.getPool(ids).orElseF(storage.getPool(ids))
          } yield (order, pool)

          getState.flatMap {
            case (Some(order), Some(pool)) =>
              orderParser.evaluated(tx, timestamp, order, pool, 0)
            case (Some(order), _) =>
              orderParser.refunded(tx, timestamp, order).someIn
            case _ =>
              info"Empty pool&order in mempool tx ${tx.id}" as none[Processed.Any]
          }
        }

    private def processPool(tx: Transaction, timestamp: Long): F[Option[Pool]] =
      tx.outputs
        .map(poolsParser.parse(_, timestamp, 0))
        .collectFirst { case Some(p) => p }
        .pure
        .flatTap(_.traverse(pool => info"Found pool ${pool.poolId} in mempool: ${tx.id} in box: ${pool.box.boxId}"))
  }

  final private class MetricsMid[F[_]: Monad](implicit metrics: Metrics[F]) extends MempoolTx[Mid[F, *]] {

    def process(event: MempoolEvent): Mid[F, Unit] =
      (event match {
        case MempoolApply(_)   => metrics.sendCount("mempool.apply", 1.0)
        case MempoolUnapply(_) => metrics.sendCount("mempool.unapply", 1.0)
      }) *> _

  }

  final private class Tracing[F[_]: Monad: Logging] extends MempoolTx[Mid[F, *]] {

    def process(event: MempoolEvent): Mid[F, Unit] =
      for {
        _ <- trace"process(${event.transaction.id})"
        r <- _
        _ <- trace"process(${event.transaction.id}) -> Success"
      } yield r
  }
}
