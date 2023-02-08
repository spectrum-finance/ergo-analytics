package fi.spectrum.parser.lm.pool

import fi.spectrum.core.domain.analytics.Version
import fi.spectrum.core.domain.analytics.Version.V1
import fi.spectrum.core.domain.pool.Pool
import fi.spectrum.core.domain.transaction.Output
import fi.spectrum.parser.domain.LmPoolType

trait LmPoolParser[+V <: Version, +T <: LmPoolType] { self =>
  def pool(output: Output, timestamp: Long, height: Int): Option[Pool]

  def or(that: => LmPoolParser[Version, LmPoolType]): LmPoolParser[Version, LmPoolType] =
    (output: Output, timestamp: Long, height: Int) =>
      self.pool(output, timestamp, height) match {
        case s @ Some(_) => s
        case None        => that.pool(output, timestamp, height)
      }
}

object LmPoolParser {

  implicit def make(implicit
    v1Self: LmPoolParser[V1, LmPoolType.SelfHosted],
    v1: LmPoolParser[V1, LmPoolType.Default]
  ): LmPoolParser[Version, LmPoolType] =
    List[LmPoolParser[Version, LmPoolType]](v1, v1Self).reduceLeft(_ or _)
}
