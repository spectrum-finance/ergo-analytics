package fi.spectrum.api.v1.routes

import cats.effect.kernel.Async
import cats.syntax.semigroupk._
import fi.spectrum.api.configs.RequestConfig
import fi.spectrum.api.v1.endpoints.AmmStatsEndpoints
import fi.spectrum.api.v1.http.AdaptThrowable.AdaptThrowableEitherT
import fi.spectrum.api.v1.http.HttpError
import fi.spectrum.api.v1.http.syntax.{toAdaptThrowableOps, toRoutesOps}
import fi.spectrum.api.v1.services.{AmmStats, LqLocks}
import org.http4s.HttpRoutes
import sttp.tapir.server.http4s.{Http4sServerInterpreter, Http4sServerOptions}

final class AmmStatsRoutes[
  F[_]: Async: AdaptThrowableEitherT[*[_], HttpError]
](stats: AmmStats[F], locks: LqLocks[F], requestConf: RequestConfig)(implicit
  opts: Http4sServerOptions[F]
) {

  private val endpoints = new AmmStatsEndpoints(requestConf)
  import endpoints._

  private val interpreter = Http4sServerInterpreter(opts)

  def routes: HttpRoutes[F] =
    platformStatsVerifiedR <+>
    platformStatsR <+>
    getPoolStatsR <+>
    getPoolsStatsR <+>
    getPoolsSummaryVerifiedR <+>
    getPoolsSummaryR <+>
    getAvgPoolSlippageR <+>
    getPoolPriceChartR <+>
    getSwapTxsR <+>
    getDepositTxsR <+>
    getPoolLocksR <+>
    convertToFiatR <+>
    getAmmMarketsR

  def platformStatsVerifiedR: HttpRoutes[F] =
    interpreter
      .toRoutes(
        platformStatsVerifiedE.serverLogic(stats.platformStatsVerified(_).adaptThrowable.value)
      )

  def platformStatsR: HttpRoutes[F] =
    interpreter
      .toRoutes(platformStatsE.serverLogic(stats.platformStats(_).adaptThrowable.value))

  def getPoolStatsR: HttpRoutes[F] =
    interpreter
      .toRoutes(getPoolStatsE.serverLogic { case (poolId, tw) =>
        stats.getPoolStats(poolId, tw).adaptThrowable.orNotFound(s"PoolStats{poolId=$poolId}").value
      })

  def getPoolsStatsR: HttpRoutes[F] =
    interpreter
      .toRoutes(getPoolsStatsE.serverLogic(stats.getPoolsStats(_).adaptThrowable.value))

  def getPoolsSummaryVerifiedR: HttpRoutes[F] = interpreter.toRoutes(getPoolsSummaryE.serverLogic { _ =>
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

  def getSwapTxsR: HttpRoutes[F] =
    interpreter.toRoutes(getSwapTxsE.serverLogic(tw => stats.getSwapTransactions(tw).adaptThrowable.value))

  def getDepositTxsR: HttpRoutes[F] =
    interpreter.toRoutes(getDepositTxsE.serverLogic(tw => stats.getDepositTransactions(tw).adaptThrowable.value))

  def getPoolLocksR: HttpRoutes[F] = interpreter.toRoutes(getPoolLocksE.serverLogic { case (poolId, leastDeadline) =>
    locks.byPool(poolId, leastDeadline).adaptThrowable.value
  })

  def convertToFiatR: HttpRoutes[F] = interpreter.toRoutes(convertToFiatE.serverLogic { req =>
    stats.convertToFiat(req.tokenId, req.amount).adaptThrowable.orNotFound(s"Token{id=${req.tokenId}}").value
  })

  def getAmmMarketsR: HttpRoutes[F] = interpreter.toRoutes(getAmmMarketsE.serverLogic { tw =>
    stats.getMarkets(tw).adaptThrowable.value
  })
}

object AmmStatsRoutes {

  def make[
    F[_]: Async: AdaptThrowableEitherT[*[_], HttpError]
  ](requestConf: RequestConfig)(implicit
    stats: AmmStats[F],
    locks: LqLocks[F],
    opts: Http4sServerOptions[F]
  ): HttpRoutes[F] =
    new AmmStatsRoutes[F](stats, locks, requestConf).routes
}
