package fi.spectrum.indexer.db.models

import fi.spectrum.core.domain.analytics.ProcessedOrder
import fi.spectrum.core.domain.order.{Order, OrderId}

final case class UpdateState(info: TxInfo, orderId: OrderId)

object UpdateState {

  def fromProcessed[O <: Order](processed: ProcessedOrder[O]): UpdateState =
    UpdateState(TxInfo(processed.state.txId, processed.state.timestamp), processed.order.id)
}
