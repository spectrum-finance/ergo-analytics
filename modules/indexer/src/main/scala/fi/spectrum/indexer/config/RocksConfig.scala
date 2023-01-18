package fi.spectrum.indexer.config

import derevo.derive
import derevo.pureconfig.pureconfigReader
import tofu.WithContext
import tofu.logging.derivation.loggable

@derive(pureconfigReader, loggable)
final case class RocksConfig(path: String)

object RocksConfig extends WithContext.Companion[RocksConfig]