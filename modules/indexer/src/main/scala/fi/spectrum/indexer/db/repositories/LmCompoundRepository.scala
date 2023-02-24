package fi.spectrum.indexer.db.repositories

import doobie.util.log.LogHandler
import doobie.{ConnectionIO, Update}
import fi.spectrum.core.domain.analytics.OrderEvaluation.LmDepositCompoundEvaluation
import fi.spectrum.core.domain.order.OrderId
import fi.spectrum.indexer.db.classes.{DeleteRepository, UpdateRepository}
import fi.spectrum.indexer.db.models.{LmCompoundDB, LmCompoundUpdate, UpdateEvaluatedTx}

class LmCompoundRepository
  extends Repository[LmCompoundDB, OrderId, LmDepositCompoundEvaluation]
  with DeleteRepository[LmCompoundDB, OrderId]
  with UpdateRepository[LmCompoundDB] {
  val tableName: String = "lm_compound"

  val executed: String   = "executed_transaction_id"
  val executedTs: String = "executed_transaction_timestamp"

  val fields: List[String] = List(
    "order_id",
    "pool_id",
    "pool_state_id",
    "v_lq_id",
    "v_lq_amount",
    "tmp_id",
    "tmp_amount",
    "interest_id",
    "interest_amount",
    "bundle_key_id",
    "redeemer",
    "version",
    "protocol_version",
    "registered_transaction_id",
    "registered_transaction_timestamp",
    executed,
    executedTs
  )

  val field: String = "order_id"

  def updateExecuted(
    update: UpdateEvaluatedTx[LmDepositCompoundEvaluation]
  )(implicit lh: LogHandler): ConnectionIO[Int] = {
    val u = LmCompoundUpdate(update.eval.map(_.tokens), update.info, update.poolStateId, update.orderId)
    Update[LmCompoundUpdate](
      s"""update $tableName set interest_id=?, interest_amount=?, $executed=?, $executedTs=?, pool_state_id=? where order_id=?""".stripMargin
    )
      .toUpdate0(u)
      .run
  }

  def deleteExecuted(delete: OrderId)(implicit lh: LogHandler): ConnectionIO[Int] =
    Update[OrderId](
      s"""
         |update $tableName
         |set interest_id=null, interest_amount=null, $executed=null, $executedTs=null, pool_state_id=null
         |where order_id=?""".stripMargin
    )
      .toUpdate0(delete)
      .run
}
