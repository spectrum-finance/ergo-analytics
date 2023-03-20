package fi.spectrum.api.db.sql

import cats.syntax.list._
import doobie._
import doobie.implicits._
import doobie.util.log.LogHandler
import fi.spectrum.api.db.models.OrderDB._
import fi.spectrum.api.db.models._
import fi.spectrum.api.v1.endpoints.models.TimeWindow
import fi.spectrum.api.v1.models.history.{OrderStatusApi, TokenPair}
import fi.spectrum.core.domain.order.OrderId
import fi.spectrum.core.domain.{PubKey, TokenId, TxId}

final class HistorySql(implicit lh: LogHandler) {

  def addressCount(list: List[PubKey]): doobie.Query0[Long] =
    sql"""
         |SELECT
         |	sum(x.y)
         |FROM (
         |	SELECT count(1) AS y FROM swaps where ${in(list)}
         |	UNION
         |	SELECT count(1) AS y FROM deposits where ${in(list)}
         |	UNION
         |	SELECT count(1) AS y FROM lq_locks where ${in(list)}
         |	UNION
         |	SELECT count(1) AS y FROM redeems where ${in(list)}
         |UNION
         |	SELECT count(1) AS y FROM lm_redeems where ${in2(list)}
         |UNION
         |	SELECT count(1) AS y FROM lm_deposits where ${in2(list)}
         |) AS x
       """.stripMargin.query[Long]

  def swapRegister(orderId: OrderId): Query0[RegisterSwap] =
    sql"""
       |select base_id, base_amount, min_quote_id, min_quote_amount, 
       |registered_transaction_id, registered_transaction_timestamp from swaps where order_id=$orderId
       |""".stripMargin.query[RegisterSwap]

  def lmDepositRegister(orderId: OrderId): Query0[RegisterLmDeposit] =
    sql"""
       |select expected_num_epochs, input_id, input_amount, registered_transaction_id, registered_transaction_timestamp from lm_deposits where order_id=$orderId
       |""".stripMargin.query[RegisterLmDeposit]

  def lmRedeemRegister(orderId: OrderId): Query0[RegisterLmRedeem] =
    sql"""
         |select bundle_key_id, expected_lq_id, expected_lq_amount, registered_transaction_id,
         |registered_transaction_timestamp from lm_redeems where order_id=$orderId
         |""".stripMargin.query[RegisterLmRedeem]

  def redeemRegister(orderId: OrderId): Query0[RegisterRedeem] =
    sql"""
         |select lp_id, lp_amount,
         |registered_transaction_id, registered_transaction_timestamp from redeems where order_id=$orderId
         |""".stripMargin.query[RegisterRedeem]

  def depositRegister(orderId: OrderId): Query0[RegisterDeposit] =
    sql"""
         |select input_id_x, input_amount_x, input_id_y, input_amount_y, 
         |registered_transaction_id, registered_transaction_timestamp from deposits where order_id=$orderId
         |""".stripMargin.query[RegisterDeposit]

