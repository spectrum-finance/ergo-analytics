package fi.spectrum.streaming.domain

import org.ergoplatform.ErgoLikeTransaction

sealed trait TxEvent

object TxEvent {
  final case class Apply(timestamp: Long, tx: ErgoLikeTransaction) extends TxEvent

  final case class Unapply(tx: ErgoLikeTransaction) extends TxEvent
}
