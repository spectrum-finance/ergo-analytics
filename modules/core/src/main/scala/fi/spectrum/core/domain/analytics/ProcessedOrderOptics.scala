package fi.spectrum.core.domain.analytics

import fi.spectrum.core.domain.order.Order._
import fi.spectrum.core.domain.order.OrderOptics._
import glass.{Contains, PContains, PProperty}
import glass.classic.{Lens, Optional}
import glass.macros.{GenContains, GenEquivalent}
import cats.syntax.either._
import fi.spectrum.core.domain.order.{OrderState, OrderStatus}

object ProcessedOrderOptics {

  implicit val stateStatusLens: Lens[Processed.Any, OrderStatus] =
    Lens[Processed.Any, OrderState] >> Lens[OrderState, OrderStatus]

  implicit val swapOptional: Optional[Processed.Any, Swap] =
    GenContains[Processed.Any](_.order) >> swapPrism

  implicit val redeemOptional: Optional[Processed.Any, Redeem] =
    GenContains[Processed.Any](_.order) >> redeemPrism

  implicit val depositOptional: Optional[Processed.Any, Deposit] =
    GenContains[Processed.Any](_.order) >> depositPrism

  implicit val lockOptional: Optional[Processed.Any, Lock] =
    GenContains[Processed.Any](_.order) >> lockPrism

  implicit val contains: Optional[Processed.Any, OffChainFee] =
    new Optional[Processed.Any, OffChainFee] {
      def set(s: Processed.Any, b: OffChainFee): Processed.Any = s.copy(offChainFee = Some(b))

      def narrow(s: Processed.Any): Either[Processed.Any, OffChainFee] =
        s.offChainFee match {
          case Some(value) => value.asRight
          case None        => s.asLeft
        }
    }

}
