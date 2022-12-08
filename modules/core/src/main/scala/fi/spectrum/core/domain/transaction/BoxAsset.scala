package fi.spectrum.core.domain.transaction

import derevo.circe.{decoder, encoder}
import derevo.derive
import fi.spectrum.core.domain.TokenId

@derive(encoder, decoder)
final case class BoxAsset(tokenId: TokenId, amount: Long)

object BoxAsset