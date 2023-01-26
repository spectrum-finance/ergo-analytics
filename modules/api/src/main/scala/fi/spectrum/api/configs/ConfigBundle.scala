package fi.spectrum.api.configs

import derevo.derive
import derevo.pureconfig.pureconfigReader
import fi.spectrum.cache.redis.RedisConfig
import fi.spectrum.core.config.ConfigBundleCompanion
import fi.spectrum.core.db.PgConfig
import glass.macros.{ClassyOptics, promote}
import tofu.WithContext
import tofu.logging.derivation.loggable

@derive(pureconfigReader, loggable)
@ClassyOptics
final case class ConfigBundle(
  @promote db: PgConfig,
  http: HttpConfig,
  redis: RedisConfig,
  tokens: TokenFetcherConfig,
  request: RequestConfig,
  consumers: Consumers
)

object ConfigBundle extends WithContext.Companion[ConfigBundle] with ConfigBundleCompanion[ConfigBundle]
