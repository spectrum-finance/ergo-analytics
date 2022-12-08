package fi.spectrum.core.domain.analytics

import derevo.circe.{decoder, encoder}
import derevo.derive
import fi.spectrum.core.domain.order.{Operation, Order, OrderState, OrderType, Version}

@derive(encoder, decoder)
final case class ProcessedOrder(
  order: Order[Version, OrderType, Operation],
  state: OrderState,
  evaluation: Option[OrderEvaluation]
)

object ProcessedOrder {}
