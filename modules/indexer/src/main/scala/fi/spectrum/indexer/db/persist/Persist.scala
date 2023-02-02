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
import fi.spectrum.indexer.db.models.{UpdateEvaluatedTx, UpdateLock, UpdateRefundedTx}
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

  def makeUpdatable[D[_]: LiftConnectionIO: FlatMap, O <: Order, E, B: Write](implicit
    elh: EmbeddableLogHandler[D],
    prism: Prism[Order, O],
    prism2: Prism[OrderEvaluation, E],
    toDB: ToDB[Processed[O], B],
    repository: Repository[B, OrderId, E]
  ): Persist[Processed.Any, D] =
    elh.embed(implicit __ => new LiveUpdatable[O, E, B].mapK(LiftConnectionIO[D].liftF))

  def makeNonUpdatable[D[_]: LiftConnectionIO: FlatMap, O, S, B: Write, T: Write](implicit
    elh: EmbeddableLogHandler[D],
    optional: Optional[O, S],
    lens: Lens[S, T],
    toDB: ToDB[S, B],
    repository: Repository[B, T, OrderEvaluation]
  ): Persist[O, D] =
    elh.embed(implicit __ => new LiveNonUpdatable[O, S, B, T].mapK(LiftConnectionIO[D].liftF))

  /** @tparam O - Order type itself e.g. Swap, Redeem, Deposit
    * @tparam E - Evaluation type e.g. SwapEvaluation, DepositEvaluation etc.
    * @tparam B - DB representation of order O
    */
  final private class LiveUpdatable[O <: Order, E, B: Write](implicit
    prism: Prism[Order, O],
    prism2: Prism[OrderEvaluation, E],
    toDB: ToDB[Processed[O], B],
    repository: Repository[B, OrderId, E],
    logHandler: LogHandler
  ) extends Persist[Processed.Any, ConnectionIO] {

    def insert(processed: Processed.Any): ConnectionIO[Int] =
      processed.wined
        .traverse { order =>
          order.state.status match {
            case OrderStatus.Registered => repository.insertNoConflict.toUpdate0(order.toDB).run
            case OrderStatus.Evaluated  => repository.updateExecuted(UpdateEvaluatedTx.fromProcessed(order))
            case OrderStatus.Refunded   => repository.updateRefunded(UpdateRefundedTx.fromProcessed(order))
            case OrderStatus.Withdraw   => repository.updateLock(UpdateLock.withdraw(order))
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
            case OrderStatus.Evaluated  => repository.deleteExecuted(order.order.id)
            case OrderStatus.Refunded   => repository.deleteRefunded(order.order.id)
            case OrderStatus.Withdraw   => repository.deleteLockUpdate(order.order.id)
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

  /** @tparam O - From what we gonna extract the model
    * @tparam S - Model to extract
    * @tparam B - DB representation of extracted model
    * @tparam T - model's id
    */

  final private class LiveNonUpdatable[O, S, B: Write, T: Write](implicit
    optional: Optional[O, S],
    lens: Lens[S, T],
    toDB: ToDB[S, B],
    repository: Repository[B, T, OrderEvaluation],
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
