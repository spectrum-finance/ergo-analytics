package fi.spectrum.core.domain.ergo

import derevo.circe.decoder
import derevo.derive
import fi.spectrum.core.domain.constants._
import fi.spectrum.core.domain.{BoxId, TokenId}
import tofu.logging.derivation.loggable

@derive(decoder, loggable)
final case class TokenInfo(
  id: TokenId,
  boxId: BoxId,
  emissionAmount: Long,
  name: Option[String],
  description: Option[String],
  decimals: Option[Int]
)

object TokenInfo {

  val ErgoTokenInfo: TokenInfo =
    TokenInfo(
      ErgoAssetId,
      ErgoGenesisBox,
      ErgoEmissionAmount,
      Some(ErgoAssetTicker),
      Some(ErgoAssetDescription),
      Some(ErgoAssetDecimals)
    )
}
