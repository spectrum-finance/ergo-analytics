package fi.spectrum.indexer.processes

import cats.data.NonEmptyList
import cats.syntax.foldable._
import cats.{Foldable, Functor, Monad}
import fi.spectrum.indexer.services.Transactions
import fi.spectrum.streaming.domain.{OrderEvent, PoolEvent}
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
    transactions: Transactions[F],
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
    transactions: Transactions[F]
  ) extends TransactionsProcessor[S] {

    override def run: S[Unit] =
      events.stream
        .chunksN(10) //todo to config
        .evalTap(batch => info"Got next transaction batch of size: ${batch.size}.")
        .flatMap { committableBatch =>
          for {
            (orders: List[OrderEvent], pools: List[PoolEvent]) <- eval {
                                                                    NonEmptyList.fromList(
                                                                      committableBatch.toList.map(_.message)
                                                                    ) match {
                                                                      case Some(value) => transactions.process(value)
                                                                      case None        => (List.empty, List.empty).pure[F]
                                                                    }
                                                                  }
            _ <- eval(info"New orders: $orders")
            _ <- eval(info"New pools: $pools")
            _ <- emits(orders.map(o => Record(o.order.order.id, o))) thrush ordersProducer.produce
            _ <- emits(pools.map(p => Record(p.pool.poolId, p))) thrush poolsProducer.produce
            _ <- eval(committableBatch.toList.lastOption.traverse_(_.commit))
          } yield ()
        }

  }
}
