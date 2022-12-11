package fi.spectrum.indexer.models

import cats.syntax.option._
import fi.spectrum.core.domain._
import fi.spectrum.core.domain.analytics.AnalyticsOptics._
import fi.spectrum.core.domain.analytics.OrderEvaluation.RedeemEvaluation
import fi.spectrum.core.domain.analytics.{OrderEvaluation, ProcessedOrder, Version}
import fi.spectrum.core.domain.order.Order.Redeem.{RedeemLegacyV1, RedeemV1, RedeemV3}
import fi.spectrum.core.domain.order.OrderStatus.{Executed, Refunded, Registered}
import fi.spectrum.core.domain.order.{Fee, Order, OrderId, PoolId}
import fi.spectrum.indexer.classes.ToSchema
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
  registeredTransactionId: Option[TxId],
  registeredTransactionTimestamp: Option[Long],
  executedTransactionId: Option[TxId],
  executedTransactionTimestamp: Option[Long],
  refundedTransactionId: Option[TxId],
  refundedTransactionTimestamp: Option[Long]
)

object RedeemDB {

  implicit val toSchema: ToSchema[ProcessedOrder, Option[RedeemDB]] = processed =>
    ___V1.transform(processed) orElse ___V3.transform(processed) orElse ___LegacyV1.transform(processed)

  val ___V1: ToSchema[ProcessedOrder, Option[RedeemDB]] =
    processed => {
      val redeemEval = processed.evaluation.flatMap(Subset[OrderEvaluation, RedeemEvaluation].getOption)
      Subset[Order.Any, RedeemV1].getOption(processed.order) match {
        case Some(redeem) =>
          RedeemDB(
            redeem.id,
            redeem.poolId,
            processed.pool.map(_.box.boxId),
            redeem.maxMinerFee.some,
            redeem.params.lp,
            redeemEval.map(_.outputX),
            redeemEval.map(_.outputY),
            redeem.fee,
            redeem.redeemer.value.some,
            ProtocolVersion(1),
            redeem.version,
            none,
            if (processed.state.status.in(Registered)) processed.state.txId.some else none,
            if (processed.state.status.in(Registered)) processed.state.timestamp.some else none,
            if (processed.state.status.in(Executed)) processed.state.txId.some else none,
            if (processed.state.status.in(Executed)) processed.state.timestamp.some else none,
            if (processed.state.status.in(Refunded)) processed.state.txId.some else none,
            if (processed.state.status.in(Refunded)) processed.state.timestamp.some else none
          ).some
        case None =>
          none
      }
    }

  val ___V3: ToSchema[ProcessedOrder, Option[RedeemDB]] =
    processed => {
      val redeemEval = processed.evaluation.flatMap(Subset[OrderEvaluation, RedeemEvaluation].getOption)
      Subset[Order.Any, RedeemV3].getOption(processed.order) match {
        case Some(redeem) =>
          RedeemDB(
            redeem.id,
            redeem.poolId,
            processed.pool.map(_.box.boxId),
            redeem.maxMinerFee.some,
            redeem.params.lp,
            redeemEval.map(_.outputX),
            redeemEval.map(_.outputY),
            redeem.fee,
            none,
            ProtocolVersion(1),
            redeem.version,
            redeem.redeemer.value.some,
            if (processed.state.status.in(Registered)) processed.state.txId.some else none,
            if (processed.state.status.in(Registered)) processed.state.timestamp.some else none,
            if (processed.state.status.in(Executed)) processed.state.txId.some else none,
            if (processed.state.status.in(Executed)) processed.state.timestamp.some else none,
            if (processed.state.status.in(Refunded)) processed.state.txId.some else none,
            if (processed.state.status.in(Refunded)) processed.state.timestamp.some else none
          ).some
        case None =>
          none
      }
    }

  val ___LegacyV1: ToSchema[ProcessedOrder, Option[RedeemDB]] =
    processed => {
      val redeemEval = processed.evaluation.flatMap(Subset[OrderEvaluation, RedeemEvaluation].getOption)
      Subset[Order.Any, RedeemLegacyV1].getOption(processed.order) match {
        case Some(redeem) =>
          RedeemDB(
            redeem.id,
            redeem.poolId,
            processed.pool.map(_.box.boxId),
            none,
            redeem.params.lp,
            redeemEval.map(_.outputX),
            redeemEval.map(_.outputY),
            redeem.fee,
            redeem.redeemer.value.some,
            ProtocolVersion(1),
            redeem.version,
            none,
            if (processed.state.status.in(Registered)) processed.state.txId.some else none,
            if (processed.state.status.in(Registered)) processed.state.timestamp.some else none,
            if (processed.state.status.in(Executed)) processed.state.txId.some else none,
            if (processed.state.status.in(Executed)) processed.state.timestamp.some else none,
            if (processed.state.status.in(Refunded)) processed.state.txId.some else none,
            if (processed.state.status.in(Refunded)) processed.state.timestamp.some else none
          ).some
        case None =>
          none
      }
    }

}
