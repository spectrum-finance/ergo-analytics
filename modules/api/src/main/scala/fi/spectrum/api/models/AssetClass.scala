package fi.spectrum.api.models

import cats.Show
import derevo.circe.{decoder, encoder}
import derevo.derive
import fi.spectrum.core.domain.TokenId
import sttp.tapir.Schema
import tofu.logging.derivation.{loggable, show}

@derive(encoder, decoder, loggable, show)
final case class AssetClass(tokenId: TokenId, ticker: Option[Ticker], decimals: Option[Int]) {
  def asFullAsset(amount: Long): FullAsset = FullAsset(tokenId, amount, ticker, decimals)
}

object AssetClass {

  implicit val schema: Schema[AssetClass] = Schema.derived
}
