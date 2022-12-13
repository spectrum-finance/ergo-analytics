package fi.spectrum.core.domain.order

import derevo.circe.{decoder, encoder}
import derevo.derive
import doobie.Read
import doobie.util.Write
import tofu.logging.derivation.{loggable, show}

/** Fee can be either in ergo or spf tokens
  */
@derive(encoder, decoder, loggable, show)
sealed trait Fee {
  val amount: Long
}

object Fee {

  @derive(encoder, decoder, loggable, show)
  final case class SPF(amount: Long) extends Fee

  @derive(encoder, decoder, loggable, show)
  final case class ERG(amount: Long) extends Fee

  implicit val put: Write[Fee] = Write[(Long, String)].contramap {
    case ERG(value) => (value, "erg")
    case SPF(value) => (value, "spf")
  }

  implicit val get: Read[Fee] = Read[(Long, String)].map {
    case (value, "erg") => ERG(value)
    case (value, "spf") => SPF(value)
  }

}
