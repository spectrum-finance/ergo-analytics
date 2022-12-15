package fi.spectrum.indexer.db.v2

import cats.FlatMap
import cats.data.NonEmptyList
import cats.tagless.implicits.toFunctorKOps
import doobie.ConnectionIO
import doobie.util.Write
import doobie.util.log.LogHandler
import fi.spectrum.indexer.classes.ToSchema
import fi.spectrum.indexer.models.UpdateState
import tofu.doobie.LiftConnectionIO
import tofu.doobie.log.EmbeddableLogHandler
import tofu.higherKind.RepresentableK
import fi.spectrum.indexer.classes.syntax._
import cats.syntax.applicative._
import fi.spectrum.core.domain.order.OrderId

/** Transforms data in DB entity and tries to insert it somehow in DB.
  * @tparam Insert - data to insert
  * @tparam Delete - data to delete
  */
trait Persist[Insert, Delete, F[_]] {
  def insert(insert: List[Insert]): F[Int]

  def updateRefunded(update: List[UpdateState]): F[Int]

  def updateExecuted(update: List[UpdateState]): F[Int]

  def delete(delete: List[Delete]): F[Int]

  def deleteRefunded(delete: List[OrderId]): F[Int]

  def deleteExecuted(delete: List[OrderId]): F[Int]
}

object Persist {

  implicit def repK[E, D]: RepresentableK[Persist[E, D, *[_]]] = {
    type Repr[F[_]] = Persist[E, D, F]
    tofu.higherKind.derived.genRepresentableK[Repr]
  }

  def make[D[_]: LiftConnectionIO: FlatMap, DB: Write, Update: Write](implicit
    repository: Repository[DB, Update],
    elh: EmbeddableLogHandler[D]
  ): Persist[DB, Update, D] =
    elh.embed(implicit __ => new Live[DB, Update].mapK(LiftConnectionIO[D].liftF))

  final private class Live[DB: Write, Update: Write](implicit
    repository: Repository[DB, Update],
    lh: LogHandler
  ) extends Persist[DB, Update, ConnectionIO] {

    def insert(insert: List[DB]): ConnectionIO[Int] =
      NonEmptyList.fromList(insert) match {
        case Some(value) => repository.insertNoConflict.updateMany(value)
        case None        => 0.pure[ConnectionIO]
      }

    def updateRefunded(update: List[UpdateState]): ConnectionIO[Int] =
      NonEmptyList.fromList(update) match {
        case Some(value) => repository.updateRefunded(value)
        case None        => 0.pure[ConnectionIO]
      }

    def updateExecuted(update: List[UpdateState]): ConnectionIO[Int] =
      NonEmptyList.fromList(update) match {
        case Some(value) => repository.updateExecuted(value)
        case None        => 0.pure[ConnectionIO]
      }

    def delete(delete: List[Update]): ConnectionIO[Int] = repository.delete.updateMany(delete)

    def deleteRefunded(delete: List[OrderId]): ConnectionIO[Int] = repository.deleteRefund.updateMany(delete)

    def deleteExecuted(delete: List[OrderId]): ConnectionIO[Int] = repository.deleteExecuted.updateMany(delete)

  }
}
