package fi.spectrum.indexer.db.models

import cats.syntax.option._
import fi.spectrum.core.domain._
import fi.spectrum.core.domain.analytics.AnalyticsOptics._
import fi.spectrum.core.domain.analytics.OrderEvaluation.SwapEvaluation
import fi.spectrum.core.domain.analytics.{OrderEvaluation, Processed, Version}
import fi.spectrum.core.domain.order.Order.Swap._
import fi.spectrum.core.domain.order.OrderStatus.{Evaluated, Refunded, Registered}
import fi.spectrum.core.domain.order.{Fee, Order, OrderId, PoolId, Redeemer}
import fi.spectrum.indexer.classes.ToDB
import fi.spectrum.indexer.classes.syntax._
import glass.Subset

final case class SwapDB(
  orderId: OrderId,
  poolId: PoolId,
  poolBoxId: Option[BoxId],
  maxMinerFee: Option[Long],
  base: AssetAmount,
  minQuote: AssetAmount,
  quoteAmount: Option[Long],
  dexFeePerTokenNum: Long,
  dexFeePerTokenDenom: Long,
  fee: Option[Fee],
  redeemer: Option[PubKey],
  protocolVersion: ProtocolVersion,
  contractVersion: Version,
  redeemerErgoTree: Option[SErgoTree],
  registeredTx: Option[TxInfo],
  executedTx: Option[TxInfo],
  refundedTx: Option[TxInfo]
)

object SwapDB {

  implicit val toDB: ToDB[Processed[Order.Swap], SwapDB] = processed => {
    processed.order match {
      case swap: SwapV3       => processed.widen(swap).toDB
      case swap: SwapV2       => processed.widen(swap).toDB
      case swap: SwapV1       => processed.widen(swap).toDB
      case swap: SwapLegacyV1 => processed.widen(swap).toDB
      case swap: SwapLegacyV2 => processed.widen(swap).toDB
    }
  }

  implicit val ___V1: ToDB[Processed[SwapV1], SwapDB] =
    processed => {
      val swapEval = processed.evaluation.flatMap(Subset[OrderEvaluation, SwapEvaluation].getOption)
      val txInfo   = TxInfo(processed.state.txId, processed.state.timestamp)
      SwapDB(
        processed.order.id,
        processed.order.poolId,
        processed.poolBoxId,
        processed.order.maxMinerFee.some,
        processed.order.params.base,
        processed.order.params.minQuote,
        swapEval.map(_.output.amount),
        processed.order.params.dexFeePerTokenNum,
        processed.order.params.dexFeePerTokenDenom,
        processed.offChainFee.map(_.fee),
        processed.order.redeemer.value.some,
        ProtocolVersion(1),
        processed.order.version,
        none,
        if (processed.state.status.in(Registered)) txInfo.some else none,
        if (processed.state.status.in(Evaluated)) txInfo.some else none,
        if (processed.state.status.in(Refunded)) txInfo.some else none
      )
    }

  implicit val ___V2: ToDB[Processed[SwapV2], SwapDB] =
    processed => {
      val swapEval = processed.evaluation.flatMap(Subset[OrderEvaluation, SwapEvaluation].getOption)
      val txInfo   = TxInfo(processed.state.txId, processed.state.timestamp)
      SwapDB(
        processed.order.id,
        processed.order.poolId,
        processed.poolBoxId,
        processed.order.maxMinerFee.some,
        processed.order.params.base,
        processed.order.params.minQuote,
        swapEval.map(_.output.amount),
        processed.order.params.dexFeePerTokenNum,
        processed.order.params.dexFeePerTokenDenom,
        processed.offChainFee.map(_.fee),
        none,
        ProtocolVersion(1),
        processed.order.version,
        processed.order.redeemer.value.some,
        if (processed.state.status.in(Registered)) txInfo.some else none,
        if (processed.state.status.in(Evaluated)) txInfo.some else none,
        if (processed.state.status.in(Refunded)) txInfo.some else none
      )
    }

  implicit val ___V3: ToDB[Processed[SwapV3], SwapDB] =
    processed => {
      val swap     = processed.order
      val swapEval = processed.evaluation.flatMap(Subset[OrderEvaluation, SwapEvaluation].getOption)
      val txInfo   = TxInfo(processed.state.txId, processed.state.timestamp)
      val (tree, pk) = swap.redeemer match {
        case Redeemer.ErgoTreeRedeemer(value)  => (value.some, none)
        case Redeemer.PublicKeyRedeemer(value) => (none, value.some)
      }
      SwapDB(
        swap.id,
        swap.poolId,
        processed.poolBoxId,
        swap.maxMinerFee.some,
        swap.params.base,
        swap.params.minQuote,
        swapEval.map(_.output.amount),
        swap.params.dexFeePerTokenNum,
        swap.params.dexFeePerTokenDenom,
        processed.offChainFee.map(_.fee),
        pk,
        ProtocolVersion(1),
        swap.version,
        tree,
        if (processed.state.status.in(Registered)) txInfo.some else none,
        if (processed.state.status.in(Evaluated)) txInfo.some else none,
        if (processed.state.status.in(Refunded)) txInfo.some else none
      )
    }

  implicit val ___LegacyV1: ToDB[Processed[SwapLegacyV1], SwapDB] =
    processed => {
      val swap     = processed.order
      val swapEval = processed.evaluation.flatMap(Subset[OrderEvaluation, SwapEvaluation].getOption)
      val txInfo   = TxInfo(processed.state.txId, processed.state.timestamp)
      SwapDB(
        swap.id,
        swap.poolId,
        processed.poolBoxId,
        none,
        swap.params.base,
        swap.params.minQuote,
        swapEval.map(_.output.amount),
        swap.params.dexFeePerTokenNum,
        swap.params.dexFeePerTokenDenom,
        processed.offChainFee.map(_.fee),
        swap.redeemer.value.some,
        ProtocolVersion(1),
        swap.version,
        none,
        if (processed.state.status.in(Registered)) txInfo.some else none,
        if (processed.state.status.in(Evaluated)) txInfo.some else none,
        if (processed.state.status.in(Refunded)) txInfo.some else none
      )
    }

  implicit val ___LegacyV2: ToDB[Processed[SwapLegacyV2], SwapDB] =
    processed => {
      val swapEval = processed.evaluation.flatMap(Subset[OrderEvaluation, SwapEvaluation].getOption)
      val txInfo   = TxInfo(processed.state.txId, processed.state.timestamp)
      SwapDB(
        processed.order.id,
        processed.order.poolId,
        processed.poolBoxId,
        processed.order.maxMinerFee.some,
        processed.order.params.base,
        processed.order.params.minQuote,
        swapEval.map(_.output.amount),
        processed.order.params.dexFeePerTokenNum,
        processed.order.params.dexFeePerTokenDenom,
        processed.offChainFee.map(_.fee),
        processed.order.redeemer.value.some,
        ProtocolVersion(1),
        processed.order.version,
        none,
        if (processed.state.status.in(Registered)) txInfo.some else none,
        if (processed.state.status.in(Evaluated)) txInfo.some else none,
        if (processed.state.status.in(Refunded)) txInfo.some else none
      )
    }
}
