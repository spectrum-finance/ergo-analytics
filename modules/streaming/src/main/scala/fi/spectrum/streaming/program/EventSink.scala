package fi.spectrum.streaming.program

import fi.spectrum.streaming.domain.TxEvent

trait EventSink[S[_]] {
  def stream: S[TxEvent]
}