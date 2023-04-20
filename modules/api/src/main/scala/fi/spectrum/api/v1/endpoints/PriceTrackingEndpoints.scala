package fi.spectrum.api.v1.endpoints

import fi.spectrum.api.v1.endpoints.models.TimeWindow
import fi.spectrum.api.v1.models.amm._
import fi.spectrum.common.http.{baseEndpoint, HttpError}
import sttp.tapir._
import sttp.tapir.json.circe.jsonBody

final class PriceTrackingEndpoints {

  val PathPrefix = "cmc"
  val Group      = "cmcStats"

  def endpoints: List[Endpoint[_, _, _, _, _]] = List(
    getCmcMarketsE
  )

  def getCmcMarketsE: Endpoint[Unit, TimeWindow, HttpError, List[AmmMarketSummary], Any] =
    baseEndpoint.get
      .in(PathPrefix / "markets")
      .in(timeWindow)
      .out(jsonBody[List[AmmMarketSummary]])
      .tag(Group)
      .name("Verified pools stats")
      .description("Provides statistic of every verified market in requested time window")
}
