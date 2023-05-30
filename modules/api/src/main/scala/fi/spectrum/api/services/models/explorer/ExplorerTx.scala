package fi.spectrum.api.services.models.explorer

import derevo.circe.decoder
import derevo.derive
import fi.spectrum.core.domain.{BlockId, TxId}
import tofu.logging.derivation.loggable

@derive(decoder, loggable)
final case class ExplorerTx(
  id: TxId,
  blockId: BlockId,
  inclusionHeight: Int,
  timestamp: Long,
  index: Int,
  globalIndex: Long,
  numConfirmations: Int,
  inputs: List[ExplorerInput],
  outputs: List[ExplorerOutput]
)
