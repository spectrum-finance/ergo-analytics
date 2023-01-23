package fi.spectrum.dex.markets.configs

import derevo.derive
import derevo.pureconfig.pureconfigReader
import fi.spectrum.core.common.redis.RedisConfig
import fi.spectrum.core.config.ConfigBundleCompanion
import fi.spectrum.core.db.PgConfig
import glass.macros.{promote, ClassyOptics}
import tofu.Context
import tofu.logging.Loggable

@derive(pureconfigReader)
@ClassyOptics
final case class ConfigBundle(
  @promote db: PgConfig,
//  @promote network: NetworkConfig,
//  @promote kafka: KafkaConfig,
  http: HttpConfig,
  redis: RedisConfig,
  tokens: TokenFetcherConfig,
  request: RequestConfig,
  consumers: Consumers
)

object ConfigBundle extends Context.Companion[ConfigBundle] with ConfigBundleCompanion[ConfigBundle] {

  implicit val loggable: Loggable[ConfigBundle] = Loggable.empty
}
