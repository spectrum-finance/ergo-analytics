package fi.spectrum.api.v1.models.locks

import derevo.circe.{decoder, encoder}
import derevo.derive
import fi.spectrum.api.db.models.locks.LiquidityLockStats
import fi.spectrum.core.domain.PubKey
import fi.spectrum.core.domain.order.PoolId
import sttp.tapir.Schema

@derive(encoder, decoder)
final case class LiquidityLockInfo(poolId: PoolId, deadline: Int, amount: Long, percent: BigDecimal, redeemer: PubKey)

object LiquidityLockInfo {

  implicit val schema: Schema[LiquidityLockInfo] = Schema.derived

  def apply(lqs: LiquidityLockStats): LiquidityLockInfo =
    LiquidityLockInfo(lqs.poolId, lqs.deadline, lqs.amount, lqs.percent, lqs.redeemer)
}
