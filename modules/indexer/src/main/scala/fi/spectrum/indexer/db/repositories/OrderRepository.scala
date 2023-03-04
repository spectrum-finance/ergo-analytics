package fi.spectrum.indexer.db.repositories

import doobie.ConnectionIO
import doobie.util.log.LogHandler
import fi.spectrum.core.domain.order.OrderId
import fi.spectrum.indexer.db.models.{UpdateEvaluatedTx, UpdateRefundedTx}

trait OrderRepository[T, E, I] extends Repository[T, I] {
  def updateRefunded(update: UpdateRefundedTx)(implicit lh: LogHandler): ConnectionIO[Int]

  def deleteRefunded(delete: OrderId)(implicit lh: LogHandler): ConnectionIO[Int]

  def updateExecuted(update: UpdateEvaluatedTx[E])(implicit lh: LogHandler): ConnectionIO[Int]

  def deleteExecuted(delete: OrderId)(implicit lh: LogHandler): ConnectionIO[Int]

}

object OrderRepository
