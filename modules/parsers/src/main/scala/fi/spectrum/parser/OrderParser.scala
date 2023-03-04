package fi.spectrum.parser

import fi.spectrum.core.domain.TokenId
import fi.spectrum.core.domain.analytics.Version
import fi.spectrum.core.domain.order.Order
import fi.spectrum.core.domain.transaction.Output
import fi.spectrum.core.protocol.ErgoTreeSerializer
import fi.spectrum.parser.amm.order.AmmOrderParser
import fi.spectrum.parser.amm.order.legacy.v1.N2TAmmOrderParser._
import fi.spectrum.parser.amm.order.legacy.v1.T2TAmmOrderParser._
import fi.spectrum.parser.amm.order.legacy.v2.N2TAmmOrderParser._
import fi.spectrum.parser.amm.order.legacy.v2.T2TAmmOrderParser._
import fi.spectrum.parser.amm.order.legacy.v3.T2TAmmOrderParser._
import fi.spectrum.parser.amm.order.legacy.v3.N2TAmmOrderParser._
import fi.spectrum.parser.amm.order.v1.N2TAmmOrderParser._
import fi.spectrum.parser.amm.order.v1.T2TAmmOrderParser._
import fi.spectrum.parser.amm.order.v2.N2TAmmOrderParser._
import fi.spectrum.parser.amm.order.v2.T2TAmmOrderParser._
import fi.spectrum.parser.amm.order.v3.N2TAmmOrderParser._
import fi.spectrum.parser.amm.order.v3.T2TAmmOrderParser._
import fi.spectrum.parser.domain.AmmType
import fi.spectrum.parser.lm.compound.v1.CompoundParserV1._
import fi.spectrum.parser.lm.order.LmOrderParser
import fi.spectrum.parser.lm.order.v1.LmOrderParserV1.v1
import fi.spectrum.parser.lock.LockOrderParser
import fi.spectrum.parser.lock.v1.LockParser._
import org.ergoplatform.ErgoAddressEncoder

/** Parses any order no matter what version or type it has.
  */
trait OrderParser { self =>
  def parse(box: Output): Option[Order]

  def or(that: => OrderParser): OrderParser = (box: Output) =>
    self.parse(box) match {
      case r @ Some(_) => r
      case None        => that.parse(box)
    }
}

object OrderParser {

  implicit def make(implicit spf: TokenId, e: ErgoAddressEncoder): OrderParser = (box: Output) =>
    List[OrderParser](ammOrderParser, lockOrderParser, lmParser).reduceLeft(_ or _).parse(box)

  private def ammOrderParser(implicit spf: TokenId, e: ErgoAddressEncoder): OrderParser = {
    val ammParser: AmmOrderParser[Version, AmmType] = AmmOrderParser.make
    (box: Output) => ammParser.order(box, ErgoTreeSerializer.default.deserialize(box.ergoTree))
  }

  private def lockOrderParser: OrderParser = {
    val lockParser: LockOrderParser[Version] = LockOrderParser.make
    (box: Output) => lockParser.lock(box, ErgoTreeSerializer.default.deserialize(box.ergoTree))
  }

  private def lmParser: OrderParser = {
    val lmParser: LmOrderParser[Version] = LmOrderParser.make
    (box: Output) => lmParser.order(box, ErgoTreeSerializer.default.deserialize(box.ergoTree))
  }

}
