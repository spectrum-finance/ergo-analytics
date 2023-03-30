package fi.spectrum.api.db.models.lm

import derevo.circe.{decoder, encoder}
import derevo.derive
import fi.spectrum.core.domain.AssetAmount
import fi.spectrum.core.domain.order.PoolId

@derive(encoder, decoder)
final case class LmPoolSnapshot(
  poolId: PoolId,
  reward: AssetAmount,
  lq: AssetAmount,
  tmp: AssetAmount,
  programBudget: Long,
  programStart: Int,
  epochLength: Int,
  epochNum: Int,
  epochIndex: Option[Int],
  initialRewardAmount: Long
) {
  def lastBlockHeight: Int = programStart + epochNum * epochLength
}
