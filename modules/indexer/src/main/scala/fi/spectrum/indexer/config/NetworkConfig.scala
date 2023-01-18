package fi.spectrum.indexer.config

import derevo.derive
import derevo.pureconfig.pureconfigReader
import sttp.model.Uri
import tofu.WithContext
import tofu.logging.derivation.loggable
import fi.spectrum.indexer.config._

@derive(pureconfigReader, loggable)
final case class NetworkConfig(explorerUri: Uri, nodeUri: Uri)

object NetworkConfig extends WithContext.Companion[NetworkConfig]