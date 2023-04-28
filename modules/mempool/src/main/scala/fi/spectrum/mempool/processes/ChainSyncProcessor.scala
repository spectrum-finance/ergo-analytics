package fi.spectrum.mempool.processes

import cats.syntax.either._
import cats.syntax.foldable._
import cats.syntax.traverse._
import cats.{Foldable, Functor, Monad}
import fi.spectrum.mempool.config.ApplicationConfig
import fi.spectrum.mempool.services.MempoolTx
import fi.spectrum.streaming.kafka.CSConsumer
import tofu.logging.Logging
import tofu.streams.{Chunks, Evals, Temporal}
import tofu.syntax.logging._
import tofu.syntax.monadic._
import tofu.syntax.streams.all._

trait ChainSyncProcessor[S[_]] {
  def run: S[Unit]
}

object ChainSyncProcessor {

  def make[
    C[_]: Functor: Foldable,
    F[_]: Monad: ApplicationConfig.Has,
    S[_]: Evals[*[_], F]: Chunks[*[_], C]: Monad: Temporal[*[_], C]
  ](implicit
    events: CSConsumer[S, F],
    mempoolTx: MempoolTx[F],
    logs: Logging.Make[F]
  ): ChainSyncProcessor[S] = logs.forService[ChainSyncProcessor[S]].map(implicit __ => new Live[C, F, S])

  final private class Live[
    C[_]: Functor: Foldable,
    F[_]: Monad: ApplicationConfig.Has: Logging,
    S[_]: Evals[*[_], F]: Chunks[*[_], C]: Monad: Temporal[*[_], C]
  ](implicit events: CSConsumer[S, F], mempoolTx: MempoolTx[F])
    extends ChainSyncProcessor[S] {

    def run: S[Unit] = eval(ApplicationConfig.access).flatMap { config =>
      events.stream
        .groupWithin(config.csBatchSize, config.csGroupTime)
        .evalTap(batch => info"Got next chai sync batch of size: ${batch.size}.")
        .evalTap { batch =>
          val count = batch.toList.filter(_.message.isLeft).map(_.message.leftMap(_.getMessage))
          Monad[F].whenA(count.nonEmpty)(
            warn"Got errors: ${count.mkString(",")}"
          )
        }
        .evalMap { committableBatch =>
          committableBatch.toList
            .flatMap(_.message.toOption)
            .flatten
            .traverse(mempoolTx.process) >> committableBatch.toList.lastOption.traverse(_.commit).void
        }
    }
  }
}
