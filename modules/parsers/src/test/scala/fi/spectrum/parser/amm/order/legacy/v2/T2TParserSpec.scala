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

class T2TParserSpec extends AnyPropSpec with Matchers with CatsPlatform {

  val parser: AmmOrderParser[Version.LegacyV2, AmmType.T2T] = T2TAmmOrderParser.t2tLegacyV2

  property("Parse t2t deposit legacy v2 contract") {
    val box = T2T.deposit.output
    val depositResult: Order.AnyDeposit =
      parser.deposit(box, ErgoTreeSerializer.default.deserialize(box.ergoTree)).get
    val expected: Order.AnyDeposit = T2T.deposit.deposit
    (depositResult eqv expected) shouldBe true
  }
}