package fi.spectrum.api.v1.models.amm

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
final case class CoinGeckoPairs(tickerId: MarketId, base: TokenId, target: TokenId, poolId: PoolId)

object CoinGeckoPairs {

  implicit def poolStatsSchema: Schema[CoinGeckoPairs] = Schema
    .derived[CoinGeckoPairs]
    .modify(_.tickerId)(_.description("Identifier of a ticker with delimiter to separate base/target"))
    .modify(_.base)(_.description("Symbol of a the base cryptoasset"))
    .modify(_.target)(_.description("Symbol of the target cryptoasset"))
    .modify(_.poolId)(_.description("Pool pair id"))
}
