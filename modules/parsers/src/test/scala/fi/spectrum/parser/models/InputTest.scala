package fi.spectrum.parser.models

import derevo.circe.{decoder, encoder}
import derevo.derive
import fi.spectrum.core.domain.transaction.{RegisterId, SConstant}
import fi.spectrum.core.domain.{Address, BoxId, HexString, SErgoTree, TxId}
import tofu.logging.derivation.loggable

@derive(encoder, decoder)
final case class InputTest(
  boxId: BoxId,
  value: Long,
  index: Int,
  spendingProof: Option[HexString],
  outputTransactionId: TxId,
  outputIndex: Int,
  outputGlobalIndex: Long,
  outputCreatedAt: Int,
  outputSettledAt: Int,
  ergoTree: SErgoTree,
  address: Address,
  assets: List[BoxAssetTest],
  additionalRegisters: Map[RegisterId, SConstant]
)
