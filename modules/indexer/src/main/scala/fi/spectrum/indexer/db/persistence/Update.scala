package fi.spectrum.indexer.db.persistence

import cats.data.NonEmptyList
import cats.{Applicative, FlatMap}
import derevo.derive
import doobie.util.log.LogHandler
import fi.spectrum.indexer.db.schema.OrderSchema
import fi.spectrum.indexer.models.UpdateState
import mouse.all._
import tofu.doobie.LiftConnectionIO
import tofu.doobie.transactor.Txr
import tofu.higherKind.derived.representableK

/** Keeps both persist and update api. Persists api is used to insert orders with Register status.
  * Update methods is used to update appropriate order status.
  */
@derive(representableK)
trait Update[T, F[_]] extends Persist[T, F] {

  /** Updates order status to executed
    */
  def updateExecuted(o: NonEmptyList[UpdateState]): F[Int]

  /** Updates order status to refunded
    */
  def updateRefunded(o: NonEmptyList[UpdateState]): F[Int]
}

object Update {

  def make[O, D[_]: FlatMap: LiftConnectionIO, F[_]: Applicative](persist: Persist[O, F])(implicit
    schema: OrderSchema[UpdateState, O],
    txr: Txr[F, D]
  ): Update[O, F] =
    new Live[O, F, D](persist)

  final private class Live[O, F[_], D[_]: LiftConnectionIO](persist: Persist[O, F])(implicit
    schema: OrderSchema[UpdateState, O],
    txr: Txr[F, D]
  ) extends Update[O, F] {

    implicit val lh: LogHandler = LogHandler.nop

    def updateExecuted(o: NonEmptyList[UpdateState]): F[Int] =
      LiftConnectionIO[D].lift(schema.updateExecuted.updateMany(o)) ||> txr.trans

    def updateRefunded(o: NonEmptyList[UpdateState]): F[Int] =
      LiftConnectionIO[D].lift(schema.updateRefunded.updateMany(o)) ||> txr.trans

    def persist(inputs: NonEmptyList[O]): F[Int] = persist.persist(inputs)
  }
}
