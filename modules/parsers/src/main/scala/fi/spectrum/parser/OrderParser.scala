package fi.spectrum.parser

import fi.spectrum.core.domain.TokenId
import fi.spectrum.core.domain.analytics.Version
import fi.spectrum.core.domain.analytics.Version.V3
import fi.spectrum.core.domain.order.{Operation, Order, OrderType}
import fi.spectrum.core.domain.transaction.Output
import fi.spectrum.core.protocol.ErgoTreeSerializer
import fi.spectrum.parser.amm.order.AmmOrderParser
import fi.spectrum.parser.domain.AmmType
import fi.spectrum.parser.domain.AmmType.{N2T, T2T}
import fi.spectrum.parser.lock.LockOrderParser
import fi.spectrum.parser.lock.v1.LockParser._
import fi.spectrum.parser.amm.order.legacy.v1.N2TAmmOrderParser._
import fi.spectrum.parser.amm.order.legacy.v1.T2TAmmOrderParser._
import fi.spectrum.parser.amm.order.legacy.v2.N2TAmmOrderParser._
import fi.spectrum.parser.amm.order.legacy.v2.T2TAmmOrderParser._
import fi.spectrum.parser.amm.order.v1.N2TAmmOrderParser._
import fi.spectrum.parser.amm.order.v1.T2TAmmOrderParser._
import fi.spectrum.parser.amm.order.v2.N2TAmmOrderParser._
import fi.spectrum.parser.amm.order.v2.T2TAmmOrderParser._
import fi.spectrum.parser.amm.order.v3.N2TAmmOrderParser._
import fi.spectrum.parser.amm.order.v3.T2TAmmOrderParser._

trait OrderParser { self =>
  def parse(box: Output): Option[Order[Version, OrderType, Operation]]

  def or(that: => OrderParser): OrderParser = (box: Output) =>
    self.parse(box) match {
      case r @ Some(_) => r
      case None        => that.parse(box)
    }
}

object OrderParser {

  def make(spf: TokenId): OrderParser = (box: Output) =>
    List[OrderParser](ammOrderParser(spf), lockOrderParser).reduceLeft(_ or _).parse(box)

  def ammOrderParser(spf: TokenId): OrderParser = new OrderParser {
    implicit val n2tV3: AmmOrderParser[V3, N2T] = fi.spectrum.parser.amm.order.v3.N2TAmmOrderParser.n2tV3(spf)
    implicit val t2tV3: AmmOrderParser[V3, T2T] = fi.spectrum.parser.amm.order.v3.T2TAmmOrderParser.t2tV3(spf)

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
