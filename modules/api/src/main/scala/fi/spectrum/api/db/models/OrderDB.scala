package fi.spectrum.api.db.models

import derevo.cats.show
import derevo.circe.{decoder, encoder}
import derevo.derive
import fi.spectrum.api.v1.models.history.TxData
import fi.spectrum.core.domain.order.{Fee, OrderId, PoolId}
import fi.spectrum.core.domain.{AssetAmount, PubKey, TokenId, TxId}
import sttp.tapir.Schema
import tofu.logging.derivation.loggable

@derive(show, encoder, decoder, loggable)
sealed trait OrderDB

object OrderDB {

  @derive(show, encoder, decoder, loggable)
  final case class SwapDB(
    id: OrderId,
    poolId: PoolId,
    base: AssetAmount,
    minQuote: AssetAmount,
    quote: Option[Long],
    fee: Option[Fee],
    address: Option[PubKey],
    registerTx: TxData,
    refundTx: Option[TxData],
    evaluateTx: Option[TxData]
  ) extends OrderDB

  @derive(show, encoder, decoder, loggable)
  final case class AmmDepositDB(
    orderId: OrderId,
    poolId: PoolId,
    inputX: AssetAmount,
    inputY: AssetAmount,
    actualX: Option[Long],
    actualY: Option[Long],
    outputLp: Option[AssetAmount],
    fee: Fee,
    address: Option[PubKey],
    registerTx: TxData,
    refundTx: Option[TxData],
    evaluateTx: Option[TxData]
  ) extends OrderDB

  @derive(show, encoder, decoder, loggable)
  final case class AmmRedeemDB(
    id: OrderId,
    poolId: PoolId,
    lp: AssetAmount,
    outX: Option[AssetAmount],
    outY: Option[AssetAmount],
    fee: Fee,
    address: Option[PubKey],
    registerTx: TxData,
    refundTx: Option[TxData],
    evaluateTx: Option[TxData]
  ) extends OrderDB

  @derive(show, encoder, decoder, loggable)
  final case class LockDB(
    id: OrderId,
    registerTx: TxData,
    deadline: Int,
    asset: AssetAmount,
    address: Option[PubKey],
    evalTxId: Option[TxId],
    evalType: Option[String]
  ) extends OrderDB

  @derive(show, encoder, decoder, loggable)
  final case class LmDepositDB(
    orderId: OrderId,
    poolId: PoolId,
    expectedNumEpochs: Int,
    input: AssetAmount,
    lp: Option[AssetAmount],
    compoundId: Option[TokenId],
    registerTx: TxData,
    refundTx: Option[TxData],
    evaluateTx: Option[TxData]
  ) extends OrderDB

  @derive(show, encoder, decoder, loggable)
  final case class LmCompoundDB(
    orderId: OrderId,
    poolId: PoolId,
    vLq: AssetAmount,
    tmp: AssetAmount,
    bundleKeyId: TokenId,
    registerTx: TxData,
    evaluateTx: Option[TxData]
  ) extends OrderDB

  @derive(show, encoder, decoder, loggable)
  final case class LmRedeemsDB(
    orderId: OrderId,
    poolId: Option[PoolId],
    bundleKeyId: TokenId,
    expectedLq: AssetAmount,
    out: Option[AssetAmount],
    boxId: Option[TokenId],
    registerTx: TxData,
    refundTx: Option[TxData],
    evaluateTx: Option[TxData]
  ) extends OrderDB

  @derive(show, encoder, decoder, loggable)
  case class AnyOrderDB(
    orderId: OrderId,
    poolId: Option[PoolId],
    orderTypeString: String,
    swapBase: Option[AssetAmount],
    swapMinQuote: Option[AssetAmount],
    swapQuote: Option[Long],
    depositX: Option[AssetAmount],
    depositY: Option[AssetAmount],
    depositLp: Option[AssetAmount],
    depositActualX: Option[Long],
    depositActualY: Option[Long],
    redeemLp: Option[AssetAmount],
    redeemX: Option[AssetAmount],
    redeemY: Option[AssetAmount],
    lockDeadline: Option[Int],
    lockAsset: Option[AssetAmount],
    lockEvalTxId: Option[TxId],
    lockEvalType: Option[String],
    lmCompoundVLq: Option[AssetAmount],
    lmCompoundTmp: Option[AssetAmount],
    lmCompoundBundleKeyId: Option[TokenId],
    lmDepositExpectedNumEpochs: Option[Int],
    lmDepositInput: Option[AssetAmount],
    lmDepositLp: Option[AssetAmount],
    lmDepositCompoundId: Option[TokenId],
    lmRedeemBundleKeyId: Option[TokenId],
    lmRedeemExpectedLq: Option[AssetAmount],
    lmRedeemOut: Option[AssetAmount],
    lmRedeemBoxId: Option[TokenId],
    fee: Option[Fee],
    redeemer: Option[PubKey],
    registerTx: TxData,
    executedTx: Option[TxData],
    refundedTx: Option[TxData]
  ) extends OrderDB

  implicit val schema: Schema[OrderDB] = Schema.derived
}
