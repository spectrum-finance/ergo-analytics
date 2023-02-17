package fi.spectrum.api.db.models

import fi.spectrum.api.db.models.amm.{AssetInfo, PoolFeesSnapshot}
import fi.spectrum.api.models.FullAsset
import fi.spectrum.core.domain.AssetAmount
import fi.spectrum.core.domain.order.PoolId

final case class PoolFeesSnapshotDB(
  poolId: PoolId,
  feesByX: AssetAmount,
  feesByY: AssetAmount
) {

  def toPoolFeesSnapshot(assets: List[AssetInfo]): Option[PoolFeesSnapshot] =
    for {
      x <- assets.find(_.id == feesByX.tokenId)
      y <- assets.find(_.id == feesByY.tokenId)
    } yield PoolFeesSnapshot(
      poolId,
      FullAsset(feesByX.tokenId, feesByX.amount, x.ticker, x.decimals),
      FullAsset(feesByY.tokenId, feesByY.amount, y.ticker, y.decimals)
    )
}
