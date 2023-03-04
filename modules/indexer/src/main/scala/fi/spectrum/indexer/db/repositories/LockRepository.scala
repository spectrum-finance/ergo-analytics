package fi.spectrum.indexer.db.repositories

import fi.spectrum.core.domain.order.OrderId
import fi.spectrum.indexer.db.classes.DeleteRepository
import fi.spectrum.indexer.db.models.LockDB

final class LockRepository extends LockRepo with DeleteRepository[LockDB, OrderId] {

  val tableName: String = "lq_locks"

  val fields: List[String] = List(
    "order_id",
    "transaction_id",
    "timestamp",
    "deadline",
    "token_id",
    "amount",
    "redeemer",
    "contract_version",
    "evaluation_transaction_id",
    "evaluation_lock_type"
  )

  val field: String = "order_id"
}
