package fi.spectrum.indexer.processes

import cats.syntax.foldable._
import cats.syntax.traverse._
import cats.{Foldable, Functor, Monad}
import fi.spectrum.indexer.config.ApplicationConfig
import fi.spectrum.indexer.services.MempoolTx
import fi.spectrum.streaming.kafka.MempoolConsumer
import tofu.logging.Logging
import tofu.streams.{Chunks, Evals, Temporal}
import tofu.syntax.logging._
import tofu.syntax.monadic._
import tofu.syntax.streams.all._

trait MempoolProcessor[S[_]] {
  def run: S[Unit]
}

object MempoolProcessor {

  def make[
    C[_]: Functor: Foldable,
    F[_]: Monad: ApplicationConfig.Has,
    S[_]: Evals[*[_], F]: Chunks[*[_], C]: Monad: Temporal[*[_], C]
  ](implicit
    events: MempoolConsumer[S, F],
    mempoolTx: MempoolTx[F],
    logs: Logging.Make[F]
  ): MempoolProcessor[S] = logs.forService[MempoolProcessor[S]].map(implicit __ => new Live[C, F, S])

  final private class Live[
    C[_]: Functor: Foldable,
    F[_]: Monad: ApplicationConfig.Has: Logging,
    S[_]: Evals[*[_], F]: Chunks[*[_], C]: Monad: Temporal[*[_], C]
  ](implicit events: MempoolConsumer[S, F], mempoolTx: MempoolTx[F])
    extends MempoolProcessor[S] {

    def run: S[Unit] = eval(ApplicationConfig.access).flatMap { config =>
      events.stream
        .groupWithin(config.mempoolBatchSize, config.mempoolGroupTime)
        .evalTap(batch => info"Got next mempool batch of size: ${batch.size}.")
        .evalMap { committableBatch =>
          committableBatch.toList
            .flatMap(_.message)
            .traverse(mempoolTx.process) >> committableBatch.toList.lastOption.traverse(_.commit).void
        }
    }
  }
}
