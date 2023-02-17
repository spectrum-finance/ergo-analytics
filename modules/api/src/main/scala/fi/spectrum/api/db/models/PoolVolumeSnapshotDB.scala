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

  def toPoolVolumeSnapshot(assets: List[AssetInfo]): Option[PoolVolumeSnapshot] =
    for {
      x <- assets.find(_.id == volumeByX.tokenId)
      y <- assets.find(_.id == volumeByY.tokenId)
    } yield PoolVolumeSnapshot(
      poolId,
      FullAsset(volumeByX.tokenId, volumeByX.amount, x.ticker, x.decimals),
      FullAsset(volumeByY.tokenId, volumeByY.amount, y.ticker, y.decimals)
    )
}

object PoolVolumeSnapshotDB
