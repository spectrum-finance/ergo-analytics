package fi.spectrum.api.models

import derevo.circe.{decoder, encoder}
import derevo.derive
import fi.spectrum.api.db.models.amm.AssetInfo
import fi.spectrum.core.domain.TokenId
import sttp.tapir.Schema
import tofu.logging.derivation.loggable

@derive(encoder, decoder, loggable)
final case class FullAsset(
  id: TokenId,
  amount: Long,
  ticker: Option[Ticker],
  decimals: Option[Int]
) {
  def evalDecimals: Int      = decimals getOrElse 0
  def assetClass: AssetClass = AssetClass(id, ticker, decimals)
}

object FullAsset {

  def fromAssetInfo(info: AssetInfo, amount: Long): FullAsset =
    FullAsset(
      info.id,
      amount * math.pow(10, info.evalDecimals).toLong,
      info.ticker,
      info.decimals
    )

  implicit val schema: Schema[FullAsset] = Schema.derived
}
