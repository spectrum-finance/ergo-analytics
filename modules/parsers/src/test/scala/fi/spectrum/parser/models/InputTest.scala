package fi.spectrum.parser.models

import derevo.circe.{decoder, encoder}
import derevo.derive
import fi.spectrum.core.domain.transaction.{BoxAsset, Output, RegisterId, SConstant}
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

object InputTest {

  def toOutput(o: InputTest): Output =
    Output(
      o.boxId,
      o.outputTransactionId,
      o.value,
      o.index,
      o.outputSettledAt,
      o.ergoTree,
      o.assets.map(fromExplorerBox),
      o.additionalRegisters
    )

  private def fromExplorerBox(a: BoxAssetTest): BoxAsset =
    BoxAsset(a.tokenId, a.amount)
}
