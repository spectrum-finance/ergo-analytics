package fi.spectrum.streaming.domain

import derevo.circe.{decoder, encoder}
import derevo.derive
import fi.spectrum.core.domain.pool.Pool
import tofu.logging.derivation.loggable

@derive(loggable, encoder, decoder)
sealed trait PoolEvent {
  val pool: Pool
}

object PoolEvent {

  @derive(loggable, encoder, decoder)
  final case class Apply(pool: Pool) extends PoolEvent

  @derive(loggable, encoder, decoder)
  final case class Unapply(pool: Pool) extends PoolEvent
}
