package fi.spectrum.indexer.db.models

import fi.spectrum.core.domain.analytics.Processed
import fi.spectrum.core.domain.order.{Order, OrderId}

final case class UpdateRefundedTx(info: TxInfo, orderId: OrderId)

object UpdateRefundedTx {

  def fromProcessed[O <: Order](processed: Processed[O]): UpdateRefundedTx =
    UpdateRefundedTx(
      TxInfo(processed.state.txId, processed.state.timestamp),
      processed.order.id
    )
}
