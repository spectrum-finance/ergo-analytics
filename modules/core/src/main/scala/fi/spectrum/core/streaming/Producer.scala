package fi.spectrum.core.streaming

import cats.effect._
import cats.effect.syntax.resource._
import cats.tagless.InvariantK
import cats.tagless.InvariantK.ops.toAllInvariantKOps
import cats.{FlatMap, Monad, ~>}
import fi.spectrum.core.streaming.config.{KafkaConfig, ProducerConfig}
import fs2._
import fs2.kafka._
import tofu.higherKind.Embed
import tofu.lift.IsoK
import tofu.syntax.funk._
import tofu.syntax.monadic._

trait Producer[K, V, F[_]] {

  def produce: F[Record[K, V]] => F[Unit]
}

object Producer {

  final private class ProducerContainer[F[_]: FlatMap, K, V](ffa: F[Producer[K, V, F]]) extends Producer[K, V, F] {
    def produce: F[Record[K, V]] => F[Unit] = fa => ffa.flatMap(_.produce(fa))
  }

  implicit def embed[K, V]: Embed[Producer[K, V, *[_]]] =
    new Embed[Producer[K, V, *[_]]] {

      def embed[F[_]: FlatMap](ft: F[Producer[K, V, F]]): Producer[K, V, F] =
        new ProducerContainer(ft)
    }

  implicit def invK[K, V]: InvariantK[Producer[K, V, *[_]]] =
    new InvariantK[Producer[K, V, *[_]]] {

      def imapK[F[_], G[_]](af: Producer[K, V, F])(fk: F ~> G)(gK: G ~> F): Producer[K, V, G] =
        new Producer[K, V, G] {
          def produce: G[Record[K, V]] => G[Unit] = ga => fk(af.produce(gK(ga)))
        }
    }

  def make[
    I[_]: Async,
    F[_]: FlatMap,
    G[_]: Monad: KafkaConfig.Has: MonadCancel[*[_], Throwable],
    K: RecordSerializer[I, *],
    V: RecordSerializer[I, *]
  ](conf: ProducerConfig)(implicit
    isoKFG: IsoK[F, Stream[G, *]],
    isoKGI: IsoK[G, I]
  ): Resource[I, Producer[K, V, F]] =
    KafkaConfig.access.toResource.mapK(isoKGI.tof).flatMap { kafka =>
      val producerSettings =
        ProducerSettings[I, K, V]
          .withBootstrapServers(kafka.bootstrapServers.mkString(","))
      KafkaProducer.resource(producerSettings).map { prod =>
        new Live(conf, prod)
          .imapK(funK[Stream[I, *], Stream[G, *]](_.translate(isoKGI.fromF)) andThen isoKFG.fromF)(
            isoKFG.tof andThen funK[Stream[G, *], Stream[I, *]](_.translate(isoKGI.tof))
          )
      }
    }

  final private class Live[F[_]: Concurrent, K, V](
    conf: ProducerConfig,
    kafkaProducer: KafkaProducer[F, K, V]
  ) extends Producer[K, V, Stream[F, *]] {

    def produce: Pipe[F, Record[K, V], Unit] =
      _.map { case Record(k, v) =>
        ProducerRecords.one(ProducerRecord(conf.topicId.value, k, v))
      }.evalMap(kafkaProducer.produce).mapAsync(conf.parallelism)(identity).drain
  }

}
