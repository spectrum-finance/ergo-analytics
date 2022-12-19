package fi.spectrum.core.config

import derevo.derive
import derevo.pureconfig.pureconfigReader
import fi.spectrum.core.domain.ergo.Network

@derive(pureconfigReader)
final case class ProtocolConfig(networkType: Network)
