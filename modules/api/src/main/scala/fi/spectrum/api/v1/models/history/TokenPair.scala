package fi.spectrum.api.v1.models.history

import derevo.circe.{decoder, encoder}
import derevo.derive
import fi.spectrum.core.domain.TokenId
import sttp.tapir.Schema
import tofu.logging.derivation.loggable

@derive(loggable, encoder, decoder)
final case class TokenPair(x: TokenId, y: TokenId)

object TokenPair {
  implicit val schema: Schema[TokenPair] = Schema.derived
}
