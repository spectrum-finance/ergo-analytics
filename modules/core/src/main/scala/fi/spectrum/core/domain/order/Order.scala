package fi.spectrum.core.domain.order

import derevo.circe.{decoder, encoder}
import derevo.derive
import fi.spectrum.core.domain.AssetAmount
import fi.spectrum.core.domain.analytics.Version
import fi.spectrum.core.domain.analytics.Version._
import fi.spectrum.core.domain.order.Fee.{ERG, SPF}
import fi.spectrum.core.domain.order.Redeemer.{ErgoTreeRedeemer, PublicKeyRedeemer}
import fi.spectrum.core.domain.transaction.Output
import glass.macros.ClassyOptics
import tofu.logging.derivation.loggable

/** This abstraction represents any order that appears in ergo network.
  * Orders are:
  *   1. AMM orders - Deposit, Redeem, Swap.
  *   2. LM orders  - Deposit, Redeem, Reward
  *   3. Lock order.
  *
  * Keeps knowledge about the order version (directly depends on contract version)
  */

@derive(encoder, decoder, loggable)
sealed trait Order {
  val box: Output
  val redeemer: Redeemer

  val version: Version

  def id: OrderId = OrderId(box.boxId.value)
}

object Order {

  /** It's any deposit order that exists in our domain.
    */
  @derive(encoder, decoder, loggable)
  sealed abstract class Deposit extends Order {
    val poolId: PoolId
    val fee: Fee
  }

  object Deposit {

    @derive(encoder, decoder, loggable)
    final case class DepositV3(
      box: Output,
      fee: SPF,
      poolId: PoolId,
      redeemer: ErgoTreeRedeemer,
      params: DepositParams,
      maxMinerFee: Long,
      version: V3
    ) extends Deposit

    object DepositV3

    @derive(encoder, decoder, loggable)
    final case class DepositV1(
      box: Output,
      fee: ERG,
      poolId: PoolId,
      redeemer: PublicKeyRedeemer,
      params: DepositParams,
      maxMinerFee: Long,
      version: V1
    ) extends Deposit

    object DepositV1

    @derive(encoder, decoder, loggable)
    final case class DepositLegacyV2(
      box: Output,
      fee: ERG,
      poolId: PoolId,
      redeemer: PublicKeyRedeemer,
      params: DepositParams,
      version: LegacyV2
    ) extends Deposit

    object DepositLegacyV2

    @derive(encoder, decoder, loggable)
    final case class DepositLegacyV1(
      box: Output,
      fee: ERG,
      poolId: PoolId,
      redeemer: PublicKeyRedeemer,
      params: DepositParams,
      version: LegacyV1
    ) extends Deposit
  }

  /** It's any redeem order that exists in our domain.
    */
  @derive(encoder, decoder, loggable)
  sealed abstract class Redeem extends Order {
    val poolId: PoolId
    val fee: Fee
  }

  object Redeem {

    @derive(encoder, decoder, loggable)
    final case class RedeemV3(
      box: Output,
      fee: SPF,
      poolId: PoolId,
      redeemer: ErgoTreeRedeemer,
      params: RedeemParams,
      maxMinerFee: Long,
      version: V3
    ) extends Redeem

    @derive(encoder, decoder, loggable)
    final case class RedeemV1(
      box: Output,
      fee: ERG,
      poolId: PoolId,
      redeemer: PublicKeyRedeemer,
      params: RedeemParams,
      maxMinerFee: Long,
      version: V1
    ) extends Redeem

    @derive(encoder, decoder, loggable)
    final case class RedeemLegacyV1(
      box: Output,
      fee: ERG,
      poolId: PoolId,
      redeemer: PublicKeyRedeemer,
      params: RedeemParams,
      version: LegacyV1
    ) extends Redeem
  }

  /** It's any swap order that exists in our domain.
    */
  @derive(encoder, decoder, loggable)
  sealed abstract class Swap extends Order {
    val poolId: PoolId
  }

  object Swap {

    @derive(encoder, decoder, loggable)
    final case class SwapV3(
      box: Output,
      poolId: PoolId,
      redeemer: ErgoTreeRedeemer,
      params: SwapParams,
      maxMinerFee: Long,
      reservedExFee: Long,
      version: V3
    ) extends Swap

    @derive(encoder, decoder, loggable)
    final case class SwapV2(
      box: Output,
      poolId: PoolId,
      redeemer: ErgoTreeRedeemer,
      params: SwapParams,
      maxMinerFee: Long,
      version: V2
    ) extends Swap

    @derive(encoder, decoder, loggable)
    final case class SwapV1(
      box: Output,
      poolId: PoolId,
      redeemer: PublicKeyRedeemer,
      params: SwapParams,
      maxMinerFee: Long,
      version: V1
    ) extends Swap

    @derive(encoder, decoder, loggable)
    final case class SwapLegacyV1(
      box: Output,
      poolId: PoolId,
      redeemer: PublicKeyRedeemer,
      params: SwapParams,
      version: LegacyV1
    ) extends Swap
  }

  /** It's any lock order that exists in our domain.
    */
  @derive(encoder, decoder, loggable)
  sealed abstract class Lock extends Order

  object Lock {

    @ClassyOptics
    @derive(encoder, decoder, loggable)
    final case class LockV1(
      box: Output,
      deadline: Int,
      amount: AssetAmount,
      redeemer: PublicKeyRedeemer,
      version: V1
    ) extends Lock

    object LockV1
  }

}
