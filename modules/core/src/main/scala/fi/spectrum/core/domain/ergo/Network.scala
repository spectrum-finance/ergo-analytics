package fi.spectrum.core.domain.ergo

import cats.syntax.either._
import enumeratum._
import org.ergoplatform.ErgoAddressEncoder
import pureconfig.ConfigReader
import pureconfig.error.CannotConvert
import tofu.logging.Loggable

sealed abstract class Network(val prefix: Byte) extends EnumEntry {
  val addressEncoder: ErgoAddressEncoder = ErgoAddressEncoder(prefix)
}

object Network extends Enum[Network] {

  case object MainNet extends Network(ErgoAddressEncoder.MainnetNetworkPrefix)
  case object TestNet extends Network(ErgoAddressEncoder.TestnetNetworkPrefix)

  implicit val configReader: ConfigReader[Network] = ConfigReader[String].emap(s =>
    withNameInsensitiveEither(s).leftMap(nsm =>
      CannotConvert(s, "Network", s"[$s] doesn't match any of ${nsm.enumValues.mkString(", ")}")
    )
  )

  implicit val loggable: Loggable[Network] = Loggable.stringValue.contramap(_.entryName)

  val values = findValues
}
