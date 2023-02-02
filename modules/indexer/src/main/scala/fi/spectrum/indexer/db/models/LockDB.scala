package fi.spectrum.indexer.db.models

import fi.spectrum.core.domain.analytics.{Processed, Version}
import fi.spectrum.core.domain.order.Order.Lock.LockV1
import fi.spectrum.core.domain.order.{Order, OrderId}
import fi.spectrum.core.domain.{AssetAmount, PubKey, TxId}
import fi.spectrum.indexer.classes.ToDB
import fi.spectrum.indexer.classes.syntax._

final case class LockDB(
  orderId: OrderId,
  txId: TxId,
  deadline: Int,
  amount: AssetAmount,
  redeemer: PubKey,
  version: Version,
  evalTxId: Option[TxId],
  evalType: Option[String]
)

object LockDB {

  implicit val toDB: ToDB[Processed[Order.Lock], LockDB] = processed => {
    processed.order match {
      case lock: LockV1 => processed.widen(lock).toDB
    }
  }

  implicit val ___V1: ToDB[Processed[LockV1], LockDB] =
    processed => {
      LockDB(
        processed.order.id,
        processed.state.txId,
        processed.order.deadline,
        processed.order.amount,
        processed.order.redeemer.value,
        processed.order.version,
        None,
        None
      )
    }
}
