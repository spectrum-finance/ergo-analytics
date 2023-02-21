package fi.spectrum.api.v1.services

import cats.data.OptionT
import cats.syntax.traverse._
import cats.{Functor, Monad, Parallel}
import derevo.derive
import fi.spectrum.api.currencies.UsdUnits
import fi.spectrum.api.db.models.amm._
import fi.spectrum.api.db.repositories.{Blocks, Pools}
import fi.spectrum.api.domain.{CryptoVolume, TotalValueLocked, Volume}
import fi.spectrum.api.models.{AssetClass, CryptoUnits}
import fi.spectrum.api.modules.AmmStatsMath
import fi.spectrum.api.modules.PriceSolver.FiatPriceSolver
import fi.spectrum.api.services._
import fi.spectrum.api.v1.endpoints.models.TimeWindow
import fi.spectrum.api.v1.endpoints.monthMillis
import fi.spectrum.api.v1.models.amm._
import fi.spectrum.api.v1.models.amm.types.{MarketId, RealPrice}
import fi.spectrum.core.domain.constants.ErgoAssetId
import fi.spectrum.core.domain.order.PoolId
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

import scala.concurrent.duration._

@derive(representableK)
trait AmmStats[F[_]] {

  def platformStatsVerified24h: F[PlatformStats]

  def platformStats24h: F[PlatformStats]

  def getPoolStats(poolId: PoolId, window: TimeWindow): F[Option[PoolStats]]

  def getPoolsStats24h: F[List[PoolStats]]

  def getPoolsSummaryVerified: F[List[PoolSummary]]

  def getPoolsSummary: F[List[PoolSummary]]

  def getAvgPoolSlippage(poolId: PoolId, depth: Int): F[Option[PoolSlippage]]

  def getPoolPriceChart(poolId: PoolId, window: TimeWindow, resolution: Int): F[List[PricePoint]]

  def getMarkets(window: TimeWindow): F[List[AmmMarketSummary]]
}

object AmmStats {

  private val MillisInYear: FiniteDuration = 365.days
  private val MillisInDay: FiniteDuration  = 1.day

  private val slippageWindowScale = 2

  def make[I[_]: Functor, F[_]: Monad: Clock: Parallel, D[_]: Monad](implicit
    txr: Txr[F, D],
    pools: Pools[D],
    blocks: Blocks[D],
    tokens: VerifiedTokens[F],
    snapshots: Snapshots[F],
    volumes24H: Volumes24H[F],
    poolsStats: PoolsStats24H[F],
    assets: Assets[F],
    solver: FiatPriceSolver[F],
    ammMath: AmmStatsMath[F],
    metrics: Metrics[F],
    logs: Logs[I, F]
  ): I[AmmStats[F]] =
    logs.forService[AmmStats[F]].map(implicit __ => new AmmMetrics[F] attach (new Tracing[F] attach new Live[F, D]))

