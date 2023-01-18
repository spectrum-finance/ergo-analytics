package fi.spectrum.indexer.db.models

import fi.spectrum.core.domain.TokenId
import fi.spectrum.indexer.classes.ToDB
import fi.spectrum.indexer.models.TokenInfo

case class AssetDB(id: TokenId, ticker: Option[String], decimals: Option[Int])

object AssetDB {
  implicit def toDb: ToDB[TokenInfo, AssetDB] = info => AssetDB(info.id, info.name, info.decimals)
}
