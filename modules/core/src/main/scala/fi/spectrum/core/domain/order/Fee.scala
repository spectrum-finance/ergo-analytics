package fi.spectrum.core.domain.order

import derevo.circe.{decoder, encoder}
import derevo.derive
import doobie.util.{Get, Put}
import fi.spectrum.core.domain.TokenId

@derive(encoder, decoder)
sealed trait Fee {
  val amount: Long
}

object Fee {

  @derive(encoder, decoder)
  final case class SPF(amount: Long, tokenId: TokenId) extends Fee

  @derive(encoder, decoder)
  final case class ERG(amount: Long) extends Fee

  implicit val put: Put[Fee] = ???
  implicit val get: Get[Fee] = ???

}
