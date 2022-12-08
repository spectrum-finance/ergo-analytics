package fi.spectrum.core.domain

import derevo.circe.{decoder, encoder}
import derevo.derive

@derive(encoder, decoder)
final case class AssetAmount(tokenId: TokenId, amount: Long)

object AssetAmount {}
