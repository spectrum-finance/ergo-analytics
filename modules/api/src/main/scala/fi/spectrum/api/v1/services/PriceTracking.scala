package fi.spectrum.api.v1.services

import cats.syntax.traverse._
import cats.{Functor, Monad, Parallel}
import derevo.derive
import fi.spectrum.api.currencies.UsdUnits
import fi.spectrum.api.db.models.amm.{PoolSnapshot, PoolVolumeSnapshot}
import fi.spectrum.api.db.repositories.Pools
import fi.spectrum.api.modules.PriceSolver.FiatPriceSolver
import fi.spectrum.api.services._
import fi.spectrum.api.v1.endpoints.models.{CMCMarket, CoinGeckoPairs, CoinGeckoTicker, TimeWindow}
import fi.spectrum.api.v1.endpoints.monthMillis
import fi.spectrum.api.v1.models.amm._
import fi.spectrum.graphite.Metrics
import tofu.doobie.transactor.Txr
import tofu.higherKind.Mid
import tofu.higherKind.derived.representableK
import tofu.logging.{Logging, Logs}
import tofu.syntax.doobie.txr._
import tofu.syntax.logging._
import tofu.syntax.monadic._
import tofu.syntax.time.now.millis
import tofu.time.Clock
import fi.spectrum.api.v1.models.amm.types.MarketId
import tofu.syntax.foption._
import fi.spectrum.api.db.models.lm.LmPoolSnapshot
import fi.spectrum.api.v1.models.lm.LMPoolStat
import scala.math.BigDecimal.RoundingMode
import fi.spectrum.api.db.models.amm.AssetInfo
import cats.data.OptionT

@derive(representableK)
trait PriceTracking[F[_]] {

  def getVerifiedMarkets: F[List[CMCMarket]]

  def getMarkets(tw: TimeWindow): F[List[AmmMarketSummary]]

  def getPairsCoinGecko: F[List[CoinGeckoPairs]]

