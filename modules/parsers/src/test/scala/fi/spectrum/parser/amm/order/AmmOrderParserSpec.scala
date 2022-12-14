package fi.spectrum.parser.amm.order

import cats.implicits.{catsSyntaxEq, toFunctorOps}
import fi.spectrum.core.domain.analytics.Version
import fi.spectrum.core.domain.order.Order
import fi.spectrum.core.protocol.ErgoTreeSerializer.default._
import fi.spectrum.parser.CatsPlatform
import fi.spectrum.parser.amm.order.legacy.v1.N2TAmmOrderParser._
import fi.spectrum.parser.amm.order.legacy.v1.T2TAmmOrderParser._
import fi.spectrum.parser.amm.order.legacy.v1.{N2T => LV1N2T, T2T => LV1T2T}
import fi.spectrum.parser.amm.order.legacy.v2.{N2T => LV2N2T, T2T => LV2T2T}
import fi.spectrum.parser.amm.order.legacy.v2.N2TAmmOrderParser._
import fi.spectrum.parser.amm.order.legacy.v2.T2TAmmOrderParser._
import fi.spectrum.parser.amm.order.v1.N2TAmmOrderParser._
import fi.spectrum.parser.amm.order.v1.T2TAmmOrderParser._
import fi.spectrum.parser.amm.order.v1.{N2T => V1N2T, T2T => V1T2T}
import fi.spectrum.parser.amm.order.v2.N2TAmmOrderParser._
import fi.spectrum.parser.amm.order.v2.T2TAmmOrderParser._
import fi.spectrum.parser.amm.order.v2.{N2T => V2N2T, T2T => V2T2T}
import fi.spectrum.parser.amm.order.v3.N2TAmmOrderParser._
import fi.spectrum.parser.amm.order.v3.T2TAmmOrderParser._
import fi.spectrum.parser.amm.order.v3.{N2T => V3N2T, T2T => V3T2T}
import fi.spectrum.parser.domain.AmmType
import org.scalatest.matchers.should.Matchers
import org.scalatest.propspec.AnyPropSpec

class AmmOrderParserSpec extends AnyPropSpec with Matchers with CatsPlatform {

  val parser: AmmOrderParser[Version, AmmType] = AmmOrderParser.make

  property("Parse any amm order via AmmOrderParser") {
    anyAmmOrder.foreach { case (output, expectedOrder) =>
      (parser.order(output, deserialize(output.ergoTree)).widen[Order.Any].get shouldEqual expectedOrder)
    }
  }

  property("Parse any amm swap order via AmmOrderParser") {
    (parser
      .swap(LV1N2T.swap.outputSell, deserialize(LV1N2T.swap.outputSell.ergoTree))
      .widen[Order.AnySwap]
      .get shouldEqual (LV1N2T.swap.sellOrder: Order.AnySwap))

    (parser
      .swap(LV1N2T.swap.outputBuy, deserialize(LV1N2T.swap.outputBuy.ergoTree))
      .widen[Order.AnySwap]
      .get shouldEqual (LV1N2T.swap.buyOrder: Order.AnySwap))

    (parser
      .swap(LV1T2T.swap.output, deserialize(LV1T2T.swap.output.ergoTree))
      .widen[Order.AnySwap]
      .get shouldEqual (LV1T2T.swap.order: Order.AnySwap))

    (parser
      .swap(V1N2T.swap.swapN2TSell, deserialize(V1N2T.swap.swapN2TSell.ergoTree))
      .widen[Order.AnySwap]
      .get shouldEqual (V1N2T.swap.swapN2TSellOrder: Order.AnySwap))

    (parser
      .swap(V1N2T.swap.swapN2TBuy, deserialize(V1N2T.swap.swapN2TBuy.ergoTree))
      .widen[Order.AnySwap]
      .get shouldEqual (V1N2T.swap.swapN2TBuyOrder: Order.AnySwap))

    (parser
      .swap(V1T2T.swap.output, deserialize(V1T2T.swap.output.ergoTree))
      .widen[Order.AnySwap]
      .get shouldEqual (V1T2T.swap.order: Order.AnySwap))

    (parser
      .swap(V2N2T.swap.outputSell, deserialize(V2N2T.swap.outputSell.ergoTree))
      .widen[Order.AnySwap]
      .get shouldEqual (V2N2T.swap.swapSell: Order.AnySwap))

    (parser
      .swap(V2N2T.swap.outputBuy, deserialize(V2N2T.swap.outputBuy.ergoTree))
      .widen[Order.AnySwap]
      .get shouldEqual (V2N2T.swap.swapBuy: Order.AnySwap))

    (parser
      .swap(V2T2T.swap.output, deserialize(V2T2T.swap.output.ergoTree))
      .widen[Order.AnySwap]
      .get shouldEqual (V2T2T.swap.swap: Order.AnySwap))

    (parser
      .swap(V3N2T.swap.outputBuyNoSpf, deserialize(V3N2T.swap.outputBuyNoSpf.ergoTree))
      .widen[Order.AnySwap]
      .get shouldEqual (V3N2T.swap.swapBuyNoSpf: Order.AnySwap))

    (parser
      .swap(V3N2T.swap.outputSellSpf, deserialize(V3N2T.swap.outputSellSpf.ergoTree))
      .widen[Order.AnySwap]
      .get shouldEqual (V3N2T.swap.swapSellSpf: Order.AnySwap))

    (parser
      .swap(V3T2T.swap.outputSpf, deserialize(V3T2T.swap.outputSpf.ergoTree))
      .widen[Order.AnySwap]
      .get shouldEqual (V3T2T.swap.swapSpf: Order.AnySwap))
  }

