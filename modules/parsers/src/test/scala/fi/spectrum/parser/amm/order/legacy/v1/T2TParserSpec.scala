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

class T2TParserSpec extends AnyPropSpec with Matchers with CatsPlatform {

  val parser: AmmOrderParser[Version.LegacyV1, AmmType.T2T] = T2TAmmOrderParser.t2tLegacyV1

  property("Parse t2t swap legacy v1 contract") {
    val box = T2T.swap.output
    val swapResult: Order.AnySwap =
      parser.swap(box, ErgoTreeSerializer.default.deserialize(box.ergoTree)).get
    (swapResult shouldEqual (T2T.swap.order: Order.AnySwap))
  }

  property("Parse t2t redeem legacy v1 contract") {
    val box = T2T.redeem.output
    val redeemResult: Order.AnyRedeem =
      parser.redeem(box, ErgoTreeSerializer.default.deserialize(box.ergoTree)).get
    val expected: Order.AnyRedeem = T2T.redeem.redeem
    (redeemResult shouldEqual expected)
  }

}