  def getAnyOrders(
    addresses: List[PubKey],
    offset: Int,
    limit: Int,
    tw: TimeWindow,
    status: Option[OrderStatusApi],
    txId: Option[TxId],
    tokens: Option[List[TokenId]],
    pair: Option[TokenPair],
    skipOrders: List[OrderId]
  ): Query0[AnyOrderDB] = {
    sql"""
         |SELECT
         |	*
         |FROM (
         |	SELECT
         |	order_id,
         |	pool_id,
         |	'swap',
         |	base_id AS swap_base_id,
         |	base_amount AS swap_base_amount,
         |	min_quote_id AS swap_min_quote_id,
         |	min_quote_amount AS swap_min_quote_amount,
         |	quote_amount AS swap_quote_amount,
         |	NULL AS deposit_input_id_x,
         |	NULL AS deposit_input_amount_x,
         |	NULL AS deposit_input_id_y,
         |	NULL AS deposit_input_amount_y,
         |	NULL AS deposit_output_id_lp,
         |	NULL AS deposit_output_amount_lp,
         |	NULL AS deposit_actual_input_amount_x,
         |	NULL AS deposit_actual_input_amount_y,
         |	NULL AS redeem_lp_id,
         |	NULL::bigint AS redeem_lp_amount,
         |	NULL AS redeem_output_id_x,
         |	NULL::bigint AS redeem_output_amount_x,
         |	NULL AS redeem_output_id_y,
         |	NULL::bigint AS redeem_output_amount_y,
         |	NULL::integer AS lock_deadline,
         |	NULL AS lock_token_id,
         |	NULL::bigint AS lock_amount,
         |	NULL AS lock_evaluation_transaction_id,
         |	NULL AS lock_evaluation_lock_type,
         |	NULL::integer AS lm_deposits_expected_num_epochs,
         |	NULL AS lm_deposits_input_id,
         |	NULL::bigint AS lm_deposits_input_amount,
         |	NULL AS lm_deposits_lp_id,
         |	NULL::bigint AS lm_deposits_lp_amount,
         |	NULL AS lm_deposits_compound_id,
         |	NULL AS lm_redeems_bundle_key_id,
         |	NULL AS lm_redeems_expected_lq_id,
         |	NULL::bigint AS lm_redeems_expected_lq_amount,
         |	NULL AS lm_redeems_out_id,
         |	NULL::bigint AS lm_redeems_out_amount,
         |	NULL AS lm_redeems_box_id,
         |	dex_fee,
         |	fee_type,
         |	redeemer,
         |	registered_transaction_id,
         |	registered_transaction_timestamp,
         |	executed_transaction_id,
         |	executed_transaction_timestamp,
         |	refunded_transaction_id,
         |	refunded_transaction_timestamp
         |FROM
         |	swaps ${orderCondition(addresses, tw, status, skipOrders)} ${txIdF(txId)} ${tokensIn(
      tokens,
      pair,
      "base_id",
      "min_quote_id"
    )}
         |	UNION
         |SELECT
         |	order_id,
         |	pool_id,
         |	'deposit',
         |	NULL AS swap_base_id,
         |	NULL AS swap_base_amount,
         |	NULL AS swap_min_quote_id,
         |	NULL AS swap_min_quote_amount,
         |	NULL AS swap_quote_amount,
         |	input_id_x AS deposit_input_id_x,
         |	input_amount_x AS deposit_input_amount_x,
         |	input_id_y AS deposit_input_id_y,
         |	input_amount_y AS deposit_input_amount_y,
         |	output_id_lp AS deposit_output_id_lp,
         |	output_amount_lp AS deposit_output_amount_lp,
         |	actual_input_amount_x AS deposit_actual_input_amount_x,
         |	actual_input_amount_y AS deposit_actual_input_amount_y,
         |	NULL AS redeem_lp_id,
         |	NULL::bigint AS redeem_lp_amount,
         |	NULL AS redeem_output_id_x,
         |	NULL::bigint AS redeem_output_amount_x,
         |	NULL AS redeem_output_id_y,
         |	NULL::bigint AS redeem_output_amount_y,
         |	NULL::integer AS lock_deadline,
         |	NULL AS lock_token_id,
         |	NULL::bigint AS lock_amount,
         |	NULL AS lock_evaluation_transaction_id,
         |	NULL AS lock_evaluation_lock_type,
         |	NULL::integer AS lm_deposits_expected_num_epochs,
         |	NULL AS lm_deposits_input_id,
         |	NULL::bigint AS lm_deposits_input_amount,
         |	NULL AS lm_deposits_lp_id,
         |	NULL::bigint AS lm_deposits_lp_amount,
         |	NULL AS lm_deposits_compound_id,
         |	NULL AS lm_redeems_bundle_key_id,
         |	NULL AS lm_redeems_expected_lq_id,
         |	NULL::bigint AS lm_redeems_expected_lq_amount,
         |	NULL AS lm_redeems_out_id,
         |	NULL::bigint AS lm_redeems_out_amount,
         |	NULL AS lm_redeems_box_id,
         |	dex_fee,
         |	fee_type,
         |	redeemer,
         |	registered_transaction_id,
         |	registered_transaction_timestamp,
         |	executed_transaction_id,
         |	executed_transaction_timestamp,
         |	refunded_transaction_id,
         |	refunded_transaction_timestamp
         |FROM
         |	deposits
         |	    ${orderCondition(addresses, tw, status, skipOrders)} ${txIdF(txId)} ${tokensIn(
      tokens,
      pair,
      "input_id_x",
      "input_id_y"
    )}
         |	UNION
         |SELECT
         |	order_id,
         |	pool_id,
         |	'redeem',
         |	NULL AS swap_base_id,
         |	NULL AS swap_base_amount,
         |	NULL AS swap_min_quote_id,
         |	NULL AS swap_min_quote_amount,
         |	NULL AS swap_quote_amount,
         |	NULL AS deposit_input_id_x,
         |	NULL AS deposit_input_amount_x,
         |	NULL AS deposit_input_id_y,
         |	NULL AS deposit_input_amount_y,
         |	NULL AS deposit_output_id_lp,
         |	NULL AS deposit_output_amount_lp,
         |	NULL AS deposit_actual_input_amount_x,
         |	NULL AS deposit_actual_input_amount_y,
         |	lp_id AS redeem_lp_id,
         |	lp_amount::bigint AS redeem_lp_amount,
         |	output_id_x AS redeem_output_id_x,
         |	output_amount_x::bigint AS redeem_output_amount_x,
         |	output_id_y AS redeem_output_id_y,
         |	output_amount_y::bigint AS redeem_output_amount_y,
         |	NULL::integer AS lock_deadline,
         |	NULL AS lock_token_id,
         |	NULL::bigint AS lock_amount,
         |	NULL AS lock_evaluation_transaction_id,
         |	NULL AS lock_evaluation_lock_type,
         |	NULL::integer AS lm_deposits_expected_num_epochs,
         |	NULL AS lm_deposits_input_id,
         |	NULL::bigint AS lm_deposits_input_amount,
         |	NULL AS lm_deposits_lp_id,
         |	NULL::bigint AS lm_deposits_lp_amount,
         |	NULL AS lm_deposits_compound_id,
         |	NULL AS lm_redeems_bundle_key_id,
         |	NULL AS lm_redeems_expected_lq_id,
         |	NULL::bigint AS lm_redeems_expected_lq_amount,
         |	NULL AS lm_redeems_out_id,
         |	NULL::bigint AS lm_redeems_out_amount,
         |	NULL AS lm_redeems_box_id,
         |	dex_fee,
         |	fee_type,
         |	redeemer,
         |	registered_transaction_id,
         |	registered_transaction_timestamp,
         |	executed_transaction_id,
         |	executed_transaction_timestamp,
         |	refunded_transaction_id,
         |	refunded_transaction_timestamp
         |	FROM redeems
         |	    ${orderCondition(addresses, tw, status, skipOrders)} ${txIdF(txId)} ${tokensIn(
      tokens,
      pair,
      "output_id_x",
      "output_id_y"
    )}
         |	UNION
         |SELECT
         |	order_id,
         |	NULL,
         |	'lock',
         |	NULL AS swap_base_id,
         |	NULL AS swap_base_amount,
         |	NULL AS swap_min_quote_id,
         |	NULL AS swap_min_quote_amount,
         |	NULL AS swap_quote_amount,
         |	NULL AS deposit_input_id_x,
         |	NULL AS deposit_input_amount_x,
         |	NULL AS deposit_input_id_y,
         |	NULL AS deposit_input_amount_y,
         |	NULL AS deposit_output_id_lp,
         |	NULL AS deposit_output_amount_lp,
         |	NULL AS deposit_actual_input_amount_x,
         |	NULL AS deposit_actual_input_amount_y,
         |	NULL AS redeem_lp_id,
         |	NULL::bigint AS redeem_lp_amount,
         |	NULL AS redeem_output_id_x,
         |	NULL::bigint AS redeem_output_amount_x,
         |	NULL AS redeem_output_id_y,
         |	NULL::bigint AS redeem_output_amount_y,
         |	deadline::integer AS lock_deadline,
         |	token_id AS lock_token_id,
         |	amount::bigint AS lock_amount,
         |	evaluation_transaction_id AS lock_evaluation_transaction_id,
         |	evaluation_lock_type AS lock_evaluation_lock_type,
         |	NULL::integer AS lm_deposits_expected_num_epochs,
         |	NULL AS lm_deposits_input_id,
         |	NULL::bigint AS lm_deposits_input_amount,
         |	NULL AS lm_deposits_lp_id,
         |	NULL::bigint AS lm_deposits_lp_amount,
         |	NULL AS lm_deposits_compound_id,
         |	NULL AS lm_redeems_bundle_key_id,
         |	NULL AS lm_redeems_expected_lq_id,
         |	NULL::bigint AS lm_redeems_expected_lq_amount,
         |	NULL AS lm_redeems_out_id,
         |	NULL::bigint AS lm_redeems_out_amount,
         |	NULL AS lm_redeems_box_id,
         |	NULL,
         |	NULL,
         |	redeemer,
         |	transaction_id,
         |	timestamp as registered_transaction_timestamp,
         |	NULL,
         |	NULL,
         |	NULL,
         |	NULL
         |	FROM lq_locks
         |    ${orderCondition(addresses, tw, None, skipOrders)} ${txIdLock(txId)} ${lockTokens(pair, tokens)}
         |UNION
         |SELECT
         |	order_id,
         |	pool_id,
         |	'lm_deposit',
         |	NULL AS swap_base_id,
         |	NULL AS swap_base_amount,
         |	NULL AS swap_min_quote_id,
         |	NULL AS swap_min_quote_amount,
         |	NULL AS swap_quote_amount,
         |	NULL AS deposit_input_id_x,
         |	NULL AS deposit_input_amount_x,
         |	NULL AS deposit_input_id_y,
         |	NULL AS deposit_input_amount_y,
         |	NULL AS deposit_output_id_lp,
         |	NULL AS deposit_output_amount_lp,
         |	NULL AS deposit_actual_input_amount_x,
         |	NULL AS deposit_actual_input_amount_y,
         |	NULL AS redeem_lp_id,
         |	NULL::bigint AS redeem_lp_amount,
         |	NULL AS redeem_output_id_x,
         |	NULL::bigint AS redeem_output_amount_x,
         |	NULL AS redeem_output_id_y,
         |	NULL::bigint AS redeem_output_amount_y,
         |	NULL::integer AS lock_deadline,
         |	NULL AS lock_token_id,
         |	NULL::bigint AS lock_amount,
         |	NULL AS lock_evaluation_transaction_id,
         |	NULL AS lock_evaluation_lock_type,
         |	expected_num_epochs::integer AS lm_deposits_expected_num_epochs,
         |	input_id AS lm_deposits_input_id,
         |	input_amount::bigint AS lm_deposits_input_amount,
         |	lp_id AS lm_deposits_lp_id,
         |	lp_amount::bigint AS lm_deposits_lp_amount,
         |	compound_id AS lm_deposits_compound_id,
         |	null AS lm_redeems_bundle_key_id,
         |	NULL AS lm_redeems_expected_lq_id,
         |	NULL::bigint AS lm_redeems_expected_lq_amount,
         |	NULL AS lm_redeems_out_id,
         |	NULL::bigint AS lm_redeems_out_amount,
         |	NULL AS lm_redeems_box_id,
         |	null,
         |	null,
         |	redeemer_ergo_tree,
         |	registered_transaction_id,
         |	registered_transaction_timestamp,
         |	executed_transaction_id,
         |	executed_transaction_timestamp,
         |	refunded_transaction_id,
         |	refunded_transaction_timestamp
         |	FROM lm_deposits
         |    ${orderCondition2(addresses, tw, None, skipOrders)} ${txIdF(txId)}
	     |UNION
         |SELECT
         |	order_id,
         |	pool_id,
         |	'lm_redeems',
         |	NULL AS swap_base_id,
         |	NULL AS swap_base_amount,
         |	NULL AS swap_min_quote_id,
         |	NULL AS swap_min_quote_amount,
         |	NULL AS swap_quote_amount,
         |	NULL AS deposit_input_id_x,
         |	NULL AS deposit_input_amount_x,
         |	NULL AS deposit_input_id_y,
         |	NULL AS deposit_input_amount_y,
         |	NULL AS deposit_output_id_lp,
         |	NULL AS deposit_output_amount_lp,
         |	NULL AS deposit_actual_input_amount_x,
         |	NULL AS deposit_actual_input_amount_y,
         |	NULL AS redeem_lp_id,
         |	NULL::bigint AS redeem_lp_amount,
         |	NULL AS redeem_output_id_x,
         |	NULL::bigint AS redeem_output_amount_x,
         |	NULL AS redeem_output_id_y,
         |	NULL::bigint AS redeem_output_amount_y,
         |	NULL::integer AS lock_deadline,
         |	NULL AS lock_token_id,
         |	NULL::bigint AS lock_amount,
         |	NULL AS lock_evaluation_transaction_id,
         |	NULL AS lock_evaluation_lock_type,
         |	null::integer AS lm_deposits_expected_num_epochs,
         |	null AS lm_deposits_input_id,
         |	null::bigint AS lm_deposits_input_amount,
         |	null AS lm_deposits_lp_id,
         |	null::bigint AS lm_deposits_lp_amount,
         |	null AS lm_deposits_compound_id,
         |	bundle_key_id AS lm_redeems_bundle_key_id,
         |	expected_lq_id AS lm_redeems_expected_lq_id,
         |	expected_lq_amount::bigint AS lm_redeems_expected_lq_amount,
         |	out_id AS lm_redeems_out_id,
         |	out_amount::bigint AS lm_redeems_out_amount,
         |	out_box_id AS lm_redeems_box_id,
         |	null,
         |	null,
         |	redeemer_ergo_tree,
         |	registered_transaction_id,
         |	registered_transaction_timestamp,
         |	executed_transaction_id,
         |	executed_transaction_timestamp,
         |	refunded_transaction_id,
         |	refunded_transaction_timestamp
         |	FROM lm_redeems
         |    ${orderCondition2(addresses, tw, None, skipOrders)} ${txIdF(txId)}
         |) AS x
         |ORDER BY x.registered_transaction_timestamp DESC 
         |OFFSET $offset LIMIT $limit;
          """.stripMargin.query[AnyOrderDB]
  }

