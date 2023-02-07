package fi.spectrum.core.domain

import fi.spectrum.core.domain.order.Redeemer
import fi.spectrum.core.domain.order.Redeemer.PublicKeyRedeemer
import fi.spectrum.core.protocol.ErgoTreeSerializer
import org.ergoplatform.{ErgoAddressEncoder, P2PKAddress}

import scala.util.Try

object address {

  def formAddress(redeemer: Redeemer)(implicit e: ErgoAddressEncoder): Option[Address] =
    redeemer match {
      case Redeemer.PublicKeyRedeemer(value) =>
        e.fromProposition(ErgoTreeSerializer.default.deserialize(value.ergoTree))
          .toOption
          .map(e.toString)
          .flatMap(Address.fromString[Try](_).toOption)
      case _ => None
    }

  def formRedeemer(address: Address)(implicit e: ErgoAddressEncoder): Option[Redeemer] =
    e
      .fromString(address.value.value)
      .collect { case address: P2PKAddress => address.pubkeyBytes }
      .map(PubKey.fromBytes)
      .toOption
      .map(Redeemer.PublicKeyRedeemer(_))

  def formPKRedeemer(address: Address)(implicit e: ErgoAddressEncoder): Option[PublicKeyRedeemer] =
    e
      .fromString(address.value.value)
      .collect { case address: P2PKAddress => address.pubkeyBytes }
      .map(PubKey.fromBytes)
      .toOption
      .map(Redeemer.PublicKeyRedeemer(_))

}
