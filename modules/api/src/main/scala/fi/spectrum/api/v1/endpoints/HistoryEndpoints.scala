package fi.spectrum.api.v1.endpoints

import fi.spectrum.api.v1.endpoints.models.{Paging, TimeWindow}
import fi.spectrum.api.v1.models.history.{ApiOrder, HistoryApiQuery, OrderHistoryResponse}
import fi.spectrum.common.http.{baseEndpoint, HttpError}
import fi.spectrum.core.domain.Address
import sttp.tapir._
import sttp.tapir.json.circe.jsonBody

final class HistoryEndpoints {

  val PathPrefix = "history"
  val Group      = "History"

  def endpoints: List[Endpoint[_, _, _, _, _]] = List(mempoolHistoryE, orderHistoryE)

  def mempoolHistoryE: Endpoint[Unit, List[Address], HttpError, List[ApiOrder], Any] =
    baseEndpoint.post
      .in(PathPrefix / "mempool")
      .in(jsonBody[List[Address]])
      .out(jsonBody[List[ApiOrder]])
      .tag(Group)
      .name("Mempool orders by addresses")
      .description("Mempool orders by addresses")

  def orderHistoryE: Endpoint[Unit, (Paging, TimeWindow, HistoryApiQuery), HttpError, OrderHistoryResponse, Any] =
    baseEndpoint.post
      .in(PathPrefix / "order")
      .in(paging)
      .in(timeWindow)
      .in(jsonBody[HistoryApiQuery])
      .out(jsonBody[OrderHistoryResponse])
      .tag(Group)
      .name("Orders history")
      .description("Provides orders history with different filters by given addresses")
}
