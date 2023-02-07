package fi.spectrum.mempool.v1

import fi.spectrum.common.http.{baseEndpoint, HttpError}
import fi.spectrum.core.domain.Address
import fi.spectrum.mempool.v1.models.AddressResponse
import sttp.tapir.{Endpoint, _}
import sttp.tapir.json.circe.jsonBody

object Endpoints {

  def getMempool: Endpoint[Unit, List[Address], HttpError, List[AddressResponse], Any] =
    baseEndpoint.post
      .in("mempool")
      .in(jsonBody[List[Address]])
      .out(jsonBody[List[AddressResponse]])

}
