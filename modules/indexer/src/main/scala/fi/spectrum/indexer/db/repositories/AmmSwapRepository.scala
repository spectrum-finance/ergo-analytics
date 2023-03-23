package fi.spectrum.indexer.db.repositories

import doobie.util.log.LogHandler
import doobie.{ConnectionIO, Update}
import fi.spectrum.core.domain.analytics.OrderEvaluation.SwapEvaluation
import fi.spectrum.core.domain.order.{Fee, OrderId}
import fi.spectrum.indexer.db.models.{SwapDB, UpdateEvaluatedTx}

final class AmmSwapRepository
  extends OrderRepository[SwapDB, SwapEvaluation, OrderId] {

  val tableName: String = "swaps"

  val executed: String   = "executed_transaction_id"
  val executedTs: String = "executed_transaction_timestamp"

  val fields: List[String] = List(
    orderId,
    "pool_id",
    "pool_state_id",
    "max_miner_fee",
    "base_id",
    "base_amount",
    "min_quote_id",
    "min_quote_amount",
    "quote_amount",
    "dex_fee_per_token_num",
    "dex_fee_per_token_denom",
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

  def updateExecuted(update: UpdateEvaluatedTx[SwapEvaluation])(implicit lh: LogHandler): ConnectionIO[Int] = {
    val db = update.mapEval(_.map(s => s.output.amount -> s.fee))
    Update[UpdateEvaluatedTx[(Long, Fee)]](
      s"""
         |update $tableName
         |set $executed=?, $executedTs=?, pool_state_id=?, quote_amount=?, dex_fee=?, fee_type=?
         |where order_id=?""".stripMargin
    )
      .toUpdate0(db)
      .run
  }

  def deleteExecuted(delete: OrderId)(implicit lh: LogHandler): ConnectionIO[Int] =
    Update[OrderId](
      s"""
         |update $tableName
         |set $executed=null, $executedTs=null, pool_state_id=null, quote_amount=null, dex_fee=null, fee_type=null
         |where order_id=?""".stripMargin
    )
      .toUpdate0(delete)
      .run
}
