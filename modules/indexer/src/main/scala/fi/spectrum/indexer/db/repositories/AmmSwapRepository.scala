package fi.spectrum.indexer.db.repositories

import fi.spectrum.core.domain.order.OrderId
import fi.spectrum.indexer.db.classes.{DeleteRepository, UpdateRepository}
import fi.spectrum.indexer.models.SwapDB

final class AmmSwapRepository
  extends Repository[SwapDB, OrderId]
  with DeleteRepository[SwapDB, OrderId]
  with UpdateRepository[SwapDB] {

  val tableName: String = "swaps"

  val fields: List[String] = List(
    "order_id",
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
