package fi.spectrum.indexer.db.classes

import doobie.util.log.LogHandler
import doobie.{ConnectionIO, Update}
import cats.syntax.applicative._
import fi.spectrum.core.domain.order.OrderId
import fi.spectrum.indexer.db.models.{UpdateLock, UpdateRefundedTx}

/** Describes the way to update T order status
  */
trait RefundRepository[T] extends InsertRepository[T] {

  val refunded: String   = "refunded_transaction_id"
  val refundedTs: String = "refunded_transaction_timestamp"
  val orderId: String    = "order_id"

  def updateRefunded(update: UpdateRefundedTx)(implicit lh: LogHandler): ConnectionIO[Int] =
    Update[UpdateRefundedTx](s"update $tableName set $refunded=?, $refundedTs=? where $orderId=?")
      .toUpdate0(update)
      .run

  def deleteRefunded(delete: OrderId)(implicit lh: LogHandler): ConnectionIO[Int] =
    Update[OrderId](s"update $tableName set $refunded=null, $refundedTs=null where $orderId=?")
      .toUpdate0(delete)
      .run
}
