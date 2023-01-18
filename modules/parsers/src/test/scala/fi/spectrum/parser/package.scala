package fi.spectrum

import fi.spectrum.core.domain.{Address, PubKey}
import org.ergoplatform.{ErgoAddressEncoder, P2PKAddress}

package object parser {

  val e: ErgoAddressEncoder = ErgoAddressEncoder(ErgoAddressEncoder.MainnetNetworkPrefix)

  def mkPubKey(address: Address): Option[PubKey] =
    e
      .fromString(address.value.value)
      .collect { case address: P2PKAddress => address.pubkeyBytes }
      .map(PubKey.fromBytes)
      .toOption
}
