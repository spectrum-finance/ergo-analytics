package fi.spectrum.core.domain.order

import cats.syntax.eq._
import cats.syntax.functor._
import cats.syntax.show._
import cats.{Eq, Show}
import derevo.cats.eqv
import derevo.circe.{decoder, encoder}
import derevo.derive
import fi.spectrum.core.domain.AssetAmount
import fi.spectrum.core.domain.analytics.Version
import fi.spectrum.core.domain.analytics.Version._
import fi.spectrum.core.domain.order.Fee.{ERG, SPF}
import fi.spectrum.core.domain.order.OrderType.{AMM, LOCK}
import fi.spectrum.core.domain.order.Redeemer.{ErgoTreeRedeemer, PublicKeyRedeemer}
import fi.spectrum.core.domain.transaction.Output
import glass.macros.ClassyOptics
import io.circe.derivation.{deriveDecoder, deriveEncoder}
import io.circe.syntax._
import io.circe.{Decoder, Encoder}
import tofu.logging.Loggable
import tofu.logging.derivation.{loggable, show}

/** This abstraction represents any order that exists in our domain e.g.
  * AMM orders like Deposit, Redeem, Swap or
  * LM orders like Deposit, Redeem, Reward or
  * Lock order.
  *
  * Keeps knowledge about the order version, the order type, and the operation
  *
  * Every order tries to keep information about its params, fee, pool and its output.
  *
  * @tparam V - version of the order e.g. v1, v2, v3. Supports legacy(during tests) versions - legacy v1, legacy v2
  * @tparam T - order type e.g. AMM, LOCK, LM
  * @tparam O - operation type, e.g., Redeem, Deposit, Swap, Lock, Reward
  */

sealed trait Order[+V <: Version, +T <: OrderType, +O <: Operation] {
  val box: Output
  val redeemer: Redeemer

  val version: V
  val orderType: T
  val orderOperation: O

  def id: OrderId = OrderId(box.boxId.value)
}

object Order {

  type Any        = Order[Version, OrderType, Operation]
  type AnySwap    = Swap[Version, OrderType]
  type AnyDeposit = Deposit[Version, OrderType]
  type AnyRedeem  = Redeem[Version, OrderType]
  type AnyLock    = Lock[Version]

  implicit def orderEncoder: Encoder[Order[Version, OrderType, Operation]] = {
    case deposit: Deposit[Version, OrderType] => deposit.asJson
    case redeem: Redeem[Version, OrderType]   => redeem.asJson
    case swap: Swap[Version, OrderType]       => swap.asJson
    case lock: Lock[Version]                  => lock.asJson
  }

  implicit def orderDecoder: Decoder[Order[Version, OrderType, Operation]] =
    List[Decoder[Order[Version, OrderType, Operation]]](
      Decoder[Deposit[Version, OrderType]].widen,
      Decoder[Redeem[Version, OrderType]].widen,
      Decoder[Swap[Version, OrderType]].widen,
      Decoder[Lock[Version]].widen
    ).reduceLeft(_ or _)

  implicit def orderLoggable: Loggable[Order.Any] = Loggable.show

  implicit def orderShow: Show[Order.Any] = {
    case deposit: Order.AnyDeposit => deposit.show
    case redeem: Order.AnyRedeem   => redeem.show
    case swap: Order.AnySwap       => swap.show
    case lock: Order.AnyLock       => lock.show
  }

  implicit def orderEqv: Eq[Order.Any] = (x: Any, y: Any) =>
    (x, y) match {
      case (x1: Order.AnySwap, y1: Order.AnySwap)       => x1 === y1
      case (x1: Order.AnyRedeem, y1: Order.AnyRedeem)   => x1 === y1
      case (x1: Order.AnyDeposit, y1: Order.AnyDeposit) => x1 === y1
      case (x1: Order.AnyLock, y1: Order.AnyLock)       => x1 === y1
      case (_, _)                                       => false
    }

  /** It's any deposit order that exists in our domain.
    *
    * @tparam V - Supported versions are V3, V1, Legacy v1, Legacy v2
    * @tparam T - Supported types are AMM, LM
    */
  sealed abstract class Deposit[+V <: Version, +T <: OrderType] extends Order[V, T, Operation.Deposit] {
    val poolId: PoolId
    val fee: Fee
  }

  object Deposit {

    implicit def depositEncoder: Encoder[Deposit[Version, OrderType]] = deriveEncoder
    implicit def depositDecoder: Decoder[Deposit[Version, OrderType]] = deriveDecoder

