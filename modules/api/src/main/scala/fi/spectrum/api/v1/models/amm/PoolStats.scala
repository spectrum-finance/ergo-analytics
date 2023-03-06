package fi.spectrum.api.v1.models.amm

import derevo.circe.{decoder, encoder}
import derevo.derive
import fi.spectrum.api.currencies.UsdCurrency
import fi.spectrum.api.domain.{FeePercentProjection, Fees, TotalValueLocked, Volume}
import fi.spectrum.api.models.{FiatUnits, FullAsset, Ticker}
import fi.spectrum.api.v1.endpoints.models.TimeWindow
import fi.spectrum.core.domain.order.PoolId
import fi.spectrum.core.domain.{HexString, TokenId}
import io.circe.syntax.EncoderOps
import sttp.tapir.Schema

@derive(encoder, decoder)
final case class PoolStats(
  id: PoolId,
  lockedX: FullAsset,
  lockedY: FullAsset,
  tvl: TotalValueLocked,
  volume: Volume,
  fees: Fees,
  yearlyFeesPercent: FeePercentProjection
)

object PoolStats {

  implicit def poolStatsSchema: Schema[PoolStats] =
    Schema
      .derived[PoolStats]
      .modify(_.id)(_.description("Id of corresponding pool"))
      .modify(_.lockedX)(_.description("X asset info"))
      .modify(_.lockedY)(_.description("Y asset info"))
      .modify(_.fees)(_.description("Fees for given pool"))
      .modify(_.yearlyFeesPercent)(_.description("Year APR"))
      .encodedExample(
        PoolStats(
          PoolId(
            TokenId(HexString.unsafeFromString("9916d75132593c8b07fe18bd8d583bda1652eed7565cf41a4738ddd90fc992ec"))
          ),
          FullAsset(
            TokenId(HexString.unsafeFromString("0000000000000000000000000000000000000000000000000000000000000000")),
            6283000000L,
            Some(Ticker("ERG")),
            Some(9)
          ),
          FullAsset(
            TokenId(HexString.unsafeFromString("03faf2cb329f2e90d6d23b58d91bbb6c046aa143261cc21f52fbe2824bfcbf04")),
            27390,
            Some(Ticker("SigUSD")),
            Some(2)
          ),
          TotalValueLocked(BigDecimal(23977), FiatUnits(UsdCurrency)),
          Volume(BigDecimal(365983), FiatUnits(UsdCurrency), TimeWindow(Some(1675327029000L), Some(1675586229000L))),
          Fees(BigDecimal(235), FiatUnits(UsdCurrency), TimeWindow(Some(1675327029000L), Some(1675586229000L))),
          FeePercentProjection(6.87)
        ).asJson
      )

}
