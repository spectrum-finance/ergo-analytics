package fi.spectrum.indexer.models

import cats.syntax.option._
import fi.spectrum.core.domain._
import fi.spectrum.core.domain.analytics.AnalyticsOptics._
import fi.spectrum.core.domain.analytics.OrderEvaluation.SwapEvaluation
import fi.spectrum.core.domain.analytics.{OrderEvaluation, ProcessedOrder, Version}
import fi.spectrum.core.domain.order.Order.Swap._
import fi.spectrum.core.domain.order.OrderStatus.{Executed, Refunded, Registered}
import fi.spectrum.core.domain.order.{Order, OrderId, PoolId}
import fi.spectrum.indexer.classes.ToSchema
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

  implicit val toSchema: ToSchema[ProcessedOrder, Option[SwapDB]] = processed =>
    ___V1.transform(processed) orElse ___V2.transform(processed) orElse ___V3.transform(processed) orElse
    ___LegacyV1.transform(processed)

  val ___V1: ToSchema[ProcessedOrder, Option[SwapDB]] =
    processed => {
      val swapEval = processed.evaluation.flatMap(Subset[OrderEvaluation, SwapEvaluation].getOption)
      Subset[Order.Any, SwapV1].getOption(processed.order) match {
        case Some(swap) =>
          val txInfo = TxInfo(processed.state.txId, processed.state.timestamp)
          SwapDB(
            swap.id,
            swap.poolId,
            processed.pool.map(_.box.boxId),
            swap.maxMinerFee.some,
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
          ).some
        case None =>
          none
      }
    }

  val ___V2: ToSchema[ProcessedOrder, Option[SwapDB]] =
    processed => {
      val swapEval = processed.evaluation.flatMap(Subset[OrderEvaluation, SwapEvaluation].getOption)
      Subset[Order.Any, SwapV2].getOption(processed.order) match {
        case Some(swap) =>
          val txInfo = TxInfo(processed.state.txId, processed.state.timestamp)
          SwapDB(
            swap.id,
            swap.poolId,
            processed.pool.map(_.box.boxId),
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
          ).some
        case None =>
          none
      }
    }

  val ___V3: ToSchema[ProcessedOrder, Option[SwapDB]] =
    processed => {
      val swapEval = processed.evaluation.flatMap(Subset[OrderEvaluation, SwapEvaluation].getOption)
      Subset[Order.Any, SwapV3].getOption(processed.order) match {
        case Some(swap) =>
          val txInfo = TxInfo(processed.state.txId, processed.state.timestamp)
          SwapDB(
            swap.id,
            swap.poolId,
            processed.pool.map(_.box.boxId),
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
          ).some
        case None =>
          none
      }
    }

  val ___LegacyV1: ToSchema[ProcessedOrder, Option[SwapDB]] =
    processed => {
      val swapEval = processed.evaluation.flatMap(Subset[OrderEvaluation, SwapEvaluation].getOption)
      Subset[Order.Any, SwapLegacyV1].getOption(processed.order) match {
        case Some(swap) =>
          val txInfo = TxInfo(processed.state.txId, processed.state.timestamp)
          SwapDB(
            swap.id,
            swap.poolId,
            processed.pool.map(_.box.boxId),
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
          ).some
        case None =>
          none
      }
    }
}
