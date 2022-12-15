package fi.spectrum.indexer.db.v2

import cats.data.NonEmptyList
import doobie.{Update => Up}
import doobie.util.log.LogHandler
import fi.spectrum.indexer.models.UpdateState

/** Describes the way to update order state with UpdateState for T entity
  */
trait Update[T] extends Insert[T] {

  def updateRefunded(update: NonEmptyList[UpdateState])(implicit lh: LogHandler): doobie.ConnectionIO[Int] =
    Up[UpdateState](
      s"update $tableName set refunded_transaction_id=?, refunded_transaction_timestamp=? where order_id=?"
    ).updateMany(update)

  def updateExecuted(update: NonEmptyList[UpdateState])(implicit lh: LogHandler): doobie.ConnectionIO[Int] =
    Up[UpdateState](
      s"update $tableName set executed_transaction_id=?, executed_transaction_timestamp=? where order_id=?"
    ).updateMany(update)
}
