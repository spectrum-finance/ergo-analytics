package fi.spectrum.api.db.models

import fi.spectrum.api.db.models.amm.{PoolFeesSnapshot, PoolSnapshot}

final case class PoolFeesSnapshotDB(feesByX: Long, feesByY: Long) {

  def tooPoolFeesSnapshot(pool: PoolSnapshot): PoolFeesSnapshot =
    PoolFeesSnapshot(
      pool.id,
      pool.lockedX.withAmount(feesByX),
      pool.lockedY.withAmount(feesByY)
    )
}
