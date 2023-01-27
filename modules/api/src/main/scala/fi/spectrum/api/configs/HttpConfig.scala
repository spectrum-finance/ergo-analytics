package fi.spectrum.api.configs

import derevo.derive
import derevo.pureconfig.pureconfigReader
import tofu.WithContext
import tofu.logging.derivation.loggable

@derive(pureconfigReader, loggable)
final case class HttpConfig(host: String, port: Int)

object HttpConfig extends WithContext.Companion[HttpConfig]