    implicit def depositLoggable: Loggable[Order.AnyDeposit] = Loggable.show

    implicit def depositShow: Show[Order.AnyDeposit] = {
      case d: DepositV3       => d.show
      case d: DepositV1       => d.show
      case d: DepositLegacyV2 => d.show
      case d: DepositLegacyV1 => d.show
    }

    implicit def eqAnySwap: Eq[Order.AnyDeposit] = (x: AnyDeposit, y: AnyDeposit) =>
      (x, y) match {
        case (x1: DepositV1, y1: DepositV1)             => x1 === y1
        case (x1: DepositV3, y1: DepositV3)             => x1 === y1
        case (x1: DepositLegacyV1, y1: DepositLegacyV1) => x1 === y1
        case (x1: DepositLegacyV2, y1: DepositLegacyV2) => x1 === y1
        case (_, _)                                     => false
      }

    @ClassyOptics
    @derive(encoder, decoder, loggable, show, eqv)
    final case class DepositV3(
      box: Output,
      fee: SPF,
      poolId: PoolId,
      redeemer: ErgoTreeRedeemer,
      params: DepositParams,
      maxMinerFee: Long,
      version: V3,
      orderType: AMM,
      orderOperation: Operation.Deposit
    ) extends Deposit[V3, AMM]

    object DepositV3

    @ClassyOptics
    @derive(encoder, decoder, loggable, show, eqv)
    final case class DepositV1(
      box: Output,
      fee: ERG,
      poolId: PoolId,
      redeemer: PublicKeyRedeemer,
      params: DepositParams,
      maxMinerFee: Long,
      version: V1,
      orderType: OrderType.AMM,
      orderOperation: Operation.Deposit
    ) extends Deposit[V1, AMM]

    object DepositV1

    @derive(encoder, decoder, loggable, show, eqv)
    final case class DepositLegacyV2(
      box: Output,
      fee: ERG,
      poolId: PoolId,
      redeemer: PublicKeyRedeemer,
      params: DepositParams,
      version: LegacyV2,
      orderType: AMM,
      orderOperation: Operation.Deposit
    ) extends Deposit[LegacyV2, AMM]

    object DepositLegacyV2

    @derive(encoder, decoder, loggable, show, eqv)
    final case class DepositLegacyV1(
      box: Output,
      fee: ERG,
      poolId: PoolId,
      redeemer: PublicKeyRedeemer,
      params: DepositParams,
      version: LegacyV1,
      orderType: OrderType.AMM,
      orderOperation: Operation.Deposit
    ) extends Deposit[LegacyV1, AMM]
  }

  /** It's any redeem order that exists in our domain.
    *
    * @tparam V - Supported versions are V3, V1, Legacy v1
    * @tparam T - Supported types are AMM, LM
    */
  sealed abstract class Redeem[+V <: Version, +T <: OrderType] extends Order[V, T, Operation.Redeem] {
    val poolId: PoolId
    val fee: Fee
  }

  object Redeem {

    implicit def redeemLoggable: Loggable[Order.AnyRedeem] = Loggable.show

    implicit def redeemShow: Show[Order.AnyRedeem] = {
      case r: RedeemV3       => r.show
      case r: RedeemV1       => r.show
      case r: RedeemLegacyV1 => r.show
    }

    implicit def redeemEncoder: Encoder[Redeem[Version, OrderType]] = deriveEncoder
    implicit def redeemDecoder: Decoder[Redeem[Version, OrderType]] = deriveDecoder

    implicit def eqAnyRedeem: Eq[Order.AnyRedeem] = (x: AnyRedeem, y: AnyRedeem) =>
      (x, y) match {
        case (x1: RedeemV1, y1: RedeemV1)             => x1 === y1
        case (x1: RedeemV3, y1: RedeemV3)             => x1 === y1
        case (x1: RedeemLegacyV1, y1: RedeemLegacyV1) => x1 === y1
        case (_, _)                                   => false
      }

    @derive(encoder, decoder, loggable, show, eqv)
    final case class RedeemV3(
      box: Output,
      fee: SPF,
      poolId: PoolId,
      redeemer: ErgoTreeRedeemer,
      params: RedeemParams,
      maxMinerFee: Long,
      version: V3,
      orderType: AMM,
      orderOperation: Operation.Redeem
    ) extends Redeem[V3, AMM]

    @derive(encoder, decoder, loggable, show, eqv)
    final case class RedeemV1(
      box: Output,
      fee: ERG,
      poolId: PoolId,
      redeemer: PublicKeyRedeemer,
      params: RedeemParams,
      maxMinerFee: Long,
      version: V1,
      orderType: AMM,
      orderOperation: Operation.Redeem
    ) extends Redeem[V1, AMM]

