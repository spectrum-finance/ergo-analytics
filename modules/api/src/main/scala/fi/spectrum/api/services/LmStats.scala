package fi.spectrum.api.services

import cats.Monad
import cats.data.OptionT
import cats.effect.Sync
import cats.syntax.either._
import cats.syntax.traverse._
import fi.spectrum.api.currencies.UsdUnits
import fi.spectrum.api.db.models.amm.PoolSnapshot
import fi.spectrum.api.db.models.lm.LmPoolSnapshot
import fi.spectrum.api.db.repositories.AppCache
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
    logs: Logs[I, F],
    cache: AppCache[F]
  ): I[LmStats[F]] =
    for {
      implicit0(logging: Logging[F]) <- logs.forService[FeesSnapshots[F]]
    } yield new Tracing[F] attach new Live[F]

  final private class Live[F[_]: Monad](implicit
    solver: FiatPriceSolver[F],
    cache: AppCache[F]
  ) extends LmStats[F] {

    def get: F[List[LMPoolStat]] = cache.getLmPoolsStats

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
                val interestsRelation =
                  Either.catchNonFatal(rewardUds.value / (xUsd.value + yUsd.value)).getOrElse(BigDecimal(1))
                (interestsRelation / days * 365 * 100).setScale(0, RoundingMode.HALF_UP)
              }).value
            }
          yearProfit.map { profit =>
            LMPoolStat(pool.poolId, compoundedReward, profit)
          }
        }
        .flatMap(cache.setLmPoolsStats)
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
