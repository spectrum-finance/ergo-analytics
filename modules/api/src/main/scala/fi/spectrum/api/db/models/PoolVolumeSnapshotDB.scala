package fi.spectrum.api.db.models

import derevo.derive
import fi.spectrum.api.db.models.amm.{PoolSnapshot, PoolVolumeSnapshot}
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

  def toPoolVolumeSnapshot(pools: List[PoolSnapshot]): PoolVolumeSnapshot = {
    val x = pools.find(_.lockedX.id == volumeByX.tokenId)
    val y = pools.find(_.lockedY.id == volumeByY.tokenId)
    PoolVolumeSnapshot(
      poolId,
      FullAsset(volumeByX.tokenId, volumeByX.amount, x.flatMap(_.lockedX.ticker), x.flatMap(_.lockedX.decimals)),
      FullAsset(volumeByY.tokenId, volumeByY.amount, y.flatMap(_.lockedY.ticker), y.flatMap(_.lockedY.decimals))
    )
  }
}

object PoolVolumeSnapshotDB
