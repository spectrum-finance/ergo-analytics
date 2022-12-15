package fi.spectrum.indexer.db.v2

import fi.spectrum.core.domain.order.OrderId
import fi.spectrum.indexer.models.RedeemDB

final class RedeemRepository extends Repository[RedeemDB, OrderId] {

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
    "executed_transaction_id",
    "executed_transaction_timestamp",
    "refunded_transaction_id",
    "refunded_transaction_timestamp"
  )

  val field: String = "order_id"
}
