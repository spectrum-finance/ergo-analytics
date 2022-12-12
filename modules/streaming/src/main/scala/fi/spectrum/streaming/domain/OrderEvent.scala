package fi.spectrum.streaming.domain

import fi.spectrum.core.domain.TxId
import fi.spectrum.core.domain.analytics.ProcessedOrder

sealed trait OrderEvent

object OrderEvent {
  final case class Apply(order: ProcessedOrder) extends OrderEvent
  final case class Unapply(tx: TxId) extends OrderEvent
}