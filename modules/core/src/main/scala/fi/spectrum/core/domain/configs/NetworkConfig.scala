package fi.spectrum.core.domain.configs

import derevo.derive
import derevo.pureconfig.pureconfigReader
import sttp.model.Uri
import tofu.WithContext
import fi.spectrum.core.common.instances._
import tofu.logging.derivation.loggable

@derive(pureconfigReader, loggable)
final case class NetworkConfig(explorerUri: Uri, nodeUri: Uri)

object NetworkConfig extends WithContext.Companion[NetworkConfig]
