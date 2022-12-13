package fi.spectrum.core.domain

import derevo.circe.{decoder, encoder}
import derevo.derive
import fi.spectrum.core.domain.constants.ErgoAssetId
import fi.spectrum.core.domain.transaction.BoxAsset
import tofu.logging.derivation.{loggable, show}

@derive(encoder, decoder, loggable, show)
final case class AssetAmount(tokenId: TokenId, amount: Long) {
  def withAmount(x: Long): AssetAmount = copy(amount = x)
  def -(that: AssetAmount): AssetAmount = withAmount(amount - that.amount)

  def isNative: Boolean = tokenId == ErgoAssetId

  def -(that: Long): AssetAmount = withAmount(amount - that)
}

object AssetAmount {
  def native(value: Long): AssetAmount =
    AssetAmount(ErgoAssetId, value)

  def fromBoxAsset(boxAsset: BoxAsset): AssetAmount =
    AssetAmount(boxAsset.tokenId, boxAsset.amount)
}
