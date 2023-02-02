package fi.spectrum.core.domain.analytics

import cats.Show
import cats.syntax.option._
import derevo.circe.{decoder, encoder}
import derevo.derive
import fi.spectrum.core.domain.BoxId
import fi.spectrum.core.domain.order.{Order, OrderState}
import glass.classic.Prism
import glass.macros.ClassyOptics
import tofu.logging.derivation.loggable

@derive(encoder, decoder, loggable)
@ClassyOptics
final case class Processed[O <: Order](
  order: O,
  state: OrderState,
  evaluation: Option[OrderEvaluation],
  offChainFee: Option[OffChainFee],
  poolBoxId: Option[BoxId]
) {
  def widen[O1 <: Order](o: O1): Processed[O1] = this.copy(order = o)

  def wined[O1 <: Order](implicit prism: Prism[O, O1]): Option[Processed[O1]] =
    prism.getOption(order).map(widen)

  def widenOrder: Processed[Order] = this.copy[Order]()
}

object Processed {
  type Any = Processed[Order]

  def make(state: OrderState, order: Order): Processed[Order] = Processed(order, state, none, none, none)

  implicit val show: Show[Any] = p => s"Processed(${p.order.id}, ${p.order.box.boxId})"
}
