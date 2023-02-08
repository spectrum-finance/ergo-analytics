package fi.spectrum.indexer.db.models

import cats.syntax.option._
import fi.spectrum.core.domain.analytics.Version
import fi.spectrum.core.domain.order.PoolId
import fi.spectrum.core.domain.pool.Pool.{LmPool, LmPoolDefault, LmPoolSelfHosted}
import fi.spectrum.core.domain.{AssetAmount, BoxId, ProtocolVersion}
import fi.spectrum.indexer.classes.ToDB
import fi.spectrum.indexer.classes.syntax.ToDBOps

final case class LmPoolDB(
  poolStateId: BoxId,
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
  executionBudget: Option[Long],
  epochIndex: Option[Int],
  timestamp: Long,
  version: Version,
  height: Int,
  protocolVersion: ProtocolVersion
)

object LmPoolDB {

  implicit def lmPool: ToDB[LmPool, LmPoolDB] = {
    case pool: LmPoolSelfHosted => pool.toDB
    case pool: LmPoolDefault    => pool.toDB
  }

  implicit def lmPoolSelfHosted: ToDB[LmPoolSelfHosted, LmPoolDB] = pool =>
    LmPoolDB(
      pool.box.boxId,
      pool.poolId,
      pool.reward,
      pool.lq,
      pool.vLq,
      pool.tmp,
      pool.epochLength,
      pool.epochsNum,
      pool.programStart,
      pool.redeemBlocksDelta,
      pool.programBudget,
      pool.maxRoundingError,
      none,
      pool.epochIndex,
      pool.timestamp,
      pool.version,
      pool.height,
      ProtocolVersion.init
    )

  implicit def lmPoolDefault: ToDB[LmPoolDefault, LmPoolDB] = pool =>
    LmPoolDB(
      pool.box.boxId,
      pool.poolId,
      pool.reward,
      pool.lq,
      pool.vLq,
      pool.tmp,
      pool.epochLength,
      pool.epochsNum,
      pool.programStart,
      pool.redeemBlocksDelta,
      pool.programBudget,
      pool.maxRoundingError,
      pool.executionBudget.some,
      pool.epochIndex,
      pool.timestamp,
      pool.version,
      pool.height,
      ProtocolVersion.init
    )

}
