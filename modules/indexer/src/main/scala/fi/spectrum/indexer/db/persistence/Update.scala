package fi.spectrum.indexer.db.persistence

import cats.data.NonEmptyList
import cats.{FlatMap, Monad}
import doobie.util.log.LogHandler
import fi.spectrum.core.domain.TxId
import fi.spectrum.indexer.db.schema.UpdateSchema
import fi.spectrum.indexer.models.UpdateState
import tofu.doobie.LiftConnectionIO
import tofu.doobie.log.EmbeddableLogHandler
import tofu.higherKind.RepresentableK
import tofu.syntax.monadic._
import doobie.implicits._

/** Keeps api for entities which can be inserted or updated.
  */
trait Update[T, F[_]] extends Persist[T, F] {

  /** Updates order status to executed
    */
  def updateExecuted(o: UpdateState): F[Int]

  /** Updates order status to refunded
    */
  def updateRefunded(o: UpdateState): F[Int]

}

object Update {

  implicit def repK[T]: RepresentableK[Update[T, *[_]]] = {
    type Repr[F[_]] = Update[T, F]
    tofu.higherKind.derived.genRepresentableK[Repr]
  }

  def make[O, D[_]: FlatMap: LiftConnectionIO](persist: Persist[O, D])(implicit
    schema: UpdateSchema[UpdateState, O],
    elh: EmbeddableLogHandler[D]
  ): Update[O, D] =
    elh.embedLift(implicit __ => new Live[O, D](persist): Update[O, D])

  def makeNonUpdatable[O, D[_]: Monad](p: Persist[O, D]): Update[O, D] =
    new Update[O, D] {
      def updateExecuted(o: UpdateState): D[Int] = 0.pure[D]

      def updateRefunded(o: UpdateState): D[Int] = 0.pure[D]

      def persist(inputs: NonEmptyList[O]): D[Int] = p.persist(inputs)
    }

  final private class Live[O, D[_]: LiftConnectionIO](persist: Persist[O, D])(implicit
    schema: UpdateSchema[UpdateState, O],
    lh: LogHandler
  ) extends Update[O, D] {

    def updateExecuted(o: UpdateState): D[Int] =
      LiftConnectionIO[D].lift(schema.updateExecuted.toUpdate0(o).run)

    def updateRefunded(o: UpdateState): D[Int] =
      LiftConnectionIO[D].lift(schema.updateRefunded.toUpdate0(o).run)

    def persist(inputs: NonEmptyList[O]): D[Int] = persist.persist(inputs)
  }
}
