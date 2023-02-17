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

  def getPoolFees(poolId: PoolId, tw: TimeWindow): Query0[PoolFeesSnapshotDB] = {
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
         |			cast(sum(CASE WHEN (s.base_id = p.y_id) THEN s.quote_amount::decimal * (1000 - p.fee_num) / 1000 ELSE 0 END) AS bigint) AS tx,
         |			cast(sum(CASE WHEN (s.base_id = p.x_id) THEN s.quote_amount::decimal * (1000 - p.fee_num) / 1000 ELSE 0 END) AS bigint) AS ty
         |		FROM
         |			swaps s
         |			LEFT JOIN pools p ON p.pool_state_id = s.pool_state_id
         |		WHERE
         |			p.pool_id = $poolId
         |			AND $fragment
         |		GROUP BY
         |			s.pool_id) AS sx ON sx.pool_id = p.pool_id
         |WHERE
         |	sx.pool_id IS NOT NULL
         """.stripMargin.query[PoolFeesSnapshotDB]
  }

  def getPrevPoolTrace(id: PoolId, depth: Int, currHeight: Int): Query0[PoolTrace] =
    sql"""
         |SELECT
         |	p.pool_id,
         |	p.x_id,
         |	p.x_amount,
         |	ax.ticker,
         |	ax.decimals,
         |	p.y_id,
         |	p.y_amount,
         |	ay.ticker,
         |	ay.decimals,
         |	p.height
         |FROM
         |	pools p
         |	LEFT JOIN assets ax ON ax.id = p.x_id
         |	LEFT JOIN assets ay ON ay.id = p.y_id
         |WHERE
         |	p.height < $currHeight - $depth
         |	AND p.pool_id = $id
         |ORDER BY
         |	p.height DESC
         |LIMIT 1
         """.stripMargin.query[PoolTrace]

  def getPoolTrace(id: PoolId, depth: Int, currHeight: Int): Query0[PoolTrace] =
    sql"""
         |SELECT
         |	p.pool_id,
         |	p.x_id,
         |	p.x_amount,
         |	ax.ticker,
         |	ax.decimals,
         |	p.y_id,
         |	p.y_amount,
         |	ay.ticker,
         |	ay.decimals,
         |	p.height
         |FROM
         |	pools p
         |	LEFT JOIN assets ax ON ax.id = p.x_id
         |	LEFT JOIN assets ay ON ay.id = p.y_id
         |WHERE
         |	p.pool_id = $id
         |	AND p.height >= $currHeight - $depth
         """.stripMargin.query[PoolTrace]

  def getAvgPoolSnapshot(id: PoolId, tw: TimeWindow, resolution: Int): Query0[AvgAssetAmounts] = {
    val fragment = mkTimestamp(tw, "b.timestamp")
    sql"""
         |SELECT
         |	avg(p.x_amount) AS avg_x_amount,
         |	avg(p.y_amount) AS avg_y_amount,
         |	avg(b.timestamp),
         |	((p.height / $resolution)::integer) AS k
         |FROM
         |	pools p
         |	LEFT JOIN blocks b ON b.height = p.height
         |WHERE
         |	pool_id = $id
         |	AND $fragment
         |GROUP BY
         |	k
         |ORDER BY
         |	k
         """.stripMargin.query[AvgAssetAmounts]
  }

  def getSwapTransactions(tw: TimeWindow): Query0[SwapInfo] = {
    val fragment  = mkTimestamp(tw, "sx.executed_transaction_timestamp")
    val fragment2 = mkTimestamp(tw, "s.executed_transaction_timestamp")
    sql"""
         |SELECT
         |	s.min_quote_id,
         |	s.min_quote_amount,
         |	a.ticker,
         |	a.decimals,
         |	(
         |		SELECT
         |			count(*)
         |		FROM
         |			swaps sx
         |		WHERE
         |			quote_amount IS NOT NULL
         |			AND $fragment) AS numTxs
         |	FROM
         |		swaps s
         |	LEFT JOIN assets a ON a.id = s.min_quote_id
         |WHERE
         |	s.quote_amount IS NOT NULL
         |	AND $fragment2
         """.stripMargin.query[SwapInfo]
  }

  def getDepositTransactions(tw: TimeWindow): Query0[DepositInfo] = {
    val fragment  = mkTimestamp(tw, "sx.executed_transaction_timestamp")
    val fragment2 = mkTimestamp(tw, "s.executed_transaction_timestamp")
    sql"""
         |SELECT
         |	s.input_id_x,
         |	s.input_amount_x,
         |	ax.ticker,
         |	ax.decimals,
         |	s.input_id_y,
         |	s.input_amount_y,
         |	ay.ticker,
         |	ay.decimals,
         |	(
         |		SELECT
         |			count(*)
         |		FROM
         |			deposits sx
         |		WHERE
         |			output_amount_lp IS NOT NULL
         |			AND $fragment) AS numTxs
         |	FROM
         |		deposits s
         |	LEFT JOIN assets ax ON ax.id = s.input_id_x
         |	LEFT JOIN assets ay ON ay.id = s.input_id_y
         |WHERE
         |	output_amount_lp IS NOT NULL
         |	AND $fragment2
         """.stripMargin.query[DepositInfo]
  }

}
