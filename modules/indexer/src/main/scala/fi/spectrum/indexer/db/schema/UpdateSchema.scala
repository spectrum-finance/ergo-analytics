package fi.spectrum.indexer.db.schema

import doobie.Update
import doobie.util.Write
import doobie.util.log.LogHandler
import fi.spectrum.indexer.models.{DepositDB, LockDB, RedeemDB, SwapDB, UpdateState}

/**
 * Extends schema with possibility to update O data for T entity
 * @tparam O - data with information for update
 * @tparam T - entity to be updated
 */
trait UpdateSchema[O, T] extends Schema[T] {

  final def updateRefunded(implicit lh: LogHandler, w: Write[O]): Update[O] =
    Update[O](s"update $tableName set refunded_transaction_id=?, refunded_transaction_timestamp=? where order_id=?")

  final def updateExecuted(implicit lh: LogHandler, w: Write[O]): Update[O] =
    Update[O](s"update $tableName set executed_transaction_id=?, executed_transaction_timestamp=? where order_id=?")
}

object UpdateSchema {
  implicit val swapSchema: UpdateSchema[UpdateState, SwapDB]       = new SwapSchema
  implicit val redeemSchema: UpdateSchema[UpdateState, RedeemDB]   = new RedeemSchema
  implicit val depositSchema: UpdateSchema[UpdateState, DepositDB] = new DepositSchema
}
