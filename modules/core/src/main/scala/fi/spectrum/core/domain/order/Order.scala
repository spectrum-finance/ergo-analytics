package fi.spectrum.core.domain.order

import cats.Eq
import derevo.circe.{decoder, encoder}
import derevo.derive
import fi.spectrum.core.domain.{AssetAmount, TokenId}
import fi.spectrum.core.domain.analytics.Version
import fi.spectrum.core.domain.analytics.Version._
import fi.spectrum.core.domain.order.Fee.{ERG, SPF}
import fi.spectrum.core.domain.order.Redeemer.{ErgoTreeRedeemer, PublicKeyRedeemer}
import fi.spectrum.core.domain.transaction.Output
import glass.macros.ClassyOptics
import tofu.logging.derivation.loggable

/** Represents any order in ergo network.
  * Orders are:
  *   1. AMM orders - Deposit, Redeem, Swap.
  *   2. LM orders  - Deposit, Redeem, Compound.
  *   3. Lock order - Lock.
  *
  * Keeps knowledge about order version (contract version), order id, type, etc.
  */

@derive(encoder, decoder, loggable)
sealed trait Order { self =>
  val box: Output
  val redeemer: Redeemer

  val version: Version

  def id: OrderId = OrderId(box.boxId.value)

  def orderType: String = self.getClass.getName
}

object Order {

  /** Any deposit order that exists in our domain.
    */
  @derive(encoder, decoder, loggable)
  sealed abstract class Deposit extends Order {
    val poolId: PoolId
  }

  object Deposit {

    @derive(encoder, decoder, loggable)
    sealed abstract class AmmDeposit extends Deposit {
      val fee: Fee
    }

    object AmmDeposit {

      @derive(encoder, decoder, loggable)
      final case class AmmDepositV3(
        box: Output,
        fee: SPF,
        poolId: PoolId,
        redeemer: Redeemer,
        params: AmmDepositParams,
        maxMinerFee: Long,
        version: V3
      ) extends AmmDeposit

      object AmmDepositV3

      @derive(encoder, decoder, loggable)
      final case class AmmDepositV1(
        box: Output,
        fee: ERG,
        poolId: PoolId,
        redeemer: PublicKeyRedeemer,
        params: AmmDepositParams,
        maxMinerFee: Long,
        version: V1
      ) extends AmmDeposit

      object AmmDepositV1

      @derive(encoder, decoder, loggable)
      final case class AmmDepositLegacyV3(
        box: Output,
        fee: ERG,
        poolId: PoolId,
        redeemer: PublicKeyRedeemer,
        params: AmmDepositParams,
        maxMinerFee: Long,
        version: LegacyV3
      ) extends AmmDeposit

      @derive(encoder, decoder, loggable)
      final case class AmmDepositLegacyV2(
        box: Output,
        fee: ERG,
        poolId: PoolId,
        redeemer: PublicKeyRedeemer,
        params: AmmDepositParams,
        version: LegacyV2
      ) extends AmmDeposit

      object AmmDepositLegacyV2

      @derive(encoder, decoder, loggable)
      final case class AmmDepositLegacyV1(
        box: Output,
        fee: ERG,
        poolId: PoolId,
        redeemer: PublicKeyRedeemer,
        params: AmmDepositParams,
        version: LegacyV1
      ) extends AmmDeposit
    }

    @derive(encoder, decoder, loggable)
    sealed abstract class LmDeposit extends Deposit

    object LmDeposit {

      @derive(encoder, decoder, loggable)
      final case class LmDepositV1(
        box: Output,
        poolId: PoolId,
        redeemer: ErgoTreeRedeemer,
        params: LmDepositParams,
        maxMinerFee: Long,
        version: V1
      ) extends LmDeposit
    }
  }

  /** It's any redeem order that exists in our domain.
    */
  @derive(encoder, decoder, loggable)
  sealed abstract class Redeem extends Order

  object Redeem {

    @derive(encoder, decoder, loggable)
    sealed abstract class AmmRedeem extends Redeem {
      val poolId: PoolId
      val fee: Fee
    }

    object AmmRedeem {

      @derive(encoder, decoder, loggable)
      final case class RedeemV3(
        box: Output,
        fee: SPF,
        poolId: PoolId,
        redeemer: Redeemer,
        params: RedeemParams,
        maxMinerFee: Long,
        version: V3
      ) extends AmmRedeem

      @derive(encoder, decoder, loggable)
      final case class RedeemV1(
        box: Output,
        fee: ERG,
        poolId: PoolId,
        redeemer: PublicKeyRedeemer,
        params: RedeemParams,
        maxMinerFee: Long,
        version: V1
      ) extends AmmRedeem

      @derive(encoder, decoder, loggable)
      final case class RedeemLegacyV1(
        box: Output,
        fee: ERG,
        poolId: PoolId,
        redeemer: PublicKeyRedeemer,
        params: RedeemParams,
        version: LegacyV1
      ) extends AmmRedeem

      @derive(encoder, decoder, loggable)
      final case class RedeemLegacyV2(
        box: Output,
        fee: ERG,
        poolId: PoolId,
        redeemer: PublicKeyRedeemer,
        params: RedeemParams,
        maxMinerFee: Long,
        version: LegacyV2
      ) extends AmmRedeem
    }

    @derive(encoder, decoder, loggable)
    sealed abstract class LmRedeem extends Redeem

    object LmRedeem {

      @derive(encoder, decoder, loggable)
      final case class LmRedeemV1(
        box: Output,
        bundleKeyId: TokenId,
        expectedLq: AssetAmount,
        maxMinerFee: Long,
        redeemer: ErgoTreeRedeemer,
        version: V1
      ) extends LmRedeem
    }
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
      redeemer: Redeemer,
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

    @derive(encoder, decoder, loggable)
    final case class SwapLegacyV2(
      box: Output,
      poolId: PoolId,
      redeemer: PublicKeyRedeemer,
      params: SwapParams,
      maxMinerFee: Long,
      version: LegacyV2
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

  @derive(loggable, encoder, decoder)
  sealed trait Compound extends Order {
    val poolId: PoolId
  }

  object Compound {

    implicit def eq: Eq[Compound] = (x: Compound, y: Compound) =>
      (x, y) match {
        case (x1: CompoundV1, y1: CompoundV1) =>
          x1.params.bundleKeyId == y1.params.bundleKeyId && x1.redeemer.value == y1.redeemer.value
      }

    @derive(loggable, encoder, decoder)
    final case class CompoundV1(
      box: Output,
      params: LmCompoundParams,
      poolId: PoolId,
      redeemer: PublicKeyRedeemer,
      version: V1
    ) extends Compound

  }

}
