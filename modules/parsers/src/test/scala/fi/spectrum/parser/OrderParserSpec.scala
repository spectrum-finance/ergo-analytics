package fi.spectrum.parser

import cats.implicits.catsSyntaxEq
import fi.spectrum.core.domain.TokenId
import fi.spectrum.core.domain.order.Order
import fi.spectrum.core.domain.transaction.Output
import fi.spectrum.parser.amm.order.anyAmmOrder
import fi.spectrum.parser.lock.v1.Lock
import org.scalatest.matchers.should.Matchers
import org.scalatest.propspec.AnyPropSpec

class OrderParserSpec extends AnyPropSpec with Matchers with CatsPlatform {

  val parser: OrderParser = OrderParser.make(TokenId.unsafeFromString(""))

  property("Parse any order via OrderParser") {
    val anyOrder: List[(Output, Order)] = List(Lock.output -> Lock.lock) ::: anyAmmOrder
    anyOrder.foreach { case (output, expectedOrder) =>
      (parser.parse(output).get shouldEqual expectedOrder)
    }
  }
}
