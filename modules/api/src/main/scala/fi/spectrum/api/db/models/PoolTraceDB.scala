package fi.spectrum.api.db.models

import fi.spectrum.api.db.models.amm.{AssetInfo, PoolTrace}
import fi.spectrum.api.models.FullAsset
import fi.spectrum.core.domain.AssetAmount
import fi.spectrum.core.domain.order.PoolId

final case class PoolTraceDB(
  id: PoolId,
  lockedX: AssetAmount,
  lockedY: AssetAmount,
  height: Long
) {

  def toPoolTrace(assets: List[AssetInfo]): PoolTrace = {
    val x = assets.find(_.id == lockedX.tokenId)
    val y = assets.find(_.id == lockedY.tokenId)
    PoolTrace(
      id,
      FullAsset(lockedX.tokenId, lockedX.amount, x.flatMap(_.ticker), x.flatMap(_.decimals)),
      FullAsset(lockedY.tokenId, lockedY.amount, y.flatMap(_.ticker), y.flatMap(_.decimals)),
      height
    )
  }
}
