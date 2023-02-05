package fi.spectrum.api.db.sql

import doobie.util.log.LogHandler
import doobie.implicits._
import doobie._
import fi.spectrum.api.models.{RegisterDeposit, RegisterRedeem, RegisterSwap}
import fi.spectrum.core.domain.order.OrderId

final class HistorySql(implicit lh: LogHandler) {

  def swapRegister(orderId: OrderId): Query0[RegisterSwap] =
    sql"""
       |select base_id, base_amount, min_quote_id, min_quote_amount, 
       |registered_transaction_id, registered_transaction_timestamp from swaps where order_id=$orderId
       |""".stripMargin.query[RegisterSwap]

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
}
