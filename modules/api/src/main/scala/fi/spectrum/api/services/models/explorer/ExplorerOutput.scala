package fi.spectrum.api.services.models.explorer

import derevo.circe.{decoder, encoder}
import derevo.derive
import fi.spectrum.core.domain.transaction.{BoxAsset, RegisterId, SConstant}
import fi.spectrum.core.domain.{Address, BoxId, SErgoTree, TxId}
import tofu.logging.derivation.loggable

@derive(encoder, decoder, loggable)
final case class ExplorerOutput(
  boxId: BoxId,
  transactionId: TxId,
  value: Long,
  index: Int,
  globalIndex: Long,
  creationHeight: Int,
  settlementHeight: Int,
  ergoTree: SErgoTree,
  address: Address,
  assets: List[BoxAsset],
  additionalRegisters: Map[RegisterId, SConstant],
  spentTransactionId: Option[TxId]
)
