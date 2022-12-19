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

class T2TParserSpec extends AnyPropSpec with Matchers with CatsPlatform {

  val parser: AmmOrderParser[Version.V3, AmmType.T2T] = T2TAmmOrderParser.t2tV3

  property("Parse t2t swap v3 no spf contract") {
    val box = T2T.swap.outputNoSpf
    val swapResult: Order.Swap =
      parser.swap(box, ErgoTreeSerializer.default.deserialize(box.ergoTree)).get
    (swapResult shouldEqual (T2T.swap.swapNoSpf: Order.Swap))
  }

  property("Parse t2t swap v3 spf contract") {
    val box = T2T.swap.outputSpf
    val swapResult: Order.Swap =
      parser.swap(box, ErgoTreeSerializer.default.deserialize(box.ergoTree)).get
    (swapResult shouldEqual (T2T.swap.swapSpf: Order.Swap))
  }

  property("Parse t2t deposit v3 y is spf contract") {
    val box = T2T.deposit.outputSpf
    val depositResult: Order.Deposit =
      parser.deposit(box, ErgoTreeSerializer.default.deserialize(box.ergoTree)).get
    val expected: Order.Deposit = T2T.deposit.depositSpf
    (depositResult shouldEqual expected)
  }

  property("Parse t2t deposit v3 x is spf contract") {
    val box = T2T.deposit.outputSpfIsX
    val depositResult: Order.Deposit =
      parser.deposit(box, ErgoTreeSerializer.default.deserialize(box.ergoTree)).get
    val expected: Order.Deposit = T2T.deposit.depositSpfIsX
    (depositResult shouldEqual expected)
  }

  property("Parse t2t redeem v3 contract") {
    val box = T2T.redeem.output
    val redeemResult: Order.Redeem =
      parser.redeem(box, ErgoTreeSerializer.default.deserialize(box.ergoTree)).get
    val expected: Order.Redeem = T2T.redeem.order
    (redeemResult shouldEqual expected)
  }
}