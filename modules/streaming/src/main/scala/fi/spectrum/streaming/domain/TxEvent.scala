package fi.spectrum.streaming.domain

import cats.Show
import derevo.derive
import org.ergoplatform.ErgoLikeTransaction
import tofu.logging.Loggable
import tofu.logging.derivation.loggable

@derive(loggable)
sealed trait TxEvent {
  val tx: ErgoLikeTransaction
}

object TxEvent {

  @derive(loggable)
  final case class Apply(timestamp: Long, tx: ErgoLikeTransaction) extends TxEvent

  @derive(loggable)
  final case class Unapply(tx: ErgoLikeTransaction) extends TxEvent

  implicit val showErgoLikeTx: Show[ErgoLikeTransaction]         = _.toString()
  implicit val loggableErgoLikeTx: Loggable[ErgoLikeTransaction] = Loggable.show

}
