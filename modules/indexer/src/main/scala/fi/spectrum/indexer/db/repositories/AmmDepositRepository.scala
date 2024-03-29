package fi.spectrum.indexer.db.repositories

import doobie.util.log.LogHandler
import doobie.{ConnectionIO, Update}
import fi.spectrum.core.domain.analytics.OrderEvaluation.AmmDepositEvaluation
import fi.spectrum.core.domain.order.OrderId
import fi.spectrum.indexer.db.models.{AmmDepositDB, UpdateEvaluatedTx}

final class AmmDepositRepository extends OrderRepository[AmmDepositDB, AmmDepositEvaluation, OrderId] {

  val tableName: String = "deposits"

  val executed: String   = "executed_transaction_id"
  val executedTs: String = "executed_transaction_timestamp"

  val fields: List[String] = List(
    "order_id",
    "pool_id",
    "pool_state_id",
    "max_miner_fee",
    "input_id_x",
    "input_amount_x",
    "input_id_y",
    "input_amount_y",
    "output_id_lp",
    "output_amount_lp",
    "actual_input_amount_x",
    "actual_input_amount_y",
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

  def updateExecuted(update: UpdateEvaluatedTx[AmmDepositEvaluation])(implicit lh: LogHandler): ConnectionIO[Int] =
    Update[UpdateEvaluatedTx[AmmDepositEvaluation]](
      s"""
         |update $tableName
         |set $executed=?, $executedTs=?, pool_state_id=?, output_id_lp=?, output_amount_lp=?,
         |actual_input_amount_x=?, actual_input_amount_y=?
         |where order_id=?""".stripMargin
    )
      .toUpdate0(update)
      .run

  def deleteExecuted(delete: OrderId)(implicit lh: LogHandler): ConnectionIO[Int] =
    Update[OrderId](
      s"""
         |update $tableName
         |set $executed=null, $executedTs=null, pool_state_id=null,output_id_lp=null,output_amount_lp=null,
         |actual_input_amount_x=null, actual_input_amount_y=null
         |where order_id=?""".stripMargin
    )
      .toUpdate0(delete)
      .run
}
