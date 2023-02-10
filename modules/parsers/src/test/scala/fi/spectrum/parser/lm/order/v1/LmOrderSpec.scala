package fi.spectrum.parser.lm.order.v1

import fi.spectrum.parser.CatsPlatform
import org.scalatest.matchers.should.Matchers
import org.scalatest.propspec.AnyPropSpec
import fi.spectrum.core.protocol.ErgoTreeSerializer
import fi.spectrum.parser.lm.compound.v1.CompoundParserV1._

class LmOrderSpec extends AnyPropSpec with Matchers with CatsPlatform {

  val parser = LmOrderParserV1.v1

  property("Parse lm deposit v1 contract") {
    val box     = LM.depositOrder
    val deposit = parser.deposit(box, ErgoTreeSerializer.default.deserialize(box.ergoTree)).get
    deposit shouldEqual LM.deposit
  }

  property("Parse lm redeem v1 contract") {
    val box = LM.redeemOrderOutput
    val redeem = parser.redeem(box, ErgoTreeSerializer.default.deserialize(box.ergoTree)).get
    redeem shouldEqual LM.redeem
  }
}
