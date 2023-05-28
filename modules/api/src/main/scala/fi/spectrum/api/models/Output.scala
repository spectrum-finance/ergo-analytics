package fi.spectrum.api.models

import derevo.cats.show
import derevo.circe.{decoder, encoder}
import derevo.derive
import fi.spectrum.core.domain.transaction.{BoxAsset, RegisterId, SConstant}
import fi.spectrum.core.domain.{BoxId, SErgoTree, TxId}
import tofu.logging.derivation.loggable

@derive(show, encoder, decoder, loggable)
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
