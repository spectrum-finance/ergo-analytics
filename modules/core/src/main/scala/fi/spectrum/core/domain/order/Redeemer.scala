package fi.spectrum.core.domain.order

import derevo.circe.{decoder, encoder}
import derevo.derive
import fi.spectrum.core.domain.{PubKey, SErgoTree}

sealed trait Redeemer

object Redeemer {
  @derive(encoder, decoder)
  final case class ErgoTreeRedeemer(value: SErgoTree) extends Redeemer
  @derive(encoder, decoder)
  final case class PublicKeyRedeemer(value: PubKey) extends Redeemer
}
