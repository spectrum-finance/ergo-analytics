package fi.spectrum.parser

import fi.spectrum.core.domain.order.{Operation, Order, OrderType, Version}
import fi.spectrum.core.domain.transaction.Output
import fi.spectrum.core.protocol.ErgoTreeSerializer
import fi.spectrum.parser.amm.AmmOrderParser
import fi.spectrum.parser.amm.legacy.v1.N2TAmmOrderParser._
import fi.spectrum.parser.amm.legacy.v1.T2TAmmOrderParser._
import fi.spectrum.parser.amm.legacy.v2.N2TAmmOrderParser._
import fi.spectrum.parser.amm.legacy.v2.T2TAmmOrderParser._
import fi.spectrum.parser.amm.v1.N2TAmmOrderParser._
import fi.spectrum.parser.amm.v1.T2TAmmOrderParser._
import fi.spectrum.parser.amm.v2.N2TAmmOrderParser._
import fi.spectrum.parser.amm.v2.T2TAmmOrderParser._
import fi.spectrum.parser.amm.v3.N2TAmmOrderParser._
import fi.spectrum.parser.amm.v3.T2TAmmOrderParser._
import fi.spectrum.parser.domain.AmmType
import fi.spectrum.parser.lock.LockOrderParser
import fi.spectrum.parser.lock.v1.LockParser._

trait OrderParser { self =>
  def parse(box: Output): Option[Order[Version, OrderType, Operation]]

  def or(that: => OrderParser): OrderParser = (box: Output) =>
    self.parse(box) match {
      case r @ Some(_) => r
      case None        => that.parse(box)
    }
}

object OrderParser {

  def make: OrderParser = (box: Output) =>
    List[OrderParser](ammOrderParser, lockOrderParser).reduceLeft(_ or _).parse(box)

  def ammOrderParser: OrderParser = new OrderParser {
    val ammParser: AmmOrderParser[Version, AmmType] = AmmOrderParser.make

    def parse(box: Output): Option[Order[Version, OrderType, Operation]] = {
      val tree = ErgoTreeSerializer.default.deserialize(box.ergoTree)
      ammParser.order(box, tree)
    }
  }

  def lockOrderParser: OrderParser = new OrderParser {
    val lockParser: LockOrderParser[Version] = LockOrderParser.make

    def parse(box: Output): Option[Order[Version, OrderType, Operation]] = {
      val tree = ErgoTreeSerializer.default.deserialize(box.ergoTree)
      lockParser.lock(box, tree)
    }
  }

}
