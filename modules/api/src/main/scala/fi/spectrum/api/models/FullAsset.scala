package fi.spectrum.api.models

import derevo.circe.{decoder, encoder}
import derevo.derive
import fi.spectrum.api.db.models.amm.AssetInfo
import fi.spectrum.core.domain.{AssetAmount, TokenId}
import sttp.tapir.Schema
import tofu.logging.derivation.loggable
import cats.syntax.option._

@derive(encoder, decoder, loggable)
final case class FullAsset(
  id: TokenId,
  amount: Long,
  ticker: Option[Ticker],
  decimals: Option[Int]
) {
  def evalDecimals: Int      = decimals getOrElse 0
  def assetClass: AssetClass = AssetClass(id, ticker, decimals)

  def withAmount(newAmount: Long): FullAsset = this.copy(amount = newAmount)

  def withAmount(x: BigInt): FullAsset = copy(amount = x.toLong)
}

object FullAsset {

  def fromAssetInfo(info: AssetInfo, amount: Long): FullAsset =
    FullAsset(
      info.id,
      amount * math.pow(10, info.evalDecimals).toLong,
      info.ticker,
      info.decimals
    )

  def fromAssetAmount(in: AssetAmount): FullAsset =
    FullAsset(
      in.tokenId,
      in.amount,
      none,
      none
    )

  implicit val schema: Schema[FullAsset] = Schema.derived
}
