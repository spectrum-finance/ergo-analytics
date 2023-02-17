package fi.spectrum.api.services

import cats.Monad
import cats.effect.Ref
import cats.effect.kernel.Sync
import derevo.derive
import fi.spectrum.api.db.models.PoolSnapshotDB
import fi.spectrum.api.db.models.amm.PoolSnapshot
import fi.spectrum.api.db.repositories.Pools
import tofu.doobie.transactor.Txr
import tofu.higherKind.Mid
import tofu.higherKind.derived.representableK
import tofu.logging.{Logging, Logs}
import tofu.syntax.doobie.txr._
import tofu.syntax.logging._
import tofu.syntax.monadic._

@derive(representableK)
trait Snapshots[F[_]] {
  def update: F[Unit]
  def get: F[List[PoolSnapshotDB]]
}

object Snapshots {

  def make[I[_]: Sync, F[_]: Sync, D[_]](implicit
    txr: Txr[F, D],
    pools: Pools[D],
    logs: Logs[I, F]
  ): I[Snapshots[F]] =
    for {
      implicit0(logging: Logging[F]) <- logs.forService[Snapshots[F]]
      cache                          <- Ref.in[I, F, List[PoolSnapshotDB]](List.empty)
    } yield new Tracing[F] attach new Live[F, D](cache)

  final private class Live[F[_]: Monad, D[_]](cache: Ref[F, List[PoolSnapshotDB]])(implicit
    txr: Txr[F, D],
    pools: Pools[D]
  ) extends Snapshots[F] {

    def update: F[Unit] = for {
      snapshots <- pools.snapshots.trans
      _         <- cache.set(snapshots)
    } yield ()

    def get: F[List[PoolSnapshotDB]] = cache.get
  }

  final private class Tracing[F[_]: Monad: Logging] extends Snapshots[Mid[F, *]] {
    def update: Mid[F, Unit] = info"It's time to update snapshots!" >> _

    def get: Mid[F, List[PoolSnapshotDB]] = info"Get current snapshots" >> _
  }

}
