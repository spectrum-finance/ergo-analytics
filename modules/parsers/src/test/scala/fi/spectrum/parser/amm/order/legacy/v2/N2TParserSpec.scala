package fi.spectrum.parser.amm.order.legacy.v2

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

  val parser: AmmOrderParser[Version.LegacyV2, AmmType.N2T] = N2TAmmOrderParser.n2tLegacyV2

  property("Parse n2t deposit legacy v2 contract") {
    val box = N2T.deposit.output
    val depositResult: Order.Deposit =
      parser.deposit(box, ErgoTreeSerializer.default.deserialize(box.ergoTree)).get
    val expected: Order.Deposit = N2T.deposit.deposit
    (depositResult shouldEqual expected)
  }
}
