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

  def toPoolSnapshot(assets: List[AssetInfo]): Option[PoolSnapshot] =
    for {
      x <- assets.find(_.id == lockedX.tokenId)
      y <- assets.find(_.id == lockedY.tokenId)
    } yield PoolSnapshot(
      id,
      FullAsset(lockedX.tokenId, lockedX.amount, x.ticker, x.decimals),
      FullAsset(lockedY.tokenId, lockedY.amount, y.ticker, y.decimals)
    )
}
