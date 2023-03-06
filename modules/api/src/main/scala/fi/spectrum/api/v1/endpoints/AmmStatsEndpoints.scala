package fi.spectrum.api.v1.endpoints

import fi.spectrum.api.configs.RequestConfig
import fi.spectrum.api.v1.endpoints.models.TimeWindow
import fi.spectrum.api.v1.models.amm._
import fi.spectrum.api.v1.models.locks.LiquidityLockInfo
import fi.spectrum.common.http.{baseEndpoint, HttpError}
import fi.spectrum.common.http.{HttpError, baseEndpoint}
import fi.spectrum.core.domain.order.PoolId
import sttp.tapir._
import fi.spectrum.common.http.{HttpError, baseEndpoint}
import sttp.tapir.json.circe.jsonBody

final class AmmStatsEndpoints {

  val PathPrefix = "amm"
  val Group      = "ammStats"

  def endpoints: List[Endpoint[_, _, _, _, _]] = List(
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

  def platformStats24hE: Endpoint[Unit, Unit, HttpError, PlatformStats, Any] =
    baseEndpoint.get
      .in(PathPrefix / "platform" / "stats")
      .out(jsonBody[PlatformStats])
      .tag(Group)
      .name("TVL/Volume statistic including all tokens in calculations")
      .description("Provides TVL and Volume for last 24h including all tokens in calculations")

  def getPoolStatsE: Endpoint[Unit, (PoolId, TimeWindow), HttpError, PoolStats, Any] =
    baseEndpoint.get
      .in(PathPrefix / "pool" / path[PoolId].description("AMM pool id (Pool NFT token id)") / "stats")
      .in(timeWindow)
      .out(jsonBody[PoolStats])
      .tag(Group)
      .name("Base statistics about selected pool")
      .description("Provides pool's statistic e.g. TVL, volume, fees, x, y, etc. in requested time window")

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
      .name("Pools summary within last 24 hours only for only pools with tokens from verified list")
      .description(
        "Provides pools base info (e.g. base/quote volume, last price, etc.) only about pools with x and y from verified token list"
      )

  def getPoolsSummaryE: Endpoint[Unit, Unit, HttpError, List[PoolSummary], Any] =
    baseEndpoint.get
      .in(PathPrefix / "pools" / "summary" / "all")
      .out(jsonBody[List[PoolSummary]])
      .tag(Group)
      .name("Pools summary within last 24 hours")
      .description("Provides pools base info (e.g. base/quote volume, last price, etc.) about every known pool")

  def getAvgPoolSlippageE: Endpoint[Unit, (PoolId, Int), HttpError, PoolSlippage, Any] =
    baseEndpoint.get
      .in(PathPrefix / "pool" / path[PoolId].description("Asset reference") / "slippage")
      .in(
        query[Int]("blockDepth")
          .default(20)
          .validate(Validator.min(1))
          .validate(Validator.max(128))
          .description("Lower block bound (depth) within which slippage is evaluated")
      )
      .out(jsonBody[PoolSlippage])
      .tag(Group)
      .name("Pool slippage")
      .description("Provides average slippage percent (pool price change percentage) by pool")

  def getPoolPriceChartE: Endpoint[Unit, (PoolId, TimeWindow, Int), HttpError, List[PricePoint], Any] =
    baseEndpoint.get
      .in(PathPrefix / "pool" / path[PoolId].description("AMM pool id (Pool NFT token id)") / "chart")
      .in(timeWindow)
      .in(
        query[Int]("resolution")
          .default(1)
          .validate(Validator.min(1))
          .description("Block resolution. Defines price points frequency")
      )
      .out(jsonBody[List[PricePoint]])
      .tag(Group)
      .name("Pool chart")
      .description("Provides price chart by pool in requested time window and resolution")

  def getAmmMarketsE: Endpoint[Unit, TimeWindow, HttpError, List[AmmMarketSummary], Any] =
    baseEndpoint.get
      .in(PathPrefix / "markets")
      .in(timeWindow)
      .out(jsonBody[List[AmmMarketSummary]])
      .tag(Group)
      .name("All pools stats")
      .description("Provides statistic of every market in requested time window")

  def getPoolLocksE: Endpoint[Unit, (PoolId, Int), HttpError, List[LiquidityLockInfo], Any] =
    baseEndpoint.get
      .in(PathPrefix / "pool" / path[PoolId].description("AMM pool id (Pool NFT token id)") / "locks")
      .in(query[Int]("leastDeadline").default(0).description("Least LQ Lock deadline"))
      .out(jsonBody[List[LiquidityLockInfo]])
      .tag(Group)
      .name("Pool locks")
      .description("Get liquidity locks for the pool with the given ID")
}
