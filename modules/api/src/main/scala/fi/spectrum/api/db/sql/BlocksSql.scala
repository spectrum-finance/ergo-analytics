package fi.spectrum.api.db.sql

import doobie.implicits.toSqlInterpolator
import doobie.util.log.LogHandler
import doobie.util.query.Query0

final class BlocksSql(implicit lg: LogHandler) {

  def getBestHeight: Query0[Int] =
    sql"""select max(height) from blocks""".stripMargin.query[Int]
}