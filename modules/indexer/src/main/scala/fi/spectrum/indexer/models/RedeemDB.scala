package fi.spectrum.indexer.models

import cats.syntax.option._
import fi.spectrum.core.domain._
import fi.spectrum.core.domain.analytics.AnalyticsOptics._
import fi.spectrum.core.domain.analytics.OrderEvaluation.RedeemEvaluation
import fi.spectrum.core.domain.analytics.{OrderEvaluation, ProcessedOrder, Version}
import fi.spectrum.core.domain.order.Order.Redeem.{RedeemLegacyV1, RedeemV1, RedeemV3}
import fi.spectrum.core.domain.order.OrderStatus.{Executed, Refunded, Registered}
import fi.spectrum.core.domain.order.{Fee, Order, OrderId, PoolId}
import fi.spectrum.indexer.classes.syntax._
import fi.spectrum.indexer.classes.{ToDB, ToSchema}
import glass.Subset

final case class RedeemDB(
  orderId: OrderId,
  poolId: PoolId,
  poolBoxId: Option[BoxId],
  maxMinerFee: Option[Long],
  lp: AssetAmount,
  x: Option[AssetAmount],
  y: Option[AssetAmount],
  dexFee: Fee,
  redeemer: Option[PubKey],
  protocolVersion: ProtocolVersion,
  contractVersion: Version,
  redeemerErgoTree: Option[SErgoTree],
  registeredTx: Option[TxInfo],
  executedTx: Option[TxInfo],
  refundedTx: Option[TxInfo]
)

object RedeemDB {

  implicit val toSchema: ToSchema[ProcessedOrder[Order.AnyRedeem], RedeemDB] = processed => {
    processed.order match {
      case redeem: RedeemV3       => processed.widen(redeem).toDB
      case redeem: RedeemV1       => processed.widen(redeem).toDB
      case redeem: RedeemLegacyV1 => processed.widen(redeem).toDB
    }
  }

  implicit val ___V1: ToDB[ProcessedOrder[RedeemV1], RedeemDB] =
    processed => {
      val redeem     = processed.order
      val redeemEval = processed.evaluation.flatMap(Subset[OrderEvaluation, RedeemEvaluation].getOption)
      val txInfo     = TxInfo(processed.state.txId, processed.state.timestamp)
      RedeemDB(
        redeem.id,
        redeem.poolId,
        processed.poolBoxId,
        redeem.maxMinerFee.some,
        redeem.params.lp,
        redeemEval.map(_.outputX),
        redeemEval.map(_.outputY),
        redeem.fee,
        redeem.redeemer.value.some,
        ProtocolVersion(1),
        redeem.version,
        none,
        if (processed.state.status.in(Registered)) txInfo.some else none,
        if (processed.state.status.in(Executed)) txInfo.some else none,
        if (processed.state.status.in(Refunded)) txInfo.some else none
      )
    }

  implicit val ___V3: ToDB[ProcessedOrder[RedeemV3], RedeemDB] =
    processed => {
      val redeem     = processed.order
      val redeemEval = processed.evaluation.flatMap(Subset[OrderEvaluation, RedeemEvaluation].getOption)
      val txInfo     = TxInfo(processed.state.txId, processed.state.timestamp)
      RedeemDB(
        redeem.id,
        redeem.poolId,
        processed.poolBoxId,
        redeem.maxMinerFee.some,
        redeem.params.lp,
        redeemEval.map(_.outputX),
        redeemEval.map(_.outputY),
        redeem.fee,
        none,
        ProtocolVersion(1),
        redeem.version,
        redeem.redeemer.value.some,
        if (processed.state.status.in(Registered)) txInfo.some else none,
        if (processed.state.status.in(Executed)) txInfo.some else none,
        if (processed.state.status.in(Refunded)) txInfo.some else none
      )
    }

  implicit val ___LegacyV1: ToDB[ProcessedOrder[RedeemLegacyV1], RedeemDB] =
    processed => {
      val redeem     = processed.order
      val redeemEval = processed.evaluation.flatMap(Subset[OrderEvaluation, RedeemEvaluation].getOption)
      val txInfo     = TxInfo(processed.state.txId, processed.state.timestamp)
      RedeemDB(
        redeem.id,
        redeem.poolId,
        processed.poolBoxId,
        none,
        redeem.params.lp,
        redeemEval.map(_.outputX),
        redeemEval.map(_.outputY),
        redeem.fee,
        redeem.redeemer.value.some,
        ProtocolVersion(1),
        redeem.version,
        none,
        if (processed.state.status.in(Registered)) txInfo.some else none,
        if (processed.state.status.in(Executed)) txInfo.some else none,
        if (processed.state.status.in(Refunded)) txInfo.some else none
      )
    }

}
