package fi.spectrum.dex.markets.api.v1.services

import cats.data.OptionT
import cats.effect.Clock
import cats.syntax.parallel._
import cats.syntax.traverse._
import cats.{Monad, Parallel}
import derevo.derive
import fi.spectrum.core.common.{AssetClass, CryptoUnits, FullAsset}
import fi.spectrum.core.domain.constants.ErgoAssetId
import fi.spectrum.core.domain.TokenId
import fi.spectrum.core.domain.order.PoolId
import fi.spectrum.core.services.ErgoNetwork
import fi.spectrum.dex.markets.api.v1.models.amm.types.RealPrice
import fi.spectrum.dex.markets.api.v1.models.amm.{AmmMarketSummary, PlatformSummary, PoolSlippage, PoolStats, PoolSummary, PricePoint, TransactionsInfo}
import fi.spectrum.dex.markets.db.models.amm.{PoolInfo, PoolSnapshot, PoolVolumeSnapshot}
import mouse.anyf._
import fi.spectrum.dex.markets.api.v1.endpoints.models.TimeWindow
import fi.spectrum.dex.markets.api.v1.models.amm._
import fi.spectrum.dex.markets.api.v1.models.amm.types._
import fi.spectrum.dex.markets.currencies.UsdUnits
import fi.spectrum.dex.markets.db.models.amm
import fi.spectrum.dex.markets.db.models.amm.{PoolFeesSnapshot, PoolSnapshot, PoolTrace, PoolVolumeSnapshot}
import fi.spectrum.dex.markets.domain.{CryptoVolume, Fees, TotalValueLocked, Volume}
import fi.spectrum.dex.markets.modules.AmmStatsMath
import fi.spectrum.dex.markets.modules.PriceSolver.FiatPriceSolver
import fi.spectrum.dex.markets.repositories.{Orders, Pools}
import fi.spectrum.dex.markets.services.TokenFetcher
import tofu.doobie.transactor.Txr
import tofu.higherKind.Mid
import tofu.higherKind.derived.representableK
import tofu.internal.carriers.ClockCE3Carrier.interop
import tofu.syntax.monadic._
import tofu.syntax.time.now.millis

import java.util.concurrent.TimeUnit
import scala.concurrent.duration._

@derive(representableK)
trait AmmStats[F[_]] {

  def convertToFiat(id: TokenId, amount: Long): F[Option[FiatEquiv]]

  def getPlatformSummary(window: TimeWindow): F[PlatformSummary]

  def getPoolStats(poolId: PoolId, window: TimeWindow): F[Option[PoolStats]]

  def getPoolsStats(window: TimeWindow): F[List[PoolStats]]

  def getPoolsSummary: F[List[PoolSummary]]

  def getAvgPoolSlippage(poolId: PoolId, depth: Int): F[Option[PoolSlippage]]

  def getPoolPriceChart(poolId: PoolId, window: TimeWindow, resolution: Int): F[List[PricePoint]]

  def getSwapTransactions(window: TimeWindow): F[TransactionsInfo]

  def getDepositTransactions(window: TimeWindow): F[TransactionsInfo]

  def getMarkets(window: TimeWindow): F[List[AmmMarketSummary]]
}

object AmmStats {

  val MillisInYear: FiniteDuration = 365.days

  val slippageWindowScale = 2

  def make[F[_]: Monad: Clock: Parallel, D[_]: Monad](implicit
    txr: Txr[F, D],
    pools: Pools[D],
    orders: Orders[D],
    network: ErgoNetwork[F],
    tokens: TokenFetcher[F],
    fiatSolver: FiatPriceSolver[F],
    //metrics: Metrics[F]
  ): AmmStats[F] = //new AmmMetrics[F] attach
    new Live[F, D]()

