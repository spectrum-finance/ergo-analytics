package fi.spectrum.api.v1.routes

import cats.effect.kernel.Async
import cats.syntax.semigroupk._
import fi.spectrum.api.v1.endpoints.AmmStatsEndpoints
import fi.spectrum.common.http.syntax.{toAdaptThrowableOps, toRoutesOps}
import fi.spectrum.api.v1.services.{AmmStats, LqLocks}
import fi.spectrum.common.http.AdaptThrowable.AdaptThrowableEitherT
import fi.spectrum.common.http.HttpError
import org.http4s.HttpRoutes
import sttp.tapir.server.http4s.{Http4sServerInterpreter, Http4sServerOptions}

final class AmmStatsRoutes[
  F[_]: Async: AdaptThrowableEitherT[*[_], HttpError]
](stats: AmmStats[F], locks: LqLocks[F])(implicit
  opts: Http4sServerOptions[F]
) {

  private val endpoints = new AmmStatsEndpoints
  import endpoints._

  private val interpreter = Http4sServerInterpreter(opts)

  def routes: HttpRoutes[F] =
    platformStatsR <+>
    getPoolStatsR <+>
    getPoolsStatsR <+>
    getPoolsSummaryVerifiedR <+>
    getPoolsSummaryR <+>
    getAvgPoolSlippageR <+>
    getPoolPriceChartR <+>
    getPoolLocksR

  def platformStatsR: HttpRoutes[F] =
    interpreter
      .toRoutes(platformStats24hE.serverLogic(_ => stats.platformStats24h.adaptThrowable.value))

  def getPoolStatsR: HttpRoutes[F] =
    interpreter
      .toRoutes(getPoolStatsE.serverLogic { case (poolId, tw) =>
        stats.getPoolStats(poolId, tw).adaptThrowable.orNotFound(s"PoolStats{poolId=$poolId}").value
      })

  def getPoolsStatsR: HttpRoutes[F] =
    interpreter
      .toRoutes(getPoolsStats24hE.serverLogic(_ => stats.getPoolsStats24h.adaptThrowable.value))

  def getPoolsSummaryVerifiedR: HttpRoutes[F] = interpreter.toRoutes(getPoolsSummaryVerifiedE.serverLogic { _ =>
    stats.getPoolsSummaryVerified.adaptThrowable.value
  })

  def getPoolsSummaryR: HttpRoutes[F] = interpreter.toRoutes(getPoolsSummaryE.serverLogic { _ =>
    stats.getPoolsSummary.adaptThrowable.value
  })

  def getAvgPoolSlippageR: HttpRoutes[F] = interpreter.toRoutes(getAvgPoolSlippageE.serverLogic {
    case (poolId, depth) =>
      stats.getAvgPoolSlippage(poolId, depth).adaptThrowable.orNotFound(s"poolId=$poolId").value
  })

  def getPoolPriceChartR: HttpRoutes[F] = interpreter.toRoutes(getPoolPriceChartE.serverLogic {
    case (poolId, window, res) =>
      stats.getPoolPriceChart(poolId, window, res).adaptThrowable.value
  })

  def getPoolLocksR: HttpRoutes[F] = interpreter.toRoutes(getPoolLocksE.serverLogic { case (poolId, leastDeadline) =>
    locks.byPool(poolId, leastDeadline).adaptThrowable.value
  })
}

object AmmStatsRoutes {

  def make[
    F[_]: Async: AdaptThrowableEitherT[*[_], HttpError]
  ](implicit
    stats: AmmStats[F],
    locks: LqLocks[F],
    opts: Http4sServerOptions[F]
  ): HttpRoutes[F] =
    new AmmStatsRoutes[F](stats, locks).routes
}
