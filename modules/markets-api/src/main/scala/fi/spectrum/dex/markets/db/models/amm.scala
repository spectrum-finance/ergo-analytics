package fi.spectrum.dex.markets.db.models

import derevo.derive
import fi.spectrum.core.common.FullAsset
import fi.spectrum.core.common.types.Ticker
import fi.spectrum.core.domain.order.PoolId
import fi.spectrum.core.domain.TokenId
import tofu.logging.derivation.loggable

object amm {

  final case class PoolInfo(confirmedAt: Long)

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
    lockedY: FullAsset,
    dummy: String
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
    height: Long,
    gindex: Long
  )

  final case class AvgAssetAmounts(
    amountX: Long,
    amountY: Long,
    timestamp: Long,
    index: Long
  )
}
