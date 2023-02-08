package fi.spectrum.core.domain.pool

import derevo.circe.{decoder, encoder}
import derevo.derive
import fi.spectrum.core.domain.analytics.Version
import fi.spectrum.core.domain.analytics.Version.V1
import fi.spectrum.core.domain.order.PoolId
import fi.spectrum.core.domain.transaction.Output
import fi.spectrum.core.domain.{AssetAmount, BoxId}
import glass.classic.Lens
import glass.macros.GenContains
import tofu.logging.derivation.{loggable, show}

/** This abstraction represents any pool in our domain, e.g. Amm pool, LM pool
  */

@derive(encoder, decoder, loggable, show)
sealed trait Pool {
  val poolId: PoolId
  val box: Output

  val height: Int
  val version: Version
}

object Pool {

  @derive(encoder, decoder, show, loggable)
  final case class AmmPool(
    poolId: PoolId,
    lp: AssetAmount,
    x: AssetAmount,
    y: AssetAmount,
    feeNum: Int,
    timestamp: Long,
    box: Output,
    version: V1,
    height: Int
  ) extends Pool

  object AmmPool {
    implicit val lens: Lens[AmmPool, BoxId] = GenContains[AmmPool](_.box.boxId)
  }

  @derive(encoder, decoder, show, loggable)
  final case class LmPoolSelfHosted(
    poolId: PoolId,
    reward: AssetAmount,
    lq: AssetAmount,
    vLq: AssetAmount,
    tmp: AssetAmount,
    epochLength: Int,
    epochsNum: Int,
    programStart: Int,
    redeemBlocksDelta: Int,
    programBudget: Long,
    maxRoundingError: Long,
    epochIndex: Option[Int],
    timestamp: Long,
    box: Output,
    version: V1,
    height: Int
  ) extends Pool

  object LmPoolSelfHosted

  @derive(encoder, decoder, show, loggable)
  final case class LmPool(
    poolId: PoolId,
    reward: AssetAmount,
    lq: AssetAmount,
    vLq: AssetAmount,
    tmp: AssetAmount,
    epochLength: Int,
    epochsNum: Int,
    programStart: Int,
    redeemBlocksDelta: Int,
    programBudget: Long,
    maxRoundingError: Long,
    executionBudget: Long,
    epochIndex: Option[Int],
    timestamp: Long,
    box: Output,
    version: V1,
    height: Int
  ) extends Pool

  object LmPool
}
