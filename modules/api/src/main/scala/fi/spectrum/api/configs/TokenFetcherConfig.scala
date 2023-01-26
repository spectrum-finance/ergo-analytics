package fi.spectrum.api.configs

import derevo.derive
import derevo.pureconfig.pureconfigReader
import tofu.WithContext
import tofu.logging.derivation.loggable

import scala.concurrent.duration.FiniteDuration

@derive(pureconfigReader, loggable)
case class TokenFetcherConfig(url: String, rate: FiniteDuration)

object TokenFetcherConfig extends WithContext.Companion[TokenFetcherConfig]
