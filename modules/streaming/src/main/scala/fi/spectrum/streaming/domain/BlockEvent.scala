package fi.spectrum.streaming.domain

import derevo.circe.decoder
import derevo.derive
import fi.spectrum.core.domain.BlockId

@derive(decoder)
sealed trait BlockEvent {
  val timestamp: Long
  val height: Long
  val id: BlockId
}

object BlockEvent {
  @derive(decoder)
  final case class BlockApply(timestamp: Long, height: Long, id: BlockId) extends BlockEvent
  @derive(decoder)
  final case class BlockUnapply(timestamp: Long, height: Long, id: BlockId) extends BlockEvent
}
