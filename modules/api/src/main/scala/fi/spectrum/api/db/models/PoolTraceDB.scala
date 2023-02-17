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

  def toPoolTrace(assets: List[AssetInfo]): Option[PoolTrace] =
    for {
      x <- assets.find(_.id == lockedX.tokenId)
      y <- assets.find(_.id == lockedY.tokenId)
    } yield PoolTrace(
      id,
      FullAsset(lockedX.tokenId, lockedX.amount, x.ticker, x.decimals),
      FullAsset(lockedY.tokenId, lockedY.amount, y.ticker, y.decimals),
      height
    )
}
