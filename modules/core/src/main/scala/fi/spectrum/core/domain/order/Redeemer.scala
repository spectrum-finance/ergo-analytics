package fi.spectrum.core.domain.order


import cats.Show
import derevo.cats.show
import derevo.circe.{decoder, encoder}
import derevo.derive
import fi.spectrum.core.domain.{HexString, PubKey, SErgoTree}
import tofu.logging.derivation.loggable

@derive(encoder, decoder, loggable)
sealed trait Redeemer {
  def hexString: HexString
}

object Redeemer {

  @derive(encoder, decoder, loggable, show)
  final case class ErgoTreeRedeemer(value: SErgoTree) extends Redeemer {
    def hexString: HexString = value.value
  }

  @derive(encoder, decoder, loggable, show)
  final case class PublicKeyRedeemer(value: PubKey) extends Redeemer {
    def hexString: HexString = value.value
  }

  implicit def showRedeemer: Show[Redeemer] = {
    case ErgoTreeRedeemer(value) => value.value.unwrapped
    case PublicKeyRedeemer(value) => value.ergoTree.value.unwrapped
  }
}
