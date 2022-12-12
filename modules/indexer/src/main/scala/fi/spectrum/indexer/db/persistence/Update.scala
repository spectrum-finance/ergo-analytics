package fi.spectrum.indexer.db.persistence

import cats.data.NonEmptyList
import cats.{Applicative, FlatMap}
import derevo.derive
import doobie.util.log.LogHandler
import fi.spectrum.core.domain.analytics.ProcessedOrder
import fi.spectrum.core.domain.order.Order
import fi.spectrum.indexer.db.schema.OrderSchema
import fi.spectrum.indexer.models.UpdateState
import mouse.all._
import tofu.doobie.LiftConnectionIO
import tofu.doobie.log.EmbeddableLogHandler
import tofu.doobie.transactor.Txr
import tofu.higherKind.derived.representableK

@derive(representableK)
trait Update[T, F[_]] extends Persist[T, F] {
  def updateExecuted(o: NonEmptyList[UpdateState]): F[Int]

  def updateRefunded(o: NonEmptyList[UpdateState]): F[Int]
}

object Update {

  def make[O, D[_]: FlatMap: LiftConnectionIO, F[_]: Applicative](persist: Persist[O, F])(implicit
    schema: OrderSchema[UpdateState, O],
    elh: EmbeddableLogHandler[D],
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
