package fi.spectrum.indexer.processes

import cats.syntax.foldable._
import cats.syntax.traverse._
import cats.{Foldable, Functor, Monad}
import fi.spectrum.indexer.services.ProcessedOrderHandler
import fi.spectrum.streaming.OrderEventsConsumer
import fi.spectrum.streaming.domain.OrderEvent
import tofu.streams.{Chunks, Evals}
import tofu.syntax.streams.all._

trait OrdersProcessor[S[_]] {
  def run: S[Unit]
}

object OrdersProcessor {

  def make[C[_]: Functor: Foldable, F[_]: Monad, S[_]: Evals[*[_], F]: Chunks[*[_], C]: Monad](
    order: OrderEventsConsumer[S, F],
    handle: List[ProcessedOrderHandler[F]]
  ): OrdersProcessor[S] = new Live[C, F, S](order, handle)

  final private class Live[C[_]: Functor: Foldable, F[_]: Monad, S[_]: Evals[*[_], F]: Chunks[*[_], C]: Monad](
    order: OrderEventsConsumer[S, F],
    handle: List[ProcessedOrderHandler[F]]
  ) extends OrdersProcessor[S] {

    override def run: S[Unit] = order.stream
      .chunksN(10)
      .evalMap { committables =>
        committables.toList.map { committable =>
          committable.message match {
            case OrderEvent.Apply(event) =>
              handle.map(_.handle(event)).sequence
            case OrderEvent.Unapply(tx) => ???
          }
        }.sequence_
      }
  }
}
