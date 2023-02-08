package fi.spectrum.parser.lm.pool

import fi.spectrum.parser.CatsPlatform
import fi.spectrum.parser.lm.pool.v1.LmPoolParserV1._
import fi.spectrum.parser.lm.pool.v1.LmPoolSelfHostedParserV1._
import fi.spectrum.parser.lm.pool.v1.SelfHosted
import org.scalatest.matchers.should.Matchers
import org.scalatest.propspec.AnyPropSpec

class AnyLmPoolDefaultSpec extends AnyPropSpec with Matchers with CatsPlatform {

  val parser = LmPoolParser.make

  property("Parse any lm pool v1 contract") {
    val box        = SelfHosted.selfHostedPool
    val poolResult = parser.pool(box, 0, 10).get
    poolResult shouldEqual SelfHosted.pool
  }
}