  def getLmRedeems(
    addresses: List[PubKey],
    offset: Int,
    limit: Int,
    tw: TimeWindow,
    status: Option[OrderStatusApi],
    txId: Option[TxId],
    skipOrders: List[OrderId]
  ): doobie.Query0[LmRedeemsDB] =
    sql"""
         |SELECT
         |	order_id,
         |	pool_id,
         |	bundle_key_id,
         |	expected_lq_id,
         |	expected_lq_amount,
         |	out_id,
         |	out_amount,
         |	out_box_id,
         |	registered_transaction_id,
         |	registered_transaction_timestamp,
         |	refunded_transaction_id,
         |	refunded_transaction_timestamp,
         |	executed_transaction_id,
         |	executed_transaction_timestamp
         |FROM
         |	lm_redeems
         |${orderCondition2(addresses, tw, status, skipOrders)} ${txIdF(txId)}
         |ORDER BY registered_transaction_timestamp DESC
         |OFFSET $offset LIMIT $limit;
         """.stripMargin.query[LmRedeemsDB]

  def getLmDeposits(
    addresses: List[PubKey],
    offset: Int,
    limit: Int,
    tw: TimeWindow,
    status: Option[OrderStatusApi],
    txId: Option[TxId],
    skipOrders: List[OrderId]
  ): doobie.Query0[LmDepositDB] =
    sql"""
         |SELECT
         |	order_id,
         |	pool_id,
         |	expected_num_epochs,
         |	input_id,
         |	input_amount,
         |	lp_id,
         |	lp_amount,
         |	compound_id,
         |	registered_transaction_id,
         |	registered_transaction_timestamp,
         |	refunded_transaction_id,
         |	refunded_transaction_timestamp,
         |	executed_transaction_id,
         |	executed_transaction_timestamp
         |FROM
         |	lm_deposits
         |${orderCondition2(addresses, tw, status, skipOrders)} ${txIdF(txId)}
         |ORDER BY registered_transaction_timestamp DESC
         |OFFSET $offset LIMIT $limit;
       """.stripMargin.query[LmDepositDB]

