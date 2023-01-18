package fi.spectrum.indexer.db.repositories

import fi.spectrum.core.domain.BoxId
import fi.spectrum.indexer.db.classes.{DeleteRepository, NonUpdatableRepository}
import fi.spectrum.indexer.db.models.PoolDB

class PoolRepository
  extends Repository[PoolDB, BoxId]
  with DeleteRepository[PoolDB, BoxId]
  with NonUpdatableRepository[PoolDB] {

  val field: String = "pool_state_id"

  val tableName: String = "pools"

  val fields: List[String] =
    List(
      "pool_state_id",
      "pool_id",
      "lp_id",
      "lp_amount",
      "x_id",
      "x_amount",
      "y_id",
      "y_amount",
      "fee_num",
      "timestamp",
      "height",
      "protocol_version"
    )

}
