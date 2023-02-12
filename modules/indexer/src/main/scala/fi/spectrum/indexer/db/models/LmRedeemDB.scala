package fi.spectrum.indexer.db.models

import fi.spectrum.core.domain.analytics.OrderEvaluation.LmRedeemEvaluation
import fi.spectrum.core.domain.analytics.{Processed, Version}
import fi.spectrum.core.domain.order.Order.Redeem.LmRedeem
import fi.spectrum.core.domain.order.Order.Redeem.LmRedeem.LmRedeemV1
import fi.spectrum.core.domain.order.OrderStatus._
import fi.spectrum.core.domain.order.{OrderId, PoolId}
import fi.spectrum.core.domain.order.Redeemer.ErgoTreeRedeemer
import fi.spectrum.core.domain.{AssetAmount, BoxId, ProtocolVersion, TokenId}
import fi.spectrum.indexer.classes.ToDB
import fi.spectrum.indexer.classes.syntax.ToDBOps
import cats.syntax.option._

final case class LmRedeemDB(
  orderId: OrderId,
  poolId: Option[PoolId],
  poolStateId: Option[BoxId],
  maxMinerFee: Long,
  bundleKeyId: TokenId,
  expectedLq: AssetAmount,
  redeemer: ErgoTreeRedeemer,
  out: Option[AssetAmount],
  boxId: Option[BoxId],
  protocolVersion: ProtocolVersion,
  version: Version,
  registeredTx: Option[TxInfo],
  executedTx: Option[TxInfo],
  refundedTx: Option[TxInfo]
)

object LmRedeemDB {

  implicit val toDBDeposit: ToDB[Processed[LmRedeem], LmRedeemDB] = processed => {
    processed.order match {
      case v1: LmRedeemV1 => processed.widen(v1).toDB
    }
  }

  implicit val ___V1: ToDB[Processed[LmRedeemV1], LmRedeemDB] =
    processed => {
      val redeem: LmRedeemV1 = processed.order
      val eval               = processed.evaluation.flatMap(_.widen[LmRedeemEvaluation])
      val txInfo             = TxInfo(processed.state.txId, processed.state.timestamp)
      LmRedeemDB(
        redeem.id,
        eval.map(_.poolId),
        processed.poolBoxId,
        redeem.maxMinerFee,
        redeem.bundleKeyId,
        redeem.expectedLq,
        redeem.redeemer,
        eval.map(_.out),
        eval.map(_.boxId),
        ProtocolVersion.init,
        redeem.version,
        if (processed.state.status.in(Registered)) txInfo.some else none,
        if (processed.state.status.in(Evaluated)) txInfo.some else none,
        if (processed.state.status.in(Refunded)) txInfo.some else none
      )
    }
}
