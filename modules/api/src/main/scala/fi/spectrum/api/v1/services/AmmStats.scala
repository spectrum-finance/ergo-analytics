package fi.spectrum.api.v1.services

import cats.data.OptionT
import cats.syntax.parallel._
import cats.syntax.traverse._
import cats.{Functor, Monad, Parallel}
import derevo.derive
import fi.spectrum.api.currencies.UsdUnits
import fi.spectrum.api.db.models.amm._
import fi.spectrum.api.db.repositories.{Asset, Blocks, Orders, Pools}
import fi.spectrum.api.domain.{CryptoVolume, Fees, TotalValueLocked, Volume}
import fi.spectrum.api.models.{AssetClass, CryptoUnits, FullAsset}
import fi.spectrum.api.modules.AmmStatsMath
import fi.spectrum.api.modules.PriceSolver.FiatPriceSolver
import fi.spectrum.api.services.{Assets, Snapshots, VerifiedTokens, Volumes24H}
import fi.spectrum.api.v1.endpoints.models.TimeWindow
import fi.spectrum.api.v1.endpoints.monthMillis
import fi.spectrum.api.v1.models.amm._
import fi.spectrum.api.v1.models.amm.types.{MarketId, RealPrice}
import fi.spectrum.core.domain.TokenId
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

  def platformStatsVerified(window: TimeWindow): F[PlatformStats]

  def platformStats(window: TimeWindow): F[PlatformStats]

  def getPoolStats(poolId: PoolId, window: TimeWindow): F[Option[PoolStats]]

  def getPoolsStats(window: TimeWindow): F[List[PoolStats]]

  def convertToFiat(id: TokenId, amount: Long): F[Option[FiatEquiv]]

  def getPoolsSummaryVerified: F[List[PoolSummary]]

  def getPoolsSummary: F[List[PoolSummary]]

  def getAvgPoolSlippage(poolId: PoolId, depth: Int): F[Option[PoolSlippage]]

  def getPoolPriceChart(poolId: PoolId, window: TimeWindow, resolution: Int): F[List[PricePoint]]

  def getSwapTransactions(window: TimeWindow): F[TransactionsInfo]

  def getDepositTransactions(window: TimeWindow): F[TransactionsInfo]

  def getMarkets(window: TimeWindow): F[List[AmmMarketSummary]]
}

object AmmStats {

  private val MillisInYear: FiniteDuration = 365.days

  private val slippageWindowScale = 2

