package fi.spectrum.indexer.db.schema

import fi.spectrum.indexer.models.LockDB

class LockSchema extends Schema[LockDB] {
  override val tableName: String = "locks"

  override val fields: List[String] = List(
    "id",
    "deadline",
    "token_id",
    "amount",
    "redeemer",
    "contract_version"
  )
}
