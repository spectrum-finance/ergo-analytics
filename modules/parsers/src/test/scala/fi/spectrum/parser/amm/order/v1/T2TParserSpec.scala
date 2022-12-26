package fi.spectrum.parser.amm.order.v1

import fi.spectrum.core.domain.analytics.Version
import fi.spectrum.core.domain.order.Order
import fi.spectrum.core.protocol.ErgoTreeSerializer
import fi.spectrum.parser.CatsPlatform
import fi.spectrum.parser.amm.order.AmmOrderParser
import fi.spectrum.parser.domain.AmmType
import org.scalatest.matchers.should.Matchers
import org.scalatest.propspec.AnyPropSpec
import cats.syntax.eq._

class T2TParserSpec extends AnyPropSpec with Matchers with CatsPlatform {
  val parser: AmmOrderParser[Version.V1, AmmType.T2T] = T2TAmmOrderParser.t2tV1

  property("Parse t2t swap v1 contract") {
    val box = T2T.swap.output
    val swapResult: Order.Swap =
      parser.swap(box, ErgoTreeSerializer.default.deserialize(box.ergoTree)).get
    (swapResult shouldEqual (T2T.swap.order: Order.Swap))
  }

  property("Parse t2t deposit v1 contract") {
    val box = T2T.deposit.depositT2T
    val depositResult: Order.Deposit =
      parser.deposit(box, ErgoTreeSerializer.default.deserialize(box.ergoTree)).get
    (depositResult shouldEqual (T2T.deposit.expectedT2TDepositV1: Order.Deposit))
  }

  property("Parse t2t redeem v1 contract") {
    val box = T2T.redeem.output
    val redeemResult: Order.Redeem =
      parser.redeem(box, ErgoTreeSerializer.default.deserialize(box.ergoTree)).get
    val expected: Order.Redeem = T2T.redeem.order
    (redeemResult shouldEqual expected)
  }
}
