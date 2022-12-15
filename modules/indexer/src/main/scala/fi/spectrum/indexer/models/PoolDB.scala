package fi.spectrum.indexer.models

import fi.spectrum.core.domain.{AssetAmount, BoxId, ProtocolVersion}
import fi.spectrum.core.domain.order.PoolId
import fi.spectrum.core.domain.pool.Pool.AmmPool
import fi.spectrum.indexer.classes.ToSchema

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

  implicit val toSchema: ToSchema[AmmPool, PoolDB] = pool =>
    PoolDB(
      pool.box.boxId,
      pool.poolId,
      pool.lp,
      pool.x,
      pool.y,
      pool.feeNum,
      pool.timestamp,
      pool.box.creationHeight,
      ProtocolVersion.init
    )
}
