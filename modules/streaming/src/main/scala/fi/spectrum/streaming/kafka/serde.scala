package fi.spectrum.streaming.kafka

import cats.effect.Sync
import cats.syntax.applicative._
import fi.spectrum.core.domain.{BlockId, TxId}
import fs2.kafka.{Deserializer, RecordDeserializer, RecordSerializer, Serializer}
import io.circe.Encoder
import io.circe.syntax._

object serde {

  private val charset = "UTF-8"

  object string {

    implicit def txIdDeserializer[F[_]: Sync]: RecordDeserializer[F, TxId] = deserializerString(TxId.apply)

    implicit def blockIdDeserializer[F[_]: Sync]: RecordDeserializer[F, BlockId] = deserializerString(BlockId.apply)

    def deserializerString[F[_]: Sync, A](f: String => A): RecordDeserializer[F, A] =
      RecordDeserializer.lift(Deserializer.string.map(f))
  }

  object json {

    implicit def deserializerViaKafkaDecoder[F[_]: Sync, A](implicit
      decoder: KafkaDecoder[A, F]
    ): RecordDeserializer[F, A] =
      RecordDeserializer.lift {
        Deserializer.lift(decoder.decode)
      }

    implicit def serializerViaCirceEncoder[F[_]: Sync, A: Encoder]: RecordSerializer[F, A] =
      RecordSerializer.lift {
        Serializer.lift { a =>
          a.asJson.noSpacesSortKeys.getBytes(charset).pure
        }
      }
  }
}
