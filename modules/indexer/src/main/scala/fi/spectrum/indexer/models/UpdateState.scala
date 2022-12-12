package fi.spectrum.indexer.models

import fi.spectrum.core.domain.analytics.ProcessedOrder
import fi.spectrum.core.domain.order.OrderId
import fi.spectrum.core.domain.order.OrderStatus.Executed
import fi.spectrum.indexer.classes.ToSchema
import cats.syntax.option._
final case class UpdateState(info: TxInfo, orderId: OrderId)

object UpdateState {

  implicit val __toSchema: ToSchema[ProcessedOrder, Option[UpdateState]] = { processed =>
    processed.state.status match {
      case Executed => UpdateState(TxInfo(processed.state.txId, processed.state.timestamp), processed.order.id).some
      case _        => none
    }
  }

}
