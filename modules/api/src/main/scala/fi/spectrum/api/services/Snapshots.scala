package fi.spectrum.api.services

import cats.Monad
import cats.effect.Ref
import cats.effect.kernel.Sync
import derevo.derive
import fi.spectrum.api.db.models.amm.{AssetInfo, PoolSnapshot}
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
  def update(assets: List[AssetInfo]): F[List[PoolSnapshot]]
  def get: F[List[PoolSnapshot]]
}

object Snapshots {

  def make[I[_]: Sync, F[_]: Sync, D[_]](implicit
    txr: Txr[F, D],
    pools: Pools[D],
    logs: Logs[I, F]
  ): I[Snapshots[F]] =
    for {
      implicit0(logging: Logging[F]) <- logs.forService[Snapshots[F]]
      cache                          <- Ref.in[I, F, List[PoolSnapshot]](List.empty)
    } yield new Tracing[F] attach new Live[F, D](cache)

  final private class Live[F[_]: Monad, D[_]](cache: Ref[F, List[PoolSnapshot]])(implicit
    txr: Txr[F, D],
    pools: Pools[D]
  ) extends Snapshots[F] {

    def update(assets: List[AssetInfo]): F[List[PoolSnapshot]] = for {
      snapshots <- pools.snapshots.trans
      res = snapshots
              .map(_.toPoolSnapshot(assets))
              .sortBy(p => BigDecimal(p.lockedX.amount) * p.lockedY.amount)(Ordering[BigDecimal].reverse)
      _ <- cache.set(res)
    } yield res

    def get: F[List[PoolSnapshot]] = cache.get
  }

  final private class Tracing[F[_]: Monad: Logging] extends Snapshots[Mid[F, *]] {
    def update(assets: List[AssetInfo]): Mid[F, List[PoolSnapshot]] = info"It's time to update snapshots!" >> _

    def get: Mid[F, List[PoolSnapshot]] = trace"Get current snapshots" >> _
  }

}
