package fi.spectrum.dex.markets.api.v1.models.amm

import derevo.circe.{decoder, encoder}
import derevo.derive
import fi.spectrum.core.common.types.Ticker
import fi.spectrum.core.domain.TokenId
import fi.spectrum.dex.markets.api.v1.models.amm.types.RealPrice
import sttp.tapir.Schema

@derive(encoder, decoder)
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
  implicit val schema: Schema[PoolSummary] = Schema.derived
}
