package fi.spectrum.api.db.sql

import cats.syntax.list._
import doobie._
import doobie.implicits._
import doobie.util.log.LogHandler
import fi.spectrum.api.db.models.OrderDB.{AmmDepositDB, AmmRedeemDB, AnyOrderDB, LmCompoundDB, LmDepositDB, LockDB, SwapDB}
import fi.spectrum.api.db.models.{RegisterCompound, RegisterDeposit, RegisterLmDeposit, RegisterRedeem, RegisterSwap}
import fi.spectrum.api.v1.endpoints.models.TimeWindow
import fi.spectrum.api.v1.models.history.{OrderStatusApi, TokenPair}
import fi.spectrum.core.domain.order.OrderId
import fi.spectrum.core.domain.{PubKey, TokenId, TxId}

final class HistorySql(implicit lh: LogHandler) {

  def totalAddressOrders(list: List[PubKey]): doobie.Query0[Long] =
    sql"""
         |SELECT
         |	sum(x.y)
         |FROM (
         |	SELECT
         |		count(1) AS y
         |	FROM
         |		swaps where ${in(list)}
         |	UNION
         |	SELECT
         |		count(1) AS y
         |	FROM
         |		deposits where ${in(list)}
         |	UNION
         |	SELECT
         |		count(1) AS y
         |	FROM
         |		lq_locks where ${in(list)}
         |	UNION
         |	SELECT
         |		count(1) AS y
         |	FROM
         |		redeems where ${in(list)}
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

  def lmCompoundRegister(orderId: OrderId): Query0[RegisterCompound] =
    sql"""
         |select registered_transaction_id, registered_transaction_timestamp from lm_compound where order_id=$orderId
         |""".stripMargin.query[RegisterCompound]

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
    pair: Option[TokenPair]
  ): Query0[AnyOrderDB] =
    sql"""
         |SELECT
         |	*
         |FROM (
         |	SELECT
         |		order_id,
         |		pool_id,
         |		'swap',
         |		base_id,
         |		base_amount,
         |		min_quote_id,
         |		min_quote_amount,
         |		quote_amount,
         |		NULL AS input_id_x,
         |		NULL AS input_amount_x,
         |		NULL AS input_id_y,
         |		NULL AS input_amount_y,
         |		NULL AS output_id_lp,
         |		NULL AS output_amount_lp,
         |		NULL AS actual_input_amount_x,
         |		NULL AS actual_input_amount_y,
         |		NULL AS lp_id,
         |		NULL::bigint AS lp_amount,
         |		NULL AS output_id_x,
         |		NULL::bigint AS output_amount_x,
         |		NULL AS output_id_y,
         |		NULL::bigint AS output_amount_y,
         |		NULL::integer AS deadline,
         |		NULL AS token_id,
         |		NULL::bigint AS amount,
         |		NULL AS evaluation_transaction_id,
         |		NULL AS evaluation_lock_type,
         |      NULL,
         |		NULL::bigint,
         |		NULL,
         |		NULL::bigint,
         |		NULL,
         |		NULL::integer,
         |		NULL,
         |		NULL::bigint,
         |		NULL,
         |		NULL::bigint,
         |		NULL,
         |		dex_fee,
         |		fee_type,
         |		redeemer,
         |		registered_transaction_id,
         |		registered_transaction_timestamp,
         |		executed_transaction_id,
         |		executed_transaction_timestamp,
         |		refunded_transaction_id,
         |		refunded_transaction_timestamp
         |	FROM swaps
         |	    ${orderCondition(addresses, tw, status)} ${txIdF(txId)} ${tokensIn(
      tokens,
      pair,
      "base_id",
      "min_quote_id"
    )}
         |	UNION
         |	SELECT
         |		order_id,
         |		pool_id,
         |		'deposit',
         |		NULL,
         |		NULL,
         |		NULL,
         |		NULL,
         |		NULL,
         |		input_id_x,
         |		input_amount_x,
         |		input_id_y,
         |		input_amount_y,
         |		output_id_lp,
         |		output_amount_lp,
         |		actual_input_amount_x,
         |		actual_input_amount_y,
         |		NULL,
         |		NULL,
         |		NULL,
         |		NULL,
         |		NULL,
         |		NULL,
         |		NULL,
         |		NULL,
         |		NULL,
         |		NULL,
         |		NULL,
         |      NULL,
         |		NULL,
         |		NULL,
         |		NULL,
         |		NULL,
         |		NULL,
         |		NULL,
         |		NULL,
         |		NULL,
         |		NULL,
         |		NULL,
         |		dex_fee,
         |		fee_type,
         |		redeemer,
         |		registered_transaction_id,
         |		registered_transaction_timestamp,
         |		executed_transaction_id,
         |		executed_transaction_timestamp,
         |		refunded_transaction_id,
         |		refunded_transaction_timestamp
         |	FROM deposits
         |	    ${orderCondition(addresses, tw, status)} ${txIdF(txId)} ${tokensIn(
      tokens,
      pair,
      "input_id_x",
      "input_id_y"
    )}
         |	UNION
         |	SELECT
         |		order_id,
         |		pool_id,
         |		'redeem',
         |		NULL,
         |		NULL,
         |		NULL,
         |		NULL,
         |		NULL,
         |		NULL,
         |		NULL,
         |		NULL,
         |		NULL,
         |		NULL,
         |		NULL,
         |		NULL,
         |		NULL,
         |		lp_id,
         |		lp_amount,
         |		output_id_x,
         |		output_amount_x,
         |		output_id_y,
         |		output_amount_y,
         |		NULL,
         |		NULL,
         |		NULL,
         |		NULL,
         |		NULL,
         |      NULL,
         |		NULL,
         |		NULL,
         |		NULL,
         |		NULL,
         |		NULL,
         |		NULL,
         |		NULL,
         |		NULL,
         |		NULL,
         |		NULL,
         |		dex_fee,
         |		fee_type,
         |		redeemer,
         |		registered_transaction_id,
         |		registered_transaction_timestamp,
         |		executed_transaction_id,
         |		executed_transaction_timestamp,
         |		refunded_transaction_id,
         |		refunded_transaction_timestamp
         |	FROM redeems
         |	    ${orderCondition(addresses, tw, status)} ${txIdF(txId)} ${tokensIn(
      tokens,
      pair,
      "output_id_x",
      "output_id_y"
    )}
         |	UNION
         |	SELECT
         |		order_id,
         |		NULL,
         |		'lock',
         |		NULL,
         |		NULL,
         |		NULL,
         |		NULL,
         |		NULL,
         |		NULL,
         |		NULL,
         |		NULL,
         |		NULL,
         |		NULL,
         |		NULL,
         |		NULL,
         |		NULL,
         |		NULL,
         |		NULL,
         |		NULL,
         |		NULL,
         |		NULL,
         |		NULL,
         |		deadline,
         |		token_id,
         |		amount,
         |		evaluation_transaction_id,
         |		evaluation_lock_type,
         |      NULL,
         |		NULL,
         |		NULL,
         |		NULL,
         |		NULL,
         |		NULL,
         |		NULL,
         |		NULL,
         |		NULL,
         |		NULL,
         |		NULL,
         |		NULL,
         |		NULL,
         |		redeemer,
         |		transaction_id,
         |		timestamp as registered_transaction_timestamp,
         |		NULL,
         |		NULL,
         |		NULL,
         |		NULL
         |	FROM lq_locks
         |    ${orderCondition(addresses, tw, None)} ${txIdLock(txId)} ${lockTokens(pair, tokens)}
         |UNION
         |	SELECT
         |		order_id,
         |		NULL,
         |		'lm_compound',
         |		NULL,
         |		NULL,
         |		NULL,
         |		NULL,
         |		NULL,
         |		NULL,
         |		NULL,
         |		NULL,
         |		NULL,
         |		NULL,
         |		NULL,
         |		NULL,
         |		NULL,
         |		NULL,
         |		NULL,
         |		NULL,
         |		NULL,
         |		NULL,
         |		NULL,
         |		NULL,
         |		NULL,
         |		NULL,
         |		NULL,
         |		NULL,
         |      v_lq_id,
         |		v_lq_amount,
         |		tmp_id,
         |		tmp_amount,
         |		bundle_key_id,
         |		NULL,
         |		NULL,
         |		NULL,
         |		NULL,
         |		NULL,
         |		NULL,
         |		NULL,
         |		NULL,
         |		redeemer,
         |		transaction_id,
         |		timestamp as registered_transaction_timestamp,
         |		NULL,
         |		NULL,
         |		NULL,
         |		NULL
         |	FROM lm_compound
         |    ${orderCondition(addresses, tw, None)} ${txIdCompound(txId)}
         |UNION
         |	SELECT
         |		order_id,
         |		NULL,
         |		'lock',
         |		NULL,
         |		NULL,
         |		NULL,
         |		NULL,
         |		NULL,
         |		NULL,
         |		NULL,
         |		NULL,
         |		NULL,
         |		NULL,
         |		NULL,
         |		NULL,
         |		NULL,
         |		NULL,
         |		NULL,
         |		NULL,
         |		NULL,
         |		NULL,
         |		NULL,
         |		NULL,
         |		NULL,
         |		NULL,
         |		NULL,
         |		NULL,
         |      NULL,
         |		NULL,
         |		NULL,
         |		NULL,
         |		NULL,
         |		expected_num_epochs,
         |		input_id,
         |		input_amount,
         |		lp_id,
         |		lp_amount,
         |		compound_id,
         |		NULL,
         |		NULL,
         |		redeemer,
         |		transaction_id,
         |		timestamp as registered_transaction_timestamp,
         |		NULL,
         |		NULL,
         |		NULL,
         |		NULL
         |	FROM lm_deposits
         |    ${orderCondition(addresses, tw, None)} ${txIdF(txId)}
	     |) AS x
         |ORDER BY x.registered_transaction_timestamp DESC 
         |OFFSET $offset LIMIT $limit;
          """.stripMargin.query[AnyOrderDB]

  def getLmDeposits(
    addresses: List[PubKey],
    offset: Int,
    limit: Int,
    tw: TimeWindow,
    status: Option[OrderStatusApi],
    txId: Option[TxId]
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
         |${orderCondition(addresses, tw, status)} ${txIdF(txId)} 
         |ORDER BY registered_transaction_timestamp DESC
         |OFFSET $offset LIMIT $limit;
       """.stripMargin.query[LmDepositDB]

