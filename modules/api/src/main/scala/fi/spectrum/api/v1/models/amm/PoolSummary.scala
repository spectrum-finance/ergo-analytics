package fi.spectrum.api.v1.models.amm

import derevo.circe.{decoder, encoder}
import derevo.derive
import fi.spectrum.core.domain.TokenId
import fi.spectrum.api.models.Ticker
import fi.spectrum.api.v1.models.amm.types.RealPrice
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
  implicit val schema: Schema[PoolSummary] = Schema.derived
}
