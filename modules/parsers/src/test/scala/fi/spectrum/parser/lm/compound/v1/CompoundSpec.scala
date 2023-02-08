package fi.spectrum.parser.lm.compound.v1

import fi.spectrum.core.protocol.ErgoTreeSerializer
import fi.spectrum.parser.CatsPlatform
import fi.spectrum.parser.lm.compound.v1.CompoundParserV1
import org.scalatest.matchers.should.Matchers
import org.scalatest.propspec.AnyPropSpec

class CompoundSpec extends AnyPropSpec with Matchers with CatsPlatform {
  val parser = CompoundParserV1.v1Compound

  property("Parse lm deposit v1 contract") {
    val box     = Bundle.output
    val deposit = parser.compound(box, ErgoTreeSerializer.default.deserialize(box.ergoTree)).get
    deposit shouldEqual Bundle.bundle
  }
}
