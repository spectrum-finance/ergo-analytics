package fi.spectrum.dex.markets.api.v1

import fi.spectrum.dex.markets.api.v1.endpoints.models.{HeightWindow, TimeWindow}
import fi.spectrum.dex.markets.api.v1.http.HttpError
import sttp.model.StatusCode
import sttp.tapir.json.circe.jsonBody
import sttp.tapir.{
  emptyOutputAs,
  endpoint,
  oneOf,
  oneOfDefaultVariant,
  oneOfVariant,
  query,
  Endpoint,
  EndpointInput,
  Schema,
  ValidationResult,
  Validator
}
import sttp.tapir.generic.auto._

import scala.concurrent.duration.FiniteDuration

package object endpoints {

  implicit def schemaBigInt: Schema[BigInt] = Schema.schemaForBigDecimal.map(_.toBigIntExact)(BigDecimal(_))

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

  def timeWindow: EndpointInput[TimeWindow] =
    (query[Option[Long]]("from")
      .description("Window lower bound (UNIX timestamp millis)")
      .validateOption(Validator.min(0L)) and
      query[Option[Long]]("to")
        .description("Window upper bound (UNIX timestamp millis)")
        .validateOption(Validator.min(0L)))
      .map { input =>
        TimeWindow(input._1, input._2)
      } { case TimeWindow(from, to) => from -> to }

  def timeWindow(maxInterval: FiniteDuration): EndpointInput[TimeWindow] =
    (query[Long]("from")
      .description("Window lower bound (UNIX timestamp millis)")
      .validate(Validator.min(0L)) and
      query[Long]("to")
        .description("Window upper bound (UNIX timestamp millis)")
        .validate(Validator.min(0L)))
      .validate(Validator.custom[(Long, Long)] { case (from, to) =>
        if (to - from < maxInterval.toMillis) ValidationResult.Valid
        else ValidationResult.Invalid("time window exceeded")
      })
      .map { input =>
        TimeWindow(Some(input._1), Some(input._2))
      } { case TimeWindow(from, to) => from.getOrElse(0L) -> to.getOrElse(0L) }

  def heightWindow: EndpointInput[HeightWindow] =
    (query[Option[Long]]("from")
      .description("Window lower bound (Block height)")
      .validateOption(Validator.min(0L)) and
      query[Option[Long]]("to")
        .description("Window upper bound (Block height)")
        .validateOption(Validator.min(0L)))
      .map { input =>
        HeightWindow(input._1, input._2)
      } { case HeightWindow(from, to) => from -> to }

}
