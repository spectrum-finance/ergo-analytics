package fi.spectrum.core.domain.order

import cats.syntax.functor._
import derevo.circe.{decoder, encoder}
import derevo.derive
import fi.spectrum.core.domain.{AssetAmount, BoxId, PubKey, SErgoTree}
import fi.spectrum.core.domain.order.Fee.{ERG, SPF}
import fi.spectrum.core.domain.order.Order.Deposit.{DepositV1, DepositV3, DepositLegacyV1, DepositLegacyV2}
import fi.spectrum.core.domain.order.OrderType.{AMM, LOCK}
import fi.spectrum.core.domain.order.Redeemer.{ErgoTreeRedeemer, PublicKeyRedeemer}
import fi.spectrum.core.domain.order.Version.{V3, _}
import fi.spectrum.core.domain.transaction.Output
import glass.Subset
import glass.macros.{ClassyOptics, GenSubset}
import io.circe.derivation.{deriveDecoder, deriveEncoder}
import io.circe.syntax._
import io.circe.{Decoder, Encoder}

sealed trait Order[+V <: Version, +T <: OrderType, +O <: Operation] {
  val box: Output
  val redeemer: Redeemer

  val version: V
  val orderType: T
  val orderOperation: O

  def id: OrderId = OrderId(box.boxId.value)
}

object Order {

  type Any = Order[Version, OrderType, Operation]

  implicit val prismOrderAMMDeposit: Subset[Order[Version, AMM, Operation], Deposit[Version, AMM]] =
    GenSubset[Order[Version, AMM, Operation], Deposit[Version, AMM]]

  implicit val prismDepositDepositV1: Subset[Deposit[Version, AMM], DepositV1] =
    GenSubset[Deposit[Version, AMM], DepositV1]

  implicit val prismDepositDepositV3: Subset[Deposit[Version, AMM], DepositV3] =
    GenSubset[Deposit[Version, AMM], DepositV3]

  implicit val prismDepositDepositLegacyV1: Subset[Deposit[Version, AMM], DepositLegacyV1] =
    GenSubset[Deposit[Version, AMM], DepositLegacyV1]

  implicit val prismDepositDepositLegacyV2: Subset[Deposit[Version, AMM], DepositLegacyV2] =
    GenSubset[Deposit[Version, AMM], DepositLegacyV2]

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

  sealed abstract class Deposit[+V <: Version, +T <: OrderType] extends Order[V, T, Operation.Deposit] {
    val poolId: PoolId
  }

  object Deposit {

    implicit def depositEncoder: Encoder[Deposit[Version, OrderType]] = deriveEncoder
    implicit def depositDecoder: Decoder[Deposit[Version, OrderType]] = deriveDecoder

    @ClassyOptics
    @derive(encoder, decoder)
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
    @derive(encoder, decoder)
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

    @ClassyOptics
    @derive(encoder, decoder)
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

    @derive(encoder, decoder)
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

  sealed abstract class Redeem[+V <: Version, +T <: OrderType] extends Order[V, T, Operation.Redeem] {
    val poolId: PoolId
  }

  object Redeem {

    implicit def redeemEncoder: Encoder[Redeem[Version, OrderType]] = deriveEncoder
    implicit def redeemDecoder: Decoder[Redeem[Version, OrderType]] = deriveDecoder

    @derive(encoder, decoder)
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

    @derive(encoder, decoder)
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

    @derive(encoder, decoder)
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

  sealed abstract class Swap[+V <: Version, +T <: OrderType] extends Order[V, T, Operation.Swap] {
    val poolId: PoolId
  }

  object Swap {
    implicit def swapEncoder: Encoder[Swap[Version, OrderType]] = deriveEncoder

    implicit def swapDecoder: Decoder[Swap[Version, OrderType]] = deriveDecoder

    @derive(encoder, decoder)
    final case class SwapV3(
      box: Output,
      fee: SPF,
      poolId: PoolId,
      redeemer: ErgoTreeRedeemer,
      params: SwapParams,
      maxMinerFee: Long,
      reservedExFee: Long,
      version: V3,
      orderType: AMM,
      orderOperation: Operation.Swap
    ) extends Swap[V3, AMM]

    @derive(encoder, decoder)
    final case class SwapV2(
      box: Output,
      fee: ERG,
      poolId: PoolId,
      redeemer: ErgoTreeRedeemer,
      params: SwapParams,
      maxMinerFee: Long,
      version: V2,
      orderType: AMM,
      orderOperation: Operation.Swap
    ) extends Swap[V2, AMM]

    @derive(encoder, decoder)
    final case class SwapV1(
      box: Output,
      fee: ERG,
      poolId: PoolId,
      redeemer: PublicKeyRedeemer,
      params: SwapParams,
      maxMinerFee: Long,
      version: V1,
      orderType: AMM,
      orderOperation: Operation.Swap
    ) extends Swap[V1, AMM]

    @derive(encoder, decoder)
    final case class SwapLegacyV1(
      box: Output,
      fee: ERG,
      poolId: PoolId,
      redeemer: PublicKeyRedeemer,
      params: SwapParams,
      version: LegacyV1,
      orderType: AMM,
      orderOperation: Operation.Swap
    ) extends Swap[LegacyV1, AMM]
  }

  sealed abstract class Lock[+V <: Version] extends Order[V, LOCK, Operation.Lock]

  object Lock {

    implicit def lockEncoder: Encoder[Lock[Version]] = deriveEncoder

    implicit def lockDecoder: Decoder[Lock[Version]] = deriveDecoder

    @derive(encoder, decoder)
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
