package fi.spectrum.api.v1.models.amm

import derevo.circe.{decoder, encoder}
import derevo.derive
import fi.spectrum.api.models.Ticker
import fi.spectrum.api.v1.models.amm.types.RealPrice
import fi.spectrum.core.domain.{HexString, TokenId}
import sttp.tapir.Schema
import tofu.logging.derivation.loggable

@derive(encoder, decoder, loggable)
final case class PoolSummary(
  baseId: TokenId,
  baseName: Ticker,
  baseSymbol: Ticker,
  quoteId: TokenId,
  quoteName: Ticker,
  quoteSymbol: Ticker,
  lastPrice: RealPrice,
  baseVolume: BigDecimal,
  quoteVolume: BigDecimal
)

object PoolSummary {

  implicit val schema: Schema[PoolSummary] =
    Schema
      .derived[PoolSummary]
      .modify(_.baseId)(_.description("Base token id"))
      .modify(_.baseName)(_.description("Base token name"))
      .modify(_.baseSymbol)(_.description("Base token ticker"))
      .modify(_.quoteId)(_.description("Quote token id"))
      .modify(_.quoteName)(_.description("Quote token name"))
      .modify(_.quoteSymbol)(_.description("Quote token ticker"))
      .modify(_.lastPrice)(_.description("Last pool price"))
      .modify(_.baseVolume)(_.description("Volume of base token"))
      .modify(_.quoteVolume)(_.description("Volume of quote token"))
      .encodedExample(
        PoolSummary(
          TokenId(HexString.unsafeFromString("0000000000000000000000000000000000000000000000000000000000000000")),
          Ticker("ERG"),
          Ticker("ERG"),
          TokenId(HexString.unsafeFromString("03faf2cb329f2e90d6d23b58d91bbb6c046aa143261cc21f52fbe2824bfcbf04")),
          Ticker("SigUSD"),
          Ticker("SigUSD"),
          RealPrice(150),
          BigDecimal(100000),
          BigDecimal(290000)
        )
      )
}
