package fi.spectrum.api.db.models.lm

import fi.spectrum.core.domain.AssetAmount
import fi.spectrum.core.domain.order.PoolId

final case class LmPoolSnapshot(
  poolId: PoolId,
  reward: AssetAmount,
  lq: AssetAmount,
  programBudget: Long,
  programStart: Int,
  epochLength: Int,
  epochNum: Int,
  initialRewardAmount: Long
) {
  def lastBlockHeight: Int = programStart + epochNum * epochLength
}
