package fi.spectrum.indexer.services

import cats.Applicative
import cats.data.NonEmptyList
import cats.syntax.traverse._
import fi.spectrum.core.domain.analytics.ProcessedOrder
import fi.spectrum.core.domain.order.Order
import fi.spectrum.core.domain.order.OrderStatus.{Executed, Refunded, Registered}
import fi.spectrum.indexer.classes.ToSchema
import fi.spectrum.indexer.classes.syntax._
import fi.spectrum.indexer.db.persistence.Update
import fi.spectrum.indexer.models.{TxInfo, UpdateState}
import glass.classic.Optional
import tofu.syntax.monadic._

trait ProcessedOrderHandler[F[_]] {
  def handle(order: ProcessedOrder): F[Unit]
}

object ProcessedOrderHandler {

  def make[O <: Order.Any, B, F[_]: Applicative](update: Update[B, F])(implicit
    optional: Optional[ProcessedOrder, O],
    toSchema: ToSchema[ProcessedOrder, Option[B]]
  ): ProcessedOrderHandler[F] = new Live[O, B, F](update)

  final private class Live[O <: Order.Any, B, F[_]: Applicative](update: Update[B, F])(implicit
    optional: Optional[ProcessedOrder, O],
    toSchema: ToSchema[ProcessedOrder, Option[B]]
  ) extends ProcessedOrderHandler[F] {

    def handle(processed: ProcessedOrder): F[Unit] =
      optional
        .getOption(processed)
        .traverse { order =>
          def orderState = UpdateState(TxInfo(processed.state.txId, processed.state.timestamp), order.id)
          processed.state.status match {
            case Registered => processed.transform.fold(unit)(o => update.persist(NonEmptyList.one(o)).void)
            case Executed   => update.updateExecuted(NonEmptyList.one(orderState)).void
            case Refunded   => update.updateRefunded(NonEmptyList.one(orderState)).void
            case _          => unit
          }
        }
        .void
  }
}