  def getSwaps(
    addresses: List[PubKey],
    offset: Int,
    limit: Int,
    tw: TimeWindow,
    status: Option[OrderStatusApi],
    txId: Option[TxId],
    tokens: Option[List[TokenId]],
    pair: Option[TokenPair],
    skipOrders: List[OrderId]
  ): Query0[SwapDB] =
    sql"""
         |SELECT
         |	order_id,
         |	pool_id,
         |	base_id,
         |	base_amount,
         |	min_quote_id,
         |	min_quote_amount,
         |	quote_amount,
         |	dex_fee,
         |	fee_type,
         |	redeemer,
         |	registered_transaction_id,
         |	registered_transaction_timestamp,
         |	refunded_transaction_id,
         |	refunded_transaction_timestamp,
	     |	executed_transaction_id,
         |	executed_transaction_timestamp
         |FROM
         |	swaps
         |${orderCondition(addresses, tw, status, skipOrders)} ${txIdF(txId)} ${tokensIn(
      tokens,
      pair,
      "base_id",
      "min_quote_id"
    )}
         |ORDER BY registered_transaction_timestamp DESC
         |OFFSET $offset LIMIT $limit;
          """.stripMargin.query[SwapDB]

  def getDeposits(
    addresses: List[PubKey],
    offset: Int,
    limit: Int,
    tw: TimeWindow,
    status: Option[OrderStatusApi],
    txId: Option[TxId],
    tokens: Option[List[TokenId]],
    pair: Option[TokenPair],
    skipOrders: List[OrderId]
  ): Query0[AmmDepositDB] =
    sql"""
         |SELECT
         |	order_id,
         |	pool_id,
         |	input_id_x,
         |	input_amount_x,
         |	input_id_y,
         |	input_amount_y,
	     |	actual_input_amount_x,
         |	actual_input_amount_y,
         |	output_id_lp,
         |	output_amount_lp,
         |	dex_fee,
         |	fee_type,
         |	redeemer,
         |	registered_transaction_id,
         |	registered_transaction_timestamp,
         |	refunded_transaction_id,
         |	refunded_transaction_timestamp,
	     |	executed_transaction_id,
         |	executed_transaction_timestamp
         |FROM
         |	deposits
         |${orderCondition(addresses, tw, status, skipOrders)} ${txIdF(txId)} ${tokensIn(
      tokens,
      pair,
      "input_id_x",
      "input_id_y"
    )}
         |ORDER BY registered_transaction_timestamp DESC
         |OFFSET $offset LIMIT $limit;
        """.stripMargin.query[AmmDepositDB]

