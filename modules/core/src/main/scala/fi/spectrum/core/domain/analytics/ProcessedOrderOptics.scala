package fi.spectrum.core.domain.analytics

import fi.spectrum.core.domain.order.Order._
import fi.spectrum.core.domain.order.OrderOptics._
import glass.{Contains, PContains, PProperty}
import glass.classic.{Lens, Optional}
import glass.macros.{GenContains, GenEquivalent}
import cats.syntax.either._
import fi.spectrum.core.domain.order.{OrderState, OrderStatus}

object ProcessedOrderOptics {

  implicit val stateStatusLens: Lens[ProcessedOrder.Any, OrderStatus] =
    Lens[ProcessedOrder.Any, OrderState] >> Lens[OrderState, OrderStatus]

  implicit val swapOptional: Optional[ProcessedOrder.Any, Swap] =
    GenContains[ProcessedOrder.Any](_.order) >> swapPrism

  implicit val redeemOptional: Optional[ProcessedOrder.Any, Redeem] =
    GenContains[ProcessedOrder.Any](_.order) >> redeemPrism

  implicit val depositOptional: Optional[ProcessedOrder.Any, Deposit] =
    GenContains[ProcessedOrder.Any](_.order) >> depositPrism

  implicit val lockOptional: Optional[ProcessedOrder.Any, Lock] =
    GenContains[ProcessedOrder.Any](_.order) >> lockPrism

  implicit val contains: Optional[ProcessedOrder.Any, OffChainFee] =
    new Optional[ProcessedOrder.Any, OffChainFee] {
      def set(s: ProcessedOrder.Any, b: OffChainFee): ProcessedOrder.Any = s.copy(offChainFee = Some(b))

      def narrow(s: ProcessedOrder.Any): Either[ProcessedOrder.Any, OffChainFee] =
        s.offChainFee match {
          case Some(value) => value.asRight
          case None        => s.asLeft
        }
    }

}
