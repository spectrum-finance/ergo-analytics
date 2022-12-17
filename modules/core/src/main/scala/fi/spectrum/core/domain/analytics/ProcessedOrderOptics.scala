package fi.spectrum.core.domain.analytics

import fi.spectrum.core.domain.order.Order._
import fi.spectrum.core.domain.order.OrderOptics._
import glass.Contains
import glass.classic.Optional
import glass.macros.GenContains

object ProcessedOrderOptics {

  implicit val swapOptional: Optional[ProcessedOrder.Any, Swap] =
    GenContains[ProcessedOrder.Any](_.order) >> swapPrism

  implicit val redeemOptional: Optional[ProcessedOrder.Any, Redeem] =
    GenContains[ProcessedOrder.Any](_.order) >> redeemPrism

  implicit val depositOptional: Optional[ProcessedOrder.Any, Deposit] =
    GenContains[ProcessedOrder.Any](_.order) >> depositPrism

  implicit val lockOptional: Optional[ProcessedOrder.Any, Lock] =
    GenContains[ProcessedOrder.Any](_.order) >> lockPrism

  implicit val offChainFeeOptional: Contains[ProcessedOrder.Any, Option[OffChainFee]] =
    GenContains[ProcessedOrder.Any](_.offChainFee)

  implicit val r: Optional[ProcessedOrder.Any, OffChainFee] = implicitly[Optional[ProcessedOrder.Any, OffChainFee]]
}
