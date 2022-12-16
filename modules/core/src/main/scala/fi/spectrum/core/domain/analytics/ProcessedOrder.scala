package fi.spectrum.core.domain.analytics

import derevo.circe.{decoder, encoder}
import derevo.derive
import fi.spectrum.core.domain.BoxId
import fi.spectrum.core.domain.order.{Order, OrderState}
import glass.classic.Prism
import glass.macros.POptics
import tofu.logging.derivation.loggable

@derive(encoder, decoder, loggable)
@POptics
final case class ProcessedOrder[O <: Order.Any](
  order: O,
  state: OrderState,
  evaluation: Option[OrderEvaluation],
  offChainFee: Option[OffChainFee],
  poolBoxId: Option[BoxId]
) {
  def widen[O1 <: Order.Any](o: O1): ProcessedOrder[O1] = this.copy(order = o)

  def wined[O1 <: Order.Any](implicit prism: Prism[O, O1]): Option[ProcessedOrder[O1]] =
    prism.getOption(order).map(widen)
}

object ProcessedOrder {
  type Any = ProcessedOrder[Order.Any]
}
