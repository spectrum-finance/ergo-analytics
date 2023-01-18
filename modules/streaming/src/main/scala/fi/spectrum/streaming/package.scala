package fi.spectrum

import fi.spectrum.core.domain.{BlockId, TxId}
import fi.spectrum.streaming.domain.TransactionEvent
import fi.spectrum.streaming.kafka.Consumer
import fi.spectrum.streaming.kafka.models.{BlockEvent, MempoolEvent}
import fi.spectrum.streaming.kafka.types.KafkaOffset

package object streaming {
  type TxConsumer[S[_], F[_]]      = Consumer.Aux[TxId, Option[TransactionEvent], KafkaOffset, S, F]
  type MempoolConsumer[S[_], F[_]] = Consumer.Aux[TxId, Option[MempoolEvent], KafkaOffset, S, F]
  type BlocksConsumer[S[_], F[_]]  = Consumer.Aux[BlockId, Option[BlockEvent], KafkaOffset, S, F]
}
