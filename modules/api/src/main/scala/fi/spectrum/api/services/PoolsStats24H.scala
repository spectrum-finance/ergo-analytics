package fi.spectrum.api.services

import cats.data.OptionT
import cats.effect.kernel.Sync
import cats.syntax.parallel._
import cats.{Monad, Parallel}
import derevo.derive
import fi.spectrum.api.currencies.UsdUnits
import fi.spectrum.api.db.models.amm.{PoolFeesSnapshot, PoolSnapshot, PoolVolumeSnapshot}
import fi.spectrum.api.db.repositories.{AppCache, Pools}
import fi.spectrum.api.domain.{Fees, TotalValueLocked, Volume}
import fi.spectrum.api.modules.AmmStatsMath
import fi.spectrum.api.modules.PriceSolver.FiatPriceSolver
import fi.spectrum.api.v1.endpoints.models.TimeWindow
import fi.spectrum.api.v1.models.amm.PoolStats
import tofu.doobie.transactor.Txr
import tofu.higherKind.Mid
import tofu.higherKind.derived.representableK
import tofu.logging.{Logging, Logs}
import tofu.syntax.doobie.txr._
import tofu.syntax.logging._
import tofu.syntax.monadic._
import tofu.syntax.time.now.millis
import tofu.time.Clock

import scala.concurrent.duration.{DurationInt, FiniteDuration}

@derive(representableK)
trait PoolsStats24H[F[_]] {
  def update(poolsList: List[PoolSnapshot], volumes: List[PoolVolumeSnapshot], fees: List[PoolFeesSnapshot]): F[Unit]

  def get: F[List[PoolStats]]

  def processPoolFee(
    feesSnap: Option[PoolFeesSnapshot],
    window: TimeWindow,
    poolSnapshots: List[PoolSnapshot]
  ): F[Option[Fees]]

  def processPoolVolume(
    vol: Option[PoolVolumeSnapshot],
    window: TimeWindow,
    poolSnapshots: List[PoolSnapshot]
  ): F[Option[Volume]]
}

object PoolsStats24H {

  private val MillisInYear: FiniteDuration = 365.days

  private val day: Int = 3600000 * 24

  def make[I[_]: Sync, F[_]: Sync: Clock: Parallel, D[_]](implicit
    txr: Txr[F, D],
    pools: Pools[D],
    solver: FiatPriceSolver[F],
    ammMath: AmmStatsMath[F],
    cache: AppCache[F],
    logs: Logs[I, F]
  ): I[PoolsStats24H[F]] =
    for {
      implicit0(logging: Logging[F]) <- logs.forService[PoolsStats24H[F]]
    } yield new Tracing[F] attach new Live[F, D]

  final private class Live[F[_]: Monad: Clock: Parallel, D[_]](implicit
    txr: Txr[F, D],
    pools: Pools[D],
    solver: FiatPriceSolver[F],
    ammMath: AmmStatsMath[F],
    cache: AppCache[F]
  ) extends PoolsStats24H[F] {

    def update(
      poolsList: List[PoolSnapshot],
      volumes: List[PoolVolumeSnapshot],
      fees: List[PoolFeesSnapshot]
    ): F[Unit] =
      millis
        .flatMap { now =>
          val window = TimeWindow(Some(now - day), Some(now))
          poolsList.map { pool =>
            def run: OptionT[F, PoolStats] = for {
              info <- OptionT(pools.getFirstPoolSwapTime(pool.id).trans)
              fee = fees.find(_.poolId == pool.id)
              vol = volumes.find(_.poolId == pool.id)
              lockedX <- OptionT(solver.convert(pool.lockedX, UsdUnits, poolsList))
              lockedY <- OptionT(solver.convert(pool.lockedY, UsdUnits, poolsList))
              tvl = TotalValueLocked(lockedX.value + lockedY.value, UsdUnits)
              volume            <- OptionT(processPoolVolume(vol, window, poolsList))
              fees              <- OptionT(processPoolFee(fee, window, poolsList))
              yearlyFeesPercent <- OptionT.liftF(ammMath.feePercentProjection(pool.id, tvl, fees, info, MillisInYear))
            } yield PoolStats(pool.id, pool.lockedX, pool.lockedY, tvl, volume, fees, yearlyFeesPercent)

            run.value
          }.parSequence
        }
        .map(_.flatten)
        .flatMap(cache.setPoolsStat24H)

    def processPoolVolume(
      vol: Option[PoolVolumeSnapshot],
      window: TimeWindow,
      poolSnapshots: List[PoolSnapshot]
    ): F[Option[Volume]] =
      (vol match {
        case Some(vol) =>
          for {
            volX <- OptionT(solver.convert(vol.volumeByX, UsdUnits, poolSnapshots))
            volY <- OptionT(solver.convert(vol.volumeByY, UsdUnits, poolSnapshots))
          } yield Volume(volX.value + volY.value, UsdUnits, window)
        case None => OptionT.pure[F](Volume.empty(UsdUnits, window))
      }).value

    def processPoolFee(
      feesSnap: Option[PoolFeesSnapshot],
      window: TimeWindow,
      poolSnapshots: List[PoolSnapshot]
    ): F[Option[Fees]] = (feesSnap match {
      case Some(feesSnap) =>
        for {
          feesX <- OptionT(solver.convert(feesSnap.feesByX, UsdUnits, poolSnapshots))
          feesY <- OptionT(solver.convert(feesSnap.feesByY, UsdUnits, poolSnapshots))
        } yield Fees(feesX.value + feesY.value, UsdUnits, window)
      case None => OptionT.pure[F](Fees.empty(UsdUnits, window))
    }).value

    def get: F[List[PoolStats]] = cache.getPoolsStat24H

  }

  final private class Tracing[F[_]: Monad: Logging: Clock] extends PoolsStats24H[Mid[F, *]] {
    def get: Mid[F, List[PoolStats]] = trace"Get current pools stats" >> _

    def update(
      poolsList: List[PoolSnapshot],
      volumes: List[PoolVolumeSnapshot],
      fees: List[PoolFeesSnapshot]
    ): Mid[F, Unit] =
      for {
        _      <- info"It's time to update pools stats!"
        start  <- millis
        r      <- _
        finish <- millis
        _      <- info"Pools stats updated. It took: ${finish - start}ms"
      } yield r

    def processPoolFee(
      feesSnap: Option[PoolFeesSnapshot],
      window: TimeWindow,
      poolSnapshots: List[PoolSnapshot]
    ): Mid[F, Option[Fees]] = trace"processPoolFee()" >> _

    def processPoolVolume(
      vol: Option[PoolVolumeSnapshot],
      window: TimeWindow,
      poolSnapshots: List[PoolSnapshot]
    ): Mid[F, Option[Volume]] = trace"processPoolVolume()" >> _
  }
}
