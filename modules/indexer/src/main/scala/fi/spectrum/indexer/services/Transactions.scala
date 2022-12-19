package fi.spectrum.indexer.services

import cats.data.NonEmptyList
import cats.{Applicative, Monad}
import derevo.derive
import fi.spectrum.core.domain.transaction.Transaction
import fi.spectrum.parser.PoolParser
import fi.spectrum.parser.evaluation.ProcessedOrderParser
import fi.spectrum.streaming.domain.TxEvent.Apply
import fi.spectrum.streaming.domain.{OrderEvent, PoolEvent, TxEvent}
import tofu.higherKind.Mid
import tofu.higherKind.derived.representableK
import tofu.logging.Logging
import tofu.syntax.logging._
import tofu.syntax.monadic._

@derive(representableK)
trait Transactions[F[_]] {
  def process(events: NonEmptyList[TxEvent]): F[(List[OrderEvent], List[PoolEvent])]
}

object Transactions {

  def make[F[_]: Monad](implicit
    orderParser: ProcessedOrderParser,
    poolsParser: PoolParser,
    logs: Logging.Make[F]
  ): Transactions[F] = logs.forService[Transactions[F]].map(implicit __ => new Tracing[F] attach new Live[F])

  final private class Live[F[_]: Applicative](implicit
    orderParser: ProcessedOrderParser,
    poolsParser: PoolParser
  ) extends Transactions[F] {

    def process(events: NonEmptyList[TxEvent]): F[(List[OrderEvent], List[PoolEvent])] =
      events.toList
        .foldLeft(List.empty[OrderEvent], List.empty[PoolEvent]) { case ((o, p), next) =>
          val tx = Transaction.fromErgoLike(next.tx)
          val (order: Option[OrderEvent], pool: Option[PoolEvent]) =
            next match {
              case Apply(timestamp, _) =>
                val order = orderParser.parse(tx, timestamp).map(OrderEvent.Apply(_))
                val pool = tx.outputs
                  .map(poolsParser.parse(_, timestamp))
                  .collectFirst { case Some(p) => p }
                  .map(PoolEvent.Apply(_))

                (order, pool)
              case TxEvent.Unapply(_) =>
                val order = orderParser.parse(tx, 0).map(OrderEvent.Unapply(_))
                val pool =
                  tx.outputs
                    .map(poolsParser.parse(_, 0))
                    .collectFirst { case Some(p) => p }
                    .map(PoolEvent.Unapply(_))

                (order, pool)
            }

          (order.fold(o)(_ :: o), pool.fold(p)(_ :: p))
        }
        .pure[F]
  }

  final private class Tracing[F[_]: Monad: Logging] extends Transactions[Mid[F, *]] {

    def process(events: NonEmptyList[TxEvent]): Mid[F, (List[OrderEvent], List[PoolEvent])] =
      for {
        _            <- info"Going to process next tx events: $events"
        r @ (r1, r2) <- _
        _            <- info"Transactions result is: $r1, $r2"
      } yield r
  }
}
