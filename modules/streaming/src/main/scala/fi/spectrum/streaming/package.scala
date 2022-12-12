package fi.spectrum

import fi.spectrum.core.domain.TxId
import fi.spectrum.streaming.domain.{OrderEvent, TxEvent}
import fi.spectrum.streaming.kafka.{Consumer, Producer}
import fi.spectrum.streaming.kafka.types.KafkaOffset

package object streaming {
  type TxEventsConsumer[S[_], F[_]]    = Consumer.Aux[TxId, TxEvent, KafkaOffset, S, F]
  type OrderEventsConsumer[S[_], F[_]] = Consumer.Aux[TxId, OrderEvent, KafkaOffset, S, F]
  type OrderEventsProducer[F[_]]       = Producer[TxId, OrderEvent, F]
}
