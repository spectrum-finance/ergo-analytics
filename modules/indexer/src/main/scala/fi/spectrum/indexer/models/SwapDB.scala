package fi.spectrum.indexer.models

import cats.syntax.option._
import fi.spectrum.core.domain._
import fi.spectrum.core.domain.analytics.AnalyticsOptics._
import fi.spectrum.core.domain.analytics.OrderEvaluation.SwapEvaluation
import fi.spectrum.core.domain.analytics.{OrderEvaluation, ProcessedOrder, Version}
import fi.spectrum.core.domain.order.Order.Swap._
import fi.spectrum.core.domain.order.OrderStatus.{Executed, Refunded, Registered}
import fi.spectrum.core.domain.order.{Order, OrderId, PoolId}
import fi.spectrum.indexer.classes.syntax._
import fi.spectrum.indexer.classes.{ToDB, ToSchema}
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
  redeemer: Option[PubKey],
  protocolVersion: ProtocolVersion,
  contractVersion: Version,
  redeemerErgoTree: Option[SErgoTree],
  registeredTx: Option[TxInfo],
  executedTx: Option[TxInfo],
  refundedTx: Option[TxInfo]
)

object SwapDB {

  implicit val toSchema: ToSchema[ProcessedOrder[Order.AnySwap], SwapDB] = processed => {
    processed.order match {
      case swap: SwapV3       => processed.widen(swap).toDB
      case swap: SwapV2       => processed.widen(swap).toDB
      case swap: SwapV1       => processed.widen(swap).toDB
      case swap: SwapLegacyV1 => processed.widen(swap).toDB
    }
  }

  implicit val ___V1: ToDB[ProcessedOrder[SwapV1], SwapDB] =
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
        processed.order.redeemer.value.some,
        ProtocolVersion(1),
        processed.order.version,
        none,
        if (processed.state.status.in(Registered)) txInfo.some else none,
        if (processed.state.status.in(Executed)) txInfo.some else none,
        if (processed.state.status.in(Refunded)) txInfo.some else none
      )
    }

  implicit val ___V2: ToDB[ProcessedOrder[SwapV2], SwapDB] =
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
        none,
        ProtocolVersion(1),
        processed.order.version,
        processed.order.redeemer.value.some,
        if (processed.state.status.in(Registered)) txInfo.some else none,
        if (processed.state.status.in(Executed)) txInfo.some else none,
        if (processed.state.status.in(Refunded)) txInfo.some else none
      )
    }

  implicit val ___V3: ToDB[ProcessedOrder[SwapV3], SwapDB] =
    processed => {
      val swap     = processed.order
      val swapEval = processed.evaluation.flatMap(Subset[OrderEvaluation, SwapEvaluation].getOption)
      val txInfo   = TxInfo(processed.state.txId, processed.state.timestamp)
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
        none,
        ProtocolVersion(1),
        swap.version,
        swap.redeemer.value.some,
        if (processed.state.status.in(Registered)) txInfo.some else none,
        if (processed.state.status.in(Executed)) txInfo.some else none,
        if (processed.state.status.in(Refunded)) txInfo.some else none
      )
    }

  implicit val ___LegacyV1: ToDB[ProcessedOrder[SwapLegacyV1], SwapDB] =
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
        swap.redeemer.value.some,
        ProtocolVersion(1),
        swap.version,
        none,
        if (processed.state.status.in(Registered)) txInfo.some else none,
        if (processed.state.status.in(Executed)) txInfo.some else none,
        if (processed.state.status.in(Refunded)) txInfo.some else none
      )
    }
}
