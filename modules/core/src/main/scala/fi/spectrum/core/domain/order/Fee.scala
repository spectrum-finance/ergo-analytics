package fi.spectrum.core.domain.order

import derevo.circe.{decoder, encoder}
import derevo.derive
import cats.syntax.option._
import doobie.{Read, Write}
import sttp.tapir.Schema
import tofu.logging.derivation.{loggable, show}

/** Fee can be either in ergo or spf tokens
  */
@derive(encoder, decoder, loggable, show)
sealed trait Fee {
  val amount: Long
}

object Fee {

  implicit val schema: Schema[Fee] = Schema.derived

  @derive(encoder, decoder, loggable, show)
  final case class SPF(amount: Long) extends Fee

  object SPF {
    def apply(in: BigInt): SPF = SPF(in.toLong)
  }

  @derive(encoder, decoder, loggable, show)
  final case class ERG(amount: Long) extends Fee

  object ERG {
    def apply(in: BigInt): ERG = ERG(in.toLong)
  }

  implicit def put: Write[Fee] = Write[(Long, String)].contramap {
    case ERG(value) => (value, "erg")
    case SPF(value) => (value, "spf")
  }

  implicit def get: Read[Fee] = Read[(Long, String)].map {
    case (value, "erg") => ERG(value)
    case (value, "spf") => SPF(value)
  }

  implicit def putOpt: Write[Option[Fee]] = Write[(Option[Long], Option[String])].contramap {
    case Some(ERG(value)) => (value.some, "erg".some)
    case Some(SPF(value)) => (value.some, "spf".some)
    case _                => none -> none
  }

  implicit def getOpt: Read[Option[Fee]] = Read[(Option[Long], Option[String])].map {
    case (Some(x), Some("erg")) => Some(ERG(x))
    case (Some(x), Some("spf")) => Some(SPF(x))
    case _                      => none
  }

}
