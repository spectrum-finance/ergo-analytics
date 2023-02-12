package fi.spectrum.parser.lm.order

import fi.spectrum.core.protocol.ErgoTreeSerializer
import fi.spectrum.parser.CatsPlatform
import fi.spectrum.parser.lm.compound.v1.Compound
import fi.spectrum.parser.lm.compound.v1.CompoundParserV1._
import fi.spectrum.parser.lm.order.v1.{LM, LmOrderParserV1}
import org.scalatest.matchers.should.Matchers
import org.scalatest.propspec.AnyPropSpec

class AnyLmOrderSpec extends AnyPropSpec with Matchers with CatsPlatform {
  implicit val __ = LmOrderParserV1.v1
  val parser      = LmOrderParser.make

  property("Parse lm deposit v1 contract") {
    val box     = LM.depositOrder
    val deposit = parser.order(box, ErgoTreeSerializer.default.deserialize(box.ergoTree)).get
    deposit shouldEqual LM.deposit
  }

  property("Parse lm redeem v1 contract") {
    val box    = LM.redeemOrderOutput
    val redeem = parser.order(box, ErgoTreeSerializer.default.deserialize(box.ergoTree)).get
    redeem shouldEqual LM.redeem
  }

  property("Parse lm compound v1 contract") {
    val box     = Compound.compoundNotLastEpochOutput
    val deposit = parser.order(box, ErgoTreeSerializer.default.deserialize(box.ergoTree)).get
    deposit shouldEqual Compound.compoundNotLastEpoch
  }
}
