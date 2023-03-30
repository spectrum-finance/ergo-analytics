package fi.spectrum.api.services

import cats.Monad
import cats.effect.kernel.Sync
import derevo.derive
import fi.spectrum.api.db.models.amm.{PoolSnapshot, PoolVolumeSnapshot}
import fi.spectrum.api.db.repositories.{AppCache, Pools}
import fi.spectrum.api.v1.endpoints.models.TimeWindow
import tofu.doobie.transactor.Txr
import tofu.higherKind.Mid
import tofu.higherKind.derived.representableK
import tofu.logging.{Logging, Logs}
import tofu.syntax.doobie.txr._
import tofu.syntax.logging._
import tofu.syntax.monadic._
import tofu.syntax.time.now.millis
import tofu.time.Clock

@derive(representableK)
trait Volumes24H[F[_]] {
  def update(snapshots: List[PoolSnapshot]): F[List[PoolVolumeSnapshot]]

  def get: F[List[PoolVolumeSnapshot]]
}

object Volumes24H {

  private val day: Int = 3600000 * 24

  def make[I[_]: Sync, F[_]: Sync: Clock, D[_]](implicit
                                                txr: Txr[F, D],
                                                pools: Pools[D],
                                                cache: AppCache[F],
                                                logs: Logs[I, F]
  ): I[Volumes24H[F]] =
    for {
      implicit0(logging: Logging[F]) <- logs.forService[Volumes24H[F]]
    } yield new Tracing[F] attach new Live[F, D]

  final private class Live[F[_]: Monad: Clock, D[_]](implicit
    txr: Txr[F, D],
    pools: Pools[D],
    cache: AppCache[F]
  ) extends Volumes24H[F] {

    def update(snapshots: List[PoolSnapshot]): F[List[PoolVolumeSnapshot]] = for {
      timestamp <- millis
      volumes   <- pools.volumes(TimeWindow(Some(timestamp - day), Some(timestamp))).trans
      res = volumes.flatMap(_.toPoolVolumeSnapshot(snapshots))
      _ <- cache.setVolume24(res)
    } yield res

    def get: F[List[PoolVolumeSnapshot]] = cache.getVolume24
  }

  final private class Tracing[F[_]: Monad: Logging] extends Volumes24H[Mid[F, *]] {

    def update(snapshots: List[PoolSnapshot]): Mid[F, List[PoolVolumeSnapshot]] =
      trace"It's time to update volumes!" >> _

    def get: Mid[F, List[PoolVolumeSnapshot]] = trace"Get current volumes" >> _
  }

}
