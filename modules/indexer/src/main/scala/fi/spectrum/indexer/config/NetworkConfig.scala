package fi.spectrum.indexer.config

import derevo.derive
import derevo.pureconfig.pureconfigReader
import sttp.model.Uri
import tofu.WithContext
import tofu.logging.derivation.loggable
import fi.spectrum.indexer.config._

import scala.concurrent.duration.FiniteDuration

@derive(pureconfigReader, loggable)
final case class NetworkConfig(explorerUri: Uri, limitRetries: Int, retryDelay: FiniteDuration)

object NetworkConfig extends WithContext.Companion[NetworkConfig]