package fi.spectrum.indexer.db.repositories

import cats.data.NonEmptyList
import doobie.{ConnectionIO, Update}
import doobie.util.Write
import doobie.util.log.LogHandler
import fi.spectrum.core.domain.order.OrderId
import fi.spectrum.indexer.db.models.UpdateState

/** Keeps Insert/Delete/Update api
  */
trait Repository[T, I] {
  def insertNoConflict(implicit lh: LogHandler, w: Write[T]): Update[T]

  def delete(implicit lh: LogHandler, w: Write[I]): Update[I]

  def updateRefunded(update: NonEmptyList[UpdateState])(implicit lh: LogHandler): ConnectionIO[Int]

  def deleteRefunded(delete: NonEmptyList[OrderId])(implicit lh: LogHandler): ConnectionIO[Int]

  def updateExecuted(update: NonEmptyList[UpdateState])(implicit lh: LogHandler): ConnectionIO[Int]

  def deleteExecuted(delete: NonEmptyList[OrderId])(implicit lh: LogHandler): ConnectionIO[Int]
}

object Repository {
  implicit val swapsRepository: AmmSwapRepository            = new AmmSwapRepository
  implicit val redeemsRepository: RedeemRepository           = new RedeemRepository
  implicit val depositsRepository: DepositRepository         = new DepositRepository
  implicit val locksRepository: LockRepository               = new LockRepository
  implicit val offChainsFeeRepository: OffChainFeeRepository = new OffChainFeeRepository
  implicit val poolsRepository: PoolRepository               = new PoolRepository

  implicit val assetInsert: AssetInsert = new AssetInsert
}
