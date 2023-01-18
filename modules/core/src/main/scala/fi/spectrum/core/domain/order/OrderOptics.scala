package fi.spectrum.core.domain.order

import fi.spectrum.core.domain.AssetAmount
import fi.spectrum.core.domain.order.Order._
import fi.spectrum.core.domain.order.Order.Deposit._
import fi.spectrum.core.domain.order.Order.Lock._
import fi.spectrum.core.domain.order.Order.Redeem._
import fi.spectrum.core.domain.order.Order.Swap._
import glass.Contains
import glass.classic.{Lens, Optional}
import glass.macros.{GenContains, GenSubset}

object OrderOptics {

  val depositPrism = GenSubset[Order, Deposit]
  val swapPrism    = GenSubset[Order, Swap]
  val redeemPrism  = GenSubset[Order, Redeem]
  val lockPrism    = GenSubset[Order, Lock]

  val swapV1       = swapPrism >> GenSubset[Swap, SwapV1]
  val swapV2       = swapPrism >> GenSubset[Swap, SwapV2]
  val swapV3       = swapPrism >> GenSubset[Swap, SwapV3]
  val swapLegacyV1 = swapPrism >> GenSubset[Swap, SwapLegacyV1]

  val redeemV1       = redeemPrism >> GenSubset[Redeem, RedeemV1]
  val redeemV3       = redeemPrism >> GenSubset[Redeem, RedeemV3]
  val redeemLegacyV1 = redeemPrism >> GenSubset[Redeem, RedeemLegacyV1]

  implicit val optionalSwapQuote: Optional[Order, AssetAmount] = {
    (swapV1 >> GenContains[SwapV1](_.params.minQuote)) orElse
    (swapV2 >> GenContains[SwapV2](_.params.minQuote)) orElse
    (swapV3 >> GenContains[SwapV3](_.params.minQuote)) orElse
    (swapLegacyV1 >> GenContains[SwapLegacyV1](_.params.minQuote))
  }

  implicit val optionalDepositParams: Optional[Order, DepositParams] = {
    (depositPrism >> GenSubset[Deposit, DepositV1] >> GenContains[DepositV1](_.params)) orElse
    (depositPrism >> GenSubset[Deposit, DepositV3] >> GenContains[DepositV3](_.params)) orElse
    (depositPrism >> GenSubset[Deposit, DepositLegacyV1] >> GenContains[DepositLegacyV1](_.params)) orElse
    (depositPrism >> GenSubset[Deposit, DepositLegacyV2] >> GenContains[DepositLegacyV2](_.params))
  }

  implicit val lensOrderIdLock: Lens[LockV1, OrderId] = Contains[LockV1, OrderId]

  implicit val lensLockOrderId: Lens[Order.Lock, OrderId] =
    (GenSubset[Order.Lock, LockV1] >> lensOrderIdLock).unsafeTotal

}
