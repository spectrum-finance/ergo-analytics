package fi.spectrum.parser.amm.order.v2

import cats.implicits.catsSyntaxEq
import fi.spectrum.core.domain.analytics.Version
import fi.spectrum.core.domain.order.Order
import fi.spectrum.core.domain.order.Order.Swap.SwapV2
import fi.spectrum.core.protocol.ErgoTreeSerializer
import fi.spectrum.parser.CatsPlatform
import fi.spectrum.parser.amm.order.AmmOrderParser
import fi.spectrum.parser.domain.AmmType
import org.scalatest.matchers.should.Matchers
import org.scalatest.propspec.AnyPropSpec

import scala.util.Try

class T2TParserSpec extends AnyPropSpec with Matchers with CatsPlatform {

  val parser: AmmOrderParser[Version.V2, AmmType.T2T] = T2TAmmOrderParser.t2tV2

  property("Ergo tree from swaps should be deserializable") {
    val box   = T2T.swap.output
    val order = parser.swap(box, ErgoTreeSerializer.default.deserialize(box.ergoTree)).get.asInstanceOf[SwapV2]

    val `try` = Try(ErgoTreeSerializer.default.deserialize(order.redeemer.value))

    `try`.isSuccess shouldEqual true
  }

  property("Parse n2t swap v2 contract") {
    val box = T2T.swap.output
    val swapResult: Order.AnySwap =
      parser.swap(box, ErgoTreeSerializer.default.deserialize(box.ergoTree)).get
    (swapResult shouldEqual (T2T.swap.swap: Order.AnySwap))
  }
}
