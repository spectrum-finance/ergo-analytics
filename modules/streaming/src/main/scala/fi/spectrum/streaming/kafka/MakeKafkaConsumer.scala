package fi.spectrum.streaming.kafka

import cats.effect._
import cats.{FlatMap, Functor}
import fi.spectrum.streaming.kafka.config.{ConsumerConfig, KafkaConfig}
import fs2.Stream
import fs2.kafka.{AutoOffsetReset, ConsumerSettings, KafkaConsumer, RecordDeserializer}
import tofu.higherKind.Embed
import tofu.syntax.monadic._

/** Kafka consumer instance maker.
  */
trait MakeKafkaConsumer[F[_], K, V] {

  def apply(config: ConsumerConfig): Stream[F, KafkaConsumer[F, K, V]]
}

object MakeKafkaConsumer {

  final private class MakeKafkaConsumerContainer[F[_]: Functor, K, V](ft: F[MakeKafkaConsumer[F, K, V]])
    extends MakeKafkaConsumer[F, K, V] {

    def apply(config: ConsumerConfig): Stream[F, KafkaConsumer[F, K, V]] =
      Stream.force(ft.map(_(config)))
  }

  implicit def embed[K, V]: Embed[MakeKafkaConsumer[*[_], K, V]] =
    new Embed[MakeKafkaConsumer[*[_], K, V]] {

      def embed[F[_]: FlatMap](ft: F[MakeKafkaConsumer[F, K, V]]): MakeKafkaConsumer[F, K, V] =
        new MakeKafkaConsumerContainer(ft)
    }

  def make[
    F[_]: Async,
    K: RecordDeserializer[F, *],
    V: RecordDeserializer[F, *]
  ](kafka: KafkaConfig): MakeKafkaConsumer[F, K, V] =
    (config: ConsumerConfig) => {
      val settings =
        ConsumerSettings[F, K, V]
          .withAutoOffsetReset(AutoOffsetReset.Earliest)
          .withBootstrapServers(kafka.bootstrapServers.mkString(","))
          .withGroupId(config.groupId.value)
          .withClientId(config.clientId.value)
          .withEnableAutoCommit(false)
      KafkaConsumer.stream(settings)
    }
}
