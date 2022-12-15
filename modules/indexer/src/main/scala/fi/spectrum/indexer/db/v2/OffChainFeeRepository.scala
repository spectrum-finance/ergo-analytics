package fi.spectrum.indexer.db.v2

import cats.Applicative
import cats.data.NonEmptyList
import doobie.util.log
import fi.spectrum.core.domain.BoxId
import fi.spectrum.core.domain.order.OrderId
import fi.spectrum.indexer.models.{LockDB, OffChainFeeDB, UpdateState}

final class OffChainFeeRepository extends Repository[OffChainFeeDB, BoxId] {

  val fields: List[String] = List(
    "pool_id",
    "order_id",
    "output_id",
    "pub_key",
    "fee",
    "fee_type"
  )

  val tableName: String = "off_chain_fee"

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
