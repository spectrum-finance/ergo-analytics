package fi.spectrum.indexer.db.classes

import cats.data.NonEmptyList
import cats.syntax.applicative._
import doobie.ConnectionIO
import doobie.util.log
import fi.spectrum.core.domain.order
import fi.spectrum.indexer.models.UpdateState

/** Provides api for non updatable entities
  */
trait NonUpdatableRepository[T] extends UpdateRepository[T] {

  override def updateExecuted(update: NonEmptyList[UpdateState])(implicit lh: log.LogHandler): ConnectionIO[Int] =
    0.pure[ConnectionIO]

  override def updateRefunded(update: NonEmptyList[UpdateState])(implicit lh: log.LogHandler): ConnectionIO[Int] =
    0.pure[ConnectionIO]

  override def deleteExecuted(delete: NonEmptyList[order.OrderId])(implicit lh: log.LogHandler): ConnectionIO[Int] =
    0.pure[ConnectionIO]

  override def deleteRefunded(delete: NonEmptyList[order.OrderId])(implicit lh: log.LogHandler): ConnectionIO[Int] =
    0.pure[ConnectionIO]
}
