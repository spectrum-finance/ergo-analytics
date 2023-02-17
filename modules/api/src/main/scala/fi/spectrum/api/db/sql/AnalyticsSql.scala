package fi.spectrum.api.db.sql

import doobie.implicits._
import doobie.util.query.Query0
import doobie.{Fragment, LogHandler}
import fi.spectrum.api.db.models.{PoolFeesSnapshotDB, PoolSnapshotDB, PoolVolumeSnapshotDB}
import fi.spectrum.api.db.models.amm._
import fi.spectrum.api.v1.endpoints.models.TimeWindow
import fi.spectrum.core.domain.order.PoolId

final class AnalyticsSql(implicit lg: LogHandler) {

  def getFirstPoolSwapTime(id: PoolId): Query0[PoolInfo] =
    sql"""
         |SELECT
         |	executed_transaction_timestamp
         |FROM
         |	swaps
         |WHERE
         |	pool_id = $id
         |	AND executed_transaction_timestamp IS NOT NULL
         |ORDER BY
         |	executed_transaction_timestamp ASC
         |LIMIT 1;
       """.stripMargin.query

  def getPoolSnapshots: Query0[PoolSnapshotDB] =
    sql"""
         |SELECT x.pool_id, x.x_id, x.x_amount, x.y_id, x.y_amount
         |FROM (
         |	SELECT p.pool_id, p.x_id, p.x_amount, p.y_id, p.y_amount, (p.x_amount::decimal) * p.y_amount AS lq
         |	FROM
         |		pools p
         |	LEFT JOIN (
         |		SELECT pool_id, max(height) AS height
         |		FROM
         |			pools
         |		GROUP BY
         |			pool_id) AS px ON p.pool_id = px.pool_id
         |		AND p.height = px.height
         |	WHERE
         |		px.height = p.height
         |	ORDER BY
         |		lq DESC) AS x;
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
         |	sx.ty,
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
         |select distinct on (p.pool_id)
         |  p.pool_id,
         |  p.x_id,
         |  sx.tx,
         |  p.y_id,
         |  sx.ty,
         |from pools p
         |left join (
         |  select
         |    s.pool_id,
         |    cast(sum(case when (s.base_id = p.y_id) then s.quote_amount else 0 end) as BIGINT) as tx,
         |    cast(sum(case when (s.base_id = p.x_id) then s.quote_amount else 0 end) as BIGINT) as ty
         |  from swaps s
         |  left join pools p on p.pool_state_id = s.pool_state_id
         |  where s.pool_id = $id and $fragment
         |  group by s.pool_id
         |) as sx on sx.pool_id = p.pool_id
         |where sx.pool_id is not null
         """.stripMargin.query[PoolVolumeSnapshotDB]
  }

  def getPoolFees(poolId: PoolId, tw: TimeWindow): Query0[PoolFeesSnapshotDB] = {
    val fragment = mkTimestamp(tw, "s.executed_transaction_timestamp")
    sql"""
         |select distinct on (p.pool_id)
         |  p.pool_id,
         |  p.x_id,
         |  sx.tx,
         |  p.y_id,
         |  sx.ty,
         |from pools p
         |left join (
         |  select
         |    s.pool_id,
         |    cast(sum(case when (s.base_id = p.y_id) then s.quote_amount::decimal * (1000 - p.fee_num) / 1000 else 0 end) as bigint) as tx,
         |    cast(sum(case when (s.base_id = p.x_id) then s.quote_amount::decimal * (1000 - p.fee_num) / 1000 else 0 end) as bigint) as ty
         |  from swaps s
         |  left join pools p on p.pool_state_id = s.pool_state_id
         |  where p.pool_id = $poolId and $fragment
         |  group by s.pool_id
         |) as sx on sx.pool_id = p.pool_id
         |where sx.pool_id is not null
         """.stripMargin.query[PoolFeesSnapshotDB]
  }

  def getPrevPoolTrace(id: PoolId, depth: Int, currHeight: Int): Query0[PoolTrace] =
    sql"""
         |select p.pool_id, p.x_id, p.x_amount, ax.ticker, ax.decimals, p.y_id, p.y_amount, ay.ticker, ay.decimals, p.height
         |from pools p
         |left join assets ax on ax.id = p.x_id
         |left join assets ay on ay.id = p.y_id
         |where p.height < $currHeight - $depth
         |and p.pool_id = $id
         |order by p.height desc
         |limit 1
         """.stripMargin.query[PoolTrace]

  def getPoolTrace(id: PoolId, depth: Int, currHeight: Int): Query0[PoolTrace] =
    sql"""
         |select p.pool_id, p.x_id, p.x_amount, ax.ticker, ax.decimals, p.y_id, p.y_amount, ay.ticker, ay.decimals, p.height
         |from pools p
         |left join assets ax on ax.id = p.x_id
         |left join assets ay on ay.id = p.y_id
         |where p.pool_id = $id
         |and p.height >= $currHeight - $depth
         """.stripMargin.query[PoolTrace]

  def getAvgPoolSnapshot(id: PoolId, tw: TimeWindow, resolution: Int): Query0[AvgAssetAmounts] = {
    val fragment = mkTimestamp(tw, "b.timestamp")
    sql"""
         |select avg(p.x_amount) as avg_x_amount, avg(p.y_amount) as avg_y_amount, avg(b.timestamp), ((p.height / $resolution)::integer) as k
         |from pools p
         |left join blocks b on b.height = p.height
         |where pool_id = $id and $fragment
         |group by k
         |order by k
         """.stripMargin.query[AvgAssetAmounts]
  }

  def getSwapTransactions(tw: TimeWindow): Query0[SwapInfo] = {
    val fragment  = mkTimestamp(tw, "sx.executed_transaction_timestamp")
    val fragment2 = mkTimestamp(tw, "s.executed_transaction_timestamp")
    sql"""
         |select s.min_quote_id, s.min_quote_amount, a.ticker, a.decimals,
         |(select count(*) from swaps sx where quote_amount is not null and $fragment) as numTxs from swaps s
         |left join assets a on a.id = s.min_quote_id
         |where s.quote_amount is not null and $fragment2
         """.stripMargin.query[SwapInfo]
  }

  def getDepositTransactions(tw: TimeWindow): Query0[DepositInfo] = {
    val fragment  = mkTimestamp(tw, "sx.executed_transaction_timestamp")
    val fragment2 = mkTimestamp(tw, "s.executed_transaction_timestamp")
    sql"""
         |select s.input_id_x, s.input_amount_x, ax.ticker, ax.decimals, s.input_id_y, s.input_amount_y, ay.ticker, ay.decimals,
         |(select count(*) from deposits sx where output_amount_lp is not null and $fragment) as numTxs from deposits s
         |left join assets ax on ax.id = s.input_id_x  
         |left join assets ay on ay.id = s.input_id_y
         |where output_amount_lp is not null and $fragment2
         """.stripMargin.query[DepositInfo]
  }

}
