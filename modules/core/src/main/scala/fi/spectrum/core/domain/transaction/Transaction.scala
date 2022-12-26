package fi.spectrum.core.domain.transaction

import cats.data.NonEmptyList
import derevo.circe.{decoder, encoder}
import derevo.derive
import fi.spectrum.core.domain.TxId
import org.ergoplatform.ErgoLikeTransaction
import tofu.logging.derivation.loggable

@derive(encoder, decoder, loggable)
final case class Transaction(id: TxId, inputs: NonEmptyList[Input], outputs: NonEmptyList[Output])

object Transaction {
  def fromErgoLike(tx: ErgoLikeTransaction): Transaction = ???
}
