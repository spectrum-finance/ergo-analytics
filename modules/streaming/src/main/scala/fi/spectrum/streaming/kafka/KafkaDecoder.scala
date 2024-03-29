package fi.spectrum.streaming.kafka

import cats.Applicative
import cats.effect.Sync
import fi.spectrum.core.common.IsOption
import io.circe.Decoder
import tofu.Throws
import tofu.syntax.monadic._
import tofu.syntax.raise._

import scala.util.Try

trait KafkaDecoder[A, F[_]] {
  def decode(xs: Array[Byte]): F[A]
}

object KafkaDecoder extends KafkaDecoderLowPriority {

  implicit def optionalDeserializerByDecoder[A: Decoder, F[_]: Sync](opt: IsOption[A]): KafkaDecoder[A, F] =
    (xs: Array[Byte]) => {
      val raw = {
        val a = new String(xs, charset)
        a
      }

      io.circe.parser.decode(raw).toOption.getOrElse(opt.none).pure
    }
}

private[streaming] trait KafkaDecoderLowPriority {

  protected val charset = "UTF-8"

  implicit def deserializerByDecoder[A: Decoder, F[_]: Applicative: Throws]: KafkaDecoder[A, F] =
    (xs: Array[Byte]) => {
      val raw = {
        val a = new String(xs, charset)
        a
      }

      io.circe.parser.decode(raw).toRaise
    }
}
