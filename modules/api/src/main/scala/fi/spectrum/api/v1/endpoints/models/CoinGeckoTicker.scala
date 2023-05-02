package fi.spectrum.api.v1.endpoints.models

import derevo.circe.{decoder, encoder}
import derevo.derive
import fi.spectrum.api.v1.models.amm.types.MarketId
import fi.spectrum.core.domain.TokenId
import fi.spectrum.core.domain.order.PoolId
import sttp.tapir.Schema
import tofu.logging.derivation.loggable

@derive(
  encoder(io.circe.derivation.renaming.snakeCase, None),
  decoder,
  loggable
)
final case class CoinGeckoTicker(
  tickerId: MarketId,
  baseCurrency: TokenId,
  targetCurrency: TokenId,
  lastPrice: BigDecimal,
  liquidityInUsd: BigDecimal,
  baseVolume: BigDecimal,
  targetVolume: BigDecimal,
  poolId: PoolId
)

object CoinGeckoTicker {

  implicit def poolStatsSchema: Schema[CoinGeckoTicker] = Schema
    .derived[CoinGeckoTicker]
    .modify(_.tickerId)(_.description("Identifier of a ticker with delimiter to separate base/target"))
    .modify(_.baseCurrency)(_.description("Symbol of a the base cryptoasset"))
    .modify(_.targetCurrency)(_.description("Symbol of the target cryptoasset"))
    .modify(_.lastPrice)(_.description("Last transacted price of base currency based on given target currency"))
    .modify(_.baseVolume)(_.description("24 hour trading volume for the pair"))
    .modify(_.targetVolume)(_.description("24 hour trading volume for the pair"))
    .modify(_.poolId)(_.description("Pool pair id"))
}