  def getRedeems(
    addresses: List[PubKey],
    offset: Int,
    limit: Int,
    tw: TimeWindow,
    status: Option[OrderStatusApi],
    txId: Option[TxId],
    tokens: Option[List[TokenId]],
    pair: Option[TokenPair],
    skipOrders: List[OrderId]
  ): Query0[AmmRedeemDB] =
    sql"""
         |SELECT
         |	order_id,
         |	pool_id,
         |	lp_id,
         |	lp_amount,
         |	output_id_x,
         |	output_amount_x,
         |	output_id_y,
         |	output_amount_y,
         |	dex_fee,
         |	fee_type,
         |	redeemer,
         |	registered_transaction_id,
         |	registered_transaction_timestamp,
         |	refunded_transaction_id,
         |	refunded_transaction_timestamp,
	     |	executed_transaction_id,
         |	executed_transaction_timestamp
         |FROM
         |	redeems
         |${orderCondition(addresses, tw, status, skipOrders)} ${txIdF(txId)} ${tokensIn(
      tokens,
      pair,
      "output_id_x",
      "output_id_y"
    )}
         |ORDER BY registered_transaction_timestamp DESC
         |OFFSET $offset LIMIT $limit;
        """.stripMargin.query[AmmRedeemDB]

  def tokensIn(list: Option[List[TokenId]], pair: Option[TokenPair], x: String, y: String): Fragment =
    list
      .flatMap(_.toNel)
      .map { list =>
        fr"and (" ++ Fragments.in(Fragment.const(x), list) ++ fr"or" ++ Fragments.in(Fragment.const(y), list) ++ fr")"
      }
      .orElse {
        pair.map { pair =>
          Fragment.const(s"and (($x = '${pair.x}' and $y = '${pair.y}') or ($x = '${pair.y}' and $y = '${pair.x}'))")
        }
      }
      .getOrElse(Fragment.empty)

