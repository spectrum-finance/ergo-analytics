package fi.spectrum.api.v1.services

import cats.data.OptionT
import cats.syntax.traverse._
import cats.{Functor, Monad, Parallel}
import derevo.derive
import fi.spectrum.api.currencies.UsdUnits
import fi.spectrum.api.db.models.amm.{PoolSnapshot, PoolVolumeSnapshot}
import fi.spectrum.api.db.repositories.Pools
import fi.spectrum.api.domain.TotalValueLocked
import fi.spectrum.api.modules.PriceSolver.FiatPriceSolver
import fi.spectrum.api.services._
import fi.spectrum.api.v1.endpoints.models.TimeWindow
import fi.spectrum.api.v1.endpoints.monthMillis
import fi.spectrum.api.v1.models.amm._
import fi.spectrum.api.v1.models.amm.types.MarketId
import fi.spectrum.graphite.Metrics
import tofu.doobie.transactor.Txr
import tofu.higherKind.Mid
import tofu.higherKind.derived.representableK
import tofu.logging.{Logging, Logs}
import tofu.syntax.doobie.txr._
import tofu.syntax.logging._
import tofu.syntax.monadic._
import tofu.syntax.foption._
import tofu.syntax.time.now.millis
import tofu.time.Clock

import scala.math.BigDecimal.RoundingMode

@derive(representableK)
trait PriceTracking[F[_]] {

  def getPairsCoinGecko: F[List[CoinGeckoPairs]]

  def getTickersCoinGecko: F[List[CoinGeckoTicker]]

  def getMarkets(window: TimeWindow): F[List[AmmMarketSummary]]

  def getCmcYFInfo: F[CMCYFInfo]
}

object PriceTracking {

  def make[I[_]: Functor, F[_]: Monad: Clock: Parallel, D[_]: Monad](implicit
    txr: Txr[F, D],
    pools: Pools[D],
    tokens: VerifiedTokens[F],
    snapshots: Snapshots[F],
    volumes24H: Volumes24H[F],
    solver: FiatPriceSolver[F],
    lmStats: LmStatsApi[F],
    lmStatss: LmStats[F],
    lmSnapshots: LMSnapshots[F],
    assets: Assets[F],
    metrics: Metrics[F],
    logs: Logs[I, F]
  ): I[PriceTracking[F]] =
    logs
      .forService[PriceTracking[F]]
      .map(implicit __ => new PriceTrackingMetrics[F] attach (new Tracing[F] attach new Live[F, D]))

