package fi.spectrum.parser.amm

import fi.spectrum.core.domain.analytics.Version
import fi.spectrum.core.domain.order.{Operation, Order, OrderType}
import fi.spectrum.core.domain.transaction.Output
import fi.spectrum.parser.amm.order.legacy.v1.{N2T => LV1N2T, T2T => LV1T2T}
import fi.spectrum.parser.amm.order.legacy.v2.{N2T => LV2N2T, T2T => LV2T2T}
import fi.spectrum.parser.amm.order.v1.{N2T => V1N2T, T2T => V1T2T}
import fi.spectrum.parser.amm.order.v2.{N2T => V2N2T, T2T => V2T2T}
import fi.spectrum.parser.amm.order.v3.{N2T => V3N2T, T2T => V3T2T}

package object order {
  val anyAmmOrder: List[(Output, Order[Version, OrderType.AMM, Operation])] = List(
    LV1N2T.swap.outputSell    -> LV1N2T.swap.sellOrder,
    LV1N2T.swap.outputBuy     -> LV1N2T.swap.buyOrder,
    LV1T2T.swap.output        -> LV1T2T.swap.order,
    V1N2T.swap.swapN2TSell    -> V1N2T.swap.swapN2TSellOrder,
    V1N2T.swap.swapN2TBuy     -> V1N2T.swap.swapN2TBuyOrder,
    V1T2T.swap.output         -> V1T2T.swap.order,
    V2N2T.swap.outputSell     -> V2N2T.swap.swapSell,
    V2N2T.swap.outputBuy      -> V2N2T.swap.swapBuy,
    V2T2T.swap.output         -> V2T2T.swap.swap,
    V3N2T.swap.outputBuyNoSpf -> V3N2T.swap.swapBuyNoSpf,
    V3N2T.swap.outputSellSpf  -> V3N2T.swap.swapSellSpf,
    V3T2T.swap.outputSpf      -> V3T2T.swap.swapSpf,
    LV1N2T.redeem.output      -> LV1N2T.redeem.redeem,
    LV1T2T.redeem.output      -> LV1T2T.redeem.redeem,
    V1N2T.redeem.output       -> V1N2T.redeem.redeem,
    V1T2T.redeem.output       -> V1T2T.redeem.order,
    V3N2T.redeem.output       -> V3N2T.redeem.order,
    V3T2T.redeem.output       -> V3T2T.redeem.order,
    LV2N2T.deposit.output     -> LV2N2T.deposit.deposit,
    LV2T2T.deposit.output     -> LV2T2T.deposit.deposit,
    V1N2T.deposit.depositN2T  -> V1N2T.deposit.expectedN2TDepositV1,
    V1T2T.deposit.depositT2T  -> V1T2T.deposit.expectedT2TDepositV1,
    V3T2T.deposit.outputSpf   -> V3T2T.deposit.depositSpf
  )
}
