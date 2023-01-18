package fi.spectrum.parser.models

import cats.data.NonEmptyList
import derevo.circe.decoder
import derevo.derive
import fi.spectrum.core.domain.TxId
import fi.spectrum.core.domain.transaction.{BoxAsset, Input, Output, Transaction}

@derive(decoder)
final case class TransactionTest(
  id: TxId,
  blockId: BlockId,
  inclusionHeight: Int,
  timestamp: Long,
  index: Int,
  globalIndex: Long,
  numConfirmations: Int,
  inputs: List[InputTest],
  outputs: List[OutputTest]
) {

  def toTransaction: Transaction =
    Transaction(
      id,
      NonEmptyList.fromListUnsafe(inputs.map(_.boxId)),
      NonEmptyList.fromListUnsafe(outputs.map(fromExplorerOut))
    )

  private def fromExplorerBox(a: BoxAssetTest): BoxAsset =
    BoxAsset(a.tokenId, a.amount)

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
}
