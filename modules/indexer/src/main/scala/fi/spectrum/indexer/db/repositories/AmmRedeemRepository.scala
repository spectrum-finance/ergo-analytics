package fi.spectrum.indexer.db.repositories

import fi.spectrum.core.domain.analytics.OrderEvaluation.AmmRedeemEvaluation
import fi.spectrum.core.domain.order.OrderId
import fi.spectrum.indexer.db.classes.{DeleteRepository, RefundRepository}
import fi.spectrum.indexer.db.models.{AmmRedeemDB, UpdateEvaluatedTx}
import doobie.{ConnectionIO, Update}
import doobie.util.log.LogHandler

final class AmmRedeemRepository
  extends OrderRepository[AmmRedeemDB, AmmRedeemEvaluation, OrderId]
  with DeleteRepository[AmmRedeemDB, OrderId]
  with RefundRepository[AmmRedeemDB] {

  val executed: String   = "executed_transaction_id"
  val executedTs: String = "executed_transaction_timestamp"

  val tableName: String = "redeems"

  val fields: List[String] = List(
    "order_id",
    "pool_id",
    "pool_state_id",
    "max_miner_fee",
    "lp_id",
    "lp_amount",
    "output_id_x",
    "output_amount_x",
    "output_id_y",
    "output_amount_y",
    "dex_fee",
    "fee_type",
    "redeemer",
    "protocol_version",
    "contract_version",
    "redeemer_ergo_tree",
    "registered_transaction_id",
    "registered_transaction_timestamp",
    executed,
    executedTs,
    refunded,
    refundedTs
  )

  val field: String = "order_id"

  def updateExecuted(update: UpdateEvaluatedTx[AmmRedeemEvaluation])(implicit lh: LogHandler): ConnectionIO[Int] =
    Update[UpdateEvaluatedTx[AmmRedeemEvaluation]](
      s"""
         |update $tableName
         |set $executed=?, $executedTs=?, pool_state_id=?, 
         |output_id_x=?, output_amount_x=?, output_id_y=?, output_amount_y=?
         |where order_id=?""".stripMargin
    )
      .toUpdate0(update)
      .run

  def deleteExecuted(delete: OrderId)(implicit lh: LogHandler): ConnectionIO[Int] =
    Update[OrderId](
      s"""
         |update $tableName
         |set $executed=null, $executedTs=null, pool_state_id=null, 
         |output_id_x=null, output_amount_x=null, output_id_y=null, output_amount_y=null
         |where order_id=?""".stripMargin
    )
      .toUpdate0(delete)
      .run
}
