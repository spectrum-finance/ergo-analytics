package fi.spectrum.indexer.db.classes

import doobie.util.log.LogHandler
import doobie.{ConnectionIO, Update}
import fi.spectrum.core.domain.order.OrderId
import fi.spectrum.indexer.db.models.UpdateState

/** Describes the way to update T order status
  */
trait UpdateRepository[T] extends InsertRepository[T] {

  final private val refunded: String   = "refunded_transaction_id"
  final private val refundedTs: String = "refunded_transaction_timestamp"
  final private val executed: String   = "executed_transaction_id"
  final private val executedTs: String = "executed_transaction_timestamp"

  def updateRefunded(update: UpdateState)(implicit lh: LogHandler): ConnectionIO[Int] =
    Update[UpdateState](s"update $tableName set $refunded=?, $refundedTs=? where order_id=?")
      .toUpdate0(update)
      .run

  def deleteRefunded(delete: OrderId)(implicit lh: LogHandler): ConnectionIO[Int] =
    Update[OrderId](s"update $tableName set $refunded=null, $refundedTs=null where order_id=?")
      .toUpdate0(delete)
      .run

  def updateExecuted(update: UpdateState)(implicit lh: LogHandler): ConnectionIO[Int] =
    Update[UpdateState](s"update $tableName set $executed=?, $executedTs=? where order_id=?")
      .toUpdate0(update)
      .run

  def deleteExecuted(delete: OrderId)(implicit lh: LogHandler): ConnectionIO[Int] =
    Update[OrderId](s"update $tableName set $executed=null, $executedTs=null where order_id=?")
      .toUpdate0(delete)
      .run

}
