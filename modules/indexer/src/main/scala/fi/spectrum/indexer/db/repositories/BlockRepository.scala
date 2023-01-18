package fi.spectrum.indexer.db.repositories

import fi.spectrum.core.domain.BlockId
import fi.spectrum.indexer.db.classes.{DeleteRepository, NonUpdatableRepository}
import fi.spectrum.indexer.db.models.BlockDB

class BlockRepository
  extends Repository[BlockDB, BlockId]
  with DeleteRepository[BlockDB, BlockId]
  with NonUpdatableRepository[BlockDB] {

  val field: String = "id"

  val tableName: String = "blocks"

  val fields: List[String] =
    List(
      "id",
      "height",
      "timestamp"
    )
}
