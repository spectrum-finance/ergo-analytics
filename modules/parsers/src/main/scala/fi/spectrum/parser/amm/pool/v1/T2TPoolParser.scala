package fi.spectrum.parser.amm.pool.v1

import fi.spectrum.core.domain.analytics.Version
import fi.spectrum.core.domain.order.PoolId
import fi.spectrum.core.domain.analytics.Version.V1
import fi.spectrum.core.domain.pool.Pool.AmmPool
import fi.spectrum.core.domain.transaction.SConstant.IntConstant
import fi.spectrum.core.domain.transaction.{Output, RegisterId}
import fi.spectrum.core.domain.{AssetAmount, constants}
import fi.spectrum.parser.amm.pool.AmmPoolParser
import fi.spectrum.parser.domain.AmmType.T2T

class T2TPoolParser extends AmmPoolParser[V1, T2T] {

  def pool(output: Output, timestamp: Long): Option[AmmPool] =
    for {
      nft <- output.assets.lift(constants.t2t.IndexNFT)
      lp  <- output.assets.lift(constants.t2t.IndexLP)
      x   <- output.assets.lift(constants.t2t.IndexX)
      y   <- output.assets.lift(constants.t2t.IndexY)
      fee <- output.additionalRegisters.get(RegisterId.R4).collect { case IntConstant(x) => x }
    } yield AmmPool(
      PoolId(nft.tokenId),
      AssetAmount.fromBoxAsset(lp),
      AssetAmount.fromBoxAsset(x),
      AssetAmount.fromBoxAsset(y),
      fee,
      timestamp,
      output,
      Version.V1
    )
}

object T2TPoolParser {
  implicit def t2tPool: AmmPoolParser[V1, T2T] = new T2TPoolParser
}
