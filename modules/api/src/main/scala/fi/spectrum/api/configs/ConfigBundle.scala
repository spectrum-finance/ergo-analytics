package fi.spectrum.api.configs

import derevo.derive
import derevo.pureconfig.pureconfigReader
import fi.spectrum.cache.redis.RedisConfig
import fi.spectrum.core.config.ConfigBundleCompanion
import fi.spectrum.core.db.PgConfig
import fi.spectrum.graphite.GraphiteSettings
import fi.spectrum.streaming.kafka.config.{ConsumerConfig, KafkaConfig}
import glass.macros.{ClassyOptics, promote}
import tofu.WithContext
import tofu.logging.derivation.loggable

@derive(pureconfigReader, loggable)
@ClassyOptics
final case class ConfigBundle(
  @promote postgres: PgConfig,
  http: HttpConfig,
  @promote redis: RedisConfig,
  request: RequestConfig,
  blockConsumer: ConsumerConfig,
  graphite: GraphiteSettings,
  @promote network: NetworkConfig,
  @promote kafka: KafkaConfig,
  @promote blocks: BlocksProcessConfig
)

object ConfigBundle extends WithContext.Companion[ConfigBundle] with ConfigBundleCompanion[ConfigBundle]
