package fi.spectrum.api.v1.models.amm

import derevo.circe.{decoder, encoder}
import derevo.derive
import fi.spectrum.api.domain.CryptoVolume
import fi.spectrum.core.domain.TokenId
import fi.spectrum.api.models.Ticker
import fi.spectrum.api.v1.models.amm.types.{MarketId, RealPrice}
import sttp.tapir.Schema
import tofu.logging.derivation.loggable

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
  implicit val poolStatsSchema: Schema[AmmMarketSummary] = Schema.derived
}
