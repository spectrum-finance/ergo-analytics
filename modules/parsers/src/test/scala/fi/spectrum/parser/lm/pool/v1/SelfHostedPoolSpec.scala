package fi.spectrum.parser.lm.pool.v1

import fi.spectrum.parser.CatsPlatform
import org.scalatest.matchers.should.Matchers
import org.scalatest.propspec.AnyPropSpec

class SelfHostedPoolSpec extends AnyPropSpec with Matchers with CatsPlatform {

  val parser = LmPoolSelfHostedParserV1.v1LmPoolSelfHosted

  property("Parse self hosted lm pool v1 contract") {
    val box = SelfHosted.selfHostedPool
    val poolResult = parser.pool(box, 0, 10).get
    poolResult shouldEqual SelfHosted.pool
  }
}
