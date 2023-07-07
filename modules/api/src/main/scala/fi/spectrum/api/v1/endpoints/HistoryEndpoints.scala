package fi.spectrum.api.v1.endpoints

import fi.spectrum.api.v1.endpoints.models.{Paging, TimeWindow}
import fi.spectrum.api.v1.models.history.{AddressesHistoryResponse, ApiOrder, HistoryApiQuery, OrderHistoryResponse}
import fi.spectrum.common.http.{HttpError, baseEndpoint}
import fi.spectrum.core.domain.Address
import sttp.tapir._
import sttp.tapir.json.circe.jsonBody

final class HistoryEndpoints {

  val PathPrefix = "history"
  val Group      = "History"

  def endpoints: List[Endpoint[_, _, _, _, _]] = List(mempoolHistoryE, orderHistoryE, addressesHistoryE)

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
      .in(paging(50))
      .in(timeWindow)
      .in(jsonBody[HistoryApiQuery])
      .out(jsonBody[OrderHistoryResponse])
      .tag(Group)
      .name("Orders history")
      .description("Provides orders history with different filters by given addresses")

  def addressesHistoryE: Endpoint[Unit, Paging, HttpError, AddressesHistoryResponse, Any] =
    baseEndpoint.get
      .in(PathPrefix / "addresses")
      .in(paging(100))
      .out(jsonBody[AddressesHistoryResponse])
      .tag(Group)
      .name("Addresses history")
      .description("Provides list of addresses that interacted with amm dex platform")
}
