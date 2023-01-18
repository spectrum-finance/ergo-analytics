package fi.spectrum.streaming.kafka.models

import cats.Show
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
