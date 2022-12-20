package fi.spectrum.streaming.domain

import cats.Show
import derevo.circe.{decoder, encoder}
import derevo.derive
import org.ergoplatform.ErgoLikeTransaction
import tofu.logging.Loggable
import tofu.logging.derivation.loggable

@derive(loggable)
final case class MempoolEvent(tx: ErgoLikeTransaction)

object MempoolEvent {
  implicit val showErgoLikeTx: Show[ErgoLikeTransaction]         = _.toString()
  implicit val loggableErgoLikeTx: Loggable[ErgoLikeTransaction] = Loggable.show
}
