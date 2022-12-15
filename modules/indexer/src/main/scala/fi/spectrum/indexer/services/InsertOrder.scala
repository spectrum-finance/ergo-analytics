package fi.spectrum.indexer.services

import cats.Monad
import cats.data.NonEmptyList
import fi.spectrum.core.domain.analytics.ProcessedOrder
import fi.spectrum.core.domain.order.{Operation, Order}
import fi.spectrum.core.domain.order.OrderStatus.{Executed, Refunded, Registered}
import fi.spectrum.indexer.classes.ToSchema
import fi.spectrum.indexer.classes.syntax._
import fi.spectrum.indexer.db.persistence.Update
import tofu.syntax.monadic._

trait InsertOrder[F[_]] {
  def insert(orders: List[ProcessedOrder]): F[Int]
}

object InsertOrder {

  def make[O <: Order.Any, B, D[_]: Monad](operation: Operation,update: Update[B, D])(implicit
    toSchema: ToSchema[ProcessedOrder, Option[B]]
  ): InsertOrder[D] = new Live[O, B, D](operation, update)

  final private class Live[O <: Order.Any, B, D[_]: Monad](
    operation: Operation,
    update: Update[B, D]
  )(implicit toSchema: ToSchema[ProcessedOrder, Option[B]])
    extends InsertOrder[D] {

    def insert(orders: List[ProcessedOrder]): D[Int] = {
      val (registered, executed, refunded) =
        orders.foldLeft((List.empty[ProcessedOrder], List.empty[ProcessedOrder], List.empty[ProcessedOrder])) {
          case ((acc1, acc2, acc3), s @ ProcessedOrder(order, state, _, _, _))
              if state.status.in(Registered) && operation == order.orderOperation =>
            (s :: acc1, acc2, acc3)
          case ((acc1, acc2, acc3), s @ ProcessedOrder(order, state, _, _, _))
              if state.status.in(Executed) && operation == order.orderOperation =>
            (acc1, s :: acc2, acc3)
          case ((acc1, acc2, acc3), s @ ProcessedOrder(order, state, _, _, _))
              if state.status.in(Refunded) && operation == order.orderOperation =>
            (acc1, acc2, s :: acc3)
          case (acc, _) => acc
        }

      for {
        x1 <- NonEmptyList.fromList(registered.map(_.transform).flatten).fold(0.pure)(update.persist)
        x2 <- NonEmptyList.fromList(executed.map(_.transform).flatten).fold(0.pure)(update.persist)
        x3 <- NonEmptyList.fromList(refunded.map(_.transform).flatten).fold(0.pure)(update.persist)
      } yield x1 + x2 + x3
    }
  }
}
