package fi.spectrum.api.v1.models

import derevo.circe.{decoder, encoder}
import derevo.derive
import sttp.tapir.Schema
import tofu.logging.derivation.loggable

@derive(encoder, decoder, loggable)
final case class TokenPriceResponse(price: BigDecimal)

object TokenPriceResponse {
  implicit val tokenPriceResponseSchema: Schema[TokenPriceResponse] = Schema.derived
}
