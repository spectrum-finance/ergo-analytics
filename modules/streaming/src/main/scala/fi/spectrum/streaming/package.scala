package fi.spectrum

import fi.spectrum.core.domain.TxId
import fi.spectrum.core.domain.order.{OrderId, PoolId}
import fi.spectrum.streaming.domain.{MempoolEvent, OrderEvent, PoolEvent, TxEvent}
import fi.spectrum.streaming.kafka.{Consumer, Producer}
import fi.spectrum.streaming.kafka.types.KafkaOffset

package object streaming {
  type TxEventsConsumer[S[_], F[_]]      = Consumer.Aux[TxId, Option[TxEvent], KafkaOffset, S, F]
  type MempoolEventsConsumer[S[_], F[_]] = Consumer.Aux[TxId, Option[MempoolEvent], KafkaOffset, S, F]
  type OrderEventsConsumer[S[_], F[_]]   = Consumer.Aux[OrderId, Option[OrderEvent], KafkaOffset, S, F]
  type PoolsEventsConsumer[S[_], F[_]]   = Consumer.Aux[PoolId, Option[PoolEvent], KafkaOffset, S, F]

  type OrderEventsProducer[F[_]] = Producer[OrderId, OrderEvent, F]
  type PoolsEventsProducer[F[_]] = Producer[PoolId, PoolEvent, F]
}
