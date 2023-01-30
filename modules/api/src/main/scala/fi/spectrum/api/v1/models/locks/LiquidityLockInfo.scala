package fi.spectrum.api.v1.models.locks

import derevo.circe.{decoder, encoder}
import derevo.derive
import fi.spectrum.api.db.models.locks.LiquidityLockStats
import fi.spectrum.core.domain.Address
import fi.spectrum.core.domain.order.PoolId
import fi.spectrum.core.protocol.ErgoTreeSerializer
import org.ergoplatform.ErgoAddressEncoder
import sttp.tapir.Schema

import scala.util.Try

@derive(encoder, decoder)
final case class LiquidityLockInfo(poolId: PoolId, deadline: Int, amount: Long, percent: BigDecimal, redeemer: Address)

object LiquidityLockInfo {

  implicit val schema: Schema[LiquidityLockInfo] = Schema.derived

  def fromLiquidityLockStats(lqs: LiquidityLockStats)(implicit e: ErgoAddressEncoder): Option[LiquidityLockInfo] =
    e.fromProposition(ErgoTreeSerializer.default.deserialize(lqs.redeemer.ergoTree))
      .toOption
      .map(e.toString)
      .flatMap(Address.fromString[Try](_).toOption)
      .map(LiquidityLockInfo(lqs.poolId, lqs.deadline, lqs.amount, lqs.percent, _))

}
