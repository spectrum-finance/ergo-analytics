package fi.spectrum.indexer.db.v2

import doobie.Update
import doobie.util.Write
import doobie.util.log.LogHandler
import fi.spectrum.core.domain.order.OrderId

/** Describes the way to delete T entity with I field
  */
trait Delete[T, I] extends Insert[T] {

  val field: String

  def delete(implicit lh: LogHandler, w: Write[I]): Update[I] =
    Update[I](s"delete from $tableName where field=?")

  def deleteExecuted(implicit lh: LogHandler): Update[OrderId] =
    Update[OrderId](
      s"update $tableName set executed_transaction_id=null, executed_transaction_timestamp=null where order_id=?"
    )

  def deleteRefund(implicit lh: LogHandler): Update[OrderId] =
    Update[OrderId](
      s"update $tableName set refunded_transaction_id=null, refunded_transaction_timestamp=null where order_id=?"
    )
}