  final class Live[F[_]: Monad: Clock: Parallel: Logging, D[_]: Monad](implicit
    txr: Txr[F, D],
    pools: Pools[D],
    tokens: VerifiedTokens[F],
    solver: FiatPriceSolver[F],
    snapshots: Snapshots[F],
    volumes24H: Volumes24H[F],
    lmStats: LmStatsApi[F],
    lmStatss: LmStats[F],
    lmSnapshots: LMSnapshots[F],
    assets: Assets[F]
  ) extends PriceTracking[F] {

    def getCmcYFInfo: F[CMCYFInfo] =
      evalPoolsCMCInfo
        .map { pools =>
          CMCYFInfo(
            "",
            "",
            "",
            List.empty,
            pools
          )
        }

    private def evalPoolsCMCInfo: F[List[PoolCMCInfo]] =
      for {
        poolSnapshots <- snapshots.get
        lmSnaps <- lmSnapshots.get
        apr <- lmStats.lmStatsApi
        assets <- assets.get
        poolsTvl <- poolSnapshots.flatTraverse(p => processPoolTvl(p).map(_.map(tvl => (tvl, p))).map(_.toList))
        poolsWithApr =
          apr.flatMap { lmPool =>
            lmSnaps.find(_.poolId == lmPool.poolId).flatMap { lm =>
              poolsTvl.find(_._2.id == lmPool.poolId).flatMap { case (tvl, snap) =>
                assets
                  .find(_.id == lm.reward.tokenId)
                  .flatMap(_.ticker)
                  .flatMap(rewardTicker =>
                    evalPairTicker(snap).flatMap { ticker =>
                      lmPool.yearProfit.map(apr => (rewardTicker.value, ticker, tvl, apr))
                    }
                  )
              }
            }
          }
      } yield poolsWithApr.map { case (reward, pair, tvl, apy) =>
        PoolCMCInfo("", pair, "", "", List(reward), apy.setScale(1), tvl.value.toLong)
      }

    private def evalPoolsCMCInfoTest: F[List[PoolCMCInfo]] =
      for {
        _ <- lmSnapshots.update
        ass           <- assets.update
        _             <- snapshots.update(ass)
        poolSnapshots <- snapshots.get
        _ = println(poolSnapshots)
        lmSnaps <- lmSnapshots.get
        _ <- lmStatss.update(poolSnapshots, 1000, lmSnaps)
        apr <- lmStats.lmStatsApi
        _ = println(apr)
        _ = println(lmSnaps)
        assets <- assets.get
        _ = println(assets)
        poolsTvl <- poolSnapshots.flatTraverse(p => processPoolTvl(p).map(_.map(tvl => (tvl, p))).map(_.toList))
        _ = println(poolsTvl)


        jj =  apr.flatMap { lmPool =>
          lmSnaps.find(_.poolId == lmPool.poolId).flatMap { lm =>
            poolsTvl.find(_._2.id == lmPool.poolId).map { case (tvl, snap) => (lmPool.yearProfit, lm, tvl, snap)}}}

        yy =  apr.flatMap { lmPool =>
            poolsTvl.find(_._2.id == lmPool.poolId).map { case (tvl, snap) => (lmPool.yearProfit, tvl, snap)}}

        _ = println(yy)

        poolsWithApr =
          apr.flatMap { lmPool =>
            lmSnaps.find(_.poolId == lmPool.poolId).flatMap { lm =>
              poolsTvl.find(_._2.id == lmPool.poolId).flatMap { case (tvl, snap) =>
                assets
                  .find(_.id == lm.reward.tokenId)
                  .flatMap(_.ticker)
                  .flatMap(rewardTicker =>
                    evalPairTicker(snap).flatMap { ticker =>
                      lmPool.yearProfit.map(apr => (rewardTicker.value, ticker, tvl, apr))
                    }
                  )
              }
            }
          }
      } yield poolsWithApr.map { case (reward, pair, tvl, apy) =>
        PoolCMCInfo("", pair, "", "", List(reward), apy.setScale(1), tvl.value.toLong)
      }

    private def processPoolTvl(pool: PoolSnapshot): F[Option[TotalValueLocked]] =
      (for {
        poolSnapshots <- OptionT.liftF(snapshots.get)
        lockedX       <- OptionT(solver.convert(pool.lockedX, UsdUnits, poolSnapshots))
        lockedY       <- OptionT(solver.convert(pool.lockedY, UsdUnits, poolSnapshots))
        tvl = TotalValueLocked(lockedX.value + lockedY.value, UsdUnits)
      } yield tvl).value

    private def evalPairTicker(pool: PoolSnapshot) =
      for {
        xTicker <- pool.lockedX.ticker
        yTicker <- pool.lockedY.ticker
      } yield s"${xTicker.value}-${yTicker.value}"

    def getPairsCoinGecko: F[List[CoinGeckoPairs]] =
      for {
        tw                     <- resolveTimeWindow(TimeWindow.empty)
        validTokens            <- tokens.get
        (volumesDB, snapshots) <- pools.volumes(tw).trans.flatMap(v => snapshots.get.map(v -> _))
        validated = snapshots.filter { ps =>
                      validTokens.contains(ps.lockedX.id) && validTokens.contains(ps.lockedY.id)
                    }
        poolVolumes    = volumesDB.flatMap(_.toPoolVolumeSnapshot(validated))
        volumesWithIds = poolVolumes.map(v => MarketId(v) -> v)
      } yield volumesWithIds
        .groupBy(_._1)
        .map { case (_, value) =>
          value.maxBy(s => s._2.volumeByX.amount + s._2.volumeByY.amount)
        }
        .toList
        .map { case (id, snapshot) =>
          CoinGeckoPairs(id, snapshot.volumeByX.id, snapshot.volumeByY.id, snapshot.poolId)
        }

    def getTickersCoinGecko: F[List[CoinGeckoTicker]] =
      volumes24H.get.flatMap { volumes =>
        snapshots.get.flatMap { snapshots =>
          volumes
            .map(v => MarketId(v) -> v)
            .groupBy(_._1)
            .map { case (_, value) =>
              value.maxBy(s => s._2.volumeByX.amount + s._2.volumeByY.amount)
            }
            .toList
            .traverse { case (id, snapshot) =>
              snapshots.find(_.id == snapshot.poolId).traverse { ammPool =>
                for {
                  xTvl <- solver
                            .convert(ammPool.lockedX, UsdUnits, snapshots)
                            .mapIn(_.value)
                            .map(_.getOrElse(BigDecimal(0)))
                  xVolume <- solver
                               .convert(snapshot.volumeByX, UsdUnits, snapshots)
                               .mapIn(_.value)
                               .map(_.getOrElse(BigDecimal(0)))
                  yVolume <- solver
                               .convert(snapshot.volumeByY, UsdUnits, snapshots)
                               .mapIn(_.value)
                               .map(_.getOrElse(BigDecimal(0)))
                } yield CoinGeckoTicker(
                  id,
                  snapshot.volumeByX.id,
                  snapshot.volumeByY.id,
                  (ammPool.lockedX.withDecimals / ammPool.lockedY.withDecimals).setScale(6, RoundingMode.HALF_UP),
                  xTvl * 2,
                  xVolume,
                  yVolume,
                  snapshot.poolId
                )
              }
            }
            .map(_.flatten)
        }
      }

    def getMarkets(window: TimeWindow): F[List[AmmMarketSummary]] =
      for {
        tw                     <- resolveTimeWindow(window)
        validTokens            <- tokens.get
        (volumesDB, snapshots) <- pools.volumes(tw).trans.flatMap(volumes => snapshots.get.map(volumes -> _))
        validSnaps = snapshots.filter(ps => validTokens.contains(ps.lockedX.id) && validTokens.contains(ps.lockedY.id))
        volumes    = volumesDB.flatMap(_.toPoolVolumeSnapshot(validSnaps))
        markets <- evalMarketsWithLiquidity(validSnaps, volumes, tw)
      } yield distinctMarkets(markets)

    private def evalMarketsWithLiquidity(
      pools: List[PoolSnapshot],
      volumes: List[PoolVolumeSnapshot],
      tw: TimeWindow
    ): F[List[(AmmMarketSummary, BigDecimal)]] =
      pools.flatTraverse { snapshot =>
        val currentOpt = volumes.find(_.poolId == snapshot.id)
        currentOpt
          .flatTraverse { vol =>
            val tx = snapshot.lockedX
            val ty = snapshot.lockedY
            val vx = vol.volumeByX
            val vy = vol.volumeByY
            solver.convert(tx, UsdUnits, pools).flatMap { xOpt =>
              xOpt.flatTraverse { xEq =>
                solver.convert(ty, UsdUnits, pools).map { yOpt =>
                  yOpt.map { yEq =>
                    (
                      AmmMarketSummary(tx, ty, vx, vy, tw),
                      xEq.value + yEq.value
                    )
                  }
                }
              }
            }
          }
          .map(_.toList)
      }

    private def distinctMarkets(markets: List[(AmmMarketSummary, BigDecimal)]): List[AmmMarketSummary] =
      markets
        .groupBy { case (market, _) => market.id }
        .map { case (_, markets) => markets.maxBy(_._2) }
        .toList
        .map(_._1)

    private def resolveTimeWindow(tw: TimeWindow): F[TimeWindow] = millis.map { now =>
      (tw.from, tw.to) match {
        case (Some(from), None) =>
          val maybeTo = from + monthMillis * 3
          val to      = if (maybeTo > now) now else maybeTo
          TimeWindow(Some(from), Some(to))
        case (None, Some(to)) => TimeWindow(Some(to - monthMillis * 3), Some(to))
        case (None, None)     => TimeWindow(Some(now - monthMillis * 3), Some(now))
        case _                => tw
      }
    }
  }

