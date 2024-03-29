package fi.spectrum.api.v1.models.history

import cats.Show
import enumeratum._
import sttp.tapir.{Schema, Validator}
import tofu.logging.Loggable

import scala.collection.immutable

sealed trait OrderType extends EnumEntry

object OrderType extends Enum[OrderType] with CirceEnum[OrderType] {

  case object Swap extends OrderType
  case object AmmRedeem extends OrderType
  case object LmRedeem extends OrderType
  case object AmmDeposit extends OrderType
  case object LmDeposit extends OrderType
  case object Lock extends OrderType

  val values: immutable.IndexedSeq[OrderType] = findValues

  implicit val loggable: Loggable[OrderType] = Loggable.stringValue.contramap(_.entryName)

  implicit val show: Show[OrderType] = _.entryName

  implicit val schema: Schema[OrderType] =
    Schema.string.validate(Validator.enumeration(OrderType.values.toList, v => Option(v)))
}
