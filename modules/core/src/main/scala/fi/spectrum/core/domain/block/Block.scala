package fi.spectrum.core.domain.block

import derevo.circe.{decoder, encoder}
import derevo.derive
import fi.spectrum.core.domain.BlockId
import glass.classic.{Lens, Optional}
import glass.macros.{GenContains, GenSubset}
import tofu.logging.derivation.loggable

@derive(encoder, decoder, loggable)
final case class Block(
  id: BlockId,
  height: Int,
  timestamp: Long
)

object Block {
  implicit val lens: Lens[Block, BlockId] = GenContains[Block](_.id)
  implicit val blockOpt: Optional[Block, Block] = GenSubset[Block, Block]
}
