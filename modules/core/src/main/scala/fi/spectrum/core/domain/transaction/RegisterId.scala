package fi.spectrum.core.domain.transaction

import cats.{Eq, Show}
import enumeratum.{CirceEnum, Enum, EnumEntry}
import io.circe.{KeyDecoder, KeyEncoder}
import tofu.logging.Loggable
import cats.syntax.eq._

sealed abstract class RegisterId extends EnumEntry

object RegisterId extends Enum[RegisterId] with CirceEnum[RegisterId] {

  case object R0 extends RegisterId

  case object R1 extends RegisterId

  case object R2 extends RegisterId

  case object R3 extends RegisterId

  case object R4 extends RegisterId

  case object R5 extends RegisterId

  case object R6 extends RegisterId

  case object R7 extends RegisterId

  case object R8 extends RegisterId

  case object R9 extends RegisterId

  val values = findValues

  implicit val keyDecoder: KeyDecoder[RegisterId] = withNameOption
  implicit val keyEncoder: KeyEncoder[RegisterId] = _.entryName

  implicit val show: Show[RegisterId]         = _.entryName
  implicit val loggable: Loggable[RegisterId] = Loggable.show

  implicit val eq: Eq[RegisterId] = (x: RegisterId, y: RegisterId) => x.entryName === y.entryName
}
