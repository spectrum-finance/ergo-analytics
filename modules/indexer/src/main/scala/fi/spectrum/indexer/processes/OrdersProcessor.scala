package fi.spectrum.indexer.processes

import cats.data.NonEmptyList
import cats.syntax.foldable._
import cats.syntax.traverse._
import cats.{Foldable, Functor, Monad}
import fi.spectrum.indexer.db.persist.PersistBundle
import fi.spectrum.streaming.OrderEventsConsumer
import fi.spectrum.streaming.domain.OrderEvent
import tofu.doobie.transactor.Txr
import tofu.streams.{Chunks, Evals}
import tofu.syntax.doobie.txr._
import tofu.syntax.monadic._
import tofu.syntax.streams.all._

trait OrdersProcessor[S[_]] {
  def run: S[Unit]
}

object OrdersProcessor {

  def make[
    C[_]: Functor: Foldable,
    F[_]: Monad,
    S[_]: Evals[*[_], F]: Chunks[*[_], C]: Monad,
    D[_]: Monad
  ](implicit order: OrderEventsConsumer[S, F], bundle: PersistBundle[D], txr: Txr[F, D]): OrdersProcessor[S] =
    new Live[C, F, S, D]

  final private class Live[
    C[_]: Functor: Foldable,
    F[_]: Monad,
    S[_]: Evals[*[_], F]: Chunks[*[_], C]: Monad,
    D[_]: Monad
  ](implicit
    order: OrderEventsConsumer[S, F],
    bundle: PersistBundle[D],
    txr: Txr[F, D]
  ) extends OrdersProcessor[S] {

    def run: S[Unit] = order.stream
      .chunksN(10) //todo config
      .evalMap { events =>
        def insert: D[Int] =
          NonEmptyList
            .fromList(events.toList.map(_.message).collect { case OrderEvent.Apply(order) => order })
            .fold(0.pure[D])(v => bundle.insertAnyOrder.traverse(_(v)).map(_.sum))

        def resolve: D[Int] =
          NonEmptyList
            .fromList(events.toList.map(_.message).collect { case OrderEvent.Unapply(order) => order })
            .fold(0.pure[D])(v => bundle.resolveAnyOrder.traverse(_(v)).map(_.sum))

        (insert >> resolve).trans.void >> events.toList.lastOption.traverse(_.commit).void
      }
  }
}
