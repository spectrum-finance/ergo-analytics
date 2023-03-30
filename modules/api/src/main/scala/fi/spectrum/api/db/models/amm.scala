package fi.spectrum.api.db.models

import derevo.circe.{decoder, encoder}
import derevo.derive
import fi.spectrum.api.models.FullAsset
import fi.spectrum.core.domain.order.PoolId
import fi.spectrum.core.domain.{AssetAmount, TokenId}
import fi.spectrum.api.models.{FullAsset, Ticker}
import tofu.logging.derivation.loggable

object amm {

  @derive(loggable)
  final case class PoolInfo(firstSwapTimestamp: Long)

  final case class AssetInfo(
    id: TokenId,
    ticker: Option[Ticker],
    decimals: Option[Int]
  ) {
    def evalDecimals: Int = decimals.getOrElse(0)
  }

  @derive(loggable, encoder, decoder)
  final case class PoolSnapshot(
    id: PoolId,
    fee: Int,
    lockedX: FullAsset,
    lockedY: FullAsset,
    lp: AssetAmount
  ) {

    def supplyLP: Long = 0x7fffffffffffffffL - lp.amount

    def shares(lpIn: AssetAmount): (FullAsset, FullAsset) =
      lockedX.withAmount(BigInt(lpIn.amount) * lockedX.amount / supplyLP) ->
      lockedY.withAmount(BigInt(lpIn.amount) * lockedY.amount / supplyLP)
  }

  @derive(loggable, encoder, decoder)
  final case class PoolVolumeSnapshot(
    poolId: PoolId,
    volumeByX: FullAsset,
    volumeByY: FullAsset
  )


  @derive(encoder, decoder)
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
