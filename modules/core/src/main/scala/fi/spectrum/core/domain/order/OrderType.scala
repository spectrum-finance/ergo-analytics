package fi.spectrum.core.domain.order

import cats.Show
import cats.syntax.either._

import derevo.circe.{decoder, encoder}
import derevo.derive
import doobie.util.{Get, Put}
import enumeratum._
import tofu.logging.Loggable
import tofu.logging.derivation.{loggable, show}

/** Represents order type ,e.g. AMM, LOCK, LM
  */
sealed abstract class OrderType(override val entryName: String) extends EnumEntry

object OrderType extends Enum[OrderType] with CirceEnum[OrderType] {

  type AMM  = AMM.type
  type LOCK = LOCK.type
  type LM   = LM.type

  @derive(encoder, decoder, loggable, show)
  case object AMM extends OrderType("amm")

  @derive(encoder, decoder, loggable, show)
  case object LOCK extends OrderType("lock")

  @derive(encoder, decoder, loggable, show)
  case object LM extends OrderType("lm")

  def values: IndexedSeq[OrderType] = findValues

  implicit val put: Put[OrderType] = Put[String].contramap(_.entryName)
  implicit val get: Get[OrderType] = Get[String].temap(s => withNameInsensitiveEither(s).leftMap(_.getMessage()))

  implicit val orderTypeShow: Show[OrderType]         = _.entryName
  implicit val orderTypeLoggable: Loggable[OrderType] = Loggable.show
}
