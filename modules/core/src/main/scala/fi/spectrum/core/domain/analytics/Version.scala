package fi.spectrum.core.domain.analytics

import cats.Show
import cats.syntax.either._
import doobie.util.{Get, Put}
import enumeratum.EnumEntry.Lowercase
import enumeratum._
import io.circe.{Decoder, Encoder}
import tofu.logging.Loggable

sealed abstract class Version extends EnumEntry with Lowercase

object Version extends Enum[Version] with CirceEnum[Version] with Lowercase {

  type V1       = V1.type
  type V2       = V2.type
  type V3       = V3.type
  type LegacyV1 = LegacyV1.type
  type LegacyV2 = LegacyV2.type

  case object V1 extends Version

  case object V2 extends Version

  case object V3 extends Version

  case object LegacyV1 extends Version

  case object LegacyV2 extends Version

  val values: IndexedSeq[Version] = findValues

  implicit val put: Put[Version] = Put[String].contramap(_.entryName)

  implicit val get: Get[Version] = Get[String].temap { s =>
    withNameInsensitiveEither(s).leftMap(_.getMessage())
  }

  implicit val versionShow: Show[Version]         = _.entryName
  implicit val versionLoggable: Loggable[Version] = Loggable.show

  implicit def encoderAnyVersion[B <: Version] = Encoder[String].contramap[B](_.entryName)

  implicit val decodeV1: Decoder[V1]             = decoderEnum[V1](V1)
  implicit val decodeV2: Decoder[V2]             = decoderEnum[V2](V2)
  implicit val decodeV3: Decoder[V3]             = decoderEnum[V3](V3)
  implicit val decodeLegacyV1: Decoder[LegacyV1] = decoderEnum[LegacyV1](LegacyV1)
  implicit val decodeLegacyV2: Decoder[LegacyV2] = decoderEnum[LegacyV2](LegacyV2)

  def decoderEnum[B <: Version](b: B): Decoder[B] = Decoder[String].emap { s: String =>
    Version.withNameInsensitiveEither(s) match {
      case Right(value) if value.in(b) => b.asRight
      case Right(value)                => s"Value $value is not $b".asLeft
      case Left(value)                 => s"Failed to decode $b - ${value.getMessage()}".asLeft
    }
  }

  implicit def showAnyVersion[B <: Version]: Show[B] = (t: B) => t.entryName

  implicit def loggableAnyVersion[B <: Version]: Loggable[B] = Loggable.show
}
