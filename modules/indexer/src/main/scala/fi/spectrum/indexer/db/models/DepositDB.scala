package fi.spectrum.indexer.db.models

import cats.syntax.option._
import fi.spectrum.core.domain._
import fi.spectrum.core.domain.analytics.AnalyticsOptics._
import fi.spectrum.core.domain.analytics.OrderEvaluation.DepositEvaluation
import fi.spectrum.core.domain.analytics.{OrderEvaluation, Processed, Version}
import fi.spectrum.core.domain.order.Order.Deposit._
import fi.spectrum.core.domain.order.OrderStatus.{Evaluated, Refunded, Registered}
import fi.spectrum.core.domain.order.{Fee, Order, OrderId, PoolId}
import fi.spectrum.indexer.classes.syntax._
import fi.spectrum.indexer.classes.ToDB
import glass.Subset

final case class DepositDB(
  orderId: OrderId,
  poolId: PoolId,
  poolStateId: Option[BoxId],
  maxMinerFee: Option[Long],
  x: AssetAmount,
  y: AssetAmount,
  lp: Option[AssetAmount],
  actualX: Option[Long],
  actualY: Option[Long],
  dexFee: Fee,
  redeemer: Option[PubKey],
  protocolVersion: ProtocolVersion,
  contractVersion: Version,
  redeemerErgoTree: Option[SErgoTree],
  registeredTx: Option[TxInfo],
  executedTx: Option[TxInfo],
  refundedTx: Option[TxInfo]
)

object DepositDB {

  implicit val toDB: ToDB[Processed[Order.Deposit], DepositDB] = processed => {
    processed.order match {
      case deposit: DepositV3       => processed.widen(deposit).toDB
      case deposit: DepositV1       => processed.widen(deposit).toDB
      case deposit: DepositLegacyV2 => processed.widen(deposit).toDB
      case deposit: DepositLegacyV1 => processed.widen(deposit).toDB
    }
  }

  implicit val ___V1: ToDB[Processed[DepositV1], DepositDB] =
    processed => {
      val deposit     = processed.order
      val depositEval = processed.evaluation.flatMap(Subset[OrderEvaluation, DepositEvaluation].getOption)
      val txInfo      = TxInfo(processed.state.txId, processed.state.timestamp)
      DepositDB(
        deposit.id,
        deposit.poolId,
        processed.poolBoxId,
        deposit.maxMinerFee.some,
        deposit.params.inX,
        deposit.params.inY,
        depositEval.map(_.outputLP),
        depositEval.map(_.actualX),
        depositEval.map(_.actualY),
        deposit.fee,
        deposit.redeemer.value.some,
        ProtocolVersion.init,
        deposit.version,
        none,
        if (processed.state.status.in(Registered)) txInfo.some else none,
        if (processed.state.status.in(Evaluated)) txInfo.some else none,
        if (processed.state.status.in(Refunded)) txInfo.some else none
      )
    }

  implicit val ___V3: ToDB[Processed[DepositV3], DepositDB] =
    processed => {
      val deposit     = processed.order
      val depositEval = processed.evaluation.flatMap(Subset[OrderEvaluation, DepositEvaluation].getOption)
      val txInfo      = TxInfo(processed.state.txId, processed.state.timestamp)
      DepositDB(
        deposit.id,
        deposit.poolId,
        processed.poolBoxId,
        deposit.maxMinerFee.some,
        deposit.params.inX,
        deposit.params.inY,
        depositEval.map(_.outputLP),
        depositEval.map(_.actualX),
        depositEval.map(_.actualY),
        deposit.fee,
        none,
        ProtocolVersion.init,
        deposit.version,
        deposit.redeemer.value.some,
        if (processed.state.status.in(Registered)) txInfo.some else none,
        if (processed.state.status.in(Evaluated)) txInfo.some else none,
        if (processed.state.status.in(Refunded)) txInfo.some else none
      )
    }

  implicit val ___LegacyV1: ToDB[Processed[DepositLegacyV1], DepositDB] =
    processed => {
      val deposit     = processed.order
      val depositEval = processed.evaluation.flatMap(Subset[OrderEvaluation, DepositEvaluation].getOption)
      val txInfo      = TxInfo(processed.state.txId, processed.state.timestamp)
      DepositDB(
        deposit.id,
        deposit.poolId,
        processed.poolBoxId,
        none,
        deposit.params.inX,
        deposit.params.inY,
        depositEval.map(_.outputLP),
        depositEval.map(_.actualX),
        depositEval.map(_.actualY),
        deposit.fee,
        deposit.redeemer.value.some,
        ProtocolVersion.init,
        deposit.version,
        none,
        if (processed.state.status.in(Registered)) txInfo.some else none,
        if (processed.state.status.in(Evaluated)) txInfo.some else none,
        if (processed.state.status.in(Refunded)) txInfo.some else none
      )
    }

  implicit val ___LegacyV2: ToDB[Processed[DepositLegacyV2], DepositDB] =
    processed => {
      val deposit     = processed.order
      val depositEval = processed.evaluation.flatMap(Subset[OrderEvaluation, DepositEvaluation].getOption)
      val txInfo      = TxInfo(processed.state.txId, processed.state.timestamp)
      DepositDB(
        deposit.id,
        deposit.poolId,
        processed.poolBoxId,
        none,
        deposit.params.inX,
        deposit.params.inY,
        depositEval.map(_.outputLP),
        depositEval.map(_.actualX),
        depositEval.map(_.actualY),
        deposit.fee,
        deposit.redeemer.value.some,
        ProtocolVersion.init,
        deposit.version,
        none,
        if (processed.state.status.in(Registered)) txInfo.some else none,
        if (processed.state.status.in(Evaluated)) txInfo.some else none,
        if (processed.state.status.in(Refunded)) txInfo.some else none
      )
    }
}
