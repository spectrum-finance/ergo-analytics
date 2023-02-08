package fi.spectrum.indexer.db.models

import cats.syntax.option._
import fi.spectrum.core.domain.analytics.OrderEvaluation.LmDepositCompoundEvaluation
import fi.spectrum.core.domain.analytics.{Processed, Version}
import fi.spectrum.core.domain.order.Order.Deposit.LmDeposit
import fi.spectrum.core.domain.order.Order.Deposit.LmDeposit.LmDepositV1
import fi.spectrum.core.domain.order.OrderStatus._
import fi.spectrum.core.domain.order.{OrderId, PoolId}
import fi.spectrum.core.domain.{AssetAmount, BoxId, ProtocolVersion, SErgoTree}
import fi.spectrum.indexer.classes.ToDB
import fi.spectrum.indexer.classes.syntax.ToDBOps

final case class LmDepositDB(
  id: OrderId,
  poolId: PoolId,
  poolState: Option[BoxId],
  maxMinerFee: Long,
  expectedNumEpochs: Int,
  in: AssetAmount,
  lp: Option[AssetAmount],
  compoundId: Option[OrderId],
  protocolVersion: ProtocolVersion,
  contractVersion: Version,
  redeemerErgoTree: SErgoTree,
  registeredTx: Option[TxInfo],
  executedTx: Option[TxInfo],
  refundedTx: Option[TxInfo]
)

object LmDepositDB {

  implicit val toDBDeposit: ToDB[Processed[LmDeposit], LmDepositDB] = processed => {
    processed.order match {
      case deposit: LmDepositV1 => processed.widen(deposit).toDB
    }
  }

  implicit val ___V1: ToDB[Processed[LmDepositV1], LmDepositDB] =
    processed => {
      val deposit: LmDepositV1 = processed.order
      val depositEval          = processed.evaluation.flatMap(_.widen[LmDepositCompoundEvaluation])
      val txInfo               = TxInfo(processed.state.txId, processed.state.timestamp)
      LmDepositDB(
        deposit.id,
        deposit.poolId,
        processed.poolBoxId,
        deposit.maxMinerFee,
        deposit.params.expectedNumEpochs,
        deposit.params.tokens,
        depositEval.map(_.tokens),
        depositEval.map(_.bundle.id),
        ProtocolVersion.init,
        deposit.version,
        deposit.redeemer.value,
        if (processed.state.status.in(Registered)) txInfo.some else none,
        if (processed.state.status.in(Evaluated)) txInfo.some else none,
        if (processed.state.status.in(Refunded)) txInfo.some else none
      )
    }
}
