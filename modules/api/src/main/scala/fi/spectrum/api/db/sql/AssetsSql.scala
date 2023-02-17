package fi.spectrum.api.db.sql

import doobie.implicits.toSqlInterpolator
import doobie.util.log.LogHandler
import doobie.util.query.Query0
import fi.spectrum.api.db.models.amm.AssetInfo
import fi.spectrum.core.domain.TokenId

final class AssetsSql(implicit lg: LogHandler) {

  def getAssetById(id: TokenId): Query0[AssetInfo] =
    sql"""select id, ticker, decimals from assets where id = $id""".stripMargin.query[AssetInfo]

  def getAll: Query0[AssetInfo] =
    sql"""select id, ticker, decimals from assets""".query
}
