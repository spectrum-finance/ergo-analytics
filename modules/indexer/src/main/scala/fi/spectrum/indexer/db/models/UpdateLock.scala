package fi.spectrum.indexer.db.models

import fi.spectrum.core.domain.TxId
import fi.spectrum.core.domain.analytics.OrderEvaluation.LockEvaluation
import fi.spectrum.core.domain.analytics.Processed
import fi.spectrum.core.domain.order.{Order, OrderId, OrderStatus}

final case class UpdateLock(txId: TxId, status: String, orderId: OrderId)

object UpdateLock {

  def withdraw[O <: Order](order: Processed[O]): UpdateLock =
    UpdateLock(order.state.txId, order.state.status.entryName, order.order.id)

  def reLock[O <: Order](order: Processed[O], eval: LockEvaluation): UpdateLock =
    UpdateLock(order.state.txId, OrderStatus.ReLock.entryName, eval.orderId)
}
