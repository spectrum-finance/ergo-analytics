package fi.spectrum.indexer.processes

import cats.data.NonEmptyList
import cats.syntax.foldable._
import cats.syntax.traverse._
import cats.{Foldable, Functor, Monad}
import fi.spectrum.indexer.services.Orders
import fi.spectrum.streaming.OrderEventsConsumer
import tofu.logging.{Logging, Logs}
import tofu.streams.{Chunks, Evals}
import tofu.syntax.monadic._
import tofu.syntax.logging._
import tofu.syntax.streams.all._

trait OrdersProcessor[S[_]] {
  def run: S[Unit]
}

object OrdersProcessor {

  def make[
    C[_]: Functor: Foldable,
    F[_]: Monad,
    S[_]: Evals[*[_], F]: Chunks[*[_], C]: Monad
  ](implicit order: OrderEventsConsumer[S, F], orders: Orders[F], logs: Logs[F, F]): F[OrdersProcessor[S]] =
    logs.forService[OrdersProcessor[S]].map(implicit __ => new Live[C, F, S])

  final private class Live[
    C[_]: Functor: Foldable,
    F[_]: Monad: Logging,
    S[_]: Evals[*[_], F]: Chunks[*[_], C]: Monad
  ](implicit
    order: OrderEventsConsumer[S, F],
    orders: Orders[F]
  ) extends OrdersProcessor[S] {

    def run: S[Unit] =
      order.stream
        .chunksN(10) //todo config
        .evalMap { events =>
          for {
            _ <- info"Got next orders events batch of size ${events.size}"
            _ <- NonEmptyList.fromList(events.toList.map(_.message)) match {
                   case Some(value) => orders.process(value)
                   case None        => unit[F]
                 }
            _ <- events.toList.lastOption.traverse(_.commit).void
            _ <- info"Finished to process orders batch"
          } yield ()
        }
  }
}
