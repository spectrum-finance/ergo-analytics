package fi.spectrum.indexer.db.models

import fi.spectrum.core.domain.BlockId
import fi.spectrum.core.domain.block.Block
import fi.spectrum.indexer.classes.ToDB

final case class BlockDB(
  id: BlockId,
  height: Int,
  timestamp: Long
)

object BlockDB {
  implicit def toDb: ToDB[Block, BlockDB] = block => BlockDB(block.id, block.height, block.timestamp)
}
