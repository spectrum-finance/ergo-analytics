package fi.spectrum.api.v1.models.amm

import derevo.circe.{decoder, encoder}
import derevo.derive
import fi.spectrum.api.domain.{FeePercentProjection, Fees, TotalValueLocked, Volume}
import fi.spectrum.api.models.FullAsset
import fi.spectrum.core.domain.order.PoolId
import fi.spectrum.api.domain.{FeePercentProjection, Fees, TotalValueLocked, Volume}
import fi.spectrum.api.models.FullAsset
import sttp.tapir.Schema
import tofu.logging.derivation.loggable

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
  implicit val schema: Schema[PoolStats] = Schema.derived
}
