package fi.spectrum.api.db.models

import fi.spectrum.api.db.models.amm.{PoolFeesSnapshot, PoolSnapshot}
import cats.syntax.option._

final case class PoolFeesSnapshotDB(feesByX: Option[Long], feesByY: Option[Long]) {

  def toPoolFeesSnapshot(pool: PoolSnapshot): Option[PoolFeesSnapshot] =
    if (feesByX.isEmpty && feesByY.isEmpty) None
    else
      PoolFeesSnapshot(
        pool.id,
        pool.lockedX.withAmount(feesByX.getOrElse(0L)),
        pool.lockedY.withAmount(feesByY.getOrElse(0L))
      ).some
}
