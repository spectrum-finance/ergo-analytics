package fi.spectrum.parser.amm.pool.v1

import cats.implicits.catsSyntaxEq
import fi.spectrum.core.domain.analytics.Version
import fi.spectrum.core.domain.pool.Pool
import fi.spectrum.parser.CatsPlatform
import fi.spectrum.parser.amm.pool.AmmPoolParser
import fi.spectrum.parser.domain.AmmType
import org.scalatest.matchers.should.Matchers
import org.scalatest.propspec.AnyPropSpec

class N2TAmmPoolSpec extends AnyPropSpec with Matchers with CatsPlatform {

  val parser: AmmPoolParser[Version.V1, AmmType.N2T] = N2TPoolParser.n2tPool

  property("Parse n2t pool v1 contract") {
    val box                      = N2T.output
    val poolResult: Pool.AmmPool = parser.pool(box, N2T.pool.timestamp, 10).get
    poolResult shouldEqual N2T.pool
    (poolResult shouldEqual N2T.pool)
  }
}
