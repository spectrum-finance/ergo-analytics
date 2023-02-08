package fi.spectrum.indexer.services

import cats.Monad
import cats.effect.MonadCancel
import cats.syntax.option._
import derevo.derive
import fi.spectrum.common.process.OrderProcess._
import fi.spectrum.core.domain.analytics.Processed
import fi.spectrum.core.domain.order.OrderStatus.Registered
import fi.spectrum.core.storage.OrdersStorage
import fi.spectrum.graphite.Metrics
import fi.spectrum.indexer.models.BlockChainEvent
import fi.spectrum.indexer.models.BlockChainEvent.{OrderEvent, PoolEvent}
import fi.spectrum.parser.PoolParser
import fi.spectrum.parser.evaluation.ProcessedOrderParser
import fi.spectrum.streaming.domain.TransactionEvent
import fi.spectrum.streaming.domain.TransactionEvent._
import tofu.higherKind.Mid
import tofu.higherKind.derived.representableK
import tofu.logging.Logging
import tofu.syntax.logging._
import tofu.syntax.monadic._

@derive(representableK)
trait Events[F[_]] {
  def process(event: TransactionEvent): F[(List[OrderEvent], Option[PoolEvent])]
}

object Events {

  def make[F[_]: MonadCancel[*[_], Throwable]](implicit
    orderParser: ProcessedOrderParser[F],
    poolsParser: PoolParser,
    storage: OrdersStorage[F],
    metrics: Metrics[F],
    logs: Logging.Make[F]
  ): Events[F] =
    logs.forService[Events[F]].map(implicit __ => new MetricsMid[F] attach (new Tracing[F] attach new Live[F]))

  final private class Live[F[_]: MonadCancel[*[_], Throwable]: Logging](implicit
    orderParser: ProcessedOrderParser[F],
    poolsParser: PoolParser,
    storage: OrdersStorage[F]
  ) extends Events[F] {

    def process(event: TransactionEvent): F[(List[OrderEvent], Option[PoolEvent])] =
      processOrder(
        event.transaction,
        event.timestamp,
        event.height,
        storage.getOrder,
        storage.getPool,
        info"Empty pool&order ${event.transaction.id}"
      ).flatMap { orders =>
        processPool(
          event.transaction,
          event.timestamp,
          event.height,
          pool => info"Found pool ${pool.poolId} in mempool: ${event.transaction.id} in box: ${pool.box.boxId}"
        ).flatMap { pool =>
          event match {
            case _: TransactionApply =>
              storage
                .insertPoolAndOrder(
                  pool,
                  if (orders.exists(_.state.status.in(Registered))) orders else List.empty[Processed.Any]
                )
                .as(orders.map(BlockChainEvent.Apply(_)), pool.map(BlockChainEvent.Apply(_)))
            case _: TransactionUnapply =>
              storage
                .deletePoolAndOrder(
                  pool,
                  if (orders.exists(_.state.status.in(Registered))) orders else List.empty[Processed.Any]
                )
                .as(orders.map(BlockChainEvent.Unapply(_)), pool.map(BlockChainEvent.Unapply(_)))
          }
        }
      }
  }

  final private class MetricsMid[F[_]: Monad](implicit metrics: Metrics[F]) extends Events[Mid[F, *]] {

    def process(event: TransactionEvent): Mid[F, (List[OrderEvent], Option[PoolEvent])] = {
      val (apply, unapply) = event match {
        case _: TransactionApply => (1, 0)
        case _                   => (0, 1)
      }
      for {
        _ <- Monad[F].whenA(apply > 0)(metrics.sendCount("tx.apply", apply.toDouble))
        _ <- Monad[F].whenA(unapply > 0)(metrics.sendCount("tx.unapply", unapply.toDouble))
        r <- _
      } yield r
    }
  }

  final private class Tracing[F[_]: Monad: Logging] extends Events[Mid[F, *]] {

    def process(event: TransactionEvent): Mid[F, (List[OrderEvent], Option[PoolEvent])] =
      for {
        _            <- trace"process(${event.transaction.id})"
        r @ (r1, r2) <- _
        _            <- trace"process(${event.transaction.id}) -> (${r1.map(_.event.order.id)}, $r2)"
      } yield r
  }
}
