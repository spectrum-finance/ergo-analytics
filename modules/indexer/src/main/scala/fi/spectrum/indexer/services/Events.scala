package fi.spectrum.indexer.services

import cats.Monad
import cats.data.NonEmptyList
import cats.effect.MonadCancel
import cats.syntax.option._
import cats.syntax.traverse._
import derevo.derive
import fi.spectrum.core.domain.analytics.Processed
import fi.spectrum.core.domain.order.OrderStatus.Registered
import fi.spectrum.core.domain.pool.Pool
import fi.spectrum.core.domain.transaction.Transaction
import fi.spectrum.graphite.Metrics
import fi.spectrum.indexer.db.local.storage.OrderStorageTransactional
import fi.spectrum.indexer.models.BlockChainEvent
import fi.spectrum.indexer.models.BlockChainEvent.{OrderEvent, PoolEvent}
import fi.spectrum.parser.PoolParser
import fi.spectrum.parser.evaluation.ProcessedOrderParser
import fi.spectrum.streaming.domain.TransactionEvent
import fi.spectrum.streaming.domain.TransactionEvent._
import tofu.higherKind.Mid
import tofu.higherKind.derived.representableK
import tofu.logging.Logging
import tofu.syntax.foption._
import tofu.syntax.logging._
import tofu.syntax.monadic._

@derive(representableK)
trait Events[F[_]] {
  def process(events: NonEmptyList[TransactionEvent]): F[(List[OrderEvent], List[PoolEvent])]
}

object Events {

  def make[F[_]: MonadCancel[*[_], Throwable]](implicit
    orderParser: ProcessedOrderParser[F],
    poolsParser: PoolParser,
    storage: OrderStorageTransactional[F],
    mempool: Mempool[F],
    metrics: Metrics[F],
    logs: Logging.Make[F]
  ): Events[F] =
    logs.forService[Events[F]].map(implicit __ => new MetricsMid[F] attach (new Tracing[F] attach new Live[F]))

  final private class Live[F[_]: MonadCancel[*[_], Throwable]: Logging](implicit
    orderParser: ProcessedOrderParser[F],
    poolsParser: PoolParser,
    storage: OrderStorageTransactional[F],
    mempool: Mempool[F]
  ) extends Events[F] {

    def process(events: NonEmptyList[TransactionEvent]): F[(List[OrderEvent], List[PoolEvent])] =
      events.toList
        .traverse { next =>
          processOrder(next.transaction, next.timestamp, next.height).flatMap { order =>
            storage.runTransaction.use { tx =>
              def run: F[(Option[OrderEvent], Option[PoolEvent])] =
                processPool(next.transaction, next.timestamp, next.height)
                  .flatMap { pool =>
                    next match {
                      case TransactionApply(_, _, _) =>
                        mempool.del(pool, order) *>
                          pool.traverse(storage.insertPool) >>
                          order
                            .traverse(o => Monad[F].whenA(o.state.status.in(Registered))(storage.insertOrder(o)))
                            .void
                            .as(order.map(BlockChainEvent.Apply(_)), pool.map(BlockChainEvent.Apply(_)))
                      case TransactionUnapply(_, _, _) =>
                        pool.traverse(p => storage.deletePool(p.box.boxId)) >>
                          order
                            .traverse(o =>
                              Monad[F].whenA(o.state.status.in(Registered))(storage.deleteOrder(o.order.id))
                            )
                            .void
                            .as(order.map(BlockChainEvent.Unapply(_)), pool.map(BlockChainEvent.Unapply(_)))
                    }
                  }
              run.flatTap(_ => tx.commit)
            }
          }
        }
        .map(elems => elems.flatMap(_._1) -> elems.flatMap(_._2))

    private def processOrder(tx: Transaction, timestamp: Long, height: Int): F[Option[Processed.Any]] =
      orderParser
        .registered(tx, timestamp)
        .orElseF {
          def getState: F[(Option[Processed.Any], Option[Pool])] = for {
            order <- storage.getOrder(tx.inputs.toList)
            pool  <- storage.getPool(tx.inputs.toList)
          } yield (order, pool)

          getState.flatMap {
            case (Some(order), Some(pool)) =>
              orderParser.evaluated(tx, timestamp, order, pool, height)
            case (Some(order), _) =>
              orderParser.refunded(tx, timestamp, order).someIn
            case _ => //todo arbitrage bots
              info"Empty pool&order ${tx.id}" as none[Processed.Any]
          }
        }

    private def processPool(tx: Transaction, timestamp: Long, height: Int): F[Option[Pool]] =
      tx.outputs
        .map(poolsParser.parse(_, timestamp, height))
        .collectFirst { case Some(p) => p }
        .pure
        .flatTap(_.traverse(pool => info"Found pool ${pool.poolId} in tx: ${tx.id} in box: ${pool.box.boxId}"))
  }

  final private class MetricsMid[F[_]: Monad](implicit metrics: Metrics[F]) extends Events[Mid[F, *]] {

    def process(events: NonEmptyList[TransactionEvent]): Mid[F, (List[OrderEvent], List[PoolEvent])] = {
      val apply   = events.collect { case t: TransactionApply => t }.length
      val unapply = events.collect { case t: TransactionUnapply => t }.length
      for {
        _ <- Monad[F].whenA(apply > 0)(metrics.sendCount("tx.apply", apply.toDouble))
        _ <- Monad[F].whenA(unapply > 0)(metrics.sendCount("tx.unapply", unapply.toDouble))
        r <- _
      } yield r
    }
  }

  final private class Tracing[F[_]: Monad: Logging] extends Events[Mid[F, *]] {

    def process(events: NonEmptyList[TransactionEvent]): Mid[F, (List[OrderEvent], List[PoolEvent])] =
      for {
        _            <- trace"process(${events.map(_.transaction.id)})"
        r @ (r1, r2) <- _
        _            <- trace"process(${events.map(_.transaction.id)}) -> ($r1, $r2)"
      } yield r
  }
}
