package fi.spectrum.indexer.models

import cats.syntax.option._
import fi.spectrum.core.domain._
import fi.spectrum.core.domain.analytics.AnalyticsOptics._
import fi.spectrum.core.domain.analytics.OrderEvaluation.DepositEvaluation
import fi.spectrum.core.domain.analytics.{OrderEvaluation, ProcessedOrder, Version}
import fi.spectrum.core.domain.order.Order.Deposit._
import fi.spectrum.core.domain.order.OrderStatus.{Executed, Refunded, Registered}
import fi.spectrum.core.domain.order.{Fee, Order, OrderId, PoolId}
import fi.spectrum.indexer.classes.ToSchema
import glass.Subset

final case class DepositDB(
  orderId: OrderId,
  poolId: PoolId,
  poolStateId: Option[BoxId],
  maxMinerFee: Option[Long],
  x: AssetAmount,
  y: AssetAmount,
  lp: Option[AssetAmount],
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

  implicit val toSchemaV2: ToSchema[Order.AnyDeposit, Option[DepositDB]] = processed => ???

  implicit val toSchema: ToSchema[ProcessedOrder, Option[DepositDB]] = processed =>
    ___V1.transform(processed) orElse
    ___V3.transform(processed) orElse
    ___LegacyV1.transform(processed) orElse
    ___LegacyV2.transform(processed)

  val ___V1: ToSchema[ProcessedOrder, Option[DepositDB]] =
    processed => {
      val depositEval = processed.evaluation.flatMap(Subset[OrderEvaluation, DepositEvaluation].getOption)
      Subset[Order.Any, DepositV1].getOption(processed.order).map { deposit =>
        val txInfo = TxInfo(processed.state.txId, processed.state.timestamp)
        DepositDB(
          deposit.id,
          deposit.poolId,
          processed.pool.map(_.box.boxId),
          deposit.maxMinerFee.some,
          deposit.params.inX,
          deposit.params.inY,
          depositEval.map(_.outputLP),
          deposit.fee,
          deposit.redeemer.value.some,
          ProtocolVersion.init,
          deposit.version,
          none,
          if (processed.state.status.in(Registered)) txInfo.some else none,
          if (processed.state.status.in(Executed)) txInfo.some else none,
          if (processed.state.status.in(Refunded)) txInfo.some else none
        )
      }
    }

  val ___V3: ToSchema[ProcessedOrder, Option[DepositDB]] =
    processed => {
      val depositEval = processed.evaluation.flatMap(Subset[OrderEvaluation, DepositEvaluation].getOption)
      Subset[Order.Any, DepositV3].getOption(processed.order) map { deposit =>
        val txInfo = TxInfo(processed.state.txId, processed.state.timestamp)
        DepositDB(
          deposit.id,
          deposit.poolId,
          processed.pool.map(_.box.boxId),
          deposit.maxMinerFee.some,
          deposit.params.inX,
          deposit.params.inY,
          depositEval.map(_.outputLP),
          deposit.fee,
          none,
          ProtocolVersion.init,
          deposit.version,
          deposit.redeemer.value.some,
          if (processed.state.status.in(Registered)) txInfo.some else none,
          if (processed.state.status.in(Executed)) txInfo.some else none,
          if (processed.state.status.in(Refunded)) txInfo.some else none
        )
      }
    }

  val ___LegacyV1: ToSchema[ProcessedOrder, Option[DepositDB]] =
    processed => {
      val depositEval = processed.evaluation.flatMap(Subset[OrderEvaluation, DepositEvaluation].getOption)
      Subset[Order.Any, DepositLegacyV1].getOption(processed.order) map { deposit =>
        val txInfo = TxInfo(processed.state.txId, processed.state.timestamp)
        DepositDB(
          deposit.id,
          deposit.poolId,
          processed.pool.map(_.box.boxId),
          none,
          deposit.params.inX,
          deposit.params.inY,
          depositEval.map(_.outputLP),
          deposit.fee,
          deposit.redeemer.value.some,
          ProtocolVersion.init,
          deposit.version,
          none,
          if (processed.state.status.in(Registered)) txInfo.some else none,
          if (processed.state.status.in(Executed)) txInfo.some else none,
          if (processed.state.status.in(Refunded)) txInfo.some else none
        )
      }
    }

  val ___LegacyV2: ToSchema[ProcessedOrder, Option[DepositDB]] =
    processed => {
      val depositEval = processed.evaluation.flatMap(Subset[OrderEvaluation, DepositEvaluation].getOption)
      Subset[Order.Any, DepositLegacyV2].getOption(processed.order) map { deposit =>
        val txInfo = TxInfo(processed.state.txId, processed.state.timestamp)
        DepositDB(
          deposit.id,
          deposit.poolId,
          processed.pool.map(_.box.boxId),
          none,
          deposit.params.inX,
          deposit.params.inY,
          depositEval.map(_.outputLP),
          deposit.fee,
          deposit.redeemer.value.some,
          ProtocolVersion.init,
          deposit.version,
          none,
          if (processed.state.status.in(Registered)) txInfo.some else none,
          if (processed.state.status.in(Executed)) txInfo.some else none,
          if (processed.state.status.in(Refunded)) txInfo.some else none
        )
      }
    }
}
