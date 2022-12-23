package fi.spectrum.indexer.db.repositories

import fi.spectrum.core.domain.order.OrderId
import fi.spectrum.indexer.db.classes.{DeleteRepository, NonUpdatableRepository}
import fi.spectrum.indexer.db.models.LockDB

final class LockRepository
  extends Repository[LockDB, OrderId]
  with DeleteRepository[LockDB, OrderId]
  with NonUpdatableRepository[LockDB] {

  val tableName: String = "locks"

  val fields: List[String] = List(
    "order_id",
    "deadline",
    "token_id",
    "amount",
    "redeemer",
    "contract_version"
  )

  val field: String = "order_id"
}
