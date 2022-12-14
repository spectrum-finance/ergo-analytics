package fi.spectrum.parser

import fi.spectrum.core.domain.order.Order
import fi.spectrum.core.domain.order.Order._
import fi.spectrum.parser.amm.order.anyAmmOrder
import fi.spectrum.parser.lock.v1.Lock
import io.circe.Encoder
import io.circe.parser.decode
import io.circe.syntax._
import org.scalatest.matchers.should.Matchers
import org.scalatest.propspec.AnyPropSpec

class OrderJsonSpec extends AnyPropSpec with Matchers with CatsPlatform {
  implicitly[Encoder[Order.Any]]
  property("Parse any order via OrderParser") {
    val anyOrder = List(Lock.output -> Lock.lock) ::: anyAmmOrder
    anyOrder.foreach { case (_, expectedOrder) =>
      decode[Order.Any](expectedOrder.asJson.noSpaces).toOption.get shouldEqual expectedOrder
    }
  }
}
