package fi.spectrum.indexer.db.schema

import fi.spectrum.indexer.models.OffChainFeeDB

class OffChainFeeSchema extends Schema[OffChainFeeDB] {

  val fields: List[String] = List(
    "pool_id",
    "order_id",
    "output_id",
    "pub_key",
    "fee",
    "fee_type"
  )

  val tableName: String = "off_chain_fee"
}
