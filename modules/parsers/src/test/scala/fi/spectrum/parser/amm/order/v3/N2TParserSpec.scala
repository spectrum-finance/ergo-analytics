package fi.spectrum.parser.amm.order.v3

import cats.syntax.eq._
import fi.spectrum.core.domain.analytics.Version
import fi.spectrum.core.domain.order.Order
import fi.spectrum.core.protocol.ErgoTreeSerializer
import fi.spectrum.parser.CatsPlatform
import fi.spectrum.parser.amm.order.AmmOrderParser
import fi.spectrum.parser.domain.AmmType
import org.scalatest.matchers.should.Matchers
import org.scalatest.propspec.AnyPropSpec

class N2TParserSpec extends AnyPropSpec with Matchers with CatsPlatform {

  val parser: AmmOrderParser[Version.V3, AmmType.N2T] = N2TAmmOrderParser.n2tV3

  property("Parse n2t swap buy v3 spf contract") {
    val box = N2T.swap.outputBuySpf
    val swapResult: Order.Swap =
      parser.swap(box, ErgoTreeSerializer.default.deserialize(box.ergoTree)).get

    (swapResult shouldEqual (N2T.swap.swapBuySpf: Order.Swap))
  }

  property("Parse n2t swap buy v3 no spf contract") {
    val box = N2T.swap.outputBuyNoSpf
    val swapResult: Order.Swap =
      parser.swap(box, ErgoTreeSerializer.default.deserialize(box.ergoTree)).get

    (swapResult shouldEqual (N2T.swap.swapBuyNoSpf: Order.Swap))
  }

  property("Parse n2t swap sell v3 no spf contract") {
    val box = N2T.swap.outputSellNotY
    val swapResult: Order.Swap =
      parser.swap(box, ErgoTreeSerializer.default.deserialize(box.ergoTree)).get
    (swapResult shouldEqual (N2T.swap.swapSellNotY: Order.Swap))
  }

  property("Parse n2t swap sell v3 spf contract") {
    val box = N2T.swap.outputSellSpf
    val swapResult: Order.Swap =
      parser.swap(box, ErgoTreeSerializer.default.deserialize(box.ergoTree)).get
    (swapResult shouldEqual (N2T.swap.swapSellSpf: Order.Swap))
  }

  property("Parse n2t deposit v3 y is spf contract") {
    val box = N2T.deposit.outputSpfY
    val depositResult: Order.Deposit =
      parser.deposit(box, ErgoTreeSerializer.default.deserialize(box.ergoTree)).get
    val expected: Order.Deposit = N2T.deposit.depositSpfY
    (depositResult shouldEqual expected)
  }

  property("Parse n2t deposit v3 y is not spf contract") {
    val box = N2T.deposit.outputSpfNotY
    val depositResult: Order.Deposit =
      parser.deposit(box, ErgoTreeSerializer.default.deserialize(box.ergoTree)).get
    val expected: Order.Deposit = N2T.deposit.depositSpfNotY
    (depositResult shouldEqual expected)
  }

  property("Parse n2t redeem v3 contract") {
    val box = N2T.redeem.output
    val redeemResult: Order.Redeem =
      parser.redeem(box, ErgoTreeSerializer.default.deserialize(box.ergoTree)).get
    val expected: Order.Redeem = N2T.redeem.order
    (redeemResult shouldEqual expected)
  }
}