  def getLocks(
    addresses: List[PubKey],
    offset: Int,
    limit: Int,
    tw: TimeWindow,
    txId: Option[TxId],
    skipOrders: List[OrderId]
  ): Query0[LockDB] =
    sql"""
         |SELECT
         |	order_id,
	     |	transaction_id,
         |	timestamp,
         |	deadline,
         |	token_id,
         |	amount,
	     |  redeemer,
         |	evaluation_transaction_id,
         |	evaluation_lock_type
         |FROM
         |	lq_locks
         |${orderCondition(addresses, tw, None, skipOrders)} ${txIdLock(txId)}
         |ORDER BY timestamp DESC
         |OFFSET $offset LIMIT $limit;
        """.stripMargin.query[LockDB]

  private def orderCondition(
    addresses: List[PubKey],
    tw: TimeWindow,
    status: Option[OrderStatusApi],
    skipOrders: List[OrderId]
  ): doobie.Fragment =
    Fragments.whereAndOpt(
      addresses.toNel.map(Fragments.in(fr"redeemer", _)),
      tw.from.map(f => Fragment.const(s"registered_transaction_timestamp > $f")),
      tw.to.map(t => Fragment.const(s"registered_transaction_timestamp <= $t")),
      orderStatus(status),
      skipOrders.toNel.map(Fragments.notIn(fr"order_id", _))
    )

