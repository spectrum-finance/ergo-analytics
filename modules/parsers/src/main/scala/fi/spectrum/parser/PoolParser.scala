package fi.spectrum.parser

import fi.spectrum.core.domain.analytics.Version
import fi.spectrum.core.domain.pool.Pool
import fi.spectrum.core.domain.transaction.Output
import fi.spectrum.parser.amm.pool.AmmPoolParser
import fi.spectrum.parser.amm.pool.v1.N2TPoolParser._
import fi.spectrum.parser.amm.pool.v1.T2TPoolParser._
import fi.spectrum.parser.domain.AmmType

/** Parses any pool no matter what version or type it has.
  */
trait PoolParser { self =>
  def parse(box: Output, timestamp: Long): Option[Pool]

  def or(that: => PoolParser): PoolParser = (box: Output, timestamp: Long) =>
    self.parse(box, timestamp) match {
      case r @ Some(_) => r
      case None        => that.parse(box, timestamp)
    }
}

object PoolParser {

  def make: PoolParser = List[PoolParser](ammPoolParser).reduceLeft(_ or _)

  private def ammPoolParser: PoolParser = {
    val poolParser: AmmPoolParser[Version, AmmType] = AmmPoolParser.make
    (box: Output, timestamp: Long) => poolParser.pool(box, timestamp)
  }
}
