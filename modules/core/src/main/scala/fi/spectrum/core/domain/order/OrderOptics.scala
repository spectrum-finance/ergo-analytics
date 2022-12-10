package fi.spectrum.core.domain.order

import fi.spectrum.core.domain.AssetAmount
import fi.spectrum.core.domain.analytics.Version
import fi.spectrum.core.domain.order.Order.Deposit._
import fi.spectrum.core.domain.order.Order.Swap._
import fi.spectrum.core.domain.order.Order._
import glass.classic.Optional
import glass.macros.{GenContains, GenSubset}

object OrderOptics {

  val depositPrism = GenSubset[Order.Any, Order.AnyDeposit]
  val swapPrism    = GenSubset[Order.Any, Order.AnySwap]

  implicit val optionalSwapQuote: Optional[Order.Any, AssetAmount] = {
    (swapPrism >> GenSubset[Order.AnySwap, SwapV1] >> GenContains[SwapV1](_.params.minQuote)) orElse
    (swapPrism >> GenSubset[Order.AnySwap, SwapV2] >> GenContains[SwapV2](_.params.minQuote)) orElse
    (swapPrism >> GenSubset[Order.AnySwap, SwapV3] >> GenContains[SwapV3](_.params.minQuote)) orElse
    (swapPrism >> GenSubset[Order.AnySwap, SwapLegacyV1] >> GenContains[SwapLegacyV1](_.params.minQuote))
  }

  implicit val optionalDepositParams: Optional[Order.Any, DepositParams] = {
    (depositPrism >> GenSubset[Order.AnyDeposit, DepositV1] >> GenContains[DepositV1](_.params)) orElse
    (depositPrism >> GenSubset[Order.AnyDeposit, DepositV3] >> GenContains[DepositV3](_.params)) orElse
    (depositPrism >> GenSubset[Order.AnyDeposit, DepositLegacyV1] >> GenContains[DepositLegacyV1](_.params)) orElse
    (depositPrism >> GenSubset[Order.AnyDeposit, DepositLegacyV2] >> GenContains[DepositLegacyV2](_.params))
  }

}