  private def orderCondition2(
    addresses: List[PubKey],
    tw: TimeWindow,
    status: Option[OrderStatusApi],
    skipOrders: List[OrderId]
  ): doobie.Fragment =
    Fragments.whereAndOpt(
      addresses.map(_.ergoTree).toNel.map(Fragments.in(fr"redeemer_ergo_tree", _)),
      tw.from.map(f => Fragment.const(s"registered_transaction_timestamp > $f")),
      tw.to.map(t => Fragment.const(s"registered_transaction_timestamp <= $t")),
      orderStatus(status),
      skipOrders.toNel.map(Fragments.notIn(fr"order_id", _))
    )

  private def orderStatus(status: Option[OrderStatusApi]): Option[Fragment] =
    status
      .map {
        case OrderStatusApi.Registered =>
          Fragment.const("executed_transaction_id is null and refunded_transaction_id is null")
        case OrderStatusApi.Evaluated =>
          Fragment.const("executed_transaction_id is not null")
        case OrderStatusApi.Refunded =>
          Fragment.const("refunded_transaction_id is not null")
      }

  private def txIdF(txId: Option[TxId]): Fragment =
    txId
      .map { id =>
        Fragment.const(
          s"and (registered_transaction_id='$id' or executed_transaction_id='$id' or refunded_transaction_id='$id')"
        )
      }
      .getOrElse(Fragment.empty)

  private def txIdLock(txId: Option[TxId]): Fragment =
    txId
      .map { id =>
        Fragment.const(
          s"and (transaction_id='$id' or evaluation_transaction_id='$id')"
        )
      }
      .getOrElse(Fragment.empty)

  def in = (in: List[PubKey]) => in.toNel.map(Fragments.in(fr"redeemer", _)).getOrElse(Fragment.empty)

  def in2 = (in: List[PubKey]) =>
    in.map(_.ergoTree).toNel.map(Fragments.in(fr"redeemer_ergo_tree", _)).getOrElse(Fragment.empty)

  def lockTokens(pair: Option[TokenPair], tokens: Option[List[TokenId]]): Fragment =
    if (pair.isEmpty && tokens.isEmpty) Fragment.const("and true") else Fragment.const("and false")

}
