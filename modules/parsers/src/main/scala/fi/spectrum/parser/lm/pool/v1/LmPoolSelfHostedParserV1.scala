package fi.spectrum.parser.lm.pool.v1

import fi.spectrum.core.domain.AssetAmount
import fi.spectrum.core.domain.analytics.Version
import fi.spectrum.core.domain.analytics.Version.V1
import fi.spectrum.core.domain.order.PoolId
import fi.spectrum.core.domain.pool.Pool.LmPoolSelfHosted
import fi.spectrum.core.domain.transaction.SConstant.{IntConstant, IntsConstant, LongConstant}
import fi.spectrum.core.domain.transaction.{Output, RegisterId}
import fi.spectrum.parser.domain.LmPoolType
import fi.spectrum.parser.lm.pool.LmPoolParser

class LmPoolSelfHostedParserV1 extends LmPoolParser[V1, LmPoolType.SelfHosted] {

  def pool(output: Output, timestamp: Long, height: Int): Option[LmPoolSelfHosted] =
    for {
      nft    <- output.assets.headOption
      reward <- output.assets.lift(1)
      lp     <- output.assets.lift(2)
      vLq    <- output.assets.lift(3)
      tmp    <- output.assets.lift(4)
      (epochLength, epochsNum, programStart, redeemBlocksDelta) <-
        output.additionalRegisters
          .get(RegisterId.R4)
          .collect { case IntsConstant(x :: x1 :: x2 :: x3 :: Nil) => (x, x1, x2, x3) }
      programBudget    <- output.additionalRegisters.get(RegisterId.R5).collect { case LongConstant(value) => value }
      maxRoundingError <- output.additionalRegisters.get(RegisterId.R6).collect { case LongConstant(value) => value }
      epochIndex = output.additionalRegisters.get(RegisterId.R7).collect { case IntConstant(value) => value }
    } yield LmPoolSelfHosted(
      PoolId(nft.tokenId),
      AssetAmount.fromBoxAsset(reward),
      AssetAmount.fromBoxAsset(lp),
      AssetAmount.fromBoxAsset(vLq),
      AssetAmount.fromBoxAsset(tmp),
      epochLength,
      epochsNum,
      programStart,
      redeemBlocksDelta,
      programBudget,
      maxRoundingError,
      epochIndex,
      timestamp,
      output,
      Version.V1,
      height
    )
}

object LmPoolSelfHostedParserV1 {
  implicit def v1LmPoolSelfHosted: LmPoolParser[V1, LmPoolType.SelfHosted] = new LmPoolSelfHostedParserV1
}
