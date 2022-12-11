package fi.spectrum.indexer.processes

import cats.data.NonEmptyList
import cats.{Foldable, Functor, Monad}
import fi.spectrum.core.domain.analytics.ProcessedOrder
import fi.spectrum.indexer.classes.Handle
import tofu.streams.{Chunks, Evals}
import tofu.syntax.streams.all._
import cats.syntax.foldable._
import tofu.syntax.monadic._

trait OrdersProcessor[S[_]] {
  def run: S[Unit]
}

object OrdersProcessor {

  def make[C[_]: Functor: Foldable, F[_]: Monad, S[_]: Evals[*[_], F]: Chunks[*[_], C]: Monad](
    stream: S[ProcessedOrder],
    handler: List[Handle[ProcessedOrder, F]]
  ): OrdersProcessor[S] = new Live[C, F, S](stream, handler)

  final private class Live[C[_]: Functor: Foldable, F[_]: Monad, S[_]: Evals[*[_], F]: Chunks[*[_], C]: Monad](
    stream: S[ProcessedOrder],
    handler: List[Handle[ProcessedOrder, F]]
  ) extends OrdersProcessor[S] {

    override def run: S[Unit] = stream.chunksN(10).evalMap { batch =>
      NonEmptyList.fromList(batch.toList) match {
        case Some(value) => ??? //handler.handle(value).void
        case None        => unit[F]
      }

    }
  }
}
