package fi.spectrum.core.domain.transaction

import enumeratum.{CirceEnum, Enum, EnumEntry}

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
}