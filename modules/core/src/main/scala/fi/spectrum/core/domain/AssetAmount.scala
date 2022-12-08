package fi.spectrum.core.domain

import derevo.circe.{decoder, encoder}
import derevo.derive
import fi.spectrum.core.domain.constants.ErgoAssetId

@derive(encoder, decoder)
final case class AssetAmount(tokenId: TokenId, amount: Long)

object AssetAmount {
  def native(value: Long): AssetAmount =
    AssetAmount(ErgoAssetId, value)
}
