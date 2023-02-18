package fi.spectrum.api.db.models

import derevo.derive
import fi.spectrum.api.models.FullAsset
import fi.spectrum.core.domain.order.PoolId
import fi.spectrum.core.domain.TokenId
import fi.spectrum.api.models.{FullAsset, Ticker}
import tofu.logging.derivation.loggable

object amm {

  @derive(loggable)
  final case class PoolInfo(firstSwapTimestamp: Long)

  final case class SwapInfo(asset: FullAsset, numTxs: Int)
  final case class DepositInfo(assetX: FullAsset, assetY: FullAsset, numTxs: Int)

  final case class AssetInfo(
    id: TokenId,
    ticker: Option[Ticker],
    decimals: Option[Int]
  ) {
    def evalDecimals: Int = decimals.getOrElse(0)
  }

  @derive(loggable)
  final case class PoolSnapshot(
    id: PoolId,
    lockedX: FullAsset,
    lockedY: FullAsset
  )

  @derive(loggable)
  final case class PoolVolumeSnapshot(
    poolId: PoolId,
    volumeByX: FullAsset,
    volumeByY: FullAsset
  )

  final case class PoolFeesSnapshot(
    poolId: PoolId,
    feesByX: FullAsset,
    feesByY: FullAsset
  )

  final case class PoolTrace(
    id: PoolId,
    lockedX: FullAsset,
    lockedY: FullAsset,
    height: Long
  )

  final case class AvgAssetAmounts(
    amountX: Long,
    amountY: Long,
    timestamp: Long,
    index: Long
  )
}
