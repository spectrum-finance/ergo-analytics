package fi.spectrum.core.domain.analytics

import cats.Show
import derevo.circe.{decoder, encoder}
import derevo.derive
import fi.spectrum.core.domain.order.{Operation, Order, OrderState, OrderType}
import fi.spectrum.core.domain.pool.Pool
import tofu.logging.Loggable
import cats.syntax.show._
import glass.macros.{Optics, POptics}

@derive(encoder, decoder)
@Optics
final case class ProcessedOrder(
  order: Order[Version, OrderType, Operation],
  state: OrderState,
  evaluation: Option[OrderEvaluation],
  offChainFee: Option[OffChainOperatorFee],
  pool: Option[Pool.Any]
)

object ProcessedOrder {
  implicit val show: Show[ProcessedOrder] = processed =>
    s"ProcessedOrder(${processed.order.orderType}, ${processed.order.version})"

  implicit val loggable: Loggable[ProcessedOrder] = Loggable.show //todo derive loggbale
}