  final class Live[F[_]: Monad: Clock: Parallel, D[_]: Monad](implicit
    txr: Txr[F, D],
    pools: Pools[D],
    orders: Orders[D],
    tokens: TokenFetcher[F],
    network: ErgoNetwork[F],
    fiatSolver: FiatPriceSolver[F],
    ammMath: AmmStatsMath[F]
  ) extends AmmStats[F] {

    def convertToFiat(id: TokenId, amount: Long): F[Option[FiatEquiv]] =
      (for {
        assetInfo <- OptionT(pools.assetById(id) ||> txr.trans)
        asset = FullAsset(
                  assetInfo.id,
                  amount * math.pow(10, assetInfo.evalDecimals).toLong,
                  assetInfo.ticker,
                  assetInfo.decimals
                )
        equiv <- OptionT(fiatSolver.convert(asset, UsdUnits, List.empty))
      } yield FiatEquiv(equiv.value, UsdUnits)).value

    def getPlatformSummary(window: TimeWindow): F[PlatformSummary] = {
      val queryPlatformStats =
        for {
          poolSnapshots <- pools.snapshots()
          volumes       <- pools.volumes(window)
        } yield (poolSnapshots, volumes)
      for {
        (poolSnapshots, volumes) <- queryPlatformStats ||> txr.trans
        validTokens              <- tokens.fetchTokens
        filteredSnaps =
          poolSnapshots.filter(ps => validTokens.contains(ps.lockedX.id) && validTokens.contains(ps.lockedY.id))
        lockedX <-
          filteredSnaps.flatTraverse(pool => fiatSolver.convert(pool.lockedX, UsdUnits, List.empty).map(_.toList))
        lockedY <-
          filteredSnaps.flatTraverse(pool => fiatSolver.convert(pool.lockedY, UsdUnits, List.empty).map(_.toList))
        tvl = TotalValueLocked(lockedX.map(_.value).sum + lockedY.map(_.value).sum, UsdUnits)

        volumeByX <-
          volumes.flatTraverse(pool => fiatSolver.convert(pool.volumeByX, UsdUnits, List.empty).map(_.toList))
        volumeByY <-
          volumes.flatTraverse(pool => fiatSolver.convert(pool.volumeByY, UsdUnits, List.empty).map(_.toList))
        volume = Volume(volumeByX.map(_.value).sum + volumeByY.map(_.value).sum, UsdUnits, window)
      } yield PlatformSummary(tvl, volume)
    }

    def getPoolsSummary: F[List[PoolSummary]] = {
      val day = 3600000 * 24

      val queryPoolData: TimeWindow => D[(List[PoolVolumeSnapshot], List[PoolSnapshot])] =
        tw =>
          for {
            volumes   <- pools.volumes(tw)
            snapshots <- pools.snapshots(true)
          } yield (volumes, snapshots)

      for {
        currTs <- millis[F]
        tw = TimeWindow(Some(currTs - day), Some(currTs))
        validTokens                                                        <- tokens.fetchTokens
        (volumes: List[PoolVolumeSnapshot], snapshots: List[PoolSnapshot]) <- queryPoolData(tw) ||> txr.trans
        filtered = snapshots.filter(ps => ps.lockedX.id == ErgoAssetId && validTokens.contains(ps.lockedY.id))
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
    }

    private def processPoolTvl(pool: PoolSnapshot): F[Option[TotalValueLocked]] =
      (for {
        lockedX <- OptionT(fiatSolver.convert(pool.lockedX, UsdUnits, List.empty))
        lockedY <- OptionT(fiatSolver.convert(pool.lockedY, UsdUnits, List.empty))
        tvl = TotalValueLocked(lockedX.value + lockedY.value, UsdUnits)
      } yield tvl).value

    def getPoolsStats(window: TimeWindow): F[List[PoolStats]] =
      (pools.snapshots() ||> txr.trans).flatMap { snapshots: List[PoolSnapshot] =>
        snapshots
          .parTraverse(pool => getPoolSummaryUsingAllPools(pool, window, snapshots))
          .map(_.flatten)
      }

    private def getPoolSummaryUsingAllPools(
      pool: PoolSnapshot,
      window: TimeWindow,
      everyKnownPool: List[PoolSnapshot]
    ): F[Option[PoolStats]] = {
      val poolId = pool.id

      def poolData: D[Option[(PoolInfo, Option[amm.PoolFeesSnapshot], Option[PoolVolumeSnapshot])]] =
        (for {
          info     <- OptionT(pools.info(poolId))
          feesSnap <- OptionT.liftF(pools.fees(poolId, window))
          vol      <- OptionT.liftF(pools.volume(poolId, window))
        } yield (info, feesSnap, vol)).value

      (for {
        (info, feesSnap, vol) <- OptionT(poolData ||> txr.trans)
        lockedX               <- OptionT(fiatSolver.convert(pool.lockedX, UsdUnits, everyKnownPool))
        lockedY               <- OptionT(fiatSolver.convert(pool.lockedY, UsdUnits, everyKnownPool))
        tvl = TotalValueLocked(lockedX.value + lockedY.value, UsdUnits)
        volume            <- processPoolVolume(vol, window, everyKnownPool)
        fees              <- processPoolFee(feesSnap, window, everyKnownPool)
        yearlyFeesPercent <- OptionT.liftF(ammMath.feePercentProjection(tvl, fees, info, MillisInYear))
      } yield PoolStats(poolId, pool.lockedX, pool.lockedY, tvl, volume, fees, yearlyFeesPercent)).value
    }

    private def processPoolVolume(
      vol: Option[PoolVolumeSnapshot],
      window: TimeWindow,
      everyKnownPool: List[PoolSnapshot]
    ): OptionT[F, Volume] =
      vol match {
        case Some(vol) =>
          for {
            volX <- OptionT(fiatSolver.convert(vol.volumeByX, UsdUnits, everyKnownPool))
            volY <- OptionT(fiatSolver.convert(vol.volumeByY, UsdUnits, everyKnownPool))
          } yield Volume(volX.value + volY.value, UsdUnits, window)
        case None => OptionT.pure[F](Volume.empty(UsdUnits, window))
      }

    private def processPoolFee(
      feesSnap: Option[PoolFeesSnapshot],
      window: TimeWindow,
      everyKnownPool: List[PoolSnapshot]
    ): OptionT[F, Fees] = feesSnap match {
      case Some(feesSnap) =>
        for {
          feesX <- OptionT(fiatSolver.convert(feesSnap.feesByX, UsdUnits, everyKnownPool))
          feesY <- OptionT(fiatSolver.convert(feesSnap.feesByY, UsdUnits, everyKnownPool))
        } yield Fees(feesX.value + feesY.value, UsdUnits, window)
      case None => OptionT.pure[F](Fees.empty(UsdUnits, window))
    }

    def getPoolStats(poolId: PoolId, window: TimeWindow): F[Option[PoolStats]] = {
      val queryPoolStats =
        (for {
          info     <- OptionT(pools.info(poolId))
          pool     <- OptionT(pools.snapshot(poolId))
          vol      <- OptionT.liftF(pools.volume(poolId, window))
          feesSnap <- OptionT.liftF(pools.fees(poolId, window))
        } yield (info, pool, vol, feesSnap)).value
      (for {
        (info, pool, vol, feesSnap) <- OptionT(queryPoolStats ||> txr.trans)
        lockedX                     <- OptionT(fiatSolver.convert(pool.lockedX, UsdUnits, List.empty))
        lockedY                     <- OptionT(fiatSolver.convert(pool.lockedY, UsdUnits, List.empty))
        tvl = TotalValueLocked(lockedX.value + lockedY.value, UsdUnits)
        volume            <- processPoolVolume(vol, window, List.empty)
        fees              <- processPoolFee(feesSnap, window, List.empty)
        yearlyFeesPercent <- OptionT.liftF(ammMath.feePercentProjection(tvl, fees, info, MillisInYear))
      } yield PoolStats(poolId, pool.lockedX, pool.lockedY, tvl, volume, fees, yearlyFeesPercent)).value
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

    def getAvgPoolSlippage(poolId: PoolId, depth: Int): F[Option[PoolSlippage]] =
      network.getCurrentHeight.flatMap { currHeight =>
        val query = for {
          initialState <- pools.prevTrace(poolId, depth, currHeight)
          traces       <- pools.trace(poolId, depth, currHeight)
          poolOpt      <- pools.info(poolId)
        } yield (traces, initialState, poolOpt)

        txr.trans(query).map { case (traces, initStateOpt, poolOpt) =>
          poolOpt.flatMap { _ =>
            traces match {
              case Nil => Some(PoolSlippage.zero)
              case xs =>
                val groupedTraces = xs
                  .sortBy(_.gindex)
                  .groupBy(_.height / slippageWindowScale)
                  .toList
                  .sortBy(_._1)
                val initState                  = initStateOpt.getOrElse(xs.minBy(_.gindex))
                val maxState                   = groupedTraces.head._2.maxBy(_.gindex)
                val firstWindowSlippagePercent = calculatePoolSlippagePercent(initState, maxState)

                groupedTraces.drop(1) match {
                  case Nil => Some(PoolSlippage(firstWindowSlippagePercent).scale(PoolSlippage.defaultScale))
                  case restTraces =>
                    val restWindowsSlippage = restTraces
                      .map { case (_, heightWindow) =>
                        val windowMinGindex = heightWindow.minBy(_.gindex).gindex
                        val min = xs.filter(_.gindex < windowMinGindex) match {
                          case Nil      => heightWindow.minBy(_.gindex)
                          case filtered => filtered.maxBy(_.gindex)
                        }
                        val max = heightWindow.maxBy(_.gindex)
                        calculatePoolSlippagePercent(min, max)
                      }
                    val slippageBySegment = firstWindowSlippagePercent +: restWindowsSlippage
                    Some(PoolSlippage(slippageBySegment.sum / slippageBySegment.size).scale(PoolSlippage.defaultScale))
                }
            }
          }
        }
      }

    def getPoolPriceChart(poolId: PoolId, window: TimeWindow, resolution: Int): F[List[PricePoint]] = {

      val queryPoolData = for {
        amounts   <- pools.avgAmounts(poolId, window, resolution)
        snapshots <- pools.snapshot(poolId)
      } yield (amounts, snapshots)

      txr
        .trans(queryPoolData)
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
        swaps  <- OptionT.liftF(txr.trans(orders.getSwapTxs(window)))
        numTxs <- OptionT.fromOption[F](swaps.headOption.map(_.numTxs))
        volumes <- OptionT.liftF(
                     swaps.flatTraverse(swap =>
                       fiatSolver
                         .convert(swap.asset, UsdUnits, List.empty)
                         .map(_.toList.map(_.value))
                     )
                   )
      } yield TransactionsInfo(numTxs, volumes.sum / numTxs, volumes.max, UsdUnits).roundAvgValue)
        .getOrElse(TransactionsInfo.empty)

    def getDepositTransactions(window: TimeWindow): F[TransactionsInfo] =
      (for {
        deposits <- OptionT.liftF(orders.getDepositTxs(window) ||> txr.trans)
        numTxs   <- OptionT.fromOption[F](deposits.headOption.map(_.numTxs))
        volumes <- OptionT.liftF(deposits.flatTraverse { deposit =>
                     fiatSolver
                       .convert(deposit.assetX, UsdUnits, List.empty)
                       .flatMap { optX =>
                         fiatSolver
                           .convert(deposit.assetY, UsdUnits, List.empty)
                           .map(optY =>
                             optX
                               .flatMap(eqX => optY.map(eqY => eqX.value + eqY.value))
                               .toList
                           )
                       }
                   })
      } yield TransactionsInfo(numTxs, volumes.sum / numTxs, volumes.max, UsdUnits).roundAvgValue)
        .getOrElse(TransactionsInfo.empty)

    def getMarkets(window: TimeWindow): F[List[AmmMarketSummary]] = {
      val queryPoolStats = for {
        volumes   <- pools.volumes(window)
        snapshots <- pools.snapshots()
      } yield (volumes, snapshots)

      txr
        .trans(queryPoolStats)
        .map { case (volumes, snapshots) =>
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
                  window
                ),
                CryptoVolume(
                  BigDecimal(vy.amount),
                  CryptoUnits(AssetClass(vy.id, vy.ticker, vy.decimals)),
                  window
                )
              )
            }
          }
        }
    }
  }

