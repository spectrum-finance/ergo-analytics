package fi.spectrum.core.domain.transaction

import fi.spectrum.core.domain.TokenId

final case class BoxAsset(tokenId: TokenId, amount: Long)

object BoxAsset