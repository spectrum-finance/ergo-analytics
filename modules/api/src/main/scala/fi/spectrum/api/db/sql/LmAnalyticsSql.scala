package fi.spectrum.api.db.sql

import cats.data.NonEmptyList
import doobie.implicits._
import doobie.util.query.Query0
import doobie.{Fragments, LogHandler}
import fi.spectrum.api.db.models.lm.{LmPoolSnapshot, UserDeposit, UserInterest}
import fi.spectrum.core.domain.SErgoTree
import fi.spectrum.core.domain.order.Redeemer.PublicKeyRedeemer

final class LmAnalyticsSql(implicit lg: LogHandler) {

  def lmPoolSnapshots: Query0[LmPoolSnapshot] =
    sql"""
         |SELECT
         |	p1.pool_id,
         |	p1.reward_id,
         |	p1.reward_amount,
         |	p1.lq_id,
         |	p1.lq_amount,
         |	p1.program_budget,
         |	p1.program_start,
         |	p1.epoch_length,
         |	p1.epochs_num,
         |	p2.reward_amount
         |FROM
         |	lm_pools p1
         |	LEFT JOIN (SELECT max(lm.id) AS id, max(reward_amount) as reward_amount, pool_id FROM lm_pools lm GROUP BY pool_id) AS p2 ON p2.id = p1.id
         |WHERE
         |	p2.id = p1.id;
       """.stripMargin.query

  def userDeposit(addresses: NonEmptyList[SErgoTree]): Query0[UserDeposit] =
    sql"""
         |SELECT
         |  pool_id,
         |	input_id,
         |	sum(input_amount)
         |FROM
         |	lm_deposits
         |WHERE executed_transaction_id IS NOT NULL and ${Fragments.in(fr"redeemer_ergo_tree", addresses)}
         |GROUP BY
         |	pool_id, input_id;
       """.stripMargin.query

  def userInterest(addresses: NonEmptyList[PublicKeyRedeemer]): Query0[UserInterest] =
    sql"""
         |SELECT
         |	*
         |FROM (
         |	SELECT
         |		pool_id,
         |		interest_id,
         |		sum(interest_amount) AS total_interest
         |	FROM
         |		lm_compound
         |	WHERE
         |		${Fragments.in(fr"redeemer", addresses)}
         |		AND executed_transaction_id IS NOT NULL
         |	GROUP BY
         |		pool_id,
         |		interest_id) p
         |WHERE
         |	p.total_interest IS NOT NULL;
       """.stripMargin.query

}
