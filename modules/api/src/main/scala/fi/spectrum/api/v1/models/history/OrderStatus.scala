package fi.spectrum.api.v1.models.history

import cats.Show
import cats.implicits.toBifunctorOps
import derevo.circe.{decoder, encoder}
import derevo.derive
import doobie.util.{Get, Put}
import enumeratum.{CirceEnum, Enum, EnumEntry}
import sttp.tapir.{Schema, Validator}
import tofu.logging.Loggable

import scala.collection.immutable
import scala.concurrent.duration.DurationInt

sealed trait OrderStatus extends EnumEntry

@derive(encoder, decoder)
object OrderStatus extends Enum[OrderStatus] with CirceEnum[OrderStatus] {

  case object Pending extends OrderStatus
  case object Register extends OrderStatus
  case object Evaluated extends OrderStatus
  case object Refunded extends OrderStatus
  case object NeedRefund extends OrderStatus

  val values: immutable.IndexedSeq[OrderStatus] = findValues

  implicit val show: Show[OrderStatus]         = _.entryName
  implicit val loggable: Loggable[OrderStatus] = Loggable.stringValue.contramap(_.entryName)

  implicit val schema: Schema[OrderStatus] =
    Schema.string.validate(Validator.enumeration(OrderStatus.values.toList, v => Option(v)))

  implicit val get: Get[OrderStatus] =
    Get[String].temap(s => withNameEither(s).leftMap(_ => s"No such OrderStatus [$s]"))

  implicit val put: Put[OrderStatus] =
    Put[String].contramap[OrderStatus](_.entryName)

  def status(register: TxData, eval: Option[TxData], refund: Option[TxData], currentTimestamp: Long) =
    if (eval.isEmpty && refund.isEmpty) NeedRefund
    else {
      (eval, refund) match {
        case (Some(_), None) => Evaluated
        case (None, Some(_)) => Refunded
        case _               => Pending
      }
    }
}
