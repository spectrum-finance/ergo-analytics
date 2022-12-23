package fi.spectrum.indexer.services

import cats.Monad
import cats.data.NonEmptyList
import derevo.derive
import fi.spectrum.core.domain.pool.Pool.AmmPool
import fi.spectrum.indexer.db.persist.PersistBundle
import fi.spectrum.streaming.domain.PoolEvent
import tofu.doobie.transactor.Txr
import tofu.higherKind.Mid
import tofu.higherKind.derived.representableK
import tofu.logging.Logging
import tofu.syntax.doobie.txr._
import tofu.syntax.logging._
import tofu.syntax.monadic._

@derive(representableK)
trait Pools[F[_]] {
  def process(events: NonEmptyList[PoolEvent]): F[Unit]
}

object Pools {

  def make[F[_]: Monad, D[_]: Monad](implicit
    bundle: PersistBundle[D],
    txr: Txr[F, D],
    assets: AssetsService[F],
    logs: Logging.Make[F]
  ): Pools[F] =
    logs.forService[Pools[F]].map(implicit __ => new Tracing[F] attach new Live[F, D])

  final private class Live[F[_]: Monad, D[_]: Monad](implicit
    bundle: PersistBundle[D],
    txr: Txr[F, D],
    assets: AssetsService[F]
  ) extends Pools[F] {

    def process(events: NonEmptyList[PoolEvent]): F[Unit] = {
      def run: D[Int] = events
        .traverse {
          case PoolEvent.Apply(pool)   => bundle.pools.insert(pool)
          case PoolEvent.Unapply(pool) => bundle.pools.resolve(pool)
        }
        .map(_.toList.sum)

      def tokens: F[Unit] = NonEmptyList.fromList(events.collect { case PoolEvent.Apply(pool: AmmPool) =>
        List(pool.lp.tokenId, pool.x.tokenId, pool.y.tokenId)
      }.flatten) match {
        case Some(value) => assets.process(value)
        case None        => unit[F]
      }

      run.trans >> tokens
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
