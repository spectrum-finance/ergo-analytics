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

  def toPoolFeesSnapshot(assets: List[AssetInfo]): PoolFeesSnapshot = {
    val x = assets.find(_.id == feesByX.tokenId)
    val y = assets.find(_.id == feesByY.tokenId)
    PoolFeesSnapshot(
      poolId,
      FullAsset(feesByX.tokenId, feesByX.amount, x.flatMap(_.ticker), x.flatMap(_.decimals)),
      FullAsset(feesByY.tokenId, feesByY.amount, y.flatMap(_.ticker), y.flatMap(_.decimals))
    )
  }
}
