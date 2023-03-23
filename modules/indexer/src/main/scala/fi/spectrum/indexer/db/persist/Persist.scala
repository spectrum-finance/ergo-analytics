package fi.spectrum.indexer.db.persist

import cats.FlatMap
import tofu.syntax.monadic._
import cats.syntax.traverse._
import cats.tagless.ApplyK.ops.toAllApplyKOps
import doobie.ConnectionIO
import doobie.util.Write
import doobie.util.log.LogHandler
import fi.spectrum.core.domain.analytics.OrderEvaluation.LockEvaluation
import fi.spectrum.core.domain.analytics.{OrderEvaluation, Processed}
import fi.spectrum.core.domain.order.{Order, OrderId, OrderStatus}
import fi.spectrum.indexer.classes.ToDB
import fi.spectrum.indexer.classes.syntax._
import fi.spectrum.indexer.db.classes.Repository
import fi.spectrum.indexer.db.models.{LockDB, UpdateEvaluatedTx, UpdateLock, UpdateRefundedTx}
import fi.spectrum.indexer.db.repositories.{LockRepository, OrderRepository}
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

  def makeOrderRepo[D[_]: LiftConnectionIO: FlatMap, O <: Order, E, B: Write](implicit
    elh: EmbeddableLogHandler[D],
    prism: Prism[Order, O],
    prism2: Prism[OrderEvaluation, E],
    toDB: ToDB[Processed[O], B],
    repository: OrderRepository[B, E, OrderId]
  ): Persist[Processed.Any, D] =
    elh.embed(implicit __ => new LiveOrderRepository[O, E, B].mapK(LiftConnectionIO[D].liftF))

  def makeLockRepo[D[_]: LiftConnectionIO: FlatMap, O <: Order](implicit
    elh: EmbeddableLogHandler[D],
    prism: Prism[Order, O],
    toDB: ToDB[Processed[O], LockDB],
    repository: LockRepository
  ): Persist[Processed.Any, D] =
    elh.embed(implicit __ => new LiveLockRepository[O].mapK(LiftConnectionIO[D].liftF))

  def makeRepo[D[_]: LiftConnectionIO: FlatMap, O, S, B: Write, T: Write](implicit
    elh: EmbeddableLogHandler[D],
    optional: Optional[O, S],
    lens: Lens[S, T],
    toDB: ToDB[S, B],
    repository: Repository[B, T]
  ): Persist[O, D] =
    elh.embed(implicit __ => new LiveRepo[O, S, B, T].mapK(LiftConnectionIO[D].liftF))

  /** @tparam O - Order type itself e.g. Swap, Redeem, Deposit
    * @tparam E - Evaluation type e.g. SwapEvaluation, DepositEvaluation etc.
    * @tparam B - DB representation of order O
    */
  final private class LiveOrderRepository[O <: Order, E, B: Write](implicit
    prism: Prism[Order, O],
    prism2: Prism[OrderEvaluation, E],
    toDB: ToDB[Processed[O], B],
    repository: OrderRepository[B, E, OrderId],
    logHandler: LogHandler
  ) extends Persist[Processed.Any, ConnectionIO] {

    def insert(processed: Processed.Any): ConnectionIO[Int] =
      processed.wined
        .traverse { order =>
          order.state.status match {
            case OrderStatus.Registered => repository.insertNoConflict.toUpdate0(order.toDB).run
            case OrderStatus.Evaluated  => repository.updateExecuted(UpdateEvaluatedTx.fromProcessed(order))
            case OrderStatus.Refunded   => repository.updateRefunded(UpdateRefundedTx.fromProcessed(order))
            case _                      => 0.pure[ConnectionIO]
          }
        }
        .map(_.getOrElse(0))

    def resolve(processed: Processed.Any): ConnectionIO[Int] =
      processed.wined
        .traverse { order =>
          order.state.status match {
            case OrderStatus.Registered => repository.delete.toUpdate0(order.order.id).run
            case OrderStatus.Evaluated  => repository.deleteExecuted(order.order.id)
            case OrderStatus.Refunded   => repository.deleteRefunded(order.order.id)
            case _                      => 0.pure[ConnectionIO]
          }
        }
        .map(_.getOrElse(0))
  }

  /** @tparam O - From what we gonna extract the model
    * @tparam S - Model to extract
    * @tparam B - DB representation of extracted model
    * @tparam T - model's id
    */

  final private class LiveRepo[O, S, B: Write, T: Write](implicit
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

  final private class LiveLockRepository[O <: Order](implicit
    prism: Prism[Order, O],
    toDB: ToDB[Processed[O], LockDB],
    repository: LockRepository,
    logHandler: LogHandler
  ) extends Persist[Processed.Any, ConnectionIO] {

    def insert(processed: Processed.Any): ConnectionIO[Int] =
      processed.wined
        .traverse { order =>
          order.state.status match {
            case OrderStatus.Registered => repository.insertNoConflict.toUpdate0(order.toDB).run
            case OrderStatus.Withdraw => repository.updateLock(UpdateLock.withdraw(order))
            case OrderStatus.ReLock =>
              for {
                update <- order.evaluation
                            .flatMap(_.widen[LockEvaluation])
                            .traverse { eval =>
                              repository.updateLock(UpdateLock.reLock(order, eval))
                            }
                            .map(_.getOrElse(0))
                insert <- repository.insertNoConflict.toUpdate0(order.toDB).run
              } yield update + insert
            case _ => 0.pure[ConnectionIO]
          }
        }
        .map(_.getOrElse(0))

    def resolve(processed: Processed.Any): ConnectionIO[Int] =
      processed.wined
        .traverse { order =>
          order.state.status match {
            case OrderStatus.Registered => repository.delete.toUpdate0(order.order.id).run
            case OrderStatus.Withdraw => repository.deleteLockUpdate(order.order.id)
            case OrderStatus.ReLock =>
              for {
                deleteUpdate <- order.evaluation
                                  .flatMap(_.widen[LockEvaluation])
                                  .traverse { eval =>
                                    repository.deleteLockUpdate(eval.orderId)
                                  }
                                  .map(_.getOrElse(0))
                deleteLock <- repository.delete.toUpdate0(order.order.id).run
              } yield deleteUpdate + deleteLock
            case _ => 0.pure[ConnectionIO]
          }
        }
        .map(_.getOrElse(0))

  }

}
