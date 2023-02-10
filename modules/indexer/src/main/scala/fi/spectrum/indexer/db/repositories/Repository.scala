package fi.spectrum.indexer.db.repositories

import doobie.util.Write
import doobie.util.log.LogHandler
import doobie.{ConnectionIO, Update}
import fi.spectrum.core.domain.order.OrderId
import fi.spectrum.indexer.db.models.{UpdateEvaluatedTx, UpdateLock, UpdateRefundedTx}

/** Keeps Insert/Delete/Update api
  */
trait Repository[T, I, E] {
  def insertNoConflict(implicit lh: LogHandler, w: Write[T]): Update[T]

  def delete(implicit lh: LogHandler, w: Write[I]): Update[I]

  def updateRefunded(update: UpdateRefundedTx)(implicit lh: LogHandler): ConnectionIO[Int]

  def deleteRefunded(delete: OrderId)(implicit lh: LogHandler): ConnectionIO[Int]

  def updateExecuted(update: UpdateEvaluatedTx[E])(implicit lh: LogHandler): ConnectionIO[Int]

  def deleteExecuted(delete: OrderId)(implicit lh: LogHandler): ConnectionIO[Int]

  def updateLock(update: UpdateLock)(implicit lh: LogHandler): ConnectionIO[Int]

  def deleteLockUpdate(orderId: OrderId)(implicit lh: LogHandler): ConnectionIO[Int]
}

object Repository {
  implicit val swapsRepository: AmmSwapRepository            = new AmmSwapRepository
  implicit val redeemsRepository: AmmRedeemRepository        = new AmmRedeemRepository
  implicit val depositsRepository: AmmDepositRepository      = new AmmDepositRepository
  implicit val lmDepositsRepository: LmDepositRepository     = new LmDepositRepository
  implicit val lmCompoundRepository: LmCompoundRepository    = new LmCompoundRepository
  implicit val lmRedeemRepository: LmRedeemRepository        = new LmRedeemRepository
  implicit val locksRepository: LockRepository               = new LockRepository
  implicit val offChainsFeeRepository: OffChainFeeRepository = new OffChainFeeRepository
  implicit val poolsRepository: PoolRepository               = new PoolRepository
  implicit val blocksRepository: BlockRepository             = new BlockRepository
  implicit val assetInsert: AssetInsert                      = new AssetInsert
  implicit val lmPoolsRepo: LmPoolRepository                 = new LmPoolRepository
}
