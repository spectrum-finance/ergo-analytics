package fi.spectrum.core.domain.order

import cats.syntax.functor._
import derevo.circe.{decoder, encoder}
import derevo.derive
import fi.spectrum.core.domain.{AssetAmount, BoxId, SErgoTree}
import fi.spectrum.core.domain.order.Fee.{ERG, SPF}
import fi.spectrum.core.domain.order.OrderType.{AMM, LOCK}
import fi.spectrum.core.domain.order.Redeemer.{ErgoTreeRedeemer, PublicKeyRedeemer}
import fi.spectrum.core.domain.order.Version.{V3, _}
import io.circe.derivation.{deriveDecoder, deriveEncoder}
import io.circe.syntax._
import io.circe.{Decoder, Encoder}

sealed trait Order[+V <: Version, +T <: OrderType, +O <: Operation] {
  val boxId: BoxId
  val fee: Fee
  val state: OrderState

  val version: V
  val orderType: T
  val orderOperation: O

  def id: OrderId = OrderId(boxId.value)
}

object Order {

  implicit def orderEncoder: Encoder[Order[Version, OrderType, Operation]] = {
    case deposit: Deposit[Version, OrderType] => deposit.asJson
    case redeem: Redeem[Version, OrderType]   => redeem.asJson
    case swap: Swap[Version, OrderType]       => swap.asJson
    case lock: Lock[Version, OrderType]       => lock.asJson
  }

  implicit def orderDecoder: Decoder[Order[Version, OrderType, Operation]] =
    List[Decoder[Order[Version, OrderType, Operation]]](
      Decoder[Deposit[Version, OrderType]].widen,
      Decoder[Redeem[Version, OrderType]].widen,
      Decoder[Swap[Version, OrderType]].widen,
      Decoder[Lock[Version, OrderType]].widen
    ).reduceLeft(_ or _)

  sealed abstract class Deposit[+V <: Version, +T <: OrderType] extends Order[V, T, Operation.Deposit] {
    val poolId: PoolId
  }

  object Deposit {

    implicit def depositEncoder: Encoder[Deposit[Version, OrderType]] = deriveEncoder
    implicit def depositDecoder: Decoder[Deposit[Version, OrderType]] = deriveDecoder

    @derive(encoder, decoder)
    final case class DepositV3(
      boxId: BoxId,
      fee: SPF,
      state: OrderState,
      poolId: PoolId,
      params: DepositParams[ErgoTreeRedeemer],
      maxMinerFee: Long,
      version: V3,
      orderType: AMM,
      orderOperation: Operation.Deposit
    ) extends Deposit[V3, AMM]

    @derive(encoder, decoder)
    final case class DepositV1(
      boxId: BoxId,
      fee: ERG,
      state: OrderState,
      poolId: PoolId,
      params: DepositParams[PublicKeyRedeemer],
      maxMinerFee: Long,
      version: V1,
      orderType: OrderType.AMM,
      orderOperation: Operation.Deposit
    ) extends Deposit[V1, AMM]

    @derive(encoder, decoder)
    final case class DepositLegacyV2(
      boxId: BoxId,
      fee: ERG,
      state: OrderState,
      poolId: PoolId,
      params: DepositParams[PublicKeyRedeemer],
      version: LegacyV2,
      orderType: AMM,
      orderOperation: Operation.Deposit
    ) extends Deposit[LegacyV2, AMM]

    @derive(encoder, decoder)
    final case class DepositLegacyV1(
      boxId: BoxId,
      fee: ERG,
      state: OrderState,
      poolId: PoolId,
      params: DepositParams[PublicKeyRedeemer],
      version: LegacyV1,
      orderType: OrderType.AMM,
      orderOperation: Operation.Deposit
    ) extends Deposit[LegacyV1, AMM]
  }

  sealed abstract class Redeem[+V <: Version, +T <: OrderType] extends Order[V, T, Operation.Redeem] {
    val poolId: PoolId
  }

  object Redeem {

