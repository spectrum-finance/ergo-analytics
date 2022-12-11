package fi.spectrum.indexer.db.schema

import fi.spectrum.indexer.models.SwapDB

class SwapSchema extends Schema[SwapDB] {
  val tableName: String = "swaps"

  val fields: List[String] = List(
    "order_id",
    "pool_id",
    "pool_box_id",
    "max_miner_fee",
    "input_id",
    "input_value",
    "min_output_id",
    "min_output_amount",
    "output_amount",
    "dex_fee_per_token_num",
    "dex_fee_per_token_denom",
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
}
