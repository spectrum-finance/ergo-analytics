package fi.spectrum.indexer.db.repositories

import doobie.util.log.LogHandler
import doobie.{ConnectionIO, Update}
import fi.spectrum.core.domain.analytics.OrderEvaluation.LmDepositCompoundEvaluation
import fi.spectrum.core.domain.order.OrderId
import fi.spectrum.indexer.db.classes.{DeleteRepository, RefundRepository}
import fi.spectrum.indexer.db.models.{LmDepositDB, LmDepositUpdate, UpdateEvaluatedTx}

class LmDepositRepository
  extends OrderRepository[LmDepositDB, LmDepositCompoundEvaluation, OrderId]
  with DeleteRepository[LmDepositDB, OrderId]
  with RefundRepository[LmDepositDB] {

  val tableName: String = "lm_deposits"

  val executed: String   = "executed_transaction_id"
  val executedTs: String = "executed_transaction_timestamp"

  val fields: List[String] = List(
    "order_id",
    "pool_id",
    "pool_state_id",
    "max_miner_fee",
    "expected_num_epochs",
    "input_id",
    "input_amount",
    "lp_id",
    "lp_amount",
    "compound_id",
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

  def updateExecuted(
    update: UpdateEvaluatedTx[LmDepositCompoundEvaluation]
  )(implicit lh: LogHandler): ConnectionIO[Int] = {
    val u = update.mapEval(_.map(e => LmDepositUpdate(e.tokens, e.bundle.id)))
    Update[UpdateEvaluatedTx[LmDepositUpdate]](
      s"""
           |update $tableName
           |set $executed=?, $executedTs=?, pool_state_id=?, lp_id=?, lp_amount=?,compound_id=?
           |where order_id=?""".stripMargin
    )
      .toUpdate0(u)
      .run
  }

  def deleteExecuted(delete: OrderId)(implicit lh: LogHandler): ConnectionIO[Int] =
    Update[OrderId](
      s"""
           |update $tableName
           |set $executed=null, $executedTs=null, pool_state_id=null,lp_id=null,lp_amount=null,
           |compound_id=null
           |where order_id=?""".stripMargin
    )
      .toUpdate0(delete)
      .run

}
