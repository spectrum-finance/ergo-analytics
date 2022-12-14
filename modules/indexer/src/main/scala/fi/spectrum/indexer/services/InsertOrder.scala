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

trait InsertOrder[F[_]] {
  def insert(order: ProcessedOrder): F[Int]
}

object InsertOrder {

  def make[O <: Order.Any, B, D[_]: Applicative](update: Update[B, D])(implicit
    optional: Optional[ProcessedOrder, O],
    toSchema: ToSchema[ProcessedOrder, Option[B]]
  ): InsertOrder[D] = new Live[O, B, D](update)

  final private class Live[O <: Order.Any, B, D[_]: Applicative](
    update: Update[B, D]
  )(implicit
    optional: Optional[ProcessedOrder, O],
    toSchema: ToSchema[ProcessedOrder, Option[B]]
  ) extends InsertOrder[D] {

    def insert(processed: ProcessedOrder): D[Int] =
      optional
        .getOption(processed)
        .traverse { order =>
          def orderState = UpdateState(TxInfo(processed.state.txId, processed.state.timestamp), order.id)
          processed.state.status match {
            case Registered =>
              processed.transform.map(NonEmptyList.one) match {
                case Some(value) => update.persist(value)
                case None        => 0.pure
              }
            case Executed => update.updateExecuted(orderState)
            case Refunded => update.updateRefunded(orderState)
            case _        => 0.pure
          }
        }
        .map(_.getOrElse(0))
  }
}
