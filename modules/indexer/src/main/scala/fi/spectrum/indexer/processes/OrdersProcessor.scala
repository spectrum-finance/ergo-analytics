package fi.spectrum.indexer.processes

import cats.data.NonEmptyList
import cats.syntax.foldable._
import cats.syntax.traverse._
import cats.{Foldable, Functor, Monad}
import fi.spectrum.indexer.services.ProcessOrder
import fi.spectrum.streaming.OrderEventsConsumer
import fi.spectrum.streaming.domain.OrderEvent
import tofu.streams.{Chunks, Evals}
import tofu.syntax.streams.all._
import tofu.syntax.monadic._

trait OrdersProcessor[S[_]] {
  def run: S[Unit]
}

object OrdersProcessor {

  def make[C[_]: Functor: Foldable, F[_]: Monad, S[_]: Evals[*[_], F]: Chunks[*[_], C]: Monad](
    order: OrderEventsConsumer[S, F],
    process: ProcessOrder[F]
  ): OrdersProcessor[S] = new Live[C, F, S](order, process)

  final private class Live[C[_]: Functor: Foldable, F[_]: Monad, S[_]: Evals[*[_], F]: Chunks[*[_], C]: Monad](
    order: OrderEventsConsumer[S, F],
    process: ProcessOrder[F]
  ) extends OrdersProcessor[S] {

    override def run: S[Unit] = order.stream
      .chunksN(10)
      .evalMap { committables =>
        val toInsert = committables.toList.map(_.message).collect { case OrderEvent.Apply(order) => order }
        val toDrop   = committables.toList.map(_.message).collect { case OrderEvent.Unapply(tx) => tx }

        def insert = NonEmptyList.fromList(toInsert) match {
          case Some(value) => process.process(value)
          case None        => unit[F]
        }

        insert >> committables.toList.lastOption.traverse(_.commit).void
      }
  }
}