    implicit def redeemEncoder: Encoder[Redeem[Version, OrderType]] = deriveEncoder
    implicit def redeemDecoder: Decoder[Redeem[Version, OrderType]] = deriveDecoder

    @derive(encoder, decoder)
    final case class RedeemV3(
      poolId: PoolId,
      boxId: BoxId,
      fee: SPF,
      state: OrderState,
      params: RedeemParams[ErgoTreeRedeemer],
      maxMinerFee: Long,
      version: V3,
      orderType: AMM,
      orderOperation: Operation.Redeem
    ) extends Redeem[V3, AMM]

    @derive(encoder, decoder)
    final case class RedeemV1(
      poolId: PoolId,
      boxId: BoxId,
      fee: ERG,
      state: OrderState,
      params: RedeemParams[PublicKeyRedeemer],
      maxMinerFee: Long,
      version: V1,
      orderType: AMM,
      orderOperation: Operation.Redeem
    ) extends Redeem[V1, AMM]

    @derive(encoder, decoder)
    final case class RedeemLegacyV1(
      poolId: PoolId,
      boxId: BoxId,
      fee: ERG,
      state: OrderState,
      params: RedeemParams[PublicKeyRedeemer],
      version: LegacyV1,
      orderType: AMM,
      orderOperation: Operation.Redeem
    ) extends Redeem[LegacyV1, AMM]
  }

  sealed abstract class Swap[+V <: Version, +T <: OrderType] extends Order[V, T, Operation.Swap] {
    val poolId: PoolId
  }

  object Swap {
    implicit def swapEncoder: Encoder[Swap[Version, OrderType]] = deriveEncoder

    implicit def swapDecoder: Decoder[Swap[Version, OrderType]] = deriveDecoder

    @derive(encoder, decoder)
    final case class SwapV3(
      poolId: PoolId,
      boxId: BoxId,
      fee: SPF,
      state: OrderState,
      params: SwapParams[ErgoTreeRedeemer],
      maxMinerFee: Long,
      reservedExFee: Long,
      version: V3,
      orderType: AMM,
      orderOperation: Operation.Swap
    ) extends Swap[V3, AMM]

    @derive(encoder, decoder)
    final case class SwapV2(
      poolId: PoolId,
      boxId: BoxId,
      fee: ERG,
      state: OrderState,
      params: SwapParams[ErgoTreeRedeemer],
      maxMinerFee: Long,
      version: V2,
      orderType: AMM,
      orderOperation: Operation.Swap
    ) extends Swap[V2, AMM]

    @derive(encoder, decoder)
    final case class SwapV1(
      poolId: PoolId,
      boxId: BoxId,
      fee: ERG,
      state: OrderState,
      params: SwapParams[PublicKeyRedeemer],
      maxMinerFee: Long,
      version: V1,
      orderType: AMM,
      orderOperation: Operation.Swap
    ) extends Swap[V1, AMM]

    @derive(encoder, decoder)
    final case class SwapLegacyV1(
      poolId: PoolId,
      boxId: BoxId,
      fee: ERG,
      state: OrderState,
      params: SwapParams[PublicKeyRedeemer],
      version: LegacyV1,
      orderType: AMM,
      orderOperation: Operation.Swap
    ) extends Swap[LegacyV1, AMM]
  }

  sealed abstract class Lock[+V <: Version, +T <: OrderType] extends Order[V, T, Operation.Lock]

  object Lock {

    implicit def lockEncoder: Encoder[Lock[Version, OrderType]] = deriveEncoder

    implicit def lockDecoder: Decoder[Lock[Version, OrderType]] = deriveDecoder

    @derive(encoder, decoder)
    final case class LockV1(
      boxId: BoxId,
      fee: ERG,
      state: OrderState,
      deadline: Int,
      amount: AssetAmount,
      redeemer: SErgoTree,
      version: V1,
      orderType: LOCK,
      orderOperation: Operation.Lock
    ) extends Lock[V1, LOCK]
  }

}
