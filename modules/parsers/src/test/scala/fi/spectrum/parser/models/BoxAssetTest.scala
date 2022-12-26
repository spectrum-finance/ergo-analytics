package fi.spectrum.parser.models

import derevo.circe.{decoder, encoder}
import derevo.derive
import fi.spectrum.core.domain.TokenId
import tofu.logging.derivation.loggable

@derive(encoder, decoder)
final case class BoxAssetTest(
  tokenId: TokenId,
  index: Int,
  amount: Long,
  name: Option[String],
  decimals: Option[Int],
  `type`: Option[TokenType]
)
