package fi.spectrum.indexer.processes

import cats.data.NonEmptyList
import cats.syntax.foldable._
import cats.syntax.traverse._
import cats.{Foldable, Functor, Monad}
import fi.spectrum.indexer.config.ApplicationConfig
import fi.spectrum.indexer.services.Pools
import fi.spectrum.streaming.PoolsEventsConsumer
import tofu.logging.Logging
import tofu.streams.{Chunks, Evals}
import tofu.syntax.logging._
import tofu.syntax.monadic._
import tofu.syntax.streams.all._

trait PoolsProcessor[S[_]] {
  def run: S[Unit]
}

object PoolsProcessor {

  def make[
    C[_]: Functor: Foldable,
    F[_]: Monad: ApplicationConfig.Has,
    S[_]: Evals[*[_], F]: Chunks[*[_], C]: Monad
  ](implicit pool: PoolsEventsConsumer[S, F], pools: Pools[F], logs: Logging.Make[F]): PoolsProcessor[S] =
    logs.forService[PoolsProcessor[S]].map(implicit __ => new Live[C, F, S])

  final private class Live[
    C[_]: Functor: Foldable,
    F[_]: Monad: Logging: ApplicationConfig.Has,
    S[_]: Evals[*[_], F]: Chunks[*[_], C]: Monad
  ](implicit
    pool: PoolsEventsConsumer[S, F],
    pools: Pools[F]
  ) extends PoolsProcessor[S] {

    def run: S[Unit] = eval(ApplicationConfig.access).flatMap { config =>
      pool.stream
        .chunksN(config.poolsBatchSize)
        .evalMap { events =>
          for {
            _ <- info"Got next pools events batch of size ${events.size}"
            _ <- NonEmptyList.fromList(events.toList.flatMap(_.message)) match {
                   case Some(value) => pools.process(value)
                   case None        => unit[F]
                 }
            _ <- events.toList.lastOption.traverse(_.commit).void
            _ <- info"Finished to process pools batch"
          } yield ()
        }
    }
  }
}
