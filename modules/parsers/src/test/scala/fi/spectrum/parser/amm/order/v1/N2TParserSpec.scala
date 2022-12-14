package fi.spectrum.parser.amm.order.v1

import cats.syntax.eq._
import fi.spectrum.core.domain.analytics.Version
import fi.spectrum.core.domain.order.Order
import fi.spectrum.core.domain.order.Order.Swap.SwapV1
import fi.spectrum.core.protocol.ErgoTreeSerializer
import fi.spectrum.parser.CatsPlatform
import fi.spectrum.parser.amm.order.AmmOrderParser
import fi.spectrum.parser.domain.AmmType
import org.scalatest.matchers.should.Matchers
import org.scalatest.propspec.AnyPropSpec

class N2TParserSpec extends AnyPropSpec with Matchers with CatsPlatform {

  val parser: AmmOrderParser[Version.V1, AmmType.N2T] = N2TAmmOrderParser.n2tV1

  property("Parse n2t swap buy v1 contract") {
    val box = N2T.swap.swapN2TBuy
    val swapResult: Order.AnySwap =
      parser.swap(box, ErgoTreeSerializer.default.deserialize(box.ergoTree)).get
    (swapResult eqv (N2T.swap.swapN2TBuyOrder: Order.AnySwap)) shouldBe true
  }

  property("Parse n2t swap sell v1 contract") {
    val box = N2T.swap.swapN2TSell
    val swapResult: Order.AnySwap =
      parser.swap(box, ErgoTreeSerializer.default.deserialize(box.ergoTree)).get.asInstanceOf[SwapV1]
    (swapResult eqv (N2T.swap.swapN2TSellOrder: Order.AnySwap)) shouldBe true
  }

  property("Parse n2t deposit v1 contract") {
    val box = N2T.deposit.depositN2T
    val depositResult: Order.AnyDeposit =
      parser.deposit(box, ErgoTreeSerializer.default.deserialize(box.ergoTree)).get
    val expected: Order.AnyDeposit = N2T.deposit.expectedN2TDepositV1
    (depositResult eqv expected) shouldBe true
  }

  property("Parse n2t redeem v1 contract") {
    val box = N2T.redeem.output
    val redeemResult: Order.AnyRedeem =
      parser.redeem(box, ErgoTreeSerializer.default.deserialize(box.ergoTree)).get
    val expected: Order.AnyRedeem = N2T.redeem.redeem
    (redeemResult eqv expected) shouldBe true
  }
}
