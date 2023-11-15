package fi.spectrum.api.services.models.explorer

import derevo.circe.{decoder, encoder}
import derevo.derive
import fi.spectrum.core.domain.transaction.{BoxAsset, RegisterId, SConstant}
import fi.spectrum.core.domain.{Address, BoxId, HexString, SErgoTree, TxId}
import tofu.logging.derivation.loggable

@derive(encoder, decoder, loggable)
final case class ExplorerInput(
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
  assets: List[BoxAsset],
  additionalRegisters: Map[RegisterId, SConstant]
)
