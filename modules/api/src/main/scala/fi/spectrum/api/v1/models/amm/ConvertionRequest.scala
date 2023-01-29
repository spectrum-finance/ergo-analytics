package fi.spectrum.api.v1.models.amm

import derevo.circe.{decoder, encoder}
import derevo.derive
import fi.spectrum.core.domain.TokenId
import sttp.tapir.Schema

@derive(encoder, decoder)
case class ConvertionRequest(tokenId: TokenId, amount: Long)

object ConvertionRequest {
  implicit val schemaConvertionReq: Schema[ConvertionRequest] = Schema.derived
}
