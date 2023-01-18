package fi.spectrum.indexer.db.models

import fi.spectrum.core.domain.analytics.{Processed, Version}
import fi.spectrum.core.domain.order.Order.Lock.LockV1
import fi.spectrum.core.domain.order.{Order, OrderId}
import fi.spectrum.core.domain.pool.Pool
import fi.spectrum.core.domain.{AssetAmount, PubKey}
import fi.spectrum.indexer.classes.syntax._
import fi.spectrum.indexer.classes.ToDB

final case class LockDB(
  orderId: OrderId,
  deadline: Int,
  amount: AssetAmount,
  redeemer: PubKey,
  version: Version
)

object LockDB {

  implicit val toDBNonUpdatable: ToDB[Order.Lock, LockDB] = { case order: LockV1 =>
    LockDB(
      order.id,
      order.deadline,
      order.amount,
      order.redeemer.value,
      order.version
    )
  }

  implicit val toDB: ToDB[Processed[Order.Lock], LockDB] = processed => {
    processed.order match {
      case lock: LockV1 => processed.widen(lock).toDB
    }
  }

  implicit val ___V1: ToDB[Processed[LockV1], LockDB] =
    processed => {
      LockDB(
        processed.order.id,
        processed.order.deadline,
        processed.order.amount,
        processed.order.redeemer.value,
        processed.order.version
      )
    }
}
