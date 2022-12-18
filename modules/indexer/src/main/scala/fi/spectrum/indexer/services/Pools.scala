package fi.spectrum.indexer.services

import cats.data.NonEmptyList
import cats.{Functor, Monad}
import derevo.derive
import fi.spectrum.indexer.db.persist.PersistBundle
import fi.spectrum.streaming.domain.PoolEvent
import tofu.doobie.transactor.Txr
import tofu.higherKind.Mid
import tofu.higherKind.derived.representableK
import tofu.logging.{Logging, Logs}
import tofu.syntax.doobie.txr._
import tofu.syntax.monadic._
import tofu.syntax.logging._

@derive(representableK)
trait Pools[F[_]] {
  def process(events: NonEmptyList[PoolEvent]): F[Unit]
}

object Pools {

  def make[F[_]: Monad, D[_]: Monad](implicit bundle: PersistBundle[D], txr: Txr[F, D], logs: Logs[F, F]): F[Pools[F]] =
    logs.forService[Pools[F]].map(implicit __ => new Tracing[F] attach new Live[F, D])

  final private class Live[F[_]: Functor, D[_]: Monad](implicit bundle: PersistBundle[D], txr: Txr[F, D])
    extends Pools[F] {

    def process(events: NonEmptyList[PoolEvent]): F[Unit] = {
      def insert: D[Int] =
        NonEmptyList
          .fromList(events.collect { case PoolEvent.Apply(order) => order })
          .fold(0.pure[D])(bundle.pools.insert(_))

      def resolve: D[Int] =
        NonEmptyList
          .fromList(events.collect { case PoolEvent.Unapply(order) => order })
          .fold(0.pure[D])(bundle.pools.resolve(_))

      (insert >> resolve).trans.void
    }
  }

  final private class Tracing[F[_]: Monad: Logging] extends Pools[Mid[F, *]] {

    def process(events: NonEmptyList[PoolEvent]): Mid[F, Unit] =
      for {
        _ <- info"Going to process next pool events: $events"
        r <- _
        _ <- info"Pools result is: $r"
      } yield r
  }
}
