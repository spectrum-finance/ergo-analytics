package fi.spectrum.parser

import cats.implicits.catsSyntaxEq
import fi.spectrum.core.domain.order.Order
import fi.spectrum.parser.amm.order.anyAmmOrder
import fi.spectrum.parser.lock.v1.Lock
import org.scalatest.matchers.should.Matchers
import org.scalatest.propspec.AnyPropSpec
import io.circe.syntax._
import io.circe.parser.decode

class OrderJsonSpec extends AnyPropSpec with Matchers with CatsPlatform {
  property("Parse any order via OrderParser") {
    val anyOrder = List(Lock.output -> Lock.lock) ::: anyAmmOrder
    anyOrder.foreach { case (_, expectedOrder) =>
      decode[Order.Any](expectedOrder.asJson.noSpaces).toOption.get eqv expectedOrder
    }
  }
}
