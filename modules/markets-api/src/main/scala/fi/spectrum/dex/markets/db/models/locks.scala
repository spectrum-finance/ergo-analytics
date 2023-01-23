package fi.spectrum.dex.markets.db.models

import fi.spectrum.core.domain.order.PoolId
import fi.spectrum.core.domain.Address

object locks {

  final case class LiquidityLockStats(
    poolId: PoolId,
    deadline: Int,
    amount: Long,
    percent: BigDecimal,
    redeemer: Address
  )
}
