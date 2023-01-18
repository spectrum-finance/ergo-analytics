package fi.spectrum.indexer.models

import derevo.circe.decoder
import derevo.derive
import fi.spectrum.core.domain.TokenId
import tofu.logging.derivation.loggable

@derive(decoder, loggable)
final case class TokenInfo(
  id: TokenId,
  name: Option[String],
  decimals: Option[Int]
)

object TokenInfo {

  val ErgoTokenInfo: TokenInfo =
    TokenInfo(
      TokenId.fromBytes(Array.fill(32)(0: Byte)),
      Some("ERG"),
      Some(9)
    )
}
