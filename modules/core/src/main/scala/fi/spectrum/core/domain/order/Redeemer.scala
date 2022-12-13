package fi.spectrum.core.domain.order

import derevo.cats.eqv
import derevo.circe.{decoder, encoder}
import derevo.derive
import fi.spectrum.core.domain.{PubKey, SErgoTree}
import tofu.logging.derivation.{loggable, show}

@derive(encoder, decoder, loggable, show, eqv)
sealed trait Redeemer

object Redeemer {

  @derive(encoder, decoder, loggable, show, eqv)
  final case class ErgoTreeRedeemer(value: SErgoTree) extends Redeemer

  @derive(encoder, decoder, loggable, show, eqv)
  final case class PublicKeyRedeemer(value: PubKey) extends Redeemer
}
