package fi.spectrum.indexer.db.models

import cats.syntax.option._
import fi.spectrum.core.domain._
import fi.spectrum.core.domain.analytics.AnalyticsOptics._
import fi.spectrum.core.domain.analytics.OrderEvaluation.AmmDepositEvaluation
import fi.spectrum.core.domain.analytics.{OrderEvaluation, Processed, Version}
import fi.spectrum.core.domain.order.Order.Deposit.AmmDeposit
import fi.spectrum.core.domain.order.Order.Deposit.AmmDeposit._
import fi.spectrum.core.domain.order.OrderStatus.{Evaluated, Refunded, Registered}
import fi.spectrum.core.domain.order.{Fee, OrderId, PoolId, Redeemer}
import fi.spectrum.indexer.classes.ToDB
import fi.spectrum.indexer.classes.syntax._
import glass.Subset

final case class AmmDepositDB(
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

object AmmDepositDB {

  implicit val toDB: ToDB[Processed[AmmDeposit], AmmDepositDB] = processed => {
    processed.order match {
      case deposit: AmmDepositV3       => processed.widen(deposit).toDB
      case deposit: AmmDepositV1       => processed.widen(deposit).toDB
      case deposit: AmmDepositLegacyV3 => processed.widen(deposit).toDB
      case deposit: AmmDepositLegacyV2 => processed.widen(deposit).toDB
      case deposit: AmmDepositLegacyV1 => processed.widen(deposit).toDB
    }
  }

  implicit val ___V1: ToDB[Processed[AmmDepositV1], AmmDepositDB] =
    processed => {
      val deposit     = processed.order
      val depositEval = processed.evaluation.flatMap(Subset[OrderEvaluation, AmmDepositEvaluation].getOption)
      val txInfo      = TxInfo(processed.state.txId, processed.state.timestamp)
      AmmDepositDB(
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

  implicit val ___V3: ToDB[Processed[AmmDepositV3], AmmDepositDB] =
    processed => {
      val deposit     = processed.order
      val depositEval = processed.evaluation.flatMap(Subset[OrderEvaluation, AmmDepositEvaluation].getOption)
      val txInfo      = TxInfo(processed.state.txId, processed.state.timestamp)
      val (tree, pk) = deposit.redeemer match {
        case Redeemer.ErgoTreeRedeemer(value)  => (value.some, none)
        case Redeemer.PublicKeyRedeemer(value) => (none, value.some)
      }
      AmmDepositDB(
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
        pk,
        ProtocolVersion.init,
        deposit.version,
        tree,
        if (processed.state.status.in(Registered)) txInfo.some else none,
        if (processed.state.status.in(Evaluated)) txInfo.some else none,
        if (processed.state.status.in(Refunded)) txInfo.some else none
      )
    }

  implicit val ___LegacyV1: ToDB[Processed[AmmDepositLegacyV1], AmmDepositDB] =
    processed => {
      val deposit     = processed.order
      val depositEval = processed.evaluation.flatMap(Subset[OrderEvaluation, AmmDepositEvaluation].getOption)
      val txInfo      = TxInfo(processed.state.txId, processed.state.timestamp)
      AmmDepositDB(
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

  implicit val ___LegacyV2: ToDB[Processed[AmmDepositLegacyV2], AmmDepositDB] =
    processed => {
      val deposit     = processed.order
      val depositEval = processed.evaluation.flatMap(Subset[OrderEvaluation, AmmDepositEvaluation].getOption)
      val txInfo      = TxInfo(processed.state.txId, processed.state.timestamp)
      AmmDepositDB(
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

  implicit val ___LegacyV3: ToDB[Processed[AmmDepositLegacyV3], AmmDepositDB] =
    processed => {
      val deposit = processed.order
      val depositEval = processed.evaluation.flatMap(Subset[OrderEvaluation, AmmDepositEvaluation].getOption)
      val txInfo = TxInfo(processed.state.txId, processed.state.timestamp)
      AmmDepositDB(
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
}