  property("Parse any amm redeem order via AmmOrderParser") {
    (parser
      .redeem(LV1N2T.redeem.output, deserialize(LV1N2T.redeem.output.ergoTree))
      .widen[Order.AnyRedeem]
      .get shouldEqual (LV1N2T.redeem.redeem: Order.AnyRedeem))

    (parser
      .redeem(LV1T2T.redeem.output, deserialize(LV1T2T.redeem.output.ergoTree))
      .widen[Order.AnyRedeem]
      .get shouldEqual (LV1T2T.redeem.redeem: Order.AnyRedeem))

    (parser
      .redeem(V1N2T.redeem.output, deserialize(V1N2T.redeem.output.ergoTree))
      .widen[Order.AnyRedeem]
      .get shouldEqual (V1N2T.redeem.redeem: Order.AnyRedeem))

    (parser
      .redeem(V1T2T.redeem.output, deserialize(V1T2T.redeem.output.ergoTree))
      .widen[Order.AnyRedeem]
      .get shouldEqual (V1T2T.redeem.order: Order.AnyRedeem))

    (parser
      .redeem(V3N2T.redeem.output, deserialize(V3N2T.redeem.output.ergoTree))
      .widen[Order.AnyRedeem]
      .get shouldEqual (V3N2T.redeem.order: Order.AnyRedeem))

    (parser
      .redeem(V3T2T.redeem.output, deserialize(V3T2T.redeem.output.ergoTree))
      .widen[Order.AnyRedeem]
      .get shouldEqual (V3T2T.redeem.order: Order.AnyRedeem))
  }

  property("Parse any amm deposit order via AmmOrderParser") {
    (parser
      .deposit(LV2N2T.deposit.output, deserialize(LV2N2T.deposit.output.ergoTree))
      .widen[Order.AnyDeposit]
      .get shouldEqual (LV2N2T.deposit.deposit: Order.AnyDeposit))

    (parser
      .deposit(LV2T2T.deposit.output, deserialize(LV2T2T.deposit.output.ergoTree))
      .widen[Order.AnyDeposit]
      .get shouldEqual (LV2T2T.deposit.deposit: Order.AnyDeposit))

    (parser
      .deposit(V1N2T.deposit.depositN2T, deserialize(V1N2T.deposit.depositN2T.ergoTree))
      .widen[Order.AnyDeposit]
      .get shouldEqual (V1N2T.deposit.expectedN2TDepositV1: Order.AnyDeposit))

    (parser
      .deposit(V1T2T.deposit.depositT2T, deserialize(V1T2T.deposit.depositT2T.ergoTree))
      .widen[Order.AnyDeposit]
      .get shouldEqual (V1T2T.deposit.expectedT2TDepositV1: Order.AnyDeposit))

    (parser
      .deposit(V3N2T.deposit.outputSpfNotY, deserialize(V3N2T.deposit.outputSpfNotY.ergoTree))
      .widen[Order.AnyDeposit]
      .get shouldEqual (V3N2T.deposit.depositSpfNotY: Order.AnyDeposit))

    (parser
      .deposit(V3T2T.deposit.outputSpf, deserialize(V3T2T.deposit.outputSpf.ergoTree))
      .widen[Order.AnyDeposit]
      .get shouldEqual (V3T2T.deposit.depositSpf: Order.AnyDeposit))
  }

}
