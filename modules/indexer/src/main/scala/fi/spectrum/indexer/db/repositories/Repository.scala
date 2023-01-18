package fi.spectrum.indexer.db.repositories

import doobie.util.Write
import doobie.util.log.LogHandler
import doobie.{ConnectionIO, Update}
import fi.spectrum.core.domain.order.OrderId
import fi.spectrum.indexer.db.models.{UpdateEvaluatedTx, UpdateRefundedTx}

/** Keeps Insert/Delete/Update api
  */
trait Repository[T, I, E] {
  def insertNoConflict(implicit lh: LogHandler, w: Write[T]): Update[T]

  def delete(implicit lh: LogHandler, w: Write[I]): Update[I]

  def updateRefunded(update: UpdateRefundedTx)(implicit lh: LogHandler): ConnectionIO[Int]

  def deleteRefunded(delete: OrderId)(implicit lh: LogHandler): ConnectionIO[Int]

  def updateExecuted(update: UpdateEvaluatedTx[E])(implicit lh: LogHandler): ConnectionIO[Int]

  def deleteExecuted(delete: OrderId)(implicit lh: LogHandler): ConnectionIO[Int]
}

object Repository {
  implicit val swapsRepository: AmmSwapRepository            = new AmmSwapRepository
  implicit val redeemsRepository: RedeemRepository           = new RedeemRepository
  implicit val depositsRepository: DepositRepository         = new DepositRepository
  implicit val locksRepository: LockRepository               = new LockRepository
  implicit val offChainsFeeRepository: OffChainFeeRepository = new OffChainFeeRepository
  implicit val poolsRepository: PoolRepository               = new PoolRepository
  implicit val blocksRepository: BlockRepository             = new BlockRepository

  implicit val assetInsert: AssetInsert = new AssetInsert
}
