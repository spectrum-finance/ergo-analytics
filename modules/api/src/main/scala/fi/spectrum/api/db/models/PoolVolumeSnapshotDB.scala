package fi.spectrum.api.db.models

import derevo.derive
import fi.spectrum.api.db.models.amm.{AssetInfo, PoolSnapshot, PoolVolumeSnapshot}
import fi.spectrum.api.models.FullAsset
import fi.spectrum.core.domain.AssetAmount
import fi.spectrum.core.domain.order.PoolId
import tofu.logging.derivation.loggable

@derive(loggable)
final case class PoolVolumeSnapshotDB(
  poolId: PoolId,
  volumeByX: Long,
  volumeByY: Long
) {

  def toPoolVolumeSnapshot(pools: List[PoolSnapshot]): Option[PoolVolumeSnapshot] =
    pools.find(_.id == poolId).map { ammPool =>
      PoolVolumeSnapshot(
        poolId,
        ammPool.lockedX.withAmount(volumeByX),
        ammPool.lockedY.withAmount(volumeByY)
      )
    }
}

object PoolVolumeSnapshotDB
