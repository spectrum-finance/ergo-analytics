package fi.spectrum.core.domain.order

import derevo.circe.{decoder, encoder}
import derevo.derive

@derive(encoder, decoder)
sealed trait Fee {
  val amount: Long
}

object Fee {
  @derive(encoder, decoder)
  final case class SPF(amount: Long) extends Fee
  @derive(encoder, decoder)
  final case class ERG(amount: Long) extends Fee
}
