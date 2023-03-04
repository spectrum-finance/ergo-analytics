package fi.spectrum.parser.ergo

import fi.spectrum.core.domain.transaction.SConstant
import fi.spectrum.parser.CatsPlatform
import org.scalatest.matchers.should.Matchers
import org.scalatest.propspec.AnyPropSpec

final class SConstantParserSpec extends AnyPropSpec with Matchers with CatsPlatform {

  property("Parse SConstant ") {
    val in = "[35,34,392,217,3,3,2]"
    SConstant.parseSInt(in) shouldEqual SConstant.IntsConstant(List(35,34,392,217,3,3,2))
    val in2 = "[0]"
    SConstant.parseSInt(in2) shouldEqual SConstant.IntsConstant(List(0))
    val in3 = "[]"
    SConstant.parseSInt(in3) shouldEqual SConstant.IntsConstant(List())
  }
}
