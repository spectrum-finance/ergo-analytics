package fi.spectrum.indexer.db.persist

import cats.FlatMap
import cats.data.NonEmptyList
import cats.tagless.ApplyK.ops.toAllApplyKOps
import doobie.ConnectionIO
import doobie.util.Write
import doobie.util.log.LogHandler
import fi.spectrum.core.domain.analytics.ProcessedOrder
import fi.spectrum.core.domain.order.OrderStatus.{Executed, Refunded, Registered}
import fi.spectrum.core.domain.order.{Order, OrderId}
import fi.spectrum.indexer.classes.ToDB
import fi.spectrum.indexer.classes.syntax._
import fi.spectrum.indexer.db.models.UpdateState
import fi.spectrum.indexer.db.repositories.Repository
import fi.spectrum.indexer.foldNel
import glass.classic.{Lens, Optional, Prism}
import tofu.doobie.LiftConnectionIO
import tofu.doobie.log.EmbeddableLogHandler
import tofu.higherKind.RepresentableK

/** Describes how to communicate with database
  */
trait Persist[R, F[_]] {
  def insert(nel: NonEmptyList[R]): F[Int]

  def resolve(nel: NonEmptyList[R]): F[Int]
}

object Persist {

  implicit def repK[R]: RepresentableK[Persist[R, *[_]]] = {
    type Repr[F[_]] = Persist[R, F]
    tofu.higherKind.derived.genRepresentableK[Repr]
  }

  def makeUpdatable[D[_]: LiftConnectionIO: FlatMap, O <: Order, B: Write](implicit
    elh: EmbeddableLogHandler[D],
    prism: Prism[Order, O],
    toDB: ToDB[ProcessedOrder[O], B],
    repository: Repository[B, OrderId]
  ): Persist[ProcessedOrder.Any, D] =
    elh.embed(implicit __ => new LiveUpdatable[O, B].mapK(LiftConnectionIO[D].liftF))

  def makeNonUpdatable[D[_]: LiftConnectionIO: FlatMap, O, S, B: Write, T: Write](implicit
    elh: EmbeddableLogHandler[D],
    optional: Optional[O, S],
    lens: Lens[S, T],
    toDB: ToDB[S, B],
    repository: Repository[B, T]
  ): Persist[O, D] =
    elh.embed(implicit __ => new LiveNonUpdatable[O, S, B, T].mapK(LiftConnectionIO[D].liftF))

  final private class LiveUpdatable[O <: Order, B: Write](implicit
    prism: Prism[Order, O],
    toDB: ToDB[ProcessedOrder[O], B],
    repository: Repository[B, OrderId],
    logHandler: LogHandler
  ) extends Persist[ProcessedOrder.Any, ConnectionIO] {

    def insert(nel: NonEmptyList[ProcessedOrder.Any]): ConnectionIO[Int] = {
      val (registered, executed, refunded) = {
        val (x, y, z) = accumulateByStatus(nel)
        (x.map(_.toDB), y.map(UpdateState.fromProcessed), z.map(UpdateState.fromProcessed))
      }

      for {
        x <- foldNel[ConnectionIO, Int, B](registered, repository.insertNoConflict.updateMany, 0)
        y <- foldNel[ConnectionIO, Int, UpdateState](executed, repository.updateExecuted, 0)
        z <- foldNel[ConnectionIO, Int, UpdateState](refunded, repository.updateRefunded, 0)
      } yield x + y + z
    }

    def resolve(nel: NonEmptyList[ProcessedOrder.Any]): ConnectionIO[Int] = {
      val (registered, executed, refunded) = {
        val (x, y, z) = accumulateByStatus(nel)
        (x.map(_.order.id), y.map(_.order.id), z.map(_.order.id))
      }

      for {
        x <- foldNel[ConnectionIO, Int, OrderId](registered, repository.delete.updateMany, 0)
        y <- foldNel[ConnectionIO, Int, OrderId](executed, repository.deleteExecuted, 0)
        z <- foldNel[ConnectionIO, Int, OrderId](refunded, repository.deleteRefunded, 0)
      } yield x + y + z

    }

    private def accumulateByStatus(
      nel: NonEmptyList[ProcessedOrder.Any]
    ): (List[ProcessedOrder[O]], List[ProcessedOrder[O]], List[ProcessedOrder[O]]) =
      nel.toList
        .flatMap(_.wined)
        .foldLeft(List.empty[ProcessedOrder[O]], List.empty[ProcessedOrder[O]], List.empty[ProcessedOrder[O]]) {
          case ((acc1, acc2, acc3), next) if next.state.status.in(Registered) => (next :: acc1, acc2, acc3)
          case ((acc1, acc2, acc3), next) if next.state.status.in(Executed)   => (acc1, next :: acc2, acc3)
          case ((acc1, acc2, acc3), next) if next.state.status.in(Refunded)   => (acc1, acc2, next :: acc3)
          case ((acc1, acc2, acc3), _)                                        => (acc1, acc2, acc3)
        }
  }

  final private class LiveNonUpdatable[O, S, B: Write, T: Write](implicit
    optional: Optional[O, S],
    lens: Lens[S, T],
    toDB: ToDB[S, B],
    repository: Repository[B, T],
    logHandler: LogHandler
  ) extends Persist[O, ConnectionIO] {

    def insert(nel: NonEmptyList[O]): ConnectionIO[Int] =
      foldNel[ConnectionIO, Int, B](
        nel
          .toList
          .flatMap(optional.getOption)
          .map(_.toDB),
        repository.insertNoConflict.updateMany,
        0
      )

    def resolve(nel: NonEmptyList[O]): ConnectionIO[Int] =
      foldNel[ConnectionIO, Int, T](
        nel.toList.flatMap(optional.getOption).map(lens.extract),
        repository.delete.updateMany,
        0
      )

  }
}
