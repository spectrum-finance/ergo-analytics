package fi.spectrum.parser.amm.pool

import fi.spectrum.core.domain.analytics.Version
import fi.spectrum.core.domain.pool.Pool
import fi.spectrum.parser.{CatsPlatform, PoolParser}
import fi.spectrum.parser.amm.pool.v1.{N2T, T2T}
import org.scalatest.matchers.should.Matchers
import org.scalatest.propspec.AnyPropSpec
import fi.spectrum.parser.amm.pool.v1.N2TPoolParser._
import fi.spectrum.parser.amm.pool.v1.T2TPoolParser._
import fi.spectrum.parser.domain.AmmType

class AnyAmmPoolSpec extends AnyPropSpec with Matchers with CatsPlatform {


  property("Parse n2t and t2t pools v1 contract via AmmPoolParser") {
    val parser: AmmPoolParser[Version, AmmType] = AmmPoolParser.make

    val n2tBox                  = N2T.output
    val t2tBox                  = T2T.output
    val n2tResult: Pool.AmmPool = parser.pool(n2tBox, N2T.pool.timestamp).get
    val t2tResult: Pool.AmmPool = parser.pool(t2tBox, T2T.pool.timestamp).get
    n2tResult shouldEqual N2T.pool
    t2tResult shouldEqual T2T.pool
  }

  property("Parse n2t and t2t pools v1 contract via PoolParser") {
    val parser = PoolParser.make

    val n2tBox = N2T.output
    val t2tBox = T2T.output
    val n2tResult: Pool.AmmPool = parser.parse(n2tBox, N2T.pool.timestamp).get.asInstanceOf[Pool.AmmPool]
    val t2tResult: Pool.AmmPool = parser.parse(t2tBox, T2T.pool.timestamp).get.asInstanceOf[Pool.AmmPool]
    n2tResult shouldEqual N2T.pool
    t2tResult shouldEqual T2T.pool
  }
}
