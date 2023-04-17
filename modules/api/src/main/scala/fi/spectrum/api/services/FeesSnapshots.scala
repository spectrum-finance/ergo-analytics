package fi.spectrum.api.services

import cats.effect.kernel.Sync
import cats.syntax.parallel._
import cats.{Applicative, Monad, Parallel}
import fi.spectrum.api.db.models.amm.{PoolFeesSnapshot, PoolSnapshot}
import fi.spectrum.api.db.repositories.{AppCache, Pools}
import fi.spectrum.api.v1.endpoints.models.TimeWindow
import tofu.doobie.transactor.Txr
import tofu.higherKind.{Mid, RepresentableK}
import tofu.logging.{Logging, Logs}
import tofu.syntax.doobie.txr._
import tofu.syntax.logging._
import tofu.syntax.monadic._
import tofu.syntax.time.now.millis
import tofu.time.Clock

trait FeesSnapshots[F[_]] {
  def update(assets: List[PoolSnapshot]): F[(List[PoolFeesSnapshot], List[PoolFeesSnapshot])]

  def get: F[List[PoolFeesSnapshot]]
}

object FeesSnapshots {

  implicit def representableK: RepresentableK[FeesSnapshots] =
    tofu.higherKind.derived.genRepresentableK

  private val day: Long = 3600000 * 24

  def make[I[_]: Sync, F[_]: Sync: Clock: Parallel, D[_]: Applicative](implicit
    txr: Txr[F, D],
    pools: Pools[D],
    cache: AppCache[F],
    logs: Logs[I, F]
  ): I[FeesSnapshots[F]] =
    for {
      implicit0(logging: Logging[F]) <- logs.forService[FeesSnapshots[F]]
    } yield new Tracing[F] attach new Live[F, D]

  final private class Live[F[_]: Monad: Clock: Parallel, D[_]: Applicative](implicit
    txr: Txr[F, D],
    pools: Pools[D],
    cache: AppCache[F]
  ) extends FeesSnapshots[F] {

    def update(poolsList: List[PoolSnapshot]): F[(List[PoolFeesSnapshot], List[PoolFeesSnapshot])] = {
      def query(gap: Long): F[List[PoolFeesSnapshot]] = millis.flatMap { now =>
        poolsList
          .parTraverse(pools.fees(_, TimeWindow(Some(now - gap), Some(now))).trans)
          .map(_.flatten)
      }

      for {
        fee1d  <- query(day).flatTap(cache.setPoolFeeSnapshots)
        fee30d <- query(day * 30)
      } yield (fee1d, fee30d)
    }

    def get: F[List[PoolFeesSnapshot]] = cache.getPoolFeeSnapshots
  }

  final private class Tracing[F[_]: Monad: Logging: Clock] extends FeesSnapshots[Mid[F, *]] {

    def update(assets: List[PoolSnapshot]): Mid[F, (List[PoolFeesSnapshot], List[PoolFeesSnapshot])] =
      for {
        _      <- info"It's time to update fees!"
        start  <- millis
        r      <- _
        finish <- millis
        _      <- info"Fees updated. It took: ${finish - start}ms"
      } yield r

    def get: Mid[F, List[PoolFeesSnapshot]] = trace"Get current fees" >> _
  }

}
