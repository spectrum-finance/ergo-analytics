package fi.spectrum.core.common

import derevo.circe.{decoder, encoder}
import derevo.derive
import fi.spectrum.core.common.types.Ticker
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
  def evalDecimals: Int = decimals getOrElse 0
  def assetClass: AssetClass = AssetClass(id, ticker, decimals)
}

object FullAsset {
  implicit val schema: Schema[FullAsset] = Schema.derived
}
