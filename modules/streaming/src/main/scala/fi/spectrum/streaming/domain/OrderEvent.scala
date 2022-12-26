package fi.spectrum.streaming.domain

import derevo.circe.{decoder, encoder}
import derevo.derive
import fi.spectrum.core.domain.analytics.ProcessedOrder
import tofu.logging.derivation.loggable

@derive(loggable, encoder, decoder)
sealed trait OrderEvent {
  val order: ProcessedOrder.Any
}

object OrderEvent {

  @derive(loggable, encoder, decoder)
  final case class Apply(order: ProcessedOrder.Any) extends OrderEvent

  @derive(loggable, encoder, decoder)
  final case class Unapply(order: ProcessedOrder.Any) extends OrderEvent
}
