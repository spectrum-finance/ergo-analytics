package fi.spectrum.api.v1.services

import cats.data.NonEmptyList
import cats.syntax.option._
import cats.syntax.either._
import cats.{Functor, Monad}
import fi.spectrum.api.db.models.lm.{UserCompound, UserInterest}
import fi.spectrum.api.db.repositories.LM
import fi.spectrum.api.services.{Height, LMSnapshots, LmStats}
import fi.spectrum.api.v1.models.AssetAmountApi
import fi.spectrum.api.v1.models.lm._
import fi.spectrum.core.domain.Address
import fi.spectrum.core.domain.address._
import org.ergoplatform.ErgoAddressEncoder
import tofu.doobie.transactor.Txr
import tofu.higherKind.{Mid, RepresentableK}
import tofu.logging.{Logging, Logs}
import tofu.syntax.doobie.txr._
import tofu.syntax.logging._
import tofu.syntax.monadic._
import tofu.time.Clock

import scala.math.BigDecimal.RoundingMode

trait LmStatsApi[F[_]] {
  def lmStatsApi: F[List[LMPoolStat]]

  def userLmStats(addresses: List[Address]): F[UserLmStats]
}

object LmStatsApi {

  implicit def representableK: RepresentableK[LmStatsApi] =
    tofu.higherKind.derived.genRepresentableK

  def make[I[_]: Functor, F[_]: Monad: Clock, D[_]: Monad](implicit
    lm: LM[D],
    txr: Txr[F, D],
    lmSnapshots: LMSnapshots[F],
    lmStats: LmStats[F],
    e: ErgoAddressEncoder,
    height: Height[F],
    logs: Logs[I, F]
  ): I[LmStatsApi[F]] =
    logs.forService[LmStatsApi[F]].map(implicit __ => new Tracing[F] attach new Live[F, D])

  final private class Live[F[_]: Monad, D[_]: Monad](implicit
    lm: LM[D],
    txr: Txr[F, D],
    lmSnapshots: LMSnapshots[F],
    lmStats: LmStats[F],
    e: ErgoAddressEncoder,
    height: Height[F]
  ) extends LmStatsApi[F] {

    def lmStatsApi: F[List[LMPoolStat]] = lmStats.get

    def userLmStats(addresses: List[Address]): F[UserLmStats] =
      NonEmptyList.fromList(addresses.flatMap(formPKRedeemer)) match {
        case Some(value) =>
          def query: D[(List[UserCompound], List[UserInterest])] = for {
            userStake    <- lm.userCompounds(value.map(_.value))
            userInterest <- lm.userInterest(value)
          } yield (userStake, userInterest)

          for {
            (userStake, userInterest) <- query.trans
            lmPools                   <- lmSnapshots.get
            height                    <- height.getCurrent
          } yield {
            val userStakes = userStake
              .flatMap { compound =>
                lmPools.find(_.poolId == compound.poolId).flatMap {
                  case pool if pool.lastBlockHeight > height =>
                    val epochsToCompound = pool.epochNum - (pool.epochIndex.getOrElse(pool.epochNum) + 1)
                    val releasedTMP      = compound.tmp.amount - epochsToCompound * compound.vLq.amount
                    val actualTMP        = 0x7fffffffffffffffL - pool.tmp.amount - (pool.lq.amount - 1L) * epochsToCompound
                    val allocRem =
                      pool.reward.amount - BigDecimal(pool.programBudget) * epochsToCompound / pool.epochNum - 1L
                    val reward =
                      Either.catchNonFatal(allocRem * releasedTMP / actualTMP - 1L).toOption.getOrElse(BigDecimal(0))
                    (compound.poolId, reward.setScale(0, RoundingMode.HALF_UP)).some
                  case _ => none
                }
              }
              .groupBy(_._1)
              .map { case (id, value) =>
                UserNextStakeReward(id, value.map(_._2).sum.toString())
              }
              .toList
            val userInterests = userInterest.map { interest =>
              UserCompoundResult(interest.poolId, AssetAmountApi.fromAssetAmount(interest.reward))
            }
            UserLmStats(userStakes, userInterests)
          }

        case None => UserLmStats(List.empty, List.empty).pure[F]
      }
  }

  final private class Tracing[F[_]: Monad: Logging] extends LmStatsApi[Mid[F, *]] {

    def lmStatsApi: Mid[F, List[LMPoolStat]] =
      for {
        _ <- info"lmStatsApi()"
        r <- _
        _ <- info"lmStatsApi() - ${r.toString}"
      } yield r

    def userLmStats(addresses: List[Address]): Mid[F, UserLmStats] =
      for {
        _ <- info"userLmStats()"
        r <- _
        _ <- info"userLmStats() - ${r.toString}"
      } yield r
  }
}
