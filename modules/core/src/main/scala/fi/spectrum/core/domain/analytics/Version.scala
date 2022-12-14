package fi.spectrum.core.domain.analytics

import cats.Show
import cats.syntax.either._
import derevo.circe.{decoder, encoder}
import derevo.derive
import doobie.util.{Get, Put}
import enumeratum._
import tofu.logging.Loggable
import tofu.logging.derivation.{loggable, show}

sealed abstract class Version(override val entryName: String) extends EnumEntry

object Version extends Enum[Version] with CirceEnum[Version] {

  val values = findValues

  type V1       = V1.type
  type V2       = V2.type
  type V3       = V3.type
  type LegacyV1 = LegacyV1.type
  type LegacyV2 = LegacyV2.type

  @derive(encoder, decoder, loggable, show)
  case object V1 extends Version("v1")

  @derive(encoder, decoder, loggable, show)
  case object V2 extends Version("v2")

  @derive(encoder, decoder, loggable, show)
  case object V3 extends Version("v3")

  @derive(encoder, decoder, loggable, show)
  case object LegacyV1 extends Version("legacyV1")

  @derive(encoder, decoder, loggable, show)
  case object LegacyV2 extends Version("legacyV2")

  implicit val put: Put[Version] = Put[String].contramap(_.entryName)
  implicit val get: Get[Version] = Get[String].temap(s => withNameInsensitiveEither(s).leftMap(_.getMessage()))

  implicit val versionShow: Show[Version]         = _.entryName
  implicit val versionLoggable: Loggable[Version] = Loggable.show
}