  final private class Tracing[F[_]: Monad: Logging] extends PriceTracking[Mid[F, *]] {

    def getPairsCoinGecko: Mid[F, List[CoinGeckoPairs]] =
      for {
        _ <- info"getPairsCoinGecko()"
        r <- _
        _ <- info"getPairsCoinGecko() finished"
        _ <- trace"getPairsCoinGecko() - $r"
      } yield r

    def getMarkets(window: TimeWindow): Mid[F, List[AmmMarketSummary]] =
      for {
        _ <- info"getMarkets($window)"
        r <- _
        _ <- info"getMarkets($window)"
        _ <- trace"getMarkets($window) - $r"
      } yield r

    def getCmcYFInfo: Mid[F, CMCYFInfo] =
      for {
        _ <- info"getCmcYFInfo()"
        r <- _
        _ <- info"getCmcYFInfo() finished"
        _ <- trace"getCmcYFInfo() - $r"
      } yield r

    def getTickersCoinGecko: Mid[F, List[CoinGeckoTicker]] =
      for {
        _ <- info"getTickersCoinGecko()"
        r <- _
        _ <- info"getTickersCoinGecko() finished"
        _ <- trace"getTickersCoinGecko() - $r"
      } yield r
  }

  final private class PriceTrackingMetrics[F[_]: Monad: Clock](implicit metrics: Metrics[F])
    extends PriceTracking[Mid[F, *]] {

    private val firstTxTs = 1628066832799L

    private def sendMetrics[B](window: TimeWindow, name: String, f: F[B]): F[B] =
      for {
        r     <- f
        finis <- millis
        _     <- metrics.sendTs(name, Math.abs(window.to.getOrElse(finis) - window.from.getOrElse(firstTxTs)).toDouble)
      } yield r

    def getMarkets(window: TimeWindow): Mid[F, List[AmmMarketSummary]] =
      sendMetrics(window, "window.getMarkets", _)

    def getCmcYFInfo: Mid[F, CMCYFInfo] = unit >> _

    def getPairsCoinGecko: Mid[F, List[CoinGeckoPairs]] = unit >> _

    def getTickersCoinGecko: Mid[F, List[CoinGeckoTicker]] = unit >> _
  }
}
