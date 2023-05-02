package fi.spectrum.api.v1.endpoints.models

import derevo.circe.{decoder, encoder}
import derevo.derive
import fi.spectrum.api.models.Ticker
import fi.spectrum.api.v1.models.amm.types.MarketId
import fi.spectrum.core.domain.TokenId
import sttp.tapir.Schema
import tofu.logging.derivation.loggable

@derive(
  encoder(io.circe.derivation.renaming.snakeCase, None),
  decoder,
  loggable
)
final case class CMCMarket(
  id: MarketId,
  baseId: TokenId,
  baseSymbol: Option[Ticker],
  baseName: Option[Ticker],
  quoteId: TokenId,
  quoteSymbol: Option[Ticker],
  quoteName: Option[Ticker],
  lastPrice: BigDecimal,
  baseVolume: BigDecimal,
  quoteVolume: BigDecimal
)

object CMCMarket {

  implicit def cmcMarketSchema: Schema[CMCMarket] = Schema.derived
}
