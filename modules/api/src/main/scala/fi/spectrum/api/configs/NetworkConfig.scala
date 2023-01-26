package fi.spectrum.api.configs

import derevo.derive
import derevo.pureconfig.pureconfigReader
import sttp.model.Uri
import tofu.WithContext
import tofu.logging.derivation.loggable
import fi.spectrum.core.common.instances._

@derive(pureconfigReader, loggable)
final case class NetworkConfig(cmcUrl: Uri, cmcApiKey: String, verifiedTokeListUrl: Uri)

object NetworkConfig extends WithContext.Companion[NetworkConfig]