  def getLmCompound(
    addresses: List[PubKey],
    offset: Int,
    limit: Int,
    tw: TimeWindow,
    status: Option[OrderStatusApi],
    txId: Option[TxId]
  ): doobie.Query0[LmCompoundDB] =
    sql"""
         |SELECT
         |	order_id,
         |	pool_id,
         |	v_lq_id,
         |	v_lq_amount,
         |	tmp_id,
         |	tmp_amount,
         |	bundle_key_id,
         |	registered_transaction_id,
         |	registered_transaction_timestamp,
         |	refunded_transaction_id,
         |	executed_transaction_timestamp
         |FROM
         |	lm_compound
         |${orderCondition(addresses, tw, status)} ${txIdCompound(txId)}
         |ORDER BY registered_transaction_timestamp DESC
         |OFFSET $offset LIMIT $limit;
       """.stripMargin.query[LmCompoundDB]

  def getSwaps(
    addresses: List[PubKey],
    offset: Int,
    limit: Int,
    tw: TimeWindow,
    status: Option[OrderStatusApi],
    txId: Option[TxId],
    tokens: Option[List[TokenId]],
    pair: Option[TokenPair]
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
         |${orderCondition(addresses, tw, status)} ${txIdF(txId)} ${tokensIn(tokens, pair, "base_id", "min_quote_id")}
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
    pair: Option[TokenPair]
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
         |${orderCondition(addresses, tw, status)} ${txIdF(txId)} ${tokensIn(tokens, pair, "input_id_x", "input_id_y")}
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
    pair: Option[TokenPair]
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
         |${orderCondition(addresses, tw, status)} ${txIdF(txId)} ${tokensIn(tokens, pair, "output_id_x", "output_id_y")}
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
    txId: Option[TxId]
  ): Query0[LockDB] =
    sql"""
         |SELECT
         |	order_id,
	     |	transaction_id,
         |	timestamp
         |	deadline,
         |	token_id,
         |	amount,
	     |  redeemer,
         |	evaluation_transaction_id,
         |	evaluation_lock_type
         |FROM
         |	lq_locks
         |${orderCondition(addresses, tw, None)} ${txIdLock(txId)}
         |ORDER BY timestamp DESC
         |OFFSET $offset LIMIT $limit;
        """.stripMargin.query[LockDB]

  private def orderCondition(
    addresses: List[PubKey],
    tw: TimeWindow,
    status: Option[OrderStatusApi]
  ): doobie.Fragment =
    Fragments.whereAndOpt(
      addresses.toNel.map(Fragments.in(fr"redeemer", _)),
      tw.from.map(f => Fragment.const(s"registered_transaction_timestamp > $f")),
      tw.to.map(t => Fragment.const(s"registered_transaction_timestamp <= $t")),
      orderStatus(status)
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

  private def txIdCompound(txId: Option[TxId]): Fragment =
    txId
      .map { id =>
        Fragment.const(
          s"and (registered_transaction_id='$id' or executed_transaction_id='$id')"
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

  def lockTokens(pair: Option[TokenPair], tokens: Option[List[TokenId]]): Fragment =
    if (pair.isEmpty && tokens.isEmpty) Fragment.const("and true") else Fragment.const("and false")

}
