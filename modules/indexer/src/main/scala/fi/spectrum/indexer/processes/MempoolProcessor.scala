package fi.spectrum.indexer.processes

import cats.effect.kernel.Clock
import cats.syntax.foldable._
import cats.syntax.traverse._
import cats.{Foldable, Functor, Monad}
import fi.spectrum.core.domain.analytics.Processed
import fi.spectrum.core.domain.transaction.Transaction
import fi.spectrum.indexer.config.ApplicationConfig
import fi.spectrum.indexer.services.Mempool
import fi.spectrum.parser.evaluation.ProcessedOrderParser
import fi.spectrum.streaming.kafka.MempoolConsumer
import tofu.logging.Logging
import tofu.streams.{Chunks, Evals, Temporal}
import tofu.syntax.logging._
import tofu.syntax.monadic._
import tofu.syntax.streams.all._
import tofu.syntax.time.now.millis

trait MempoolProcessor[S[_]] {
  def run: S[Unit]
}

object MempoolProcessor {

  def make[
    C[_]: Functor: Foldable,
    F[_]: Monad: ApplicationConfig.Has: Clock,
    S[_]: Evals[*[_], F]: Chunks[*[_], C]: Monad: Temporal[*[_], C]
  ](implicit
    events: MempoolConsumer[S, F],
    mempoolCache: Mempool[F],
    orderParser: ProcessedOrderParser[F],
    logs: Logging.Make[F]
  ): MempoolProcessor[S] = logs.forService[MempoolProcessor[S]].map(implicit __ => new Live[C, F, S])

  final private class Live[
    C[_]: Functor: Foldable,
    F[_]: Monad: ApplicationConfig.Has: Logging: Clock,
    S[_]: Evals[*[_], F]: Chunks[*[_], C]: Monad: Temporal[*[_], C]
  ](implicit events: MempoolConsumer[S, F], mempoolCache: Mempool[F], orderParser: ProcessedOrderParser[F])
    extends MempoolProcessor[S] {

    def run: S[Unit] = eval(ApplicationConfig.access).flatMap { config =>
      events.stream
        .groupWithin(config.mempoolBatchSize, config.mempoolGroupTime)
        .evalTap(batch => info"Got next mempool batch of size: ${batch.size}.")
        .flatMap { committableBatch =>
          def orders(ts: Long): List[Processed.Any] = List.empty
//            committableBatch.toList
//            .flatMap(_.message)
//            .map(tx => Transaction.fromErgoLike(tx.tx))
//            .flatMap(tx => orderParser.parse(tx, ts))

          for {
            ts <- eval(millis[F])
            _  <- eval(orders(ts).traverse(mempoolCache.put))
            _  <- eval(committableBatch.toList.lastOption.traverse_(_.commit))
          } yield ()
        }
    }
  }
}
