package fi.spectrum.streaming.domain

import fi.spectrum.core.domain.BlockId

trait BlockEvent {
  val timestamp: Long
  val height: Long
  val id: BlockId
}

object BlockEvent {
  final case class BlockApply(timestamp: Long, height: Long, id: BlockId) extends BlockEvent
  final case class BlockUnapply(timestamp: Long, height: Long, id: BlockId) extends BlockEvent
}
