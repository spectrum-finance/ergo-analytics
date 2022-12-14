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

class N2TParserSpec extends AnyPropSpec with Matchers with CatsPlatform {

  val parser: AmmOrderParser[Version.V2, AmmType.N2T] = N2TAmmOrderParser.n2tV2

  property("Ergo tree from swaps should be deserializable") {
    val boxBuy  = N2T.swap.outputBuy
    val swapBuy = parser.swap(boxBuy, ErgoTreeSerializer.default.deserialize(boxBuy.ergoTree)).get.asInstanceOf[SwapV2]
    val tryBuy  = ErgoTreeSerializer.default.deserialize(swapBuy.redeemer.value)

    val boxSell = N2T.swap.outputSell
    val swapSell =
      parser.swap(boxSell, ErgoTreeSerializer.default.deserialize(boxSell.ergoTree)).get.asInstanceOf[SwapV2]
    val trySell = ErgoTreeSerializer.default.deserialize(swapSell.redeemer.value)

    tryBuy shouldBe trySell
  }

  property("Parse n2t swap buy v2 contract") {
    val box = N2T.swap.outputBuy
    val swapResult: Order.AnySwap =
      parser.swap(box, ErgoTreeSerializer.default.deserialize(box.ergoTree)).get
    (swapResult shouldEqual (N2T.swap.swapBuy: Order.AnySwap))
  }

  property("Parse n2t swap sell v2 contract") {
    val box = N2T.swap.outputSell
    val swapResult: Order.AnySwap =
      parser.swap(box, ErgoTreeSerializer.default.deserialize(box.ergoTree)).get
    (swapResult shouldEqual (N2T.swap.swapSell: Order.AnySwap))
  }

}
