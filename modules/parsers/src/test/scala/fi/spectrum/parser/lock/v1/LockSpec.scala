package fi.spectrum.parser.lock.v1

import cats.syntax.eq._
import fi.spectrum.core.domain.order.Order.Lock.LockV1
import fi.spectrum.core.protocol.ErgoTreeSerializer
import fi.spectrum.parser.CatsPlatform
import org.scalatest.matchers.should.Matchers
import org.scalatest.propspec.AnyPropSpec

class LockSpec extends AnyPropSpec with Matchers with CatsPlatform {

  val parser = LockParser.ev

  property("Parse lock v1 contract") {
    val box    = Lock.output
    val result = parser.lock(box, ErgoTreeSerializer.default.deserialize(box.ergoTree)).get.asInstanceOf[LockV1]
    (result eqv Lock.lock) shouldBe true
  }
}
