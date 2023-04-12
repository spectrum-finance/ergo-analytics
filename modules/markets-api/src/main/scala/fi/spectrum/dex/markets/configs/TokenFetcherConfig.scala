package fi.spectrum.dex.markets.configs

import derevo.derive
import derevo.pureconfig.pureconfigReader
import tofu.Context
import tofu.logging.derivation.loggable
import scala.concurrent.duration.FiniteDuration

@derive(pureconfigReader, loggable)
case class TokenFetcherConfig(url: String, rate: FiniteDuration)

object TokenFetcherConfig extends Context.Companion[TokenFetcherConfig]
