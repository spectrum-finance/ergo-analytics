package fi.spectrum.core.common.redis

import derevo.derive
import derevo.pureconfig.pureconfigReader
import tofu.WithContext

import scala.concurrent.duration.FiniteDuration

@derive(pureconfigReader)
final case class RedisConfig(
  password: String,
  host: String,
  port: Int,
  timeout: FiniteDuration,
  retryTimeout: FiniteDuration
)

object RedisConfig extends WithContext.Companion[RedisConfig]