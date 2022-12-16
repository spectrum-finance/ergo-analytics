package fi.spectrum.indexer.processes

import cats.syntax.foldable._
import cats.{Foldable, Functor, Monad}
import fi.spectrum.core.domain.transaction.Transaction
import fi.spectrum.parser.PoolParser
import fi.spectrum.parser.evaluation.ProcessedOrderParser
import fi.spectrum.streaming.domain.TxEvent.Apply
import fi.spectrum.streaming.domain.{OrderEvent, PoolEvent, TxEvent}
import fi.spectrum.streaming.kafka.Record
import fi.spectrum.streaming.{OrderEventsProducer, PoolsEventsProducer, TxEventsConsumer}
import mouse.all._
import tofu.logging.{Logging, Logs}
import tofu.streams.{Chunks, Evals}
import tofu.syntax.logging._
import tofu.syntax.monadic._
import tofu.syntax.streams.all._

trait TransactionsProcessor[S[_]] {
  def run: S[Unit]
}

object TransactionsProcessor {

  def make[
    C[_]: Functor: Foldable,
    F[_]: Monad,
    S[_]: Evals[*[_], F]: Chunks[*[_], C]: Monad
  ](implicit
    events: TxEventsConsumer[S, F],
    orders: OrderEventsProducer[S],
    pools: PoolsEventsProducer[S],
    parser: ProcessedOrderParser,
    poolsParser: PoolParser,
    logs: Logs[F, F]
  ): F[TransactionsProcessor[S]] = logs.forService[TransactionsProcessor[S]].map(implicit __ => new Live[C, F, S])

  final private class Live[
    C[_]: Functor: Foldable,
    F[_]: Monad: Logging,
    S[_]: Evals[*[_], F]: Chunks[*[_], C]: Monad
  ](implicit
    events: TxEventsConsumer[S, F],
    ordersProducer: OrderEventsProducer[S],
    poolsProducer: PoolsEventsProducer[S],
    orderParser: ProcessedOrderParser,
    poolsParser: PoolParser
  ) extends TransactionsProcessor[S] {

    override def run: S[Unit] =
      events.stream
        .chunksN(10) //todo to config
        .evalTap(batch => info"Got next transaction batch of size: ${batch.size}.")
        .flatMap { committableBatch =>
          val (orders: List[OrderEvent], pools: List[PoolEvent]) =
            committableBatch.toList.foldLeft(List.empty[OrderEvent], List.empty[PoolEvent]) { case ((o, p), next) =>
              val tx = Transaction.fromErgoLike(next.message.tx)
              val (order: Option[OrderEvent], pool: Option[PoolEvent]) =
                next.message match {
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
          for {
            _ <- eval(info"New orders: $orders")
            _ <- eval(info"New pools: $pools")
            _ <- emits(orders.map(o => Record(o.order.order.id, o))) thrush ordersProducer.produce
            _ <- emits(pools.map(p => Record(p.pool.poolId, p))) thrush poolsProducer.produce
            _ <- eval(committableBatch.toList.lastOption.traverse_(_.commit))
          } yield ()
        }

  }
}
