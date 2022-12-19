package fi.spectrum.streaming.kafka

import cats.effect.Sync
import cats.syntax.option._
import cats.syntax.applicative._
import derevo.circe.{decoder, encoder}
import derevo.derive
import fi.spectrum.streaming.domain.TxEvent
import fi.spectrum.streaming.domain.TxEvent.Apply
import fs2.kafka.{Deserializer, RecordDeserializer, RecordSerializer, Serializer}
import io.circe.parser.parse
import io.circe.syntax._
import io.circe.{Decoder, Encoder}
import org.ergoplatform.ErgoLikeTransactionSerializer
import scorex.util.encode.Base64

import scala.util.Try

object serde {

  private val charset = "UTF-8"

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

  object tx {

    @derive(encoder, decoder)
    sealed trait KafkaTxEvent {
      val tx: String
    }

    @derive(encoder, decoder)
    final case class TxApply(tx: String, ts: Long) extends KafkaTxEvent

    @derive(encoder, decoder)
    final case class TxUnapply(tx: String) extends KafkaTxEvent

    implicit def txSerializer[F[_]: Sync]: RecordSerializer[F, TxEvent] = RecordSerializer.lift(
      Serializer.string.contramap { event: TxEvent =>
        val txBytes      = ErgoLikeTransactionSerializer.toBytes(event.tx)
        val base64Encode = Base64.encode(txBytes)
        event match {
          case Apply(timestamp, _) => TxApply(base64Encode, timestamp).asJson.noSpaces
          case TxEvent.Unapply(_)  => TxUnapply(base64Encode).asJson.noSpaces
        }
      }
    )

    implicit def txDeserializer[F[_]: Sync]: RecordDeserializer[F, Option[TxEvent]] = RecordDeserializer.lift(
      Deserializer.string.map { str =>
        parse(str).flatMap(_.as[KafkaTxEvent]).toOption.flatMap { event =>
          val bytes = Base64.decode(event.tx)
          val tx    = bytes.flatMap(b => Try(ErgoLikeTransactionSerializer.fromBytes(b))).toOption
          (event, tx) match {
            case (TxApply(_, ts), Some(tx)) => TxEvent.Apply(ts, tx).some
            case (TxUnapply(_), Some(tx))   => TxEvent.Unapply(tx).some
            case _                          => none[TxEvent]
          }
        }
      }
    )
  }

}
