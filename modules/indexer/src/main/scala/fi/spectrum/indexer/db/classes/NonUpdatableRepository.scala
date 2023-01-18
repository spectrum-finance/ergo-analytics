package fi.spectrum.indexer.db.classes

import cats.syntax.applicative._
import doobie.ConnectionIO
import doobie.util.log
import fi.spectrum.core.domain.order
import fi.spectrum.indexer.db.models.UpdateState

/** Provides api for non updatable entities
  */
trait NonUpdatableRepository[T] extends UpdateRepository[T] {

  override def updateExecuted(update: UpdateState)(implicit lh: log.LogHandler): ConnectionIO[Int] =
    0.pure[ConnectionIO]

  override def updateRefunded(update: UpdateState)(implicit lh: log.LogHandler): ConnectionIO[Int] =
    0.pure[ConnectionIO]

  override def deleteExecuted(delete: order.OrderId)(implicit lh: log.LogHandler): ConnectionIO[Int] =
    0.pure[ConnectionIO]

  override def deleteRefunded(delete: order.OrderId)(implicit lh: log.LogHandler): ConnectionIO[Int] =
    0.pure[ConnectionIO]
}
