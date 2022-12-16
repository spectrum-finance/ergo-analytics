package fi.spectrum.indexer.processes

import cats.data.NonEmptyList
import cats.syntax.foldable._
import cats.syntax.traverse._
import cats.{Foldable, Functor, Monad}
import fi.spectrum.indexer.db.persist.PersistBundle
import fi.spectrum.streaming.PoolsEventsConsumer
import fi.spectrum.streaming.domain.PoolEvent
import tofu.doobie.transactor.Txr
import tofu.streams.{Chunks, Evals}
import tofu.syntax.doobie.txr._
import tofu.syntax.monadic._
import tofu.syntax.streams.all._

trait PoolsProcessor[S[_]] {
  def run: S[Unit]
}

object PoolsProcessor {

  def make[
    C[_]: Functor: Foldable,
    F[_]: Monad,
    S[_]: Evals[*[_], F]: Chunks[*[_], C]: Monad,
    D[_]: Monad
  ](implicit pools: PoolsEventsConsumer[S, F], bundle: PersistBundle[D], txr: Txr[F, D]): PoolsProcessor[S] =
    new Live[C, F, S, D]

  final private class Live[
    C[_]: Functor: Foldable,
    F[_]: Monad,
    S[_]: Evals[*[_], F]: Chunks[*[_], C]: Monad,
    D[_]: Monad
  ](implicit
    pools: PoolsEventsConsumer[S, F],
    bundle: PersistBundle[D],
    txr: Txr[F, D]
  ) extends PoolsProcessor[S] {

    def run: S[Unit] = pools.stream
      .chunksN(10) //todo config
      .evalMap { events =>
        def insert: D[Int] =
          NonEmptyList
            .fromList(events.toList.map(_.message).collect { case PoolEvent.Apply(pool) => pool })
            .fold(0.pure[D])(bundle.pools.insert)

        def resolve: D[Int] =
          NonEmptyList
            .fromList(events.toList.map(_.message).collect { case PoolEvent.Unapply(pool) => pool })
            .fold(0.pure[D])(bundle.pools.resolve)

        (insert >> resolve).trans.void >> events.toList.lastOption.traverse(_.commit).void
      }
  }
}
