package fi.spectrum.parser.models

import derevo.circe.{decoder, encoder}
import derevo.derive
import fi.spectrum.core.domain.transaction.{RegisterId, SConstant}
import fi.spectrum.core.domain.{Address, BoxId, SErgoTree, TxId}
import tofu.logging.derivation.loggable

@derive(encoder, decoder)
final case class OutputTest(
  boxId: BoxId,
  transactionId: TxId,
  value: Long,
  index: Int,
  globalIndex: Long,
  creationHeight: Int,
  settlementHeight: Int,
  ergoTree: SErgoTree,
  address: Address,
  assets: List[BoxAssetTest],
  additionalRegisters: Map[RegisterId, SConstant]
)
