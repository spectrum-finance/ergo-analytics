package fi.spectrum.core.domain.transaction

import cats.data.NonEmptyList
import derevo.circe.{decoder, encoder}
import derevo.derive
import fi.spectrum.core.domain.{BoxId, TxId}
import org.ergoplatform.ErgoLikeTransaction
import scorex.util.ModifierId
import scorex.util.encode.Base16
import tofu.logging.derivation.loggable

@derive(encoder, decoder, loggable)
final case class Transaction(id: TxId, inputs: NonEmptyList[BoxId], outputs: NonEmptyList[Output])

object Transaction {

  def fromErgoLike(tx: ErgoLikeTransaction): Transaction =
    Transaction(
      TxId(ModifierId !@@ tx.id),
      NonEmptyList.fromListUnsafe(tx.inputs.map(_.boxId).map(BoxId.fromErgo).toList),
      NonEmptyList.fromListUnsafe(tx.outputs.toList.map(Output.fromErgoBox(_, tx.id))).sortBy(_.index)
    )
}
