package fi.spectrum.indexer.db.repositories

import doobie.{ConnectionIO, Update}
import doobie.util.log.LogHandler
import fi.spectrum.core.domain.order.OrderId
import fi.spectrum.indexer.db.classes.{DeleteRepository, Repository}
import fi.spectrum.indexer.db.models.{LockDB, UpdateLock}

final class LockRepository extends Repository[LockDB, OrderId] {

  val tableName: String = "lq_locks"

  val fields: List[String] = List(
    "order_id",
    "transaction_id",
    "timestamp",
    "deadline",
    "token_id",
    "amount",
    "redeemer",
    "contract_version",
    "evaluation_transaction_id",
    "evaluation_lock_type"
  )

  val field: String = "order_id"

  def updateLock(update: UpdateLock)(implicit lh: LogHandler): ConnectionIO[Int] =
    Update[UpdateLock](
      s"update $tableName set evaluation_transaction_id=?, evaluation_lock_type=? where order_id=?"
    )
      .toUpdate0(update)
      .run

  def deleteLockUpdate(orderId: OrderId)(implicit lh: LogHandler): ConnectionIO[Int] =
    Update[OrderId](
      s"update $tableName set evaluation_transaction_id=null, evaluation_lock_type=null where order_id=?"
    )
      .toUpdate0(orderId)
      .run
}
