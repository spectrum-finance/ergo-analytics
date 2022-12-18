package fi.spectrum.indexer.processes

import cats.data.NonEmptyList
import cats.syntax.foldable._
import cats.syntax.traverse._
import cats.{Foldable, Functor, Monad}
import fi.spectrum.indexer.services.Pools
import fi.spectrum.streaming.PoolsEventsConsumer
import tofu.logging.{Logging, Logs}
import tofu.streams.{Chunks, Evals}
import tofu.syntax.monadic._
import tofu.syntax.logging._
import tofu.syntax.streams.all._

trait PoolsProcessor[S[_]] {
  def run: S[Unit]
}

object PoolsProcessor {

  def make[
    C[_]: Functor: Foldable,
    F[_]: Monad,
    S[_]: Evals[*[_], F]: Chunks[*[_], C]: Monad
  ](implicit pool: PoolsEventsConsumer[S, F], pools: Pools[F], logs: Logs[F, F]): F[PoolsProcessor[S]] =
    logs.forService[PoolsProcessor[S]].map(implicit __ => new Live[C, F, S])

  final private class Live[
    C[_]: Functor: Foldable,
    F[_]: Monad: Logging,
    S[_]: Evals[*[_], F]: Chunks[*[_], C]: Monad
  ](implicit
    pool: PoolsEventsConsumer[S, F],
    pools: Pools[F]
  ) extends PoolsProcessor[S] {

    def run: S[Unit] = pool.stream
      .chunksN(10) //todo config
      .evalMap { events =>
        for {
          _ <- info"Got next pools events batch of size ${events.size}"
          _ <- NonEmptyList.fromList(events.toList.map(_.message)) match {
                 case Some(value) => pools.process(value)
                 case None        => unit[F]
               }
          _ <- events.toList.lastOption.traverse(_.commit).void
          _ <- info"Finished to process pools batch"
        } yield ()
      }
  }
}