    @derive(encoder, decoder, loggable, show, eqv)
    final case class RedeemLegacyV1(
      box: Output,
      fee: ERG,
      poolId: PoolId,
      redeemer: PublicKeyRedeemer,
      params: RedeemParams,
      version: LegacyV1,
      orderType: AMM,
      orderOperation: Operation.Redeem
    ) extends Redeem[LegacyV1, AMM]
  }

  /** It's any swap order that exists in our domain.
    *
    * @tparam V - Supported versions are V3, V2, V1, Legacy v1
    * @tparam T - Supported types are AMM
    */
  sealed abstract class Swap[+V <: Version, +T <: OrderType] extends Order[V, T, Operation.Swap] {
    val poolId: PoolId
  }

  object Swap {

    implicit def swapLoggable: Loggable[Order.AnySwap] = Loggable.show

    implicit def swapShow: Show[Order.AnySwap] = {
      case s: SwapV3       => s.show
      case s: SwapV2       => s.show
      case s: SwapV1       => s.show
      case s: SwapLegacyV1 => s.show
    }

    implicit def swapEncoder: Encoder[Swap[Version, OrderType]] = deriveEncoder

    implicit def swapDecoder: Decoder[Swap[Version, OrderType]] = deriveDecoder

    implicit def eqAnySwap: Eq[Order.AnySwap] = (x: AnySwap, y: AnySwap) =>
      (x, y) match {
        case (x1: SwapV1, y1: SwapV1)             => x1 === y1
        case (x1: SwapV2, y1: SwapV2)             => x1 === y1
        case (x1: SwapV3, y1: SwapV3)             => x1 === y1
        case (x1: SwapLegacyV1, y1: SwapLegacyV1) => x1 === y1
        case (_, _)                               => false
      }

    @derive(encoder, decoder, loggable, show, eqv)
    final case class SwapV3(
      box: Output,
      poolId: PoolId,
      redeemer: ErgoTreeRedeemer,
      params: SwapParams,
      maxMinerFee: Long,
      reservedExFee: Long,
      version: V3,
      orderType: AMM,
      orderOperation: Operation.Swap
    ) extends Swap[V3, AMM]

    @derive(encoder, decoder, loggable, show, eqv)
    final case class SwapV2(
      box: Output,
      poolId: PoolId,
      redeemer: ErgoTreeRedeemer,
      params: SwapParams,
      maxMinerFee: Long,
      version: V2,
      orderType: AMM,
      orderOperation: Operation.Swap
    ) extends Swap[V2, AMM]

    @derive(encoder, decoder, loggable, show, eqv)
    final case class SwapV1(
      box: Output,
      poolId: PoolId,
      redeemer: PublicKeyRedeemer,
      params: SwapParams,
      maxMinerFee: Long,
      version: V1,
      orderType: AMM,
      orderOperation: Operation.Swap
    ) extends Swap[V1, AMM]

    @derive(encoder, decoder, loggable, show, eqv)
    final case class SwapLegacyV1(
      box: Output,
      poolId: PoolId,
      redeemer: PublicKeyRedeemer,
      params: SwapParams,
      version: LegacyV1,
      orderType: AMM,
      orderOperation: Operation.Swap
    ) extends Swap[LegacyV1, AMM]
  }

  /** It's any lock order that exists in our domain.
    *
    * @tparam V - Supported version is V1
    */
  sealed abstract class Lock[+V <: Version] extends Order[V, LOCK, Operation.Lock]

  object Lock {

    implicit def lockLoggable: Loggable[Order.AnyLock] = Loggable.show

    implicit def lockShow: Show[Order.AnyLock] = { case l: LockV1 =>
      l.show
    }

    implicit def lockEncoder: Encoder[Lock[Version]] = deriveEncoder

    implicit def lockDecoder: Decoder[Lock[Version]] = deriveDecoder

    implicit def eqAnyLock: Eq[Order.AnyLock] = (x: AnyLock, y: AnyLock) =>
      (x, y) match {
        case (x1: LockV1, y1: LockV1) => x1 === y1
        case (_, _)                   => false
      }

    @derive(encoder, decoder, loggable, show, eqv)
    final case class LockV1(
      box: Output,
      deadline: Int,
      amount: AssetAmount,
      redeemer: PublicKeyRedeemer,
      version: V1,
      orderType: LOCK,
      orderOperation: Operation.Lock
    ) extends Lock[V1]
  }

}
