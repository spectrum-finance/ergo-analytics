package fi.spectrum.streaming.domain

import derevo.circe.{decoder, encoder}
import derevo.derive
import fi.spectrum.core.domain.block.Block
import tofu.logging.derivation.loggable

@derive(loggable, encoder, decoder)
sealed trait BlockEvent {
  val block: Block
}

object BlockEvent {

  @derive(loggable, encoder, decoder)
  final case class Apply(block: Block) extends BlockEvent

  @derive(loggable, encoder, decoder)
  final case class Unapply(block: Block) extends BlockEvent
}
