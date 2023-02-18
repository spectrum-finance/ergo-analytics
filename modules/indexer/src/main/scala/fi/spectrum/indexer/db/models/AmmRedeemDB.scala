package fi.spectrum.indexer.db.models

import cats.syntax.option._
import fi.spectrum.core.domain._
import fi.spectrum.core.domain.analytics.AnalyticsOptics._
import fi.spectrum.core.domain.analytics.OrderEvaluation.AmmRedeemEvaluation
import fi.spectrum.core.domain.analytics.{OrderEvaluation, Processed, Version}
import fi.spectrum.core.domain.order.Order.Redeem.AmmRedeem
import fi.spectrum.core.domain.order.Order.Redeem.AmmRedeem.{RedeemLegacyV1, RedeemV1, RedeemV3}
import fi.spectrum.core.domain.order.OrderStatus.{Evaluated, Refunded, Registered}
import fi.spectrum.core.domain.order.{Fee, Order, OrderId, PoolId, Redeemer}
import fi.spectrum.indexer.classes.syntax._
import fi.spectrum.indexer.classes.ToDB
import glass.Subset

final case class AmmRedeemDB(
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

object AmmRedeemDB {

  implicit val toDB: ToDB[Processed[AmmRedeem], AmmRedeemDB] = processed => {
    processed.order match {
      case redeem: RedeemV3       => processed.widen(redeem).toDB
      case redeem: RedeemV1       => processed.widen(redeem).toDB
      case redeem: RedeemLegacyV1 => processed.widen(redeem).toDB
    }
  }

  implicit val ___V1: ToDB[Processed[RedeemV1], AmmRedeemDB] =
    processed => {
      val redeem     = processed.order
      val redeemEval = processed.evaluation.flatMap(Subset[OrderEvaluation, AmmRedeemEvaluation].getOption)
      val txInfo     = TxInfo(processed.state.txId, processed.state.timestamp)
      AmmRedeemDB(
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
        if (processed.state.status.in(Evaluated)) txInfo.some else none,
        if (processed.state.status.in(Refunded)) txInfo.some else none
      )
    }

  implicit val ___V3: ToDB[Processed[RedeemV3], AmmRedeemDB] =
    processed => {
      val redeem     = processed.order
      val redeemEval = processed.evaluation.flatMap(Subset[OrderEvaluation, AmmRedeemEvaluation].getOption)
      val txInfo     = TxInfo(processed.state.txId, processed.state.timestamp)
      val (tree, pk) = redeem.redeemer match {
        case Redeemer.ErgoTreeRedeemer(value) => (value.some, none)
        case Redeemer.PublicKeyRedeemer(value) => (none, value.some)
      }
      AmmRedeemDB(
        redeem.id,
        redeem.poolId,
        processed.poolBoxId,
        redeem.maxMinerFee.some,
        redeem.params.lp,
        redeemEval.map(_.outputX),
        redeemEval.map(_.outputY),
        redeem.fee,
        pk,
        ProtocolVersion(1),
        redeem.version,
        tree,
        if (processed.state.status.in(Registered)) txInfo.some else none,
        if (processed.state.status.in(Evaluated)) txInfo.some else none,
        if (processed.state.status.in(Refunded)) txInfo.some else none
      )
    }

  implicit val ___LegacyV1: ToDB[Processed[RedeemLegacyV1], AmmRedeemDB] =
    processed => {
      val redeem     = processed.order
      val redeemEval = processed.evaluation.flatMap(Subset[OrderEvaluation, AmmRedeemEvaluation].getOption)
      val txInfo     = TxInfo(processed.state.txId, processed.state.timestamp)
      AmmRedeemDB(
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
        if (processed.state.status.in(Evaluated)) txInfo.some else none,
        if (processed.state.status.in(Refunded)) txInfo.some else none
      )
    }

}
