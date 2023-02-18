package fi.spectrum.api.db.models

import derevo.derive
import fi.spectrum.api.db.models.amm.{AssetInfo, PoolSnapshot}
import fi.spectrum.api.models.FullAsset
import fi.spectrum.core.domain.AssetAmount
import fi.spectrum.core.domain.order.PoolId
import tofu.logging.derivation.loggable

@derive(loggable)
final case class PoolSnapshotDB(
  id: PoolId,
  lockedX: AssetAmount,
  lockedY: AssetAmount
) {

  def toPoolSnapshot(assets: List[AssetInfo]): PoolSnapshot = {
    val x = assets.find(_.id == lockedX.tokenId)
    val y = assets.find(_.id == lockedY.tokenId)
    PoolSnapshot(
      id,
      FullAsset(lockedX.tokenId, lockedX.amount, x.flatMap(_.ticker), x.flatMap(_.decimals)),
      FullAsset(lockedY.tokenId, lockedY.amount, y.flatMap(_.ticker), y.flatMap(_.decimals))
    )
  }
}
