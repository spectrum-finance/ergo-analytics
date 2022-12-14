package fi.spectrum.core.domain.analytics

import fi.spectrum.core.domain.order.Order
import fi.spectrum.core.domain.order.OrderOptics._
import glass.classic.Optional
import glass.macros.GenContains

object ProcessedOrderOptics {

  implicit val swapOptional: Optional[ProcessedOrder, Order.AnySwap] =
    GenContains[ProcessedOrder](_.order) >> swapPrism

  implicit val redeemOptional: Optional[ProcessedOrder, Order.AnyRedeem] =
    GenContains[ProcessedOrder](_.order)  >> redeemPrism

  implicit val depositOptional: Optional[ProcessedOrder, Order.AnyDeposit] =
    GenContains[ProcessedOrder](_.order)  >> depositPrism

  implicit val lockOptional: Optional[ProcessedOrder, Order.AnyLock] =
    GenContains[ProcessedOrder](_.order) >> lockPrism

}
