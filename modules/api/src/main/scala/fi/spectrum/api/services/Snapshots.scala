package fi.spectrum.api.services

import cats.Monad
import cats.effect.Ref
import cats.effect.kernel.Sync
import derevo.derive
import fi.spectrum.api.db.models.amm.PoolSnapshot
import fi.spectrum.api.db.repositories.Pools
import tofu.doobie.transactor.Txr
import tofu.higherKind.Mid
import tofu.higherKind.derived.representableK
import tofu.lift.Lift
import tofu.logging.{Logging, Logs}
import tofu.syntax.doobie.txr._
import tofu.syntax.lift._
import tofu.syntax.logging._
import tofu.syntax.monadic._

@derive(representableK)
trait Snapshots[F[_]] {
  def update: F[Unit]
  def get: F[List[PoolSnapshot]]
}

object Snapshots {

  def make[I[_]: Sync, F[_]: Sync, D[_]](implicit
    txr: Txr[F, D],
    pools: Pools[D],
    lift: Lift[F, I],
    logs: Logs[I, F]
  ): I[Snapshots[F]] =
    for {
      implicit0(logging: Logging[F]) <- logs.forService[Snapshots[F]]
      cache                          <- Ref.in[I, F, List[PoolSnapshot]](List.empty)
      service = new Tracing[F] attach new Live[F, D](cache)
      _ <- service.update.lift
    } yield service

  final private class Live[F[_]: Monad, D[_]](cache: Ref[F, List[PoolSnapshot]])(implicit
    txr: Txr[F, D],
    pools: Pools[D]
  ) extends Snapshots[F] {

    def update: F[Unit] = for {
      snapshots <- pools.snapshots.trans
      _         <- cache.set(snapshots)
    } yield ()

    def get: F[List[PoolSnapshot]] = cache.get
  }

  final private class Tracing[F[_]: Monad: Logging] extends Snapshots[Mid[F, *]] {
    def update: Mid[F, Unit] = _ <* trace"It's time to update snapshots!"

    def get: Mid[F, List[PoolSnapshot]] = _ <* trace"Get current snapshots"
  }

}