  final class Live[F[_]: Monad: Clock: Parallel: Logging, D[_]: Monad](implicit
    txr: Txr[F, D],
    pools: Pools[D],
    blocks: Blocks[D],
    tokens: VerifiedTokens[F],
    snapshots: Snapshots[F],
    poolsStats: PoolsStats24H[F],
    volumes24H: Volumes24H[F],
    assets: Assets[F],
    solver: FiatPriceSolver[F],
    ammMath: AmmStatsMath[F]
  ) extends AmmStats[F] {

    def platformStatsVerified24h: F[PlatformStats] =
      for {
        validTokens <- tokens.get
        res <- platformStats(snapshot =>
                 validTokens.contains(snapshot.lockedX.id) && validTokens.contains(snapshot.lockedY.id)
               )
      } yield res

    def platformStats24h: F[PlatformStats] = platformStats(_ => true)

    private def platformStats(f: PoolSnapshot => Boolean): F[PlatformStats] =
      for {
        now      <- millis
        volumes  <- volumes24H.get
        filtered <- snapshots.get.map(_.filter(f))
        lockedX  <- filtered.flatTraverse(p => solver.convert(p.lockedX, UsdUnits, filtered).map(_.toList))
        lockedY  <- filtered.flatTraverse(p => solver.convert(p.lockedY, UsdUnits, filtered).map(_.toList))
        tvl = TotalValueLocked(lockedX.map(_.value).sum + lockedY.map(_.value).sum, UsdUnits)
        volumeByX <- volumes.flatTraverse(p => solver.convert(p.volumeByX, UsdUnits, filtered).map(_.toList))
        volumeByY <- volumes.flatTraverse(p => solver.convert(p.volumeByY, UsdUnits, filtered).map(_.toList))
        tw     = TimeWindow(now - MillisInDay.toMillis, now)
        volume = Volume(volumeByX.map(_.value).sum + volumeByY.map(_.value).sum, UsdUnits, tw)
        finish <- millis
        _      <- info"platformStats took: ${finish - now}ms"
      } yield PlatformStats(tvl, volume)

    def getPoolsSummary: F[List[PoolSummary]] = calculatePoolsSummary(_ => true)

    def getPoolsSummaryVerified: F[List[PoolSummary]] =
      for {
        validTokens <- tokens.get
        res         <- calculatePoolsSummary(s => validTokens.contains(s.lockedY.id))
      } yield res

    private def calculatePoolsSummary(f: PoolSnapshot => Boolean): F[List[PoolSummary]] =
      for {
        volumes       <- volumes24H.get
        poolSnapshots <- snapshots.get
        filtered = poolSnapshots.filter { s =>
                     s.lockedX.ticker.nonEmpty && s.lockedY.ticker.nonEmpty &&
                     s.lockedX.id == ErgoAssetId && f(s)
                   }
        poolsTvl <- filtered.flatTraverse(p => processPoolTvl(p).map(_.map(tvl => (tvl, p))).map(_.toList))
        maxTvlPools =
          poolsTvl
            .groupBy { case (_, pool) => (pool.lockedX.id, pool.lockedY.id) }
            .map { case (_, tvls) =>
              tvls.maxBy(_._1.value)._2
            }
            .toList
        res = maxTvlPools.flatMap { pool =>
                volumes.find(_.poolId == pool.id).toList.map { vol =>
                  val x = pool.lockedX
                  val y = pool.lockedY
                  PoolSummary(
                    x.id,
                    x.ticker.get,
                    x.ticker.get,
                    y.id,
                    y.ticker.get,
                    y.ticker.get,
                    RealPrice
                      .calculate(x.amount, x.decimals, y.amount, y.decimals)
                      .setScale(6),
                    BigDecimal(vol.volumeByX.amount) / BigDecimal(10).pow(vol.volumeByX.decimals.getOrElse(0)),
                    BigDecimal(vol.volumeByY.amount) / BigDecimal(10).pow(vol.volumeByY.decimals.getOrElse(0))
                  )
                }
              }
      } yield res

    private def processPoolTvl(pool: PoolSnapshot): F[Option[TotalValueLocked]] =
      (for {
        poolSnapshots <- OptionT.liftF(snapshots.get)
        lockedX       <- OptionT(solver.convert(pool.lockedX, UsdUnits, poolSnapshots))
        lockedY       <- OptionT(solver.convert(pool.lockedY, UsdUnits, poolSnapshots))
        tvl = TotalValueLocked(lockedX.value + lockedY.value, UsdUnits)
      } yield tvl).value

    def getPoolsStats24h: F[List[PoolStats]] =
      poolsStats.get

    def getPoolStats(poolId: PoolId, window: TimeWindow): F[Option[PoolStats]] =
      resolveTimeWindow(window).flatMap { tw =>
        snapshots.get.flatMap { poolSnapshots =>
          val maybePool = poolSnapshots.find(_.id == poolId)
          val queryPoolStats =
            (for {
              info     <- OptionT(pools.getFirstPoolSwapTime(poolId))
              vol      <- OptionT.liftF(pools.volume(poolId, tw))
              pool     <- OptionT.fromOption[D](maybePool)
              feesSnap <- OptionT.liftF(pools.fees(pool, tw))
            } yield (info, vol, feesSnap)).value
          (for {
            (info, volDB, feesSnap) <- OptionT(queryPoolStats.trans)
            vol = volDB.map(_.toPoolVolumeSnapshot(poolSnapshots))
            pool    <- OptionT.fromOption[F](maybePool)
            lockedX <- OptionT(solver.convert(pool.lockedX, UsdUnits, poolSnapshots))
            lockedY <- OptionT(solver.convert(pool.lockedY, UsdUnits, poolSnapshots))
            tvl = TotalValueLocked(lockedX.value + lockedY.value, UsdUnits)
            volume            <- OptionT(poolsStats.processPoolVolume(vol, tw, poolSnapshots))
            fees              <- OptionT(poolsStats.processPoolFee(feesSnap, tw, poolSnapshots))
            yearlyFeesPercent <- OptionT.liftF(ammMath.feePercentProjection(poolId, tvl, fees, info, MillisInYear))
          } yield PoolStats(poolId, pool.lockedX, pool.lockedY, tvl, volume, fees, yearlyFeesPercent)).value
        }
      }

    private def calculatePoolSlippagePercent(initState: PoolTrace, finalState: PoolTrace): BigDecimal = {
      val minPrice = RealPrice.calculate(
        initState.lockedX.amount,
        initState.lockedX.decimals,
        initState.lockedY.amount,
        initState.lockedY.decimals
      )
      val maxPrice = RealPrice.calculate(
        finalState.lockedX.amount,
        finalState.lockedX.decimals,
        finalState.lockedY.amount,
        finalState.lockedY.decimals
      )
      (maxPrice.value - minPrice.value).abs / (minPrice.value / 100)
    }

    def getAvgPoolSlippage(poolId: PoolId, depth: Int): F[Option[PoolSlippage]] = {
      val query = for {
        currHeight   <- blocks.getCurrentHeight
        initialState <- pools.prevTrace(poolId, depth, currHeight)
        traces       <- pools.trace(poolId, depth, currHeight)
      } yield (traces, initialState)

      assets.get.flatMap { assets =>
        query.trans.map { case (tracesDB, initStateOptDB) =>
          val traces       = tracesDB.map(_.toPoolTrace(assets))
          val initStateOpt = initStateOptDB.map(_.toPoolTrace(assets))
          traces match {
            case Nil => Some(PoolSlippage.zero)
            case xs =>
              val groupedTraces = xs
                .sortBy(_.height)
                .groupBy(_.height / slippageWindowScale)
                .toList
                .sortBy(_._1)
              val initState                  = initStateOpt.getOrElse(xs.minBy(_.height))
              val maxState                   = groupedTraces.head._2.maxBy(_.height)
              val firstWindowSlippagePercent = calculatePoolSlippagePercent(initState, maxState)

              groupedTraces.drop(1) match {
                case Nil => Some(PoolSlippage(firstWindowSlippagePercent).scale(PoolSlippage.defaultScale))
                case restTraces =>
                  val restWindowsSlippage = restTraces
                    .map { case (_, heightWindow) =>
                      val windowMinGindex = heightWindow.minBy(_.height).height
                      val min = xs.filter(_.height < windowMinGindex) match {
                        case Nil      => heightWindow.minBy(_.height)
                        case filtered => filtered.maxBy(_.height)
                      }
                      val max = heightWindow.maxBy(_.height)
                      calculatePoolSlippagePercent(min, max)
                    }
                  val slippageBySegment = firstWindowSlippagePercent +: restWindowsSlippage
                  Some(PoolSlippage(slippageBySegment.sum / slippageBySegment.size).scale(PoolSlippage.defaultScale))
              }
          }
        }
      }
    }

    def getPoolPriceChart(poolId: PoolId, window: TimeWindow, resolution: Int): F[List[PricePoint]] =
      resolveTimeWindow(window).flatMap { tw =>
        pools
          .avgAmounts(poolId, tw, resolution)
          .trans
          .flatMap { amounts =>
            snapshots.get.map(_.find(_.id == poolId)).map(amounts -> _)
          }
          .map {
            case (amounts, Some(snap)) =>
              amounts.map { amount =>
                val price =
                  RealPrice.calculate(amount.amountX, snap.lockedX.decimals, amount.amountY, snap.lockedY.decimals)
                PricePoint(amount.timestamp, price.setScale(RealPrice.defaultScale))
              }
            case _ => List.empty
          }
      }

    def getMarkets(window: TimeWindow): F[List[AmmMarketSummary]] =
      resolveTimeWindow(window).flatMap { tw =>
        pools
          .volumes(tw)
          .trans
          .flatMap(volumes => snapshots.get.map(volumes -> _))
          .map { case (volumesDB, snapshots) =>
            val volumes = volumesDB.map(_.toPoolVolumeSnapshot(snapshots))
            snapshots.flatMap { snapshot =>
              val currentOpt = volumes
                .find(_.poolId == snapshot.id)

              currentOpt.toList.map { vol =>
                val tx = snapshot.lockedX
                val ty = snapshot.lockedY
                val vx = vol.volumeByX
                val vy = vol.volumeByY
                AmmMarketSummary(
                  MarketId(tx.id, ty.id),
                  tx.id,
                  tx.ticker,
                  ty.id,
                  ty.ticker,
                  RealPrice.calculate(tx.amount, tx.decimals, ty.amount, ty.decimals).setScale(6),
                  CryptoVolume(
                    BigDecimal(vx.amount),
                    CryptoUnits(AssetClass(vx.id, vx.ticker, vx.decimals)),
                    tw
                  ),
                  CryptoVolume(
                    BigDecimal(vy.amount),
                    CryptoUnits(AssetClass(vy.id, vy.ticker, vy.decimals)),
                    tw
                  )
                )
              }
            }
          }
      }

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

  final private class Tracing[F[_]: Monad: Logging] extends AmmStats[Mid[F, *]] {

    def platformStatsVerified24h: Mid[F, PlatformStats] =
      for {
        _ <- info"platformStatsVerified()"
        r <- _
        _ <- info"platformStatsVerified() - ${r.toString}"
      } yield r

    def platformStats24h: Mid[F, PlatformStats] =
      for {
        _ <- info"platformStats()"
        r <- _
        _ <- info"platformStats() - ${r.toString}"
      } yield r

    def getPoolStats(poolId: PoolId, window: TimeWindow): Mid[F, Option[PoolStats]] =
      for {
        _ <- info"getPoolStats($poolId, $window)"
        r <- _
        _ <- info"getPoolStats($poolId, $window) - ${r.map(_.toString)}"
      } yield r

    def getPoolsStats24h: Mid[F, List[PoolStats]] =
      for {
        _ <- info"getPoolsStats24h()"
        r <- _
        _ <- info"getPoolsStats24h()"
        _ <- trace"getPoolsStats24h() - ${r.mkString(",")}"
      } yield r

    def getPoolsSummaryVerified: Mid[F, List[PoolSummary]] =
      for {
        _ <- info"getPoolsSummaryVerified()"
        r <- _
        _ <- info"getPoolsSummaryVerified() - $r"
      } yield r

    def getPoolsSummary: Mid[F, List[PoolSummary]] =
      for {
        _ <- info"getPoolsSummary()"
        r <- _
        _ <- info"getPoolsSummary() - $r"
      } yield r

    def getAvgPoolSlippage(poolId: PoolId, depth: Int): Mid[F, Option[PoolSlippage]] =
      for {
        _ <- info"getAvgPoolSlippage($poolId, $depth)"
        r <- _
        _ <- info"getAvgPoolSlippage($poolId, $depth) - $r"
      } yield r

    def getPoolPriceChart(poolId: PoolId, window: TimeWindow, resolution: Int): Mid[F, List[PricePoint]] =
      for {
        _ <- info"getPoolPriceChart($poolId, $window, $resolution)"
        r <- _
        _ <- info"getPoolPriceChart($poolId, $window, $resolution)"
        _ <- trace"getPoolPriceChart($poolId, $window, $resolution) - $r"
      } yield r

    def getMarkets(window: TimeWindow): Mid[F, List[AmmMarketSummary]] =
      for {
        _ <- info"getMarkets($window)"
        r <- _
        _ <- info"getMarkets($window)"
        _ <- trace"getMarkets($window) - $r"
      } yield r
  }

  final private class AmmMetrics[F[_]: Monad: Clock](implicit metrics: Metrics[F]) extends AmmStats[Mid[F, *]] {

    private val firstTxTs = 1628066832799L

    private def sendMetrics[B](window: TimeWindow, name: String, f: F[B]): F[B] =
      for {
        r     <- f
        finis <- millis
        _     <- metrics.sendTs(name, Math.abs(window.to.getOrElse(finis) - window.from.getOrElse(firstTxTs)).toDouble)
      } yield r

    def getPoolStats(poolId: PoolId, window: TimeWindow): Mid[F, Option[PoolStats]] =
      sendMetrics(window, "window.getPoolStats", _)

    def getPoolsStats24h: Mid[F, List[PoolStats]] =
      _ <* unit

    def getPoolsSummary: Mid[F, List[PoolSummary]] =
      _ <* unit

    def getAvgPoolSlippage(poolId: PoolId, depth: Int): Mid[F, Option[PoolSlippage]] =
      _ <* unit

    def getPoolPriceChart(poolId: PoolId, window: TimeWindow, resolution: Int): Mid[F, List[PricePoint]] =
      sendMetrics(window, "window.getPoolPriceChart", _)

    def platformStats24h: Mid[F, PlatformStats] =
      _ <* unit

    def platformStatsVerified24h: Mid[F, PlatformStats] =
      _ <* unit

    def getPoolsSummaryVerified: Mid[F, List[PoolSummary]] =
      _ <* unit

    def getMarkets(window: TimeWindow): Mid[F, List[AmmMarketSummary]] =
      sendMetrics(window, "window.getMarkets", _)
  }
}
