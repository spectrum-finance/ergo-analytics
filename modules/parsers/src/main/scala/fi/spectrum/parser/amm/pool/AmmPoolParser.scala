package fi.spectrum.parser.amm.pool

import fi.spectrum.core.domain.analytics.Version
import fi.spectrum.core.domain.analytics.Version.V1
import fi.spectrum.core.domain.pool.Pool.AmmPool
import fi.spectrum.core.domain.transaction.Output
import fi.spectrum.parser.domain.AmmType
import fi.spectrum.parser.domain.AmmType.{N2T, T2T}

/** Parse any amm pool
  * @tparam V - pool version
  * @tparam T - pool type
  */
trait AmmPoolParser[+V <: Version, +T <: AmmType] { self =>
  def pool(output: Output): Option[AmmPool]

  def or(that: => AmmPoolParser[Version, AmmType]): AmmPoolParser[Version, AmmType] =
    (output: Output) =>
      self.pool(output) match {
        case s @ Some(_) => s
        case None        => that.pool(output)
      }
}

object AmmPoolParser {

  implicit def make(implicit
    n2tV1: AmmPoolParser[V1, N2T],
    t2tV1: AmmPoolParser[V1, T2T]
  ): AmmPoolParser[Version, AmmType] =
    List[AmmPoolParser[Version, AmmType]](t2tV1, n2tV1).reduceLeft(_ or _)
}
