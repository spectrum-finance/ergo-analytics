package fi.spectrum.indexer.db.repositories

import doobie.util.log.LogHandler
import doobie.{ConnectionIO, Update}
import fi.spectrum.core.domain.order.OrderId
import fi.spectrum.indexer.db.classes.Repository
import fi.spectrum.indexer.db.models.{UpdateEvaluatedTx, UpdateRefundedTx}

trait OrderRepository[T, E, I] extends Repository[T, I] {
  val tableName: String
  val refunded: String   = "refunded_transaction_id"
  val refundedTs: String = "refunded_transaction_timestamp"
  val orderId: String    = "order_id"

  final def updateRefunded(update: UpdateRefundedTx)(implicit lh: LogHandler): ConnectionIO[Int] =
    Update[UpdateRefundedTx](s"update $tableName set $refunded=?, $refundedTs=? where $orderId=?")
      .toUpdate0(update)
      .run

  final def deleteRefunded(delete: OrderId)(implicit lh: LogHandler): ConnectionIO[Int] =
    Update[OrderId](s"update $tableName set $refunded=null, $refundedTs=null where $orderId=?")
      .toUpdate0(delete)
      .run

  def updateExecuted(update: UpdateEvaluatedTx[E])(implicit lh: LogHandler): ConnectionIO[Int]

  def deleteExecuted(delete: OrderId)(implicit lh: LogHandler): ConnectionIO[Int]

}

object OrderRepository
