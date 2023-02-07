package fi.spectrum.api.v1.endpoints

import fi.spectrum.api.v1.models.amm._
import fi.spectrum.common.http.{baseEndpoint, HttpError}
import fi.spectrum.core.domain.Address
import sttp.tapir._
import sttp.tapir.json.circe.jsonBody

final class HistoryEndpoints {

  val PathPrefix = "history"
  val Group      = "History"

  def endpoints: List[Endpoint[_, _, _, _, _]] = List(mempoolHistoryE)

  def mempoolHistoryE: Endpoint[Unit, List[Address], HttpError, List[ApiOrder], Any] =
    baseEndpoint.post
      .in(PathPrefix / "mempool")
      .in(jsonBody[List[Address]])
      .out(jsonBody[List[ApiOrder]])
      .tag(Group)
      .name("Mempool orders by addresses")
      .description("Provides mempool orders history by given addresses")
}
