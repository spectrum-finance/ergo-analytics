package fi.spectrum.indexer.models

import fi.spectrum.core.domain.analytics.{ProcessedOrder, Version}
import fi.spectrum.core.domain.order.Order.Lock.LockV1
import fi.spectrum.core.domain.order.{Order, OrderId}
import fi.spectrum.core.domain.pool.Pool
import fi.spectrum.core.domain.{AssetAmount, PubKey}
import fi.spectrum.indexer.classes.syntax._
import fi.spectrum.indexer.classes.{ToDB, ToSchema}

final case class LockDB(
  orderId: OrderId,
  deadline: Int,
  amount: AssetAmount,
  redeemer: PubKey,
  version: Version
)

object LockDB {

  implicit val toSchema: ToSchema[ProcessedOrder[Order.AnyLock], LockDB] = processed => {
    processed.order match {
      case lock: LockV1 => processed.widen(lock).toDB
    }
  }

  implicit val ___V1: ToDB[ProcessedOrder[LockV1], LockDB] =
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
