package fi.spectrum.streaming

import fi.spectrum.core.domain.{BlockId, TxId}
import fi.spectrum.streaming.domain.{BlockEvent, ChainSyncEvent, MempoolEvent, TransactionEvent}
import fi.spectrum.streaming.kafka.types.KafkaOffset

package object kafka {
  type TxConsumer[S[_], F[_]]      = Consumer.Aux[TxId, Option[TransactionEvent], KafkaOffset, S, F]
  type MempoolConsumer[S[_], F[_]] = Consumer.Aux[TxId, Option[MempoolEvent], KafkaOffset, S, F]
  type BlocksConsumer[S[_], F[_]]  = Consumer.Aux[BlockId, Option[BlockEvent], KafkaOffset, S, F]

  type CSConsumer[S[_], F[_]] = Consumer.Aux[String, Either[Throwable, Option[ChainSyncEvent]], KafkaOffset, S, F]
  type CSProducer[S[_]]       = Producer[String, ChainSyncEvent, S]
}
