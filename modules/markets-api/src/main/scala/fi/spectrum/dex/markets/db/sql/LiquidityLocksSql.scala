package fi.spectrum.dex.markets.db.sql

import doobie.LogHandler
import doobie.implicits._
import doobie.util.query.Query0
import fi.spectrum.core.domain.constants.cfmm.TotalEmissionLP
import fi.spectrum.core.domain.order.PoolId
import fi.spectrum.dex.markets.db.models.locks.LiquidityLockStats
import fi.spectrum.dex.markets.db.models.locks.LiquidityLockStats

final class LiquidityLocksSql(implicit lg: LogHandler) {

  def getLocksByPool(poolId: PoolId, leastDeadline: Int): Query0[LiquidityLockStats] =
    sql"""
         |select
         |  p.pool_id,
         |  lq.deadline,
         |  lq.amount,
         |  (lq.amount::decimal) * 100 / p.lp_emission as percent,
         |  lq.redeemer
         |from lq_locks lq
         |left join (
         |  select p.pool_id, p.lp_id, $TotalEmissionLP - p.lp_amount as lp_emission from pools p
         |  where p.pool_id = $poolId
         |  order by p.gindex desc limit 1
         |) as p on p.lp_id = lq.token_id
         |where p.pool_id is not null and lq.deadline >= $leastDeadline
         |order by lq.deadline asc
         """.stripMargin.query[LiquidityLockStats]
}