  def make[I[_]: Functor, F[_]: Monad: Clock: Parallel, D[_]: Monad](implicit
    txr: Txr[F, D],
    pools: Pools[D],
    orders: Orders[D],
    asset: Asset[D],
    blocks: Blocks[D],
    tokens: VerifiedTokens[F],
    snapshots: Snapshots[F],
    volumes24H: Volumes24H[F],
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
    orders: Orders[D],
    asset: Asset[D],
    blocks: Blocks[D],
    tokens: VerifiedTokens[F],
    snapshots: Snapshots[F],
    volumes24H: Volumes24H[F],
    assets: Assets[F],
    solver: FiatPriceSolver[F],
    ammMath: AmmStatsMath[F]
  ) extends AmmStats[F] {

    def convertToFiat(id: TokenId, amount: Long): F[Option[FiatEquiv]] =
      (for {
        poolSnapshots <- OptionT.liftF(snapshots.get)
        assetInfo     <- OptionT(asset.assetById(id).trans)
        equiv         <- OptionT(solver.convert(FullAsset.fromAssetInfo(assetInfo, amount), UsdUnits, poolSnapshots))
      } yield FiatEquiv(equiv.value, UsdUnits)).value

    def platformStatsVerified(window: TimeWindow): F[PlatformStats] =
      for {
        validTokens <- tokens.get
        res <- calculatePlatformSummary(
                 window,
                 snapshot => validTokens.contains(snapshot.lockedX.id) && validTokens.contains(snapshot.lockedY.id)
               )
      } yield res

    def platformStats(window: TimeWindow): F[PlatformStats] =
      calculatePlatformSummary(window, _ => true)

    private def calculatePlatformSummary(window: TimeWindow, f: PoolSnapshot => Boolean): F[PlatformStats] =
      for {
        start1        <- millis
        tw            <- resolveTimeWindow(window)
        finish1       <- millis
        _             <- info"calculatePlatformSummary1: ${finish1 - start1}"
        volumesDB     <- pools.volumes(tw).trans
        finish2       <- millis
        _             <- info"calculatePlatformSummary2: ${finish2 - finish1}"
        poolSnapshots <- snapshots.get
        finish3       <- millis
        _             <- info"calculatePlatformSummary3: ${finish3 - finish2}"
        volumes = volumesDB.map(_.toPoolVolumeSnapshot(poolSnapshots))
        finish4 <- millis
        _       <- info"calculatePlatformSummary4: ${finish4 - finish3}"
        finish6 <- millis
        filtered = poolSnapshots.filter(f)
        finish7 <- millis
        _       <- info"calculatePlatformSummary5: ${finish7 - finish6}"
        lockedX <- filtered.flatTraverse(p => solver.convert(p.lockedX, UsdUnits, filtered).map(_.toList))
        finish8 <- millis
        _       <- info"calculatePlatformSummary6: ${finish8 - finish7}"
        lockedY <- filtered.flatTraverse(p => solver.convert(p.lockedY, UsdUnits, filtered).map(_.toList))
        finish9 <- millis
        _       <- info"calculatePlatformSummary7: ${finish9 - finish8}"
        tvl = TotalValueLocked(lockedX.map(_.value).sum + lockedY.map(_.value).sum, UsdUnits)
        finish10  <- millis
        _         <- info"calculatePlatformSummary8: ${finish10 - finish9}"
        volumeByX <- volumes.flatTraverse(p => solver.convert(p.volumeByX, UsdUnits, filtered).map(_.toList))
        finish11  <- millis
        _         <- info"calculatePlatformSummary9: ${finish11 - finish10}"
        volumeByY <- volumes.flatTraverse(p => solver.convert(p.volumeByY, UsdUnits, filtered).map(_.toList))
        finish12  <- millis
        _         <- info"calculatePlatformSummary10: ${finish12 - finish11}"
        volume = Volume(volumeByX.map(_.value).sum + volumeByY.map(_.value).sum, UsdUnits, tw)
        finish13 <- millis
        _        <- info"calculatePlatformSummary11: ${finish13 - finish12}"
        _        <- info"calculatePlatformSummary12: ${finish13 - start1}"
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

    def getPoolsStats(window: TimeWindow): F[List[PoolStats]] =
      resolveTimeWindow(window).flatMap { tw =>
        snapshots.get.flatMap { snapshots =>
          snapshots
            .parTraverse(pool => getPoolSummary(pool, tw, snapshots))
            .map(_.flatten)
        }
      }

    private def getPoolSummary(
      pool: PoolSnapshot,
      window: TimeWindow,
      poolSnapshots: List[PoolSnapshot]
    ): F[Option[PoolStats]] = {
      val poolId = pool.id

      def poolData =
        (for {
          info     <- OptionT(pools.getFirstPoolSwapTime(poolId))
          feesSnap <- OptionT.liftF(pools.fees(pool, window))
          vol      <- OptionT.liftF(pools.volume(poolId, window))
        } yield (info, feesSnap, vol)).value

      (for {
        start                   <- OptionT.liftF(millis)
        (info, feesSnap, volDB) <- OptionT(poolData.trans)
        finish2                 <- OptionT.liftF(millis)
        _                       <- OptionT.liftF(info"${pool.id} - getPoolSummary2: ${finish2 - start}")
        vol = volDB.map(_.toPoolVolumeSnapshot(poolSnapshots))
        finish3 <- OptionT.liftF(millis)
        _       <- OptionT.liftF(info"${pool.id} - getPoolSummary3: ${finish3 - finish2}")
        finish4 <- OptionT.liftF(millis)
        _       <- OptionT.liftF(info"${pool.id} - getPoolSummary4: ${finish4 - finish3}")
        lockedX <- OptionT(solver.convert(pool.lockedX, UsdUnits, poolSnapshots))
        finish5 <- OptionT.liftF(millis)
        _       <- OptionT.liftF(info"${pool.id} - getPoolSummary5: ${finish5 - finish4}")
        lockedY <- OptionT(solver.convert(pool.lockedY, UsdUnits, poolSnapshots))
        finish6 <- OptionT.liftF(millis)
        _       <- OptionT.liftF(info"${pool.id} - getPoolSummary6: ${finish6 - finish5}")
        tvl = TotalValueLocked(lockedX.value + lockedY.value, UsdUnits)
        finish7           <- OptionT.liftF(millis)
        _                 <- OptionT.liftF(info"${pool.id} - getPoolSummary7: ${finish7 - finish6}")
        volume            <- processPoolVolume(vol, window, poolSnapshots)
        finish8           <- OptionT.liftF(millis)
        _                 <- OptionT.liftF(info"${pool.id} - getPoolSummary8: ${finish8 - finish7}")
        fees              <- processPoolFee(feesSnap, window, poolSnapshots)
        finish9           <- OptionT.liftF(millis)
        _                 <- OptionT.liftF(info"${pool.id} - getPoolSummary9: ${finish9 - finish8}")
        yearlyFeesPercent <- OptionT.liftF(ammMath.feePercentProjection(poolId, tvl, fees, info, MillisInYear))
        finish10          <- OptionT.liftF(millis)
        _                 <- OptionT.liftF(info"${pool.id} - getPoolSummary10: ${finish10 - finish9}")
        _                 <- OptionT.liftF(info"${pool.id} - getPoolSummary11: ${finish10 - start}")
      } yield PoolStats(poolId, pool.lockedX, pool.lockedY, tvl, volume, fees, yearlyFeesPercent)).value
    }

    private def processPoolVolume(
      vol: Option[PoolVolumeSnapshot],
      window: TimeWindow,
      poolSnapshots: List[PoolSnapshot]
    ): OptionT[F, Volume] =
      vol match {
        case Some(vol) =>
          for {
            volX <- OptionT(solver.convert(vol.volumeByX, UsdUnits, poolSnapshots))
            volY <- OptionT(solver.convert(vol.volumeByY, UsdUnits, poolSnapshots))
          } yield Volume(volX.value + volY.value, UsdUnits, window)
        case None => OptionT.pure[F](Volume.empty(UsdUnits, window))
      }

    private def processPoolFee(
      feesSnap: Option[PoolFeesSnapshot],
      window: TimeWindow,
      poolSnapshots: List[PoolSnapshot]
    ): OptionT[F, Fees] = feesSnap match {
      case Some(feesSnap) =>
        for {
          feesX <- OptionT(solver.convert(feesSnap.feesByX, UsdUnits, poolSnapshots))
          feesY <- OptionT(solver.convert(feesSnap.feesByY, UsdUnits, poolSnapshots))
        } yield Fees(feesX.value + feesY.value, UsdUnits, window)
      case None => OptionT.pure[F](Fees.empty(UsdUnits, window))
    }

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
            volume            <- processPoolVolume(vol, tw, poolSnapshots)
            fees              <- processPoolFee(feesSnap, tw, poolSnapshots)
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

    def getSwapTransactions(window: TimeWindow): F[TransactionsInfo] =
      (for {
        swaps         <- OptionT.liftF(orders.getSwapTxs(window).trans)
        numTxs        <- OptionT.fromOption[F](swaps.headOption.map(_.numTxs))
        poolSnapshots <- OptionT.liftF(snapshots.get)
        volumes <- OptionT.liftF(
                     swaps.flatTraverse(swap =>
                       solver
                         .convert(swap.asset, UsdUnits, poolSnapshots)
                         .map(_.toList.map(_.value))
                     )
                   )
      } yield TransactionsInfo(numTxs, volumes.sum / numTxs, volumes.max, UsdUnits).roundAvgValue)
        .getOrElse(TransactionsInfo.empty)

    def getDepositTransactions(window: TimeWindow): F[TransactionsInfo] =
      (for {
        deposits      <- OptionT.liftF(orders.getDepositTxs(window).trans)
        numTxs        <- OptionT.fromOption[F](deposits.headOption.map(_.numTxs))
        poolSnapshots <- OptionT.liftF(snapshots.get)
        volumes <- OptionT.liftF(deposits.flatTraverse { deposit =>
                     solver
                       .convert(deposit.assetX, UsdUnits, poolSnapshots)
                       .flatMap { optX =>
                         solver
                           .convert(deposit.assetY, UsdUnits, poolSnapshots)
                           .map(optY =>
                             optX
                               .flatMap(eqX => optY.map(eqY => eqX.value + eqY.value))
                               .toList
                           )
                       }
                   })
      } yield TransactionsInfo(numTxs, volumes.sum / numTxs, volumes.max, UsdUnits).roundAvgValue)
        .getOrElse(TransactionsInfo.empty)

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

    def platformStatsVerified(window: TimeWindow): Mid[F, PlatformStats] =
      for {
        _ <- info"platformStatsVerified($window)"
        r <- _
        _ <- info"platformStatsVerified($window) - $r"
      } yield r

    def platformStats(window: TimeWindow): Mid[F, PlatformStats] =
      for {
        _ <- info"platformStats($window)"
        r <- _
        _ <- info"platformStats($window) - $r"
      } yield r

    def getPoolStats(poolId: PoolId, window: TimeWindow): Mid[F, Option[PoolStats]] =
      for {
        _ <- info"getPoolStats($poolId, $window)"
        r <- _
        _ <- info"getPoolStats($poolId, $window) - $r"
      } yield r

    def getPoolsStats(window: TimeWindow): Mid[F, List[PoolStats]] =
      for {
        _ <- info"getPoolsStats($window)"
        r <- _
        _ <- info"getPoolsStats($window)"
        _ <- trace"getPoolsStats($window) - $r"
      } yield r

    def convertToFiat(id: TokenId, amount: Long): Mid[F, Option[FiatEquiv]] =
      for {
        _ <- trace"convertToFiat($id, $amount)"
        r <- _
        _ <- trace"convertToFiat($id, $amount) - $r"
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

    def getSwapTransactions(window: TimeWindow): Mid[F, TransactionsInfo] =
      for {
        _ <- trace"getSwapTransactions($window)"
        r <- _
        _ <- trace"getSwapTransactions($window) - $r"
      } yield r

    def getDepositTransactions(window: TimeWindow): Mid[F, TransactionsInfo] =
      for {
        _ <- trace"getDepositTransactions($window)"
        r <- _
        _ <- trace"getDepositTransactions($window) - $r"
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

    def convertToFiat(id: TokenId, amount: Long): Mid[F, Option[FiatEquiv]] =
      _ <* unit

    def getPoolStats(poolId: PoolId, window: TimeWindow): Mid[F, Option[PoolStats]] =
      sendMetrics(window, "window.getPoolStats", _)

    def getPoolsStats(window: TimeWindow): Mid[F, List[PoolStats]] =
      sendMetrics(window, "window.getPoolsStats", _)

    def getPoolsSummary: Mid[F, List[PoolSummary]] =
      _ <* unit

    def getAvgPoolSlippage(poolId: PoolId, depth: Int): Mid[F, Option[PoolSlippage]] =
      _ <* unit

    def getPoolPriceChart(poolId: PoolId, window: TimeWindow, resolution: Int): Mid[F, List[PricePoint]] =
      sendMetrics(window, "window.getPoolPriceChart", _)

    def getSwapTransactions(window: TimeWindow): Mid[F, TransactionsInfo] =
      sendMetrics(window, "window.getSwapTransactions", _)

    def getDepositTransactions(window: TimeWindow): Mid[F, TransactionsInfo] =
      sendMetrics(window, "window.getDepositTransactions", _)

    def platformStats(window: TimeWindow): Mid[F, PlatformStats] =
      sendMetrics(window, "window.getPlatformSummary", _)

    def platformStatsVerified(window: TimeWindow): Mid[F, PlatformStats] =
      sendMetrics(window, "window.getPlatformSummary.verified", _)

    def getPoolsSummaryVerified: Mid[F, List[PoolSummary]] =
      _ <* unit

    def getMarkets(window: TimeWindow): Mid[F, List[AmmMarketSummary]] =
      sendMetrics(window, "window.getMarkets", _)
  }
}
