package fi.spectrum.parser.amm.pool.v1

import cats.implicits.catsSyntaxEq
import fi.spectrum.core.domain.analytics.Version
import fi.spectrum.core.domain.pool.Pool
import fi.spectrum.parser.CatsPlatform
import fi.spectrum.parser.amm.pool.AmmPoolParser
import fi.spectrum.parser.domain.AmmType
import org.scalatest.matchers.should.Matchers
import org.scalatest.propspec.AnyPropSpec

class T2TAmmPoolSpec extends AnyPropSpec with Matchers with CatsPlatform {

  val parser: AmmPoolParser[Version.V1, AmmType.T2T] = T2TPoolParser.t2tPool

  property("Parse t2t pool v1 contract") {
    val box                      = T2T.output
    val poolResult: Pool.AmmPool = parser.pool(box).get
    poolResult shouldEqual T2T.pool
    (poolResult shouldEqual T2T.pool)
  }
}
