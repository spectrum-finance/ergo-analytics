package fi.spectrum.api.modules

import cats.{Functor, Monad}
import cats.effect.Clock
import derevo.derive
import fi.spectrum.api.db.models.amm.PoolInfo
import fi.spectrum.api.domain.{FeePercentProjection, Fees, TotalValueLocked}
import fi.spectrum.core.domain.order.PoolId
import tofu.higherKind.Mid
import tofu.higherKind.derived.representableK
import tofu.logging.{Logging, Logs}
import tofu.syntax.monadic._
import tofu.syntax.logging._
import tofu.syntax.time.now._

import scala.concurrent.duration.FiniteDuration
import scala.math.BigDecimal.RoundingMode

@derive(representableK)
trait AmmStatsMath[F[_]] {

  def feePercentProjection(
    poolId: PoolId,
    tvl: TotalValueLocked,
    fees: Fees,
    poolInfo: PoolInfo,
    projectionPeriod: FiniteDuration
  ): F[FeePercentProjection]
}

object AmmStatsMath {

  def make[I[_]: Functor, F[_]: Monad: Clock](implicit logs: Logs[I, F]): I[AmmStatsMath[F]] =
    logs.forService[AmmStatsMath[F]].map { implicit __ =>
      new Tracing[F] attach new Live[F]
    }

  final class Live[F[_]: Monad: Clock] extends AmmStatsMath[F] {

    def feePercentProjection(
      poolId: PoolId,
      tvl: TotalValueLocked,
      fees: Fees,
      poolInfo: PoolInfo,
      projectionPeriod: FiniteDuration
    ): F[FeePercentProjection] =
      for {
        windowSizeMillis <-
          for {
            ub <- fees.window.to.fold(millis[F])(_.pure[F])
            lb = fees.window.from.getOrElse(poolInfo.firstSwapTimestamp)
          } yield ub - lb
        periodFees =
          if (windowSizeMillis > 0) fees.value * (BigDecimal(projectionPeriod.toMillis) / windowSizeMillis)
          else BigDecimal(0)
        periodFeesPercent =
          if (tvl.value > 0) {
            (periodFees * 100 / tvl.value).setScale(2, RoundingMode.HALF_UP).toDouble
          } else .0
      } yield FeePercentProjection(periodFeesPercent)
  }

  final class Tracing[F[_]: Monad: Logging] extends AmmStatsMath[Mid[F, *]] {

    def feePercentProjection(
      poolId: PoolId,
      tvl: TotalValueLocked,
      fees: Fees,
      poolInfo: PoolInfo,
      projectionPeriod: FiniteDuration
    ): Mid[F, FeePercentProjection] =
      for {
        _ <-
          trace"feePercentProjection(poolId=$poolId,tvl=$tvl,fees=$fees,poolInfo=$poolInfo,projectionPeriod=$projectionPeriod)"
        r <- _
        _ <-
          trace"feePercentProjection(poolId=$poolId,tvl=$tvl,fees=$fees,poolInfo=$poolInfo,projectionPeriod=$projectionPeriod) -> $r"
      } yield r
  }
}
