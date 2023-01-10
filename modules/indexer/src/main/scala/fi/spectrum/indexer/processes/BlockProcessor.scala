package fi.spectrum.indexer.processes

import cats.data.NonEmptyList
import cats.syntax.foldable._
import cats.syntax.traverse._
import cats.{Foldable, Functor, Monad}
import fi.spectrum.indexer.config.ApplicationConfig
import fi.spectrum.indexer.services.Blocks
import fi.spectrum.streaming.BlocksEventsConsumer
import tofu.logging.Logging
import tofu.streams.{Chunks, Evals}
import tofu.syntax.logging._
import tofu.syntax.monadic._
import tofu.syntax.streams.all._

trait BlockProcessor[S[_]] {
  def run: S[Unit]
}

object BlockProcessor {

  def make[
    C[_]: Functor: Foldable,
    F[_]: Monad: ApplicationConfig.Has,
    S[_]: Evals[*[_], F]: Chunks[*[_], C]: Monad
  ](implicit blocks: BlocksEventsConsumer[S, F], repo: Blocks[F], logs: Logging.Make[F]): BlockProcessor[S] =
    logs.forService[BlockProcessor[S]] map { implicit __ => new Live[C, F, S] }

  final class Live[
    C[_]: Functor: Foldable,
    F[_]: Monad: Logging: ApplicationConfig.Has,
    S[_]: Evals[*[_], F]: Chunks[*[_], C]: Monad
  ](implicit
    blocks: BlocksEventsConsumer[S, F],
    repo: Blocks[F]
  ) extends BlockProcessor[S] {

    def run: S[Unit] =
      eval(ApplicationConfig.access).flatMap { config =>
        blocks.stream.chunksN(config.poolsBatchSize).evalMap { events =>
          for {
            _ <- info"Got next block events batch of size ${events.size}"
            _ <- NonEmptyList.fromList(events.toList.flatMap(_.message)) match {
                   case Some(value) => repo.process(value)
                   case None        => unit[F]
                 }
            _ <- events.toList.lastOption.traverse(_.commit).void
            _ <- info"Finished to process pools batch"
          } yield ()
        }
      }
  }
}
