package fi.spectrum.api.v1.models.locks

import derevo.circe.{decoder, encoder}
import derevo.derive
import fi.spectrum.api.db.models.locks.LiquidityLockStats
import fi.spectrum.core.domain.{HexString, TokenId}
import fi.spectrum.core.domain.order.{PoolId, Redeemer}
import fi.spectrum.core.domain.{address, Address}
import org.ergoplatform.ErgoAddressEncoder
import sttp.tapir.Schema

@derive(encoder, decoder)
final case class LiquidityLockInfo(poolId: PoolId, deadline: Int, amount: Long, percent: BigDecimal, redeemer: Address)

object LiquidityLockInfo {

  implicit val schema: Schema[LiquidityLockInfo] = Schema
    .derived[LiquidityLockInfo]
    .modify(_.poolId)(_.description("Id of corresponding pool"))
    .modify(_.deadline)(_.description("Least LQ lock deadline"))
    .modify(_.amount)(_.description("Locked amount"))
    .modify(_.redeemer)(_.description("Lock committed address"))
    .encodedExample(
      LiquidityLockInfo(
        PoolId(TokenId(HexString.unsafeFromString("9916d75132593c8b07fe18bd8d583bda1652eed7565cf41a4738ddd90fc992ec"))),
        6629344,
        3122948,
        BigDecimal(3.54),
        Address.fromStringUnsafe("9gEwsJePmqhCXwdtCWVhvoRUgNsnpgWkFQ2kFhLwYhRwW7tMc61")
      )
    )

  def fromLiquidityLockStats(lqs: LiquidityLockStats)(implicit e: ErgoAddressEncoder): Option[LiquidityLockInfo] =
    address
      .formAddress(Redeemer.PublicKeyRedeemer(lqs.redeemer))
      .map(LiquidityLockInfo(lqs.poolId, lqs.deadline, lqs.amount, lqs.percent, _))

}
