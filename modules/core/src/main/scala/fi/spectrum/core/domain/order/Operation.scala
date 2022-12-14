package fi.spectrum.core.domain.order

import cats.Show
import cats.syntax.either._

import derevo.circe.{decoder, encoder}
import derevo.derive
import doobie.util.{Get, Put}
import enumeratum.{CirceEnum, Enum, EnumEntry}
import tofu.logging.Loggable
import tofu.logging.derivation.{loggable, show}

/** Represents order operation ,e.g. Swap, Deposit, Redeem, Lock, Reward.
  * Doesn't depend on order type.
  */
sealed abstract class Operation(override val entryName: String) extends EnumEntry

object Operation extends Enum[Operation] with CirceEnum[Operation] {

  type Swap    = Swap.type
  type Deposit = Deposit.type
  type Redeem  = Redeem.type
  type Lock    = Lock.type

  @derive(encoder, decoder, show, loggable)
  case object Swap extends Operation("swap")

  @derive(encoder, decoder, show, loggable)
  case object Deposit extends Operation("deposit")

  @derive(encoder, decoder, show, loggable)
  case object Redeem extends Operation("redeem")

  @derive(encoder, decoder, show, loggable)
  case object Lock extends Operation("lock")

  def values: IndexedSeq[Operation] = findValues

  implicit val put: Put[Operation] = Put[String].contramap(_.entryName)
  implicit val get: Get[Operation] = Get[String].temap(s => withNameInsensitiveEither(s).leftMap(_.getMessage()))

  implicit val operationShow: Show[Operation]         = _.entryName
  implicit val operationLoggable: Loggable[Operation] = Loggable.show
}
