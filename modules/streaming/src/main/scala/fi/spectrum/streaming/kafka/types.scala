package fi.spectrum.streaming.kafka

import org.apache.kafka.clients.consumer.OffsetAndMetadata
import org.apache.kafka.common.TopicPartition

object types {

  type KafkaOffset = (TopicPartition, OffsetAndMetadata)
}
