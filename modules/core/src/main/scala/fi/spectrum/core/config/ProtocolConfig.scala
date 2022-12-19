package fi.spectrum.core.config

import derevo.derive
import derevo.pureconfig.pureconfigReader
import fi.spectrum.core.domain.ergo.Network
import tofu.WithContext

@derive(pureconfigReader)
final case class ProtocolConfig(networkType: Network)

object ProtocolConfig extends WithContext.Companion[ProtocolConfig]