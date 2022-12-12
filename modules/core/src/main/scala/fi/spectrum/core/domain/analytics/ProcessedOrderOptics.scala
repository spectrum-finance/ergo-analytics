package fi.spectrum.core.domain.analytics

import fi.spectrum.core.domain.order.Order
import glass.classic.{Lens, Optional}
import fi.spectrum.core.domain.order.OrderOptics._
import ProcessedOrder._
import fi.spectrum.core.domain.order.Order.Swap.SwapV1
import glass.Contains
import glass.macros.GenContains
object ProcessedOrderOptics {

  implicit val swapOptional: Optional[ProcessedOrder, Order.AnySwap] =
    GenContains[ProcessedOrder](_.order) >> swapPrism

  implicit val redeemOptional: Optional[ProcessedOrder, Order.AnyRedeem] =
    GenContains[ProcessedOrder](_.order)  >> redeemPrism

  implicit val depositOptional: Optional[ProcessedOrder, Order.AnyDeposit] =
    GenContains[ProcessedOrder](_.order)  >> depositPrism
}
