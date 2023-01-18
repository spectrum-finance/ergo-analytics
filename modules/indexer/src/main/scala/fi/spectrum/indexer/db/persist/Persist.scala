package fi.spectrum.indexer.db.persist

import cats.FlatMap
import cats.syntax.applicative._
import cats.syntax.traverse._
import cats.tagless.ApplyK.ops.toAllApplyKOps
import doobie.ConnectionIO
import doobie.util.Write
import doobie.util.log.LogHandler
import fi.spectrum.core.domain.analytics.ProcessedOrder
import fi.spectrum.core.domain.order.{Order, OrderId, OrderStatus}
import fi.spectrum.indexer.classes.ToDB
import fi.spectrum.indexer.classes.syntax._
import fi.spectrum.indexer.db.models.UpdateState
import fi.spectrum.indexer.db.repositories.Repository
import glass.classic.{Lens, Optional, Prism}
import tofu.doobie.LiftConnectionIO
import tofu.doobie.log.EmbeddableLogHandler
import tofu.higherKind.RepresentableK

/** Describes how to communicate with database
  */
trait Persist[R, F[_]] {
  def insert(r: R): F[Int]

  def resolve(r: R): F[Int]
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

    def insert(processed: ProcessedOrder.Any): ConnectionIO[Int] =
      processed.wined
        .traverse { order =>
          order.state.status match {
            case OrderStatus.Registered => repository.insertNoConflict.toUpdate0(order.toDB).run
            case OrderStatus.Executed   => repository.updateExecuted(UpdateState.fromProcessed(order))
            case OrderStatus.Refunded   => repository.updateRefunded(UpdateState.fromProcessed(order))
            case _                      => 0.pure[ConnectionIO]
          }
        }
        .map(_.getOrElse(0))

    def resolve(processed: ProcessedOrder.Any): ConnectionIO[Int] =
      processed.wined
        .traverse { order =>
          order.state.status match {
            case OrderStatus.Registered => repository.delete.toUpdate0(order.order.id).run
            case OrderStatus.Executed   => repository.deleteExecuted(order.order.id)
            case OrderStatus.Refunded   => repository.deleteRefunded(order.order.id)
            case _                      => 0.pure[ConnectionIO]
          }
        }
        .map(_.getOrElse(0))
  }

  final private class LiveNonUpdatable[O, S, B: Write, T: Write](implicit
    optional: Optional[O, S],
    lens: Lens[S, T],
    toDB: ToDB[S, B],
    repository: Repository[B, T],
    logHandler: LogHandler
  ) extends Persist[O, ConnectionIO] {

    def insert(o: O): ConnectionIO[Int] =
      optional.getOption(o).map(_.toDB) match {
        case Some(value) => repository.insertNoConflict.toUpdate0(value).run
        case None        => 0.pure[ConnectionIO]
      }

    def resolve(o: O): ConnectionIO[Int] =
      optional.getOption(o).map(lens.extract) match {
        case Some(value) => repository.delete.toUpdate0(value).run
        case None        => 0.pure[ConnectionIO]
      }

  }
}
