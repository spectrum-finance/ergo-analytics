package fi.spectrum.api.v1.endpoints

import fi.spectrum.api.configs.RequestConfig
import fi.spectrum.core.domain.order.PoolId
import fi.spectrum.api.v1.endpoints.models.TimeWindow
import fi.spectrum.api.v1.endpoints.models.Paging
import fi.spectrum.api.v1.models.amm._
import fi.spectrum.api.v1.models.locks.LiquidityLockInfo
import fi.spectrum.common.http.{HttpError, baseEndpoint}
import fi.spectrum.core.domain.Address
import sttp.tapir.json.circe.jsonBody
import sttp.tapir._

final class HistoryEndpoints {

  val PathPrefix = "history"
  val Group = "History"

  def endpoints: List[Endpoint[_, _, _, _, _]] = List(mempoolHistoryE)

  def mempoolHistoryE: Endpoint[Unit, List[Address], HttpError, List[ApiOrder], Any] =
    baseEndpoint.post
      .in(PathPrefix / "mempool")
      .in(jsonBody[List[Address]])
      .out(jsonBody[List[ApiOrder]])
      .tag(Group)
      .name("Mempool orders by addresses")
      .description("Mempool orders by addresses")
}
