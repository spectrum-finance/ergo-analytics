package fi.spectrum.api.v1.services

import cats.data.OptionT
import cats.syntax.traverse._
import cats.{Monad, Parallel}
import derevo.derive
import fi.spectrum.api.configs.PriceTrackingConfig
import fi.spectrum.api.currencies.UsdUnits
import fi.spectrum.api.db.models.amm.{PoolSnapshot, PoolVolumeSnapshot}
import fi.spectrum.api.db.repositories.Pools
import fi.spectrum.api.models.FullAsset
import fi.spectrum.api.modules.PriceSolver.FiatPriceSolver
import fi.spectrum.api.services._
import fi.spectrum.api.v1.endpoints.models._
import fi.spectrum.api.v1.endpoints.monthMillis
import fi.spectrum.api.v1.models.amm._
import fi.spectrum.api.v1.models.amm.types.MarketId
import fi.spectrum.core.domain.{Address, BoxId, TokenId, TxId}
import fi.spectrum.graphite.Metrics
import tofu.doobie.transactor.Txr
import tofu.higherKind.Mid
import tofu.higherKind.derived.representableK
import tofu.lift.Lift
import tofu.logging.{Logging, Logs}
import tofu.syntax.doobie.txr._
import tofu.syntax.foption._
import tofu.syntax.lift._
import tofu.syntax.logging._
import tofu.syntax.monadic._
import tofu.syntax.time.now.millis
import tofu.time.Clock
import scala.math.BigDecimal.RoundingMode

@derive(representableK)
trait PriceTracking[F[_]] {

  def getVerifiedMarkets: F[List[CMCMarket]]

  def getMarkets(tw: TimeWindow): F[List[AmmMarketSummary]]

  def getPairsCoinGecko: F[List[CoinGeckoPairs]]

  def getTickersCoinGecko: F[List[CoinGeckoTicker]]

  def getTokenSupply: F[Option[TokenSupply]]

}

object PriceTracking {

  def make[I[_]: Monad, F[_]: Monad: Clock: Parallel: PriceTrackingConfig.Has, D[_]: Monad](implicit
    lift: Lift[F, I],
    txr: Txr[F, D],
    pools: Pools[D],
    tokens: VerifiedTokens[F],
    snapshots: Snapshots[F],
    volumes24H: Volumes24H[F],
    solver: FiatPriceSolver[F],
    assets: Assets[F],
    explorer: Network[F],
    metrics: Metrics[F],
    lmSnapshots: LMSnapshots[F],
    logs: Logs[I, F]
  ): I[PriceTracking[F]] =
    logs
      .forService[PriceTracking[F]]
      .flatMap(implicit __ =>
        PriceTrackingConfig.access.lift.map { config =>
          new PriceTrackingMetrics[F] attach (new Tracing[F] attach new Live[F, D](config))
        }
      )

  final class Live[F[_]: Monad: Clock: Parallel: Logging, D[_]: Monad](conf: PriceTrackingConfig)(implicit
    txr: Txr[F, D],
    pools: Pools[D],
    tokens: VerifiedTokens[F],
    solver: FiatPriceSolver[F],
    snapshots: Snapshots[F],
    assets: Assets[F],
    explorer: Network[F],
    lmSnapshots: LMSnapshots[F],
    volumes24H: Volumes24H[F]
  ) extends PriceTracking[F] {

    def getTokenSupply: F[Option[TokenSupply]] =
      (for {
        ass <- OptionT.liftF(assets.update)
        _ <- OptionT.liftF(snapshots.update(ass))
        snapshots <- OptionT.liftF(snapshots.get)
        spfAddress = Address.fromStringUnsafe(conf.spfMintingAddress)
        spfTokenId = TokenId.unsafeFromString(conf.spfTokenId)
        boxes         <- OptionT.liftF(explorer.getUnspentBoxes(spfAddress))
        initBox       <- OptionT(explorer.getBoxById(BoxId(conf.initAirdropOutput)))
        airdropAmount <- initBox.spentTransactionId.fold(OptionT.some[F](initBox.assets.map(_.amount).sum))(trackOutput)
        spfInfo       <- OptionT(assets.get.map(_.find(_.id == spfTokenId)))
        spfAsset = FullAsset(spfInfo.id, conf.totalSpfSupply, spfInfo.ticker, spfInfo.decimals)
        dilutedMc <- OptionT(solver.convert(spfAsset, UsdUnits, snapshots))
        addrSpfBalance = boxes.flatMap(_.assets).filter(_.tokenId == spfTokenId).map(_.amount).sum
        lockedSpf <-
          OptionT.liftF(lmSnapshots.get.map(_.filter(_.lq.tokenId == spfTokenId).map(_.reward.amount).sum))

        circulatingSupply =
          conf.totalSpfSupply - ((addrSpfBalance + lockedSpf + airdropAmount) / math
            .pow(10, spfInfo.evalDecimals)
            .toLong)
        mcUsd    = (dilutedMc.value / conf.totalSpfSupply) * circulatingSupply
        supplyPc = circulatingSupply / conf.totalSpfSupply * 100
      } yield TokenSupply(
        mcUsd,
        dilutedMc.value,
        circulatingSupply,
        conf.totalSpfSupply,
        supplyPc
      )).value

    private def trackOutput(txId: TxId): OptionT[F, Long] =
      for {
        tx     <- OptionT(explorer.getTransactionById(txId))
        box    <- OptionT.fromOption[F](tx.outputs.headOption)
        amount <- box.spentTransactionId.fold(OptionT.some[F](box.assets.map(_.amount).sum))(trackOutput)
      } yield amount

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

    def getTokenSupply: Mid[F, Option[TokenSupply]] =
      for {
        _ <- info"getTokenSupply()"
        r <- _
        _ <- info"getTokenSupply() finished"
        _ <- trace"getTokenSupply() - $r"
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

    def getTokenSupply: Mid[F, Option[TokenSupply]] = unit >> _
  }
}
