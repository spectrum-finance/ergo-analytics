package fi.spectrum.core.domain.order

import enumeratum.{CirceEnum, Enum, EnumEntry}

sealed abstract class OrderStatus extends EnumEntry

object OrderStatus extends Enum[OrderStatus] with CirceEnum[OrderStatus] {

  case object WaitingRegistration extends OrderStatus

  case object Registered extends OrderStatus

  case object WaitingExecution extends OrderStatus

  case object Executed extends OrderStatus

  case object Stacked extends OrderStatus

  case object Refunded extends OrderStatus

  val values = findValues
}
