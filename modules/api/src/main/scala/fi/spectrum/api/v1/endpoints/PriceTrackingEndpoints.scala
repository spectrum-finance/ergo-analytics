package fi.spectrum.api.v1.endpoints

import fi.spectrum.api.v1.endpoints.models.{CMCMarket, CoinGeckoPairs, CoinGeckoTicker, TimeWindow}
import fi.spectrum.api.v1.models.amm._
import fi.spectrum.common.http.{HttpError, baseEndpoint}
import sttp.tapir._
import io.circe.generic.auto._
import sttp.tapir.generic.auto._
import sttp.tapir.json.circe.jsonBody

final class PriceTrackingEndpoints {

  val PathPrefixPriceTracking = "price-tracking"
  val PathPrefixCMC           = "cmc"
  val PathPrefixCG            = "cg"
  val Group                   = "PriceTracking"

  def endpoints: List[Endpoint[_, _, _, _, _]] = List(
    getVerifiedMarketsE,
    getMarketsE,
    getPairsCoinGeckoE,
    getTickersCoinGeckoE,
    getCmcYFInfoE
  )

  def getVerifiedMarketsE: Endpoint[Unit, Unit, HttpError, List[CMCMarket], Any] =
    baseEndpoint.get
      .in(PathPrefixPriceTracking / PathPrefixCMC / "markets")
      .out(jsonBody[List[CMCMarket]])
      .tag(Group)
      .name("CMC pools stats API")
      .description("CMC pools stats API")

  def getMarketsE: Endpoint[Unit, TimeWindow, HttpError, List[AmmMarketSummary], Any] =
    baseEndpoint.get
      .in(PathPrefixPriceTracking / "markets")
      .in(timeWindow)
      .out(jsonBody[List[AmmMarketSummary]])
      .tag(Group)
      .name("Get pools stats")
      .description("Provides statistic of every market in requested time window")

  def getPairsCoinGeckoE: Endpoint[Unit, Unit, HttpError, List[CoinGeckoPairs], Any] =
    baseEndpoint.get
      .in(PathPrefixPriceTracking / PathPrefixCG / "pairs")
      .out(jsonBody[List[CoinGeckoPairs]])
      .tag(Group)
      .name("Coin Gecko pairs API")

  def getTickersCoinGeckoE: Endpoint[Unit, Unit, HttpError, List[CoinGeckoTicker], Any] =
    baseEndpoint.get
      .in(PathPrefixPriceTracking / PathPrefixCG / "tickers")
      .out(jsonBody[List[CoinGeckoTicker]])
      .tag(Group)
      .name("Coin Gecko tickers API")

  def getCmcYFInfoE: Endpoint[Unit, Unit, HttpError, CMCYFInfo, Any] =
    baseEndpoint.get
      .in(PathPrefixPriceTracking / "cmc" / "yf")
      .out(jsonBody[CMCYFInfo])
      .tag(Group)
      .name("Coin market cap yield farming API")
}
