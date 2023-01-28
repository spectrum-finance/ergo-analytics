package fi.spectrum.indexer.processes

import cats.data.NonEmptyList
import cats.syntax.foldable._
import cats.{Foldable, Functor, Monad}
import fi.spectrum.indexer.config.ApplicationConfig
import fi.spectrum.indexer.models.BlockChainEvent.{OrderEvent, PoolEvent}
import fi.spectrum.indexer.services.{Events, Orders, Pools}
import fi.spectrum.streaming.kafka.TxConsumer
import tofu.logging.Logging
import tofu.streams.{Chunks, Evals, Temporal}
import tofu.syntax.logging._
import tofu.syntax.monadic._
import tofu.syntax.streams.all._

trait TransactionsProcessor[S[_]] {
  def run: S[Unit]
}

object TransactionsProcessor {

  def make[
    C[_]: Functor: Foldable,
    F[_]: Monad: ApplicationConfig.Has,
    S[_]: Evals[*[_], F]: Temporal[*[_], C]: Chunks[*[_], C]: Monad
  ](implicit
    events: TxConsumer[S, F],
    orders: Orders[F],
    pools: Pools[F],
    transactions: Events[F],
    logs: Logging.Make[F]
  ): TransactionsProcessor[S] = logs.forService[TransactionsProcessor[S]].map(implicit __ => new Live[C, F, S])

  final private class Live[
    C[_]: Functor: Foldable,
    F[_]: Monad: Logging: ApplicationConfig.Has,
    S[_]: Evals[*[_], F]: Chunks[*[_], C]: Temporal[*[_], C]: Monad
  ](implicit
    events: TxConsumer[S, F],
    orders: Orders[F],
    pools: Pools[F],
    transactions: Events[F]
  ) extends TransactionsProcessor[S] {

    override def run: S[Unit] = eval(ApplicationConfig.access).flatMap { config =>
      events.stream
        .groupWithin(config.transactionsBatchSize, config.transactionsGroupTime)
        .evalTap(batch => info"Received transactions: ${batch.size}.")
        .evalMap { batch =>
          val batchElems = batch.toList.flatMap(_.message)
          for {
            _ <- info"batch elems size: ${batchElems.length}"
            (orderEvents, poolEvents) <-
              NonEmptyList
                .fromList(batchElems)
                .fold((List.empty[OrderEvent], List.empty[PoolEvent]).pure[F])(transactions.process)
            _ <- info"New orders: $orderEvents"
            _ <- info"New pools: $poolEvents"
            _ <- NonEmptyList.fromList(orderEvents).fold(unit[F])(orders.process)
            _ <- NonEmptyList.fromList(poolEvents).fold(unit[F])(pools.process)
            _ <- batch.toList.lastOption.traverse_(_.commit)
          } yield ()
        }

    }
  }
}
