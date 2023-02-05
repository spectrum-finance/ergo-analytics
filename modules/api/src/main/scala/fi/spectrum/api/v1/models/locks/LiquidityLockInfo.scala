package fi.spectrum.api.v1.models.locks

import derevo.circe.{decoder, encoder}
import derevo.derive
import fi.spectrum.api.db.models.locks.LiquidityLockStats
import fi.spectrum.core.domain.order.{PoolId, Redeemer}
import fi.spectrum.core.domain.{Address, address}
import org.ergoplatform.ErgoAddressEncoder
import sttp.tapir.Schema

@derive(encoder, decoder)
final case class LiquidityLockInfo(poolId: PoolId, deadline: Int, amount: Long, percent: BigDecimal, redeemer: Address)

object LiquidityLockInfo {

  implicit val schema: Schema[LiquidityLockInfo] = Schema.derived

  def fromLiquidityLockStats(lqs: LiquidityLockStats)(implicit e: ErgoAddressEncoder): Option[LiquidityLockInfo] =
    address
      .formAddress(Redeemer.PublicKeyRedeemer(lqs.redeemer))
      .map(LiquidityLockInfo(lqs.poolId, lqs.deadline, lqs.amount, lqs.percent, _))

}
