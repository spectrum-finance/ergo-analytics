package fi.spectrum.parser.models

import derevo.circe.{decoder, encoder}
import derevo.derive
import fi.spectrum.core.domain.transaction.{BoxAsset, Output, RegisterId, SConstant}
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

object OutputTest {

  def fromExplorerOut(o: OutputTest): Output =
    Output(
      o.boxId,
      o.transactionId,
      o.value,
      o.index,
      o.creationHeight,
      o.ergoTree,
      o.assets.map(fromExplorerBox),
      o.additionalRegisters
    )

  private def fromExplorerBox(a: BoxAssetTest): BoxAsset =
    BoxAsset(a.tokenId, a.amount)
}
