package fi.spectrum.api.modules

import cats.Monad
import cats.effect.Clock
import fi.spectrum.api.db.models.amm.PoolInfo
import fi.spectrum.api.domain.{FeePercentProjection, Fees, TotalValueLocked}
import tofu.syntax.monadic._
import tofu.syntax.time.now._

import scala.concurrent.duration.FiniteDuration
import scala.math.BigDecimal.RoundingMode

trait AmmStatsMath[F[_]] {

  def feePercentProjection(
    tvl: TotalValueLocked,
    fees: Fees,
    poolInfo: PoolInfo,
    projectionPeriod: FiniteDuration
  ): F[FeePercentProjection]
}

object AmmStatsMath {

  implicit def instance[F[_]: Monad: Clock]: AmmStatsMath[F] =
    (tvl: TotalValueLocked, fees: Fees, poolInfo: PoolInfo, projectionPeriod: FiniteDuration) =>
      for {
        windowSizeMillis <-
          for {
            ub <- fees.window.to.fold(millis[F])(_.pure[F])
            lb = fees.window.from.getOrElse(poolInfo.confirmedAt)
          } yield ub - lb
        periodFees =
          if (windowSizeMillis > 0) fees.value * (BigDecimal(projectionPeriod.toMillis) / windowSizeMillis)
          else BigDecimal(0)
        periodFeesPercent =
          if (tvl.value > 0) (periodFees * 100 / tvl.value).setScale(2, RoundingMode.HALF_UP).toDouble
          else .0
      } yield FeePercentProjection(periodFeesPercent)
}
