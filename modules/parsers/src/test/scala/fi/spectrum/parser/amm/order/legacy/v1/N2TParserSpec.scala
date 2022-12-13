package fi.spectrum.parser.amm.order.legacy.v1

import cats.implicits.catsSyntaxEq
import fi.spectrum.core.domain.analytics.Version
import fi.spectrum.core.domain.order.Order
import fi.spectrum.core.protocol.ErgoTreeSerializer
import fi.spectrum.parser.CatsPlatform
import fi.spectrum.parser.amm.order.AmmOrderParser
import fi.spectrum.parser.domain.AmmType
import org.scalatest.matchers.should.Matchers
import org.scalatest.propspec.AnyPropSpec

class N2TParserSpec extends AnyPropSpec with Matchers with CatsPlatform {

  val parser: AmmOrderParser[Version.LegacyV1, AmmType.N2T] = N2TAmmOrderParser.n2tLegacyV1

  property("Parse n2t swap buy legacy v1 contract") {
    val box = N2T.swap.outputBuy
    val swapResult: Order.AnySwap =
      parser.swap(box, ErgoTreeSerializer.default.deserialize(box.ergoTree)).get
    (swapResult eqv (N2T.swap.buyOrder: Order.AnySwap)) shouldBe true
  }

  property("Parse n2t swap sell legacy v1 contract") {
    val box = N2T.swap.outputSell
    val swapResult: Order.AnySwap =
      parser.swap(box, ErgoTreeSerializer.default.deserialize(box.ergoTree)).get
    (swapResult eqv (N2T.swap.sellOrder: Order.AnySwap)) shouldBe true
  }

  property("Parse n2t redeem legacy v1 contract") {
    val box = N2T.redeem.output
    val redeemResult: Order.AnyRedeem =
      parser.redeem(box, ErgoTreeSerializer.default.deserialize(box.ergoTree)).get
    val expected: Order.AnyRedeem = N2T.redeem.redeem
    (redeemResult eqv expected) shouldBe true
  }
}
