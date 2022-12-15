package fi.spectrum.parser.amm.pool.v1

import fi.spectrum.core.domain.order.PoolId
import fi.spectrum.core.domain.analytics.Version.V1
import fi.spectrum.core.domain.pool.Pool.AmmPool
import fi.spectrum.core.domain.transaction.SConstant.IntConstant
import fi.spectrum.core.domain.transaction.{Output, RegisterId}
import fi.spectrum.core.domain.{constants, AssetAmount}
import fi.spectrum.parser.amm.pool.AmmPoolParser
import fi.spectrum.parser.domain.AmmType.N2T

class N2TPoolParser extends AmmPoolParser[V1, N2T] {

  def pool(output: Output, timestamp: Long): Option[AmmPool] =
    for {
      nft <- output.assets.lift(constants.n2t.IndexNFT)
      lp  <- output.assets.lift(constants.n2t.IndexLP)
      y   <- output.assets.lift(constants.n2t.IndexY)
      fee <- output.additionalRegisters.get(RegisterId.R4).collect { case IntConstant(x) => x }
    } yield AmmPool(
      PoolId(nft.tokenId),
      AssetAmount.fromBoxAsset(lp),
      AssetAmount.native(output.value),
      AssetAmount.fromBoxAsset(y),
      fee,
      timestamp,
      output
    )
}

object N2TPoolParser {
  implicit def n2tPool: AmmPoolParser[V1, N2T] = new N2TPoolParser
}
