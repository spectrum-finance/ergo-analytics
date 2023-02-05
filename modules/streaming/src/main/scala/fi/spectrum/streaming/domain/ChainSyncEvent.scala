package fi.spectrum.streaming.domain

import cats.Show
import cats.syntax.show._
import derevo.circe.{decoder, encoder}
import derevo.derive
import fi.spectrum.core.domain.analytics.Processed
import fi.spectrum.core.domain.pool.Pool
import tofu.logging.Loggable

@derive(encoder, decoder)
sealed trait ChainSyncEvent

object ChainSyncEvent {

  implicit def show3: Show[ChainSyncEvent] = {
    case r: ApplyChainSync   => r.show
    case r: UnapplyChainSync => r.show
  }

  implicit def loggable3: Loggable[ChainSyncEvent] = Loggable.show

  @derive(encoder, decoder)
  final case class ApplyChainSync(order: Option[Processed.Any], pool: Option[Pool]) extends ChainSyncEvent

  @derive(encoder, decoder)
  final case class UnapplyChainSync(order: Option[Processed.Any], pool: Option[Pool]) extends ChainSyncEvent

  implicit def show1: Show[ApplyChainSync] = a =>
    s"ApplyChainSync(${a.pool.map(_.box.boxId)}, ${a.order.map(_.order.id)})"

  implicit def show2: Show[UnapplyChainSync] = a =>
    s"UnapplyChainSync(${a.pool.map(_.box.boxId)}, ${a.order.map(_.order.id)})"
}
