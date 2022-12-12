package fi.spectrum.indexer.db.schema

import doobie.Update
import doobie.util.Write
import doobie.util.log.LogHandler
import fi.spectrum.indexer.models.{DepositDB, RedeemDB, SwapDB, UpdateState}

trait OrderSchema[O, T] extends Schema[T] {

  final def updateRefunded(implicit lh: LogHandler, w: Write[O]): Update[O] =
    Update[O](s"update $tableName set refunded_transaction_id=?, refunded_transaction_timestamp=? where order_id=?")

  final def updateExecuted(implicit lh: LogHandler, w: Write[O]): Update[O] =
    Update[O](s"update $tableName set executed_transaction_id=?, executed_transaction_timestamp=? where order_id=?")
}

object OrderSchema {
  implicit val swapSchema: OrderSchema[UpdateState, SwapDB]       = new SwapSchema
  implicit val redeemSchema: OrderSchema[UpdateState, RedeemDB]   = new RedeemSchema
  implicit val depositSchema: OrderSchema[UpdateState, DepositDB] = new DepositSchema
}
