package fi.spectrum.api.v1.endpoints

import fi.spectrum.api.v1.endpoints.models.TimeWindow
import fi.spectrum.api.v1.models.amm._
import fi.spectrum.common.http.{HttpError, baseEndpoint}
import io.circe.generic.auto._
import sttp.tapir._
import sttp.tapir.generic.auto._
import sttp.tapir.json.circe.jsonBody

final class PriceTrackingEndpoints {

  val PathPrefixPriceTracking = "price-tracking"
  val Group                   = "price-tracking"

  def endpoints: List[Endpoint[_, _, _, _, _]] = List(
    getCmcMarketsE,
    getPairsCoinGeckoE,
    getTickersCoinGeckoE,
    getCmcYFInfoE
  )

  def getCmcYFInfoE: Endpoint[Unit, Unit, HttpError, CMCYFInfo, Any] =
    baseEndpoint.get
      .in(PathPrefixPriceTracking / "cmc" / "yf")
      .out(jsonBody[CMCYFInfo])
      .tag(Group)
      .name("Coin market cap yield farming API")

  def getCmcMarketsE: Endpoint[Unit, TimeWindow, HttpError, List[AmmMarketSummary], Any] =
    baseEndpoint.get
      .in(PathPrefixPriceTracking / "cmc" / "markets")
      .in(timeWindow)
      .out(jsonBody[List[AmmMarketSummary]])
      .tag(Group)
      .name("Coin market cap markets API")

  def getPairsCoinGeckoE: Endpoint[Unit, Unit, HttpError, List[CoinGeckoPairs], Any] =
    baseEndpoint.get
      .in(PathPrefixPriceTracking / "coin" / "gecko" / "pairs")
      .out(jsonBody[List[CoinGeckoPairs]])
      .tag(Group)
      .name("Coin Gecko pairs API")

  def getTickersCoinGeckoE: Endpoint[Unit, Unit, HttpError, List[CoinGeckoTicker], Any] =
    baseEndpoint.get
      .in(PathPrefixPriceTracking / "coin" / "gecko" / "tickers")
      .out(jsonBody[List[CoinGeckoTicker]])
      .tag(Group)
      .name("Coin Gecko tickers API")
}
