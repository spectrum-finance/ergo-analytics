package fi.spectrum.core.domain.pool

import fi.spectrum.core.domain.pool.Pool.AmmPool
import fi.spectrum.core.domain.{AssetAmount, BoxId, TokenId}
import glass.{Label, Subset}
import glass.classic.Optional
import glass.macros.{GenContains, GenSubset}

object PoolOptics {

  implicit val ammPool: Subset[Pool, AmmPool] = GenSubset[Pool, AmmPool]

  implicit val poolLP: Optional[Pool, TokenId] =
    ammPool >> GenContains[AmmPool](_.lp.tokenId)

  implicit val poolX: Optional[Pool, AssetAmount] with Label["x"] =
    (ammPool>> GenContains[AmmPool](_.x)).label["x"]

  implicit val poolY: Optional[Pool, AssetAmount] with Label["y"] =
    (ammPool >> GenContains[AmmPool](_.y)).label["y"]

  implicit val poolBoxId: Optional[AmmPool, BoxId] =
    GenContains[AmmPool](_.box.boxId)

  implicit val poolAnyBoxId: Optional[Pool, BoxId] = ammPool >> poolBoxId

}
