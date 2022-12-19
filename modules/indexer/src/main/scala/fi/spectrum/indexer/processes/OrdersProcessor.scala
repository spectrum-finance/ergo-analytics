package fi.spectrum.indexer.processes

import cats.data.NonEmptyList
import cats.syntax.foldable._
import cats.syntax.traverse._
import cats.{Foldable, Functor, Monad}
import fi.spectrum.indexer.config.ApplicationConfig
import fi.spectrum.indexer.services.Orders
import fi.spectrum.streaming.OrderEventsConsumer
import tofu.logging.Logging
import tofu.streams.{Chunks, Evals}
import tofu.syntax.logging._
import tofu.syntax.monadic._
import tofu.syntax.streams.all._
import tofu.syntax.context._

trait OrdersProcessor[S[_]] {
  def run: S[Unit]
}

object OrdersProcessor {

  def make[
    C[_]: Functor: Foldable,
    F[_]: Monad: ApplicationConfig.Has,
    S[_]: Evals[*[_], F]: Chunks[*[_], C]: Monad
  ](implicit order: OrderEventsConsumer[S, F], orders: Orders[F], logs: Logging.Make[F]): OrdersProcessor[S] =
    logs.forService[OrdersProcessor[S]].map(implicit __ => new Live[C, F, S])

  final private class Live[
    C[_]: Functor: Foldable,
    F[_]: Monad: Logging: ApplicationConfig.Has,
    S[_]: Evals[*[_], F]: Chunks[*[_], C]: Monad
  ](implicit
    order: OrderEventsConsumer[S, F],
    orders: Orders[F]
  ) extends OrdersProcessor[S] {

    def run: S[Unit] = eval(context).flatMap { config =>
      order.stream
        .chunksN(config.ordersBatchSize)
        .evalMap { events =>
          for {
            _ <- info"Got next orders events batch of size ${events.size}"
            _ <- NonEmptyList.fromList(events.toList.flatMap(_.message)) match {
                   case Some(value) => orders.process(value)
                   case None        => unit[F]
                 }
            _ <- events.toList.lastOption.traverse(_.commit).void
            _ <- info"Finished to process orders batch"
          } yield ()
        }
    }
  }
}
