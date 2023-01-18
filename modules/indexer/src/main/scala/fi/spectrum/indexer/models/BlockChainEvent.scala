package fi.spectrum.indexer.models

import cats.Show
import fi.spectrum.core.domain.analytics.Processed
import fi.spectrum.core.domain.pool.Pool
import tofu.logging.Loggable

sealed trait BlockChainEvent[A] {
  val event: A
}

object BlockChainEvent {
  type OrderEvent = BlockChainEvent[Processed.Any]
  type PoolEvent  = BlockChainEvent[Pool]

  implicit def showOrderEvent: Show[OrderEvent] = o => s"OrderEvent(${o.event.order.id}, ${o.event.state}})"
  implicit def showPoolEvent: Show[PoolEvent]   = p => s"PoolEvent(${p.event.poolId}, ${p.event.box.boxId}})"

  implicit def loggableOrderEvent: Loggable[OrderEvent] = Loggable.show
  implicit def loggablePoolEvent: Loggable[PoolEvent]   = Loggable.show

  final case class Apply[A](event: A) extends BlockChainEvent[A]

  final case class Unapply[A](event: A) extends BlockChainEvent[A]
}
