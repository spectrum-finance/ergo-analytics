package fi.spectrum.api.db.sql

import doobie.implicits._
import doobie.util.query.Query0
import doobie.{Fragment, LogHandler}
import fi.spectrum.api.db.models.{PoolFeesSnapshotDB, PoolInfoDB, PoolSnapshotDB, PoolTraceDB, PoolVolumeSnapshotDB}
import fi.spectrum.api.db.models.amm._
import fi.spectrum.api.v1.endpoints.models.TimeWindow
import fi.spectrum.core.domain.order.PoolId

final class AnalyticsSql(implicit lg: LogHandler) {

  def getFirstPoolSwapTime(id: PoolId): Query0[PoolInfoDB] =
    sql"""
         |SELECT min(executed_transaction_timestamp)
         |FROM swaps
         |WHERE pool_id = $id AND executed_transaction_timestamp IS NOT NULL;
       """.stripMargin.query

  def getPoolSnapshots: Query0[PoolSnapshotDB] =
    sql"""
         |SELECT p1.pool_id, p1.x_id, p1.x_amount, p1.y_id, p1.y_amount, p1.fee_num
         |FROM
         |	pools p1
         |	LEFT JOIN (
         |		SELECT max(height) AS height, pool_id FROM pools GROUP BY pool_id
		 |  ) AS p2 ON p2.height = p1.height AND p2.pool_id = p1.pool_id
         |WHERE p2.height = p1.height AND p2.pool_id = p1.pool_id;
         """.stripMargin.query[PoolSnapshotDB]

  def mkTimestamp(window: TimeWindow, key: String): Fragment =
    Fragment.const(s"$key is not null") ++
    Fragment.const(window.from.map(ts => s"and $key >= $ts ").getOrElse("")) ++
    Fragment.const(window.to.map(ts => s"and $key <= $ts ").getOrElse(""))

  def getPoolVolumes(tw: TimeWindow): Query0[PoolVolumeSnapshotDB] = {
    val fragment = mkTimestamp(tw, "s.executed_transaction_timestamp")
    sql"""
         |SELECT DISTINCT ON (p.pool_id)
         |	p.pool_id,
         |	p.x_id,
         |	sx.tx,
         |	p.y_id,
         |	sx.ty
         |FROM
         |	pools p
         |	LEFT JOIN (
         |		SELECT
         |			s.pool_id,
         |			cast(sum(CASE WHEN (s.base_id = p.y_id) THEN s.quote_amount ELSE 0 END) AS BIGINT) AS tx,
         |			cast(sum(CASE WHEN (s.base_id = p.x_id) THEN s.quote_amount ELSE 0 END) AS BIGINT) AS ty
         |		FROM
         |			swaps s
         |			LEFT JOIN pools p ON p.pool_state_id = s.pool_state_id and $fragment
         |		GROUP BY
         |			s.pool_id) AS sx ON sx.pool_id = p.pool_id
         |WHERE
         |	sx.pool_id IS NOT NULL
         """.stripMargin.query[PoolVolumeSnapshotDB]
  }

  def getPoolVolumes(id: PoolId, tw: TimeWindow): Query0[PoolVolumeSnapshotDB] = {
    val fragment = mkTimestamp(tw, "s.executed_transaction_timestamp")
    sql"""
         |SELECT DISTINCT ON (p.pool_id)
         |	p.pool_id,
         |	p.x_id,
         |	sx.tx,
         |	p.y_id,
         |	sx.ty
         |FROM
         |	pools p
         |	LEFT JOIN (
         |		SELECT
         |			s.pool_id,
         |			cast(sum(CASE WHEN (s.base_id = p.y_id) THEN s.quote_amount ELSE 0 END) AS BIGINT) AS tx,
         |			cast(sum(CASE WHEN (s.base_id = p.x_id) THEN s.quote_amount ELSE 0 END) AS BIGINT) AS ty
         |		FROM
         |			swaps s
         |			LEFT JOIN pools p ON p.pool_state_id = s.pool_state_id
         |		WHERE
         |			s.pool_id = $id and $fragment
         |		GROUP BY
         |			s.pool_id) AS sx ON sx.pool_id = p.pool_id
         |WHERE
         |	sx.pool_id IS NOT NULL
         """.stripMargin.query[PoolVolumeSnapshotDB]
  }

  def getPoolFees(pool: PoolSnapshot, tw: TimeWindow): Query0[PoolFeesSnapshotDB] = {
    val fragment = mkTimestamp(tw, "executed_transaction_timestamp")

    sql"""
         |SELECT
         |	cast(sum(CASE WHEN (base_id = ${pool.lockedY.id}) THEN quote_amount::decimal * (1000 - ${pool.fee}) / 1000 ELSE 0 END) AS bigint) AS tx,
         |	cast(sum(CASE WHEN (base_id = ${pool.lockedX.id}) THEN quote_amount::decimal * (1000 - ${pool.fee}) / 1000 ELSE 0 END) AS bigint) AS ty
         |FROM swaps
         |WHERE pool_id = ${pool.id} and $fragment
       """.stripMargin.query[PoolFeesSnapshotDB]
  }

  def getPrevPoolTrace(id: PoolId, depth: Int, currHeight: Int): Query0[PoolTraceDB] =
    sql"""
         |SELECT
         |	p.pool_id,
         |	p.x_id,
         |	p.x_amount,
         |	p.y_id,
         |	p.y_amount,
         |	p.height
         |FROM
         |	pools p
         |WHERE
         |	p.height < $currHeight - $depth
         |	AND p.pool_id = $id
         |ORDER BY
         |	p.height DESC
         |LIMIT 1
         """.stripMargin.query[PoolTraceDB]

  def getPoolTrace(id: PoolId, depth: Int, currHeight: Int): Query0[PoolTraceDB] =
    sql"""
         |SELECT
         |	p.pool_id,
         |	p.x_id,
         |	p.x_amount,
         |	p.y_id,
         |	p.y_amount,
         |	p.height
         |FROM
         |	pools p
         |WHERE
         |	p.pool_id = $id
         |	AND p.height >= $currHeight - $depth
         """.stripMargin.query[PoolTraceDB]

  def getAvgPoolSnapshot(id: PoolId, tw: TimeWindow, resolution: Int): Query0[AvgAssetAmounts] = {
    val fragment = mkTimestamp(tw, "p.timestamp")
    sql"""
         |SELECT
         |	avg(p.x_amount) AS avg_x_amount,
         |	avg(p.y_amount) AS avg_y_amount,
         |	avg(p.timestamp),
         |	((p.height / $resolution)::integer) AS k
         |FROM pools p
         |WHERE pool_id = $id AND $fragment
         |GROUP BY
         |	k
         |ORDER BY
         |	k
         """.stripMargin.query[AvgAssetAmounts]
  }
}
