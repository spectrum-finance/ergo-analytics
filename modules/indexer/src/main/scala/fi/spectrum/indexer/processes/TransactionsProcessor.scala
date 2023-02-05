package fi.spectrum.indexer.processes

import cats.data.NonEmptyList
import cats.syntax.foldable._
import cats.syntax.option._
import cats.syntax.traverse._
import cats.{Foldable, Functor, Monad}
import fi.spectrum.core.domain.analytics.Processed
import fi.spectrum.core.domain.pool.Pool
import fi.spectrum.indexer.config.ApplicationConfig
import fi.spectrum.indexer.models.BlockChainEvent
import fi.spectrum.indexer.services.{Events, Orders, Pools}
import fi.spectrum.streaming.domain.ChainSyncEvent
import fi.spectrum.streaming.kafka.{CSProducer, Record, TxConsumer}
import mouse.all._
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
    producer: CSProducer[S],
    logs: Logging.Make[F]
  ): TransactionsProcessor[S] = logs.forService[TransactionsProcessor[S]].map(implicit __ => new Live[C, F, S])

  final private class Live[
    C[_]: Functor: Foldable,
    F[_]: Monad: Logging: ApplicationConfig.Has,
    S[_]: Evals[*[_], F]: Chunks[*[_], C]: Temporal[*[_], C]: Monad
  ](implicit
    events: TxConsumer[S, F],
    producer: CSProducer[S],
    orders: Orders[F],
    pools: Pools[F],
    transactions: Events[F]
  ) extends TransactionsProcessor[S] {

    override def run: S[Unit] = eval(ApplicationConfig.access).flatMap { config =>
      events.stream
        .groupWithin(config.transactionsBatchSize, config.transactionsGroupTime)
        .evalTap(batch => info"Received transactions: ${batch.size}.")
        .flatMap { batch =>
          val batchElems = batch.toList.flatMap(_.message)
          def run: F[List[ChainSyncEvent]] = for {
            _         <- info"batch elems size: ${batchElems.length}"
            processed <- batchElems.traverse(transactions.process)
            (orderEvents, poolEvents) = {
              val (x, y) = processed.unzip
              x.flatten -> y.flatten
            }
            _ <- info"New orders: $orderEvents"
            _ <- info"New pools: $poolEvents"
            _ <- NonEmptyList.fromList(orderEvents).fold(unit[F])(orders.process)
            _ <- NonEmptyList.fromList(poolEvents).fold(unit[F])(pools.process)
            events = processed.flatMap[ChainSyncEvent] {
                       case (Some(r: BlockChainEvent.Apply[Processed.Any]), pool) =>
                         ChainSyncEvent.ApplyChainSync(r.event.some, pool.map(_.event)).some
                       case (order, Some(r: BlockChainEvent.Apply[Pool])) =>
                         ChainSyncEvent.ApplyChainSync(order.map(_.event), r.event.some).some
                       case (o, p) if o.nonEmpty || p.nonEmpty =>
                         ChainSyncEvent.UnapplyChainSync(o.map(_.event), p.map(_.event)).some
                       case _ => none
                     }
          } yield events

          eval(run).flatMap { events =>
            emits(events.map(event => Record("chain_sync", event))).thrush(producer.produce)
          } >> eval(batch.toList.lastOption.traverse_(_.commit))
        }

    }
  }
}
