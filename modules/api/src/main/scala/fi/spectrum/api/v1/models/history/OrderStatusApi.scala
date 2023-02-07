package fi.spectrum.api.v1.models.history

import cats.Show
import enumeratum._
import sttp.tapir.{Schema, Validator}
import tofu.logging.Loggable

import scala.collection.immutable

sealed trait OrderStatusApi extends EnumEntry

object OrderStatusApi extends Enum[OrderStatusApi] with CirceEnum[OrderStatusApi] {

  case object Registered extends OrderStatusApi
  case object Evaluated extends OrderStatusApi
  case object Refunded extends OrderStatusApi

  val values: immutable.IndexedSeq[OrderStatusApi] = findValues

  implicit val loggable: Loggable[OrderStatusApi] = Loggable.stringValue.contramap(_.entryName)

  implicit val show: Show[OrderStatusApi] = _.entryName

  implicit val schema: Schema[OrderStatusApi] =
    Schema.string.validate(Validator.enumeration(OrderStatusApi.values.toList, v => Option(v)))
}
