package fi.spectrum.core.domain.pool

import cats.Show
import cats.syntax.either._
import derevo.circe.{decoder, encoder}
import derevo.derive
import doobie.util.{Get, Put}
import enumeratum.{CirceEnum, Enum, EnumEntry}
import tofu.logging.Loggable
import tofu.logging.derivation.{loggable, show}

sealed abstract class PoolType(override val entryName: String) extends EnumEntry

object PoolType extends Enum[PoolType] with CirceEnum[PoolType] {

  type AMM = AMM.type
  type LM = LM.type

  @derive(encoder, decoder, loggable, show)
  case object AMM extends PoolType("amm")

  @derive(encoder, decoder, loggable, show)
  case object LM extends PoolType("lm")

  def values: IndexedSeq[PoolType] = findValues

  implicit val put: Put[PoolType] = Put[String].contramap(_.entryName)
  implicit val get: Get[PoolType] = Get[String].temap(s => withNameInsensitiveEither(s).leftMap(_.getMessage()))

  implicit val poolTypeShow: Show[PoolType]         = _.entryName
  implicit val poolTypeLoggable: Loggable[PoolType] = Loggable.show
}
