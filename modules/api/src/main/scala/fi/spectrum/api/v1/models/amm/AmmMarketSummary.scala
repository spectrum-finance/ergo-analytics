package fi.spectrum.api.v1.models.amm

import derevo.circe.{decoder, encoder}
import derevo.derive
import fi.spectrum.api.domain.CryptoVolume
import fi.spectrum.core.domain.{HexString, TokenId}
import fi.spectrum.api.models.{AssetClass, CryptoUnits, FullAsset, Ticker}
import fi.spectrum.api.v1.endpoints.models.TimeWindow
import fi.spectrum.api.v1.models.amm.types.{MarketId, RealPrice}
import sttp.tapir.Schema
import tofu.logging.derivation.loggable
import io.circe.syntax._

@derive(encoder, decoder, loggable)
case class AmmMarketSummary(
  id: MarketId,
  baseId: TokenId,
  baseSymbol: Option[Ticker],
  quoteId: TokenId,
  quoteSymbol: Option[Ticker],
  lastPrice: RealPrice,
  baseVolume: CryptoVolume,
  quoteVolume: CryptoVolume
)

object AmmMarketSummary {

  def apply(x: FullAsset, y: FullAsset, volByX: FullAsset, volByY: FullAsset, tw: TimeWindow): AmmMarketSummary =
    AmmMarketSummary(
      MarketId(x.id, y.id),
      x.id,
      x.ticker,
      y.id,
      y.ticker,
      RealPrice.calculate(x.amount, x.decimals, y.amount, y.decimals).setScale(6),
      CryptoVolume(
        BigDecimal(volByX.amount),
        CryptoUnits(AssetClass(volByX.id, volByX.ticker, volByX.decimals)),
        tw
      ),
      CryptoVolume(
        BigDecimal(volByY.amount),
        CryptoUnits(AssetClass(volByY.id, volByY.ticker, volByY.decimals)),
        tw
      )
    )

  implicit def poolStatsSchema: Schema[AmmMarketSummary] = Schema
    .derived[AmmMarketSummary]
    .modify(_.id)(_.description("Market id in format xId_yId"))
    .modify(_.baseId)(_.description("Id of base token"))
    .modify(_.baseSymbol)(_.description("Ticker of base token"))
    .modify(_.quoteId)(_.description("Id of quote token"))
    .modify(_.quoteSymbol)(_.description("Ticker of quote token"))
    .modify(_.lastPrice)(_.description("Last market price"))
    .modify(_.baseVolume)(_.description("Volume of base token within time window"))
    .modify(_.quoteVolume)(_.description("Volume of quote token within time window"))
    .encodedExample(
      AmmMarketSummary(
        MarketId(
          "0000000000000000000000000000000000000000000000000000000000000000_03faf2cb329f2e90d6d23b58d91bbb6c046aa143261cc21f52fbe2824bfcbf04"
        ),
        TokenId(HexString.unsafeFromString("0000000000000000000000000000000000000000000000000000000000000000")),
        Some(Ticker("ERG")),
        TokenId(HexString.unsafeFromString("03faf2cb329f2e90d6d23b58d91bbb6c046aa143261cc21f52fbe2824bfcbf04")),
        Some(Ticker("SigUSD")),
        RealPrice(150),
        CryptoVolume(
          BigDecimal(100000),
          CryptoUnits(
            AssetClass(
              TokenId(HexString.unsafeFromString("0000000000000000000000000000000000000000000000000000000000000000")),
              Some(Ticker("ERG")),
              Some(9)
            )
          ),
          TimeWindow(Some(1675327029000L), Some(1675586229000L))
        ),
        CryptoVolume(
          BigDecimal(290000),
          CryptoUnits(
            AssetClass(
              TokenId(HexString.unsafeFromString("03faf2cb329f2e90d6d23b58d91bbb6c046aa143261cc21f52fbe2824bfcbf04")),
              Some(Ticker("SigUSD")),
              Some(2)
            )
          ),
          TimeWindow(Some(1675327029000L), Some(1675586229000L))
        )
      ).asJson
    )
}
