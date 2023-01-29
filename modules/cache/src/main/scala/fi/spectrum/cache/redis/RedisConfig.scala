package fi.spectrum.cache.redis

import derevo.derive
import derevo.pureconfig.pureconfigReader
import tofu.WithContext
import tofu.logging.derivation.loggable

import scala.concurrent.duration.FiniteDuration

@derive(pureconfigReader, loggable)
final case class RedisConfig(
  password: String,
  host: String,
  port: Int,
  timeout: FiniteDuration,
  retryTimeout: FiniteDuration
)

object RedisConfig extends WithContext.Companion[RedisConfig]