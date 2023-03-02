package fi.spectrum.api.services

import cats.Monad
import cats.data.OptionT
import cats.effect.{Ref, Sync}
import cats.syntax.traverse._
import fi.spectrum.api.currencies.UsdUnits
import fi.spectrum.api.db.models.amm.PoolSnapshot
import fi.spectrum.api.db.models.lm.LmPoolSnapshot
import fi.spectrum.api.models.FullAsset
import fi.spectrum.api.modules.PriceSolver.FiatPriceSolver
import fi.spectrum.api.v1.models.lm.LMPoolStat
import tofu.higherKind.{Mid, RepresentableK}
import tofu.logging.{Logging, Logs}
import tofu.syntax.foption._
import tofu.syntax.logging._
import tofu.syntax.monadic._
import tofu.syntax.time.now.millis
import tofu.time.Clock

import scala.concurrent.duration.DurationInt
import scala.math.BigDecimal.RoundingMode

trait LmStats[F[_]] {
  def get: F[List[LMPoolStat]]
  def update(ammPools: List[PoolSnapshot], currentHeight: Int, lmPools: List[LmPoolSnapshot]): F[Unit]
}

object LmStats {

  implicit def representableK: RepresentableK[LmStats] =
    tofu.higherKind.derived.genRepresentableK

  def make[I[_]: Sync, F[_]: Sync: Clock](implicit
    solver: FiatPriceSolver[F],
    logs: Logs[I, F]
  ): I[LmStats[F]] =
    for {
      implicit0(logging: Logging[F]) <- logs.forService[Fees24H[F]]
      cache                          <- Ref.in[I, F, List[LMPoolStat]](List.empty)
    } yield new Tracing[F] attach new Live[F](cache)

  final private class Live[F[_]: Monad](cache: Ref[F, List[LMPoolStat]])(implicit
    solver: FiatPriceSolver[F]
  ) extends LmStats[F] {

    def get: F[List[LMPoolStat]] = cache.get

    def update(ammPools: List[PoolSnapshot], currentHeight: Int, lmPools: List[LmPoolSnapshot]): F[Unit] =
      lmPools
        .traverse { pool =>
          val compoundedReward = (BigDecimal(pool.programBudget - pool.reward.amount) / pool.programBudget * 100)
            .setScale(2, RoundingMode.HALF_UP)

          val yearProfit =
            if (currentHeight > pool.lastBlockHeight) noneF[F, BigDecimal]
            else {
              (for {
                ammPool <- OptionT.fromOption[F](ammPools.find(_.lp.tokenId == pool.lq.tokenId))
                (x, y) = ammPool.shares(pool.lq)
                xUsd      <- OptionT(solver.convert(x, UsdUnits, ammPools))
                yUsd      <- OptionT(solver.convert(y, UsdUnits, ammPools))
                rewardUds <- OptionT(solver.convert(FullAsset.fromAssetAmount(pool.reward), UsdUnits, ammPools))
              } yield {
                val programBlocksLeft = pool.lastBlockHeight - currentHeight
                val days = {
                  val raw = (programBlocksLeft * 2.minutes).toDays
                  if (raw == 0) 1 else raw
                }
                val interestsRelation = rewardUds.value / (xUsd.value + yUsd.value)
                (interestsRelation / days * 365 * 100).setScale(0, RoundingMode.HALF_UP)
              }).value
            }
          yearProfit.map { profit =>
            LMPoolStat(pool.poolId, compoundedReward, profit)
          }
        }
        .flatMap(cache.set)
  }

  final private class Tracing[F[_]: Monad: Logging: Clock] extends LmStats[Mid[F, *]] {

    def update(ammPools: List[PoolSnapshot], currentHeight: Int, lmPools: List[LmPoolSnapshot]): Mid[F, Unit] =
      for {
        _      <- info"It's time to update lm pools stats!"
        start  <- millis
        r      <- _
        finish <- millis
        _      <- info"Lm pools stats updated. It took: ${finish - start}ms"
      } yield r

    def get: Mid[F, List[LMPoolStat]] = trace"Get current lm pools stats" >> _
  }
}
