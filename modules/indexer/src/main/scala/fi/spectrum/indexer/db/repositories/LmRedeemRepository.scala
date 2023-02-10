package fi.spectrum.indexer.db.repositories

import doobie.util.log.LogHandler
import doobie.{ConnectionIO, Update}
import fi.spectrum.core.domain.analytics.OrderEvaluation.LmRedeemEvaluation
import fi.spectrum.core.domain.order.OrderId
import fi.spectrum.indexer.db.classes.{DeleteRepository, UpdateRepository}
import fi.spectrum.indexer.db.models.{LmRedeemDB, UpdateEvaluatedTx}

class LmRedeemRepository
  extends Repository[LmRedeemDB, OrderId, LmRedeemEvaluation]
  with DeleteRepository[LmRedeemDB, OrderId]
  with UpdateRepository[LmRedeemDB] {

  val tableName: String = "lm_redeems"

  val executed: String   = "executed_transaction_id"
  val executedTs: String = "executed_transaction_timestamp"

  val fields: List[String] = List(
    "order_id",
    "pool_id",
    "pool_state_id",
    "bundle_key_id",
    "expected_lq_id",
    "expected_lq_amount",
    "max_miner_fee",
    "redeemer_ergo_tree",
    "out_id",
    "out_amount",
    "box_id",
    "contract_version",
    "protocol_version",
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
    update: UpdateEvaluatedTx[LmRedeemEvaluation]
  )(implicit lh: LogHandler): ConnectionIO[Int] =
    Update[UpdateEvaluatedTx[LmRedeemEvaluation]](
      s"""
         |update $tableName
         |set $executed=?, $executedTs=?, pool_state_id=?, out_id=?, out_amount=?, box_id=?, pool_id=?
         |where order_id=?""".stripMargin
    )
      .toUpdate0(update)
      .run

  def deleteExecuted(delete: OrderId)(implicit lh: LogHandler): ConnectionIO[Int] =
    Update[OrderId](
      s"""
         |update $tableName
         |set $executed=null, $executedTs=null, pool_id=null, pool_state_id=null, out_id=null,
         |out_amount=null, box_id=null
         |where order_id=?""".stripMargin
    )
      .toUpdate0(delete)
      .run

}
