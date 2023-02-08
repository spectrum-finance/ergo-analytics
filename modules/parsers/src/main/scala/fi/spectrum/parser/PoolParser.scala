package fi.spectrum.parser

import fi.spectrum.core.domain.analytics.Version
import fi.spectrum.core.domain.pool.Pool
import fi.spectrum.core.domain.transaction.Output
import fi.spectrum.parser.amm.pool.AmmPoolParser
import fi.spectrum.parser.amm.pool.v1.N2TPoolParser._
import fi.spectrum.parser.amm.pool.v1.T2TPoolParser._
import fi.spectrum.parser.domain.{AmmType, LmPoolType}
import fi.spectrum.parser.lm.pool.LmPoolParser
import fi.spectrum.parser.lm.pool.v1.LmPoolSelfHostedParserV1._
import fi.spectrum.parser.lm.pool.v1.LmPoolParserV1._

/** Parses any pool no matter what version or type it has.
  */
trait PoolParser { self =>
  def parse(box: Output, timestamp: Long, height: Int): Option[Pool]

  def or(that: => PoolParser): PoolParser = (box: Output, timestamp: Long, height: Int) =>
    self.parse(box, timestamp, height) match {
      case r @ Some(_) => r
      case None        => that.parse(box, timestamp, height)
    }
}

object PoolParser {

  implicit def make: PoolParser = List[PoolParser](ammPoolParser, lmPoolParser).reduceLeft(_ or _)

  private def ammPoolParser: PoolParser = {
    val poolParser: AmmPoolParser[Version, AmmType] = AmmPoolParser.make
    (box: Output, timestamp: Long, height: Int) => poolParser.pool(box, timestamp, height)
  }

  private def lmPoolParser: PoolParser = {
    val poolParser: LmPoolParser[Version, LmPoolType] = LmPoolParser.make
    (box: Output, timestamp: Long, height: Int) => poolParser.pool(box, timestamp, height)
  }
}
