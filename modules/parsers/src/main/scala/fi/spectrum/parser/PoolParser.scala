package fi.spectrum.parser

import fi.spectrum.core.domain.analytics.Version
import fi.spectrum.core.domain.pool.{Pool, PoolType}
import fi.spectrum.core.domain.transaction.Output
import fi.spectrum.parser.amm.pool.AmmPoolParser
import fi.spectrum.parser.amm.pool.v1.N2TPoolParser._
import fi.spectrum.parser.amm.pool.v1.T2TPoolParser._
import fi.spectrum.parser.domain.AmmType

trait PoolParser { self =>
  def parse(box: Output): Option[Pool[Version, PoolType]]

  def or(that: => PoolParser): PoolParser = (box: Output) =>
    self.parse(box) match {
      case r @ Some(_) => r
      case None        => that.parse(box)
    }
}

object PoolParser {

  def make: PoolParser = List[PoolParser](ammPoolParser).reduceLeft(_ or _)

  def ammPoolParser: PoolParser = new PoolParser {
    val poolParser: AmmPoolParser[Version, AmmType] = AmmPoolParser.make

    def parse(box: Output): Option[Pool[Version, PoolType]] = poolParser.pool(box)
  }
}