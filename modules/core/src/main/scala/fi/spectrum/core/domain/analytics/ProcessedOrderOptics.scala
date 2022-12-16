package fi.spectrum.core.domain.analytics

import fi.spectrum.core.domain.order.Order
import fi.spectrum.core.domain.order.OrderOptics._
import glass.Contains
import glass.classic.{Lens, Optional}
import glass.macros.{GenContains, GenSubset}

object ProcessedOrderOptics {

  implicit val swapOptional: Optional[ProcessedOrder.Any, Order.AnySwap] =
    GenContains[ProcessedOrder.Any](_.order) >> swapPrism

  implicit val redeemOptional: Optional[ProcessedOrder.Any, Order.AnyRedeem] =
    GenContains[ProcessedOrder.Any](_.order) >> redeemPrism

  implicit val depositOptional: Optional[ProcessedOrder.Any, Order.AnyDeposit] =
    GenContains[ProcessedOrder.Any](_.order) >> depositPrism

  implicit val lockOptional: Optional[ProcessedOrder.Any, Order.AnyLock] =
    GenContains[ProcessedOrder.Any](_.order) >> lockPrism

  implicit val offChainFeeOptional: Contains[ProcessedOrder.Any, Option[OffChainFee]] =
    GenContains[ProcessedOrder.Any](_.offChainFee)

  implicit val r: Optional[ProcessedOrder.Any, OffChainFee] = implicitly[Optional[ProcessedOrder.Any, OffChainFee]]
}
