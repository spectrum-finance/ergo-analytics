package fi.spectrum.indexer.db.models

import fi.spectrum.core.domain.order.PoolId
import fi.spectrum.core.domain.pool.Pool
import fi.spectrum.core.domain.pool.Pool.AmmPool
import fi.spectrum.core.domain.{AssetAmount, BoxId, ProtocolVersion}
import fi.spectrum.indexer.classes.ToDB

final case class PoolDB(
  poolStateId: BoxId,
  poolId: PoolId,
  lp: AssetAmount,
  x: AssetAmount,
  y: AssetAmount,
  feeNum: Int,
  timestamp: Long,
  height: Int,
  protocolVersion: ProtocolVersion
)

object PoolDB {

  implicit def amm: ToDB[AmmPool, PoolDB] = pool =>
    PoolDB(
      pool.box.boxId,
      pool.poolId,
      pool.lp,
      pool.x,
      pool.y,
      pool.feeNum,
      pool.timestamp,
      pool.height,
      ProtocolVersion.init
    )

}
