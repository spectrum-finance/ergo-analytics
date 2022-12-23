package fi.spectrum.indexer.db.repositories

import fi.spectrum.indexer.db.classes.InsertRepository
import fi.spectrum.indexer.db.models.AssetDB

final class AssetInsert extends InsertRepository[AssetDB] {
  val tableName: String = "assets"

  val fields: List[String] =
    List(
      "id",
      "ticker",
      "decimals"
    )
}