  def getTickersCoinGecko: F[List[CoinGeckoTicker]]

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
    metrics: Metrics[F],
    lmStats: LmStatsApi[F],
    lmSnapshots: LMSnapshots[F],
    assets: Assets[F],
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
    lmSnapshots: LMSnapshots[F],
    assets: Assets[F]
  ) extends PriceTracking[F] {

    def getCmcYFInfo: F[CMCYFInfo] =
      for {
        ammPools    <- snapshots.get
        lmPools     <- lmSnapshots.get
        assets      <- assets.get
        lmPoolStats <- lmStats.lmStatsApi
        result      <- lmPools.flatTraverse(snap => processLmPoolInfo(snap, assets, lmPoolStats, ammPools).map(_.toList))
      } yield CMCYFInfo(
        "",
        "",
        "",
        List.empty,
        result
      )

    private def processLmPoolInfo(
      pool: LmPoolSnapshot,
      assets: List[AssetInfo],
      poolStats: List[LMPoolStat],
      ammPools: List[PoolSnapshot]
    ) =
      (for {
        ammPool      <- OptionT.fromOption[F](ammPools.find(_.lp.tokenId == pool.lq.tokenId))
        apy          <- OptionT.fromOption[F](poolStats.find(_.poolId == pool.poolId).flatMap(_.yearProfit))
        rewardTicker <- OptionT.fromOption[F](assets.find(_.id == pool.reward.tokenId).flatMap(_.ticker))
        lockedX      <- OptionT(solver.convert(ammPool.lockedX, UsdUnits, ammPools))
        lockedY      <- OptionT(solver.convert(ammPool.lockedY, UsdUnits, ammPools))
        pairTicker   <- OptionT.fromOption[F](evalPairTicker(ammPool))
        tvl    = lockedX.value + lockedY.value
        staked = tvl.toLong * pool.lq.amount / (Long.MaxValue - ammPool.lp.amount)
      } yield PoolCMCInfo(
        "",
        pairTicker,
        "",
        "",
        List(rewardTicker.value),
        apy.setScale(1),
        staked
      )).value

    private def evalPairTicker(pool: PoolSnapshot) =
      for {
        xTicker <- pool.lockedX.ticker
        yTicker <- pool.lockedY.ticker
      } yield s"${xTicker.value}-${yTicker.value}"

    def getPairsCoinGecko: F[List[CoinGeckoPairs]] =
      for {
        tw          <- resolveTimeWindow(TimeWindow.empty)
        validTokens <- tokens.get
        volumesDB   <- pools.volumes(tw).trans
        snapshots   <- snapshots.get
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

    def getMarkets(tw: TimeWindow): F[List[AmmMarketSummary]] =
      pools
        .volumes(tw)
        .trans
        .flatMap(volumes => snapshots.get.map(volumes -> _))
        .map { case (volumesDB, snapshots) =>
          val volumes = volumesDB.flatMap(_.toPoolVolumeSnapshot(snapshots))
          snapshots.flatMap { snapshot =>
            val currentOpt = volumes
              .find(_.poolId == snapshot.id)

            currentOpt.toList.map { vol =>
              AmmMarketSummary(
                snapshot.lockedX,
                snapshot.lockedY,
                vol.volumeByX,
                vol.volumeByY,
                tw
              )
            }
          }
        }

    def getVerifiedMarkets: F[List[CMCMarket]] =
      for {
        validTokens <- tokens.get
        volumes     <- volumes24H.get
        snapshots   <- snapshots.get
        validSnaps = snapshots.filter(ps => validTokens.contains(ps.lockedX.id) && validTokens.contains(ps.lockedY.id))
        markets <- evalMarketsWithLiquidity(validSnaps, volumes)
      } yield distinctMarkets(markets)

    private def evalMarketsWithLiquidity(
      pools: List[PoolSnapshot],
      volumes: List[PoolVolumeSnapshot]
    ): F[List[(CMCMarket, BigDecimal)]] =
      pools.flatTraverse { snapshot =>
        val currentOpt = volumes.find(_.poolId == snapshot.id)
        currentOpt
          .traverse { vol =>
            val tx = snapshot.lockedX
            val ty = snapshot.lockedY
            val vx = vol.volumeByX
            val vy = vol.volumeByY
            for {
              tvlX <- solver.convert(tx, UsdUnits, pools)
              tvlY <- solver.convert(ty, UsdUnits, pools)
              volX <- solver.convert(vx, UsdUnits, pools)
              volY <- solver.convert(vy, UsdUnits, pools)
            } yield (
              CMCMarket(
                MarketId(tx.id, ty.id),
                tx.id,
                tx.ticker,
                tx.ticker,
                ty.id,
                ty.ticker,
                ty.ticker,
                tx.withDecimals / ty.withDecimals,
                volX.map(_.value).getOrElse(BigDecimal(0)),
                volY.map(_.value).getOrElse(BigDecimal(0))
              ),
              tvlX.map(_.value).getOrElse(BigDecimal(0)) + tvlY.map(_.value).getOrElse(BigDecimal(0))
            )
          }
          .map(_.toList)
      }

    private def distinctMarkets(markets: List[(CMCMarket, BigDecimal)]): List[CMCMarket] =
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

    def getVerifiedMarkets: Mid[F, List[CMCMarket]] =
      for {
        _ <- info"getVerifiedMarkets()"
        r <- _
        _ <- info"getVerifiedMarkets()"
        _ <- trace"getVerifiedMarkets() - $r"
      } yield r

    def getMarkets(window: TimeWindow): Mid[F, List[AmmMarketSummary]] =
      for {
        _ <- info"getMarkets($window)"
        r <- _
        _ <- info"getMarkets($window)"
        _ <- trace"getMarkets($window) - $r"
      } yield r

    def getTickersCoinGecko: Mid[F, List[CoinGeckoTicker]] =
      for {
        _ <- info"getTickersCoinGecko()"
        r <- _
        _ <- info"getTickersCoinGecko() finished"
        _ <- trace"getTickersCoinGecko() - $r"
      } yield r

    def getPairsCoinGecko: Mid[F, List[CoinGeckoPairs]] =
      for {
        _ <- info"getPairsCoinGecko()"
        r <- _
        _ <- info"getPairsCoinGecko() finished"
        _ <- trace"getPairsCoinGecko() - $r"
      } yield r

    def getCmcYFInfo: Mid[F, CMCYFInfo] =
      for {
        _ <- info"getCmcYFInfo()"
        r <- _
        _ <- info"getCmcYFInfo() finished"
        _ <- trace"getCmcYFInfo() - $r"
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

    def getVerifiedMarkets: Mid[F, List[CMCMarket]] = unit >> _

    def getMarkets(window: TimeWindow): Mid[F, List[AmmMarketSummary]] =
      sendMetrics(window, "window.getMarkets", _)

    def getPairsCoinGecko: Mid[F, List[CoinGeckoPairs]] = unit >> _

    def getTickersCoinGecko: Mid[F, List[CoinGeckoTicker]] = unit >> _

    def getCmcYFInfo: Mid[F, CMCYFInfo] = unit >> _
  }
}
