package fi.spectrum.core.domain.pool

import fi.spectrum.core.domain.pool.Pool.AmmPool
import fi.spectrum.core.domain.{AssetAmount, TokenId}
import glass.Label
import glass.classic.Optional
import glass.macros.{GenContains, GenSubset}

object PoolOptics {

  implicit val ammPool = GenSubset[Pool.Any, AmmPool]

  implicit val poolLP: Optional[Pool.Any, TokenId] =
    ammPool >> GenContains[AmmPool](_.lp.tokenId)

  implicit val poolX: Optional[Pool.Any, AssetAmount] with Label["x"] =
    (ammPool>> GenContains[AmmPool](_.x)).label["x"]

  implicit val poolY: Optional[Pool.Any, AssetAmount] with Label["y"] =
    (ammPool >> GenContains[AmmPool](_.y)).label["y"]

}
