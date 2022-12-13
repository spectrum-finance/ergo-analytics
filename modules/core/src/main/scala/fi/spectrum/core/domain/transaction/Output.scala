package fi.spectrum.core.domain.transaction

import derevo.circe.{decoder, encoder}
import derevo.derive
import fi.spectrum.core.domain.{BoxId, SErgoTree, TxId}
import tofu.logging.derivation.{loggable, show}

@derive(encoder, decoder, loggable, show)
final case class Output(
  boxId: BoxId,
  transactionId: TxId,
  value: Long,
  index: Int,
  creationHeight: Int,
  ergoTree: SErgoTree,
  assets: List[BoxAsset],
  additionalRegisters: Map[RegisterId, SConstant]
)

object Output
