package fi.spectrum.common

import sttp.model.StatusCode
import sttp.tapir._
import sttp.tapir.json.circe.jsonBody
import sttp.tapir.generic.auto._

package object http {
  val VersionPrefix = "v1"

  val baseEndpoint: Endpoint[Unit, Unit, HttpError, Unit, Any] =
    endpoint
      .in(VersionPrefix)
      .errorOut(
        oneOf[HttpError](
          oneOfVariant(StatusCode.NotFound, jsonBody[HttpError.NotFound].description("not found")),
          oneOfVariant(StatusCode.NoContent, emptyOutputAs(HttpError.NoContent)),
          oneOfDefaultVariant(jsonBody[HttpError.Unknown].description("unknown"))
        )
      )
}
