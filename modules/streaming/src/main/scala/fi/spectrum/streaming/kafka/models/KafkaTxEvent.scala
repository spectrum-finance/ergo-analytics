package fi.spectrum.streaming.kafka.models

import derevo.circe.{decoder, encoder}
import derevo.derive

@derive(encoder, decoder)
sealed trait KafkaTxEvent {
  val tx: String
}

object KafkaTxEvent {

  @derive(encoder, decoder)
  final case class AppliedEvent(timestamp: Long, tx: String) extends KafkaTxEvent

  @derive(encoder, decoder)
  final case class UnappliedEvent(tx: String) extends KafkaTxEvent
}
