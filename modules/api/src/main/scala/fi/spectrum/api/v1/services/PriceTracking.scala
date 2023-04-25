package fi.spectrum.api.v1.services

import cats.syntax.traverse._
import cats.{Functor, Monad, Parallel}
import derevo.derive
import fi.spectrum.api.currencies.UsdUnits
import fi.spectrum.api.db.models.amm.{PoolSnapshot, PoolVolumeSnapshot}
import fi.spectrum.api.db.repositories.Pools
import fi.spectrum.api.modules.PriceSolver.FiatPriceSolver
import fi.spectrum.api.services._
import fi.spectrum.api.v1.endpoints.models.TimeWindow
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

@derive(representableK)
trait PriceTracking[F[_]] {

  def getVerifiedMarkets(tw: TimeWindow): F[List[AmmMarketSummary]]

  def getMarkets(tw: TimeWindow): F[List[AmmMarketSummary]]

}

object PriceTracking {

  def make[I[_]: Functor, F[_]: Monad: Clock: Parallel, D[_]: Monad](implicit
    txr: Txr[F, D],
    pools: Pools[D],
    tokens: VerifiedTokens[F],
    snapshots: Snapshots[F],
    solver: FiatPriceSolver[F],
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
    snapshots: Snapshots[F]
  ) extends PriceTracking[F] {

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

    def getVerifiedMarkets(tw: TimeWindow): F[List[AmmMarketSummary]] =
      for {
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
  }

  final private class Tracing[F[_]: Monad: Logging] extends PriceTracking[Mid[F, *]] {

    def getVerifiedMarkets(window: TimeWindow): Mid[F, List[AmmMarketSummary]] =
      for {
        _ <- info"getVerifiedMarkets($window)"
        r <- _
        _ <- info"getVerifiedMarkets($window)"
        _ <- trace"getVerifiedMarkets($window) - $r"
      } yield r

    def getMarkets(window: TimeWindow): Mid[F, List[AmmMarketSummary]] =
      for {
        _ <- info"getMarkets($window)"
        r <- _
        _ <- info"getMarkets($window)"
        _ <- trace"getMarkets($window) - $r"
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

    def getVerifiedMarkets(window: TimeWindow): Mid[F, List[AmmMarketSummary]] =
      sendMetrics(window, "window.getVerifiedMarkets", _)

    def getMarkets(window: TimeWindow): Mid[F, List[AmmMarketSummary]] =
      sendMetrics(window, "window.getMarkets", _)
  }
}
