package fi.spectrum.api.db.models

import derevo.derive
import fi.spectrum.api.db.models.amm.{AssetInfo, PoolVolumeSnapshot}
import fi.spectrum.api.models.FullAsset
import fi.spectrum.core.domain.AssetAmount
import fi.spectrum.core.domain.order.PoolId
import tofu.logging.derivation.loggable

@derive(loggable)
final case class PoolVolumeSnapshotDB(
  poolId: PoolId,
  volumeByX: AssetAmount,
  volumeByY: AssetAmount
) {

  def toPoolVolumeSnapshot(assets: List[AssetInfo]): PoolVolumeSnapshot = {
    val x = assets.find(_.id == volumeByX.tokenId)
    val y = assets.find(_.id == volumeByY.tokenId)
    PoolVolumeSnapshot(
      poolId,
      FullAsset(volumeByX.tokenId, volumeByX.amount, x.flatMap(_.ticker), x.flatMap(_.decimals)),
      FullAsset(volumeByY.tokenId, volumeByY.amount, y.flatMap(_.ticker), y.flatMap(_.decimals))
    )
  }
}

object PoolVolumeSnapshotDB
