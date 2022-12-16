package fi.spectrum.indexer.db.repositories

import fi.spectrum.core.domain.order.OrderId
import fi.spectrum.indexer.db.classes.{DeleteRepository, UpdateRepository}
import fi.spectrum.indexer.models.DepositDB

final class DepositRepository
  extends Repository[DepositDB, OrderId]
  with DeleteRepository[DepositDB, OrderId]
  with UpdateRepository[DepositDB] {

  val tableName: String = "deposits"

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
    "dex_fee",
    "fee_type",
    "redeemer",
    "protocol_version",
    "contract_version",
    "redeemer_ergo_tree",
    "registered_transaction_id",
    "registered_transaction_timestamp",
    "executed_transaction_id",
    "executed_transaction_timestamp",
    "refunded_transaction_id",
    "refunded_transaction_timestamp"
  )

  val field: String = "order_id"
}