//  final private class AmmMetrics[F[_]: Monad: Clock](implicit metrics: Metrics[F]) extends AmmStats[Mid[F, *]] {
//
//    val firstTxTs = 1628766987000L
//
//    def sendMetrics[B](window: TimeWindow, name: String, f: F[B]): F[B] =
//      for {
//        r     <- f
//        finis <- millis
//        _     <- metrics.sendTs(name, Math.abs(window.to.getOrElse(finis) - window.from.getOrElse(firstTxTs)).toDouble)
//      } yield r
//
//    def convertToFiat(id: TokenId, amount: Long): Mid[F, Option[FiatEquiv]] =
//      _ <* unit
//
//    def getPlatformSummary(window: TimeWindow): Mid[F, PlatformSummary] =
//      sendMetrics(window, "window.getPlatformSummary", _)
//
//    def getPoolStats(poolId: PoolId, window: TimeWindow): Mid[F, Option[PoolStats]] =
//      sendMetrics(window, "window.getPoolStats", _)
//
//    def getPoolsStats(window: TimeWindow): Mid[F, List[PoolStats]] =
//      sendMetrics(window, "window.getPoolsStats", _)
//
//    def getPoolsSummary: Mid[F, List[PoolSummary]] =
//      _ <* unit
//
//    def getAvgPoolSlippage(poolId: PoolId, depth: Int): Mid[F, Option[PoolSlippage]] =
//      _ <* unit
//
//    def getPoolPriceChart(poolId: PoolId, window: TimeWindow, resolution: Int): Mid[F, List[PricePoint]] =
//      sendMetrics(window, "window.getPoolPriceChart", _)
//
//    def getSwapTransactions(window: TimeWindow): Mid[F, TransactionsInfo] =
//      sendMetrics(window, "window.getSwapTransactions", _)
//
//    def getDepositTransactions(window: TimeWindow): Mid[F, TransactionsInfo] =
//      sendMetrics(window, "window.getDepositTransactions", _)
//  }
}
