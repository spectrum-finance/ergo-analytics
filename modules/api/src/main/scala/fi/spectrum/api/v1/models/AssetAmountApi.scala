package fi.spectrum.api.v1.models

import derevo.circe.{decoder, encoder}
import derevo.derive
import fi.spectrum.core.domain.{AssetAmount, TokenId}
import sttp.tapir.Schema
import tofu.logging.derivation.loggable

@derive(encoder, decoder, loggable)
final case class AssetAmountApi(tokenId: TokenId, amount: String)

object AssetAmountApi {
  implicit val assetAmountApiSchema: Schema[AssetAmountApi] = Schema.derived

  def fromAssetAmount(in: AssetAmount): AssetAmountApi =
    AssetAmountApi(in.tokenId, s"${in.amount}")
}
