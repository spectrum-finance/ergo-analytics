package fi.spectrum.indexer.models

import cats.syntax.option._
import fi.spectrum.core.domain.analytics.{ProcessedOrder, Version}
import fi.spectrum.core.domain.order.Order.Lock.LockV1
import fi.spectrum.core.domain.order.{Order, OrderId}
import fi.spectrum.core.domain.{AssetAmount, PubKey}
import fi.spectrum.indexer.classes.ToSchema
import glass.Subset

final case class LockDB(
  orderId: OrderId,
  deadline: Int,
  amount: AssetAmount,
  redeemer: PubKey,
  version: Version
)

object LockDB {
  implicit val toSchema: ToSchema[ProcessedOrder, Option[LockDB]] = processed => ___V1.transform(processed)

  val ___V1: ToSchema[ProcessedOrder, Option[LockDB]] =
    processed => {
      Subset[Order.Any, LockV1].getOption(processed.order) match {
        case Some(lock) =>
          LockDB(
            lock.id,
            lock.deadline,
            lock.amount,
            lock.redeemer.value,
            lock.version
          ).some
        case None =>
          none
      }
    }
}
