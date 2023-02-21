package fi.spectrum.api.v1.endpoints

import fi.spectrum.api.configs.RequestConfig
import fi.spectrum.api.v1.endpoints.models.TimeWindow
import fi.spectrum.api.v1.models.amm._
import fi.spectrum.api.v1.models.locks.LiquidityLockInfo
import fi.spectrum.common.http.{baseEndpoint, HttpError}
import fi.spectrum.core.domain.order.PoolId
import sttp.tapir._
import sttp.tapir.json.circe.jsonBody

final class AmmStatsEndpoints(conf: RequestConfig) {

  val PathPrefix = "amm"
  val Group      = "ammStats"

  def endpoints: List[Endpoint[_, _, _, _, _]] = List(
    platformStatsVerified24hE,
    platformStats24hE,
    getPoolStatsE,
    getPoolsStats24hE,
    getPoolsSummaryVerifiedE,
    getPoolsSummaryE,
    getAvgPoolSlippageE,
    getPoolPriceChartE,
    getAmmMarketsE,
    getPoolLocksE
  )

  def platformStatsVerified24hE: Endpoint[Unit, Unit, HttpError, PlatformStats, Any] =
    baseEndpoint.get
      .in(PathPrefix / "platform" / "stats" / "verified")
      .out(jsonBody[PlatformStats])
      .tag(Group)
      .name("TVL/Volume statistic for verified tokens only")
      .description("Provides TVL and Volume for last 24h for verified tokens only")

  def platformStats24hE: Endpoint[Unit, Unit, HttpError, PlatformStats, Any] =
    baseEndpoint.get
      .in(PathPrefix / "platform" / "stats")
      .out(jsonBody[PlatformStats])
      .tag(Group)
      .name("TVL/Volume statistic for all tokens only")
      .description("Provides TVL and Volume for last 24h for all tokens")

  def getPoolStatsE: Endpoint[Unit, (PoolId, TimeWindow), HttpError, PoolStats, Any] =
    baseEndpoint.get
      .in(PathPrefix / "pool" / path[PoolId].description("Asset reference") / "stats")
      .in(timeWindow)
      .out(jsonBody[PoolStats])
      .tag(Group)
      .name("Pool statistics")
      .description("Provides pool's statistic e.g. TVL, volume, fees, x, y, etc.")

  def getPoolsStats24hE: Endpoint[Unit, Unit, HttpError, List[PoolStats], Any] =
    baseEndpoint.get
      .in(PathPrefix / "pools" / "stats")
      .out(jsonBody[List[PoolStats]])
      .tag(Group)
      .name("Pools statistics")
      .description("Provides pool's statistic of every known pool for last 24h")

  def getPoolsSummaryVerifiedE: Endpoint[Unit, Unit, HttpError, List[PoolSummary], Any] =
    baseEndpoint.get
      .in(PathPrefix / "pools" / "summary")
      .out(jsonBody[List[PoolSummary]])
      .tag(Group)
      .name("Pools summary")
      .description("Provides pools base info only about pools with x and y from verified token list")

  def getPoolsSummaryE: Endpoint[Unit, Unit, HttpError, List[PoolSummary], Any] =
    baseEndpoint.get
      .in(PathPrefix / "pools" / "summary" / "all")
      .out(jsonBody[List[PoolSummary]])
      .tag(Group)
      .name("Pools summary")
      .description("Provides pools base info about every known pool")

  def getAvgPoolSlippageE: Endpoint[Unit, (PoolId, Int), HttpError, PoolSlippage, Any] =
    baseEndpoint.get
      .in(PathPrefix / "pool" / path[PoolId].description("Asset reference") / "slippage")
      .in(query[Int]("blockDepth").default(20).validate(Validator.min(1)).validate(Validator.max(128)))
      .out(jsonBody[PoolSlippage])
      .tag(Group)
      .name("Pool slippage")
      .description("Provides average slippage by pool")

  def getPoolPriceChartE: Endpoint[Unit, (PoolId, TimeWindow, Int), HttpError, List[PricePoint], Any] =
    baseEndpoint.get
      .in(PathPrefix / "pool" / path[PoolId].description("Asset reference") / "chart")
      .in(timeWindow)
      .in(query[Int]("resolution").default(1).validate(Validator.min(1)))
      .out(jsonBody[List[PricePoint]])
      .tag(Group)
      .name("Pool chart")
      .description("Provides price chart by pool")

  def getAmmMarketsE: Endpoint[Unit, TimeWindow, HttpError, List[AmmMarketSummary], Any] =
    baseEndpoint.get
      .in(PathPrefix / "markets")
      .in(timeWindow)
      .out(jsonBody[List[AmmMarketSummary]])
      .tag(Group)
      .name("All pools stats")
      .description("Get statistics on all pools")

  def getPoolLocksE: Endpoint[Unit, (PoolId, Int), HttpError, List[LiquidityLockInfo], Any] =
    baseEndpoint.get
      .in(PathPrefix / "pool" / path[PoolId].description("Asset reference") / "locks")
      .in(query[Int]("leastDeadline").default(0).description("Least LQ Lock deadline"))
      .out(jsonBody[List[LiquidityLockInfo]])
      .tag(Group)
      .name("Pool locks")
      .description("Get liquidity locks for the pool with the given ID")
}
