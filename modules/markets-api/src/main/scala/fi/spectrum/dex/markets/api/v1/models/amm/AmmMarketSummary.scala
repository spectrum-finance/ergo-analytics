package fi.spectrum.dex.markets.api.v1.models.amm

import derevo.circe.{decoder, encoder}
import derevo.derive
import fi.spectrum.core.common.types.Ticker
import fi.spectrum.core.domain.TokenId
import fi.spectrum.dex.markets.api.v1.models.amm.types.{MarketId, RealPrice}
import fi.spectrum.dex.markets.api.v1.models.amm.types.{MarketId, RealPrice}
import fi.spectrum.dex.markets.domain.CryptoVolume
import sttp.tapir.Schema

@derive(encoder, decoder)
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