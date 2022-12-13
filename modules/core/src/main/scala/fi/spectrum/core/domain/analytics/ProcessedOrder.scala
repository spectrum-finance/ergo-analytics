package fi.spectrum.core.domain.analytics

import derevo.circe.{decoder, encoder}
import derevo.derive
import fi.spectrum.core.domain.order.{Operation, Order, OrderState, OrderType}
import fi.spectrum.core.domain.pool.Pool
import glass.macros.Optics
import tofu.logging.derivation.loggable

@derive(encoder, decoder, loggable)
@Optics
final case class ProcessedOrder(
  order: Order[Version, OrderType, Operation],
  state: OrderState,
  evaluation: Option[OrderEvaluation],
  offChainFee: Option[OffChainOperatorFee],
  pool: Option[Pool.Any]
)

object ProcessedOrder
