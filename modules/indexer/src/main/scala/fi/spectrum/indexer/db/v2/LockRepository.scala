package fi.spectrum.indexer.db.v2

import cats.Applicative
import cats.data.NonEmptyList
import doobie.util.log
import fi.spectrum.core.domain.BoxId
import fi.spectrum.core.domain.order.OrderId
import fi.spectrum.indexer.models.{LockDB, UpdateState}

final class LockRepository extends Repository[LockDB, OrderId] {

  val tableName: String = "locks"

  val fields: List[String] = List(
    "order_id",
    "deadline",
    "token_id",
    "amount",
    "redeemer",
    "contract_version"
  )

  val field: String = "order_id"

  override def updateExecuted(update: NonEmptyList[UpdateState])(implicit
    lh: log.LogHandler
  ): doobie.ConnectionIO[Int] =
    Applicative[doobie.ConnectionIO].pure(0)

  override def updateRefunded(update: NonEmptyList[UpdateState])(implicit
    lh: log.LogHandler
  ): doobie.ConnectionIO[Int] =
    Applicative[doobie.ConnectionIO].pure(0)
}
