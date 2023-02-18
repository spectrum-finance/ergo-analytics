package fi.spectrum.mempool.config

import derevo.derive
import derevo.pureconfig.pureconfigReader
import fi.spectrum.cache.redis.RedisConfig
import fi.spectrum.core.config.{ConfigBundleCompanion, ProtocolConfig}
import fi.spectrum.core.domain.TokenId
import fi.spectrum.graphite.GraphiteSettings
import fi.spectrum.streaming.kafka.config.{ConsumerConfig, KafkaConfig}
import glass.macros.{ClassyPOptics, promote}
import tofu.WithContext
import tofu.logging.Loggable

@derive(pureconfigReader)
@ClassyPOptics
final case class ConfigBundle(
  @promote application: ApplicationConfig,
  @promote protocol: ProtocolConfig,
  @promote kafka: KafkaConfig,
  @promote redis: RedisConfig,
  rocks: RocksConfig,
  graphite: GraphiteSettings,
  csConsumer: ConsumerConfig,
  mempoolConsumer: ConsumerConfig,
  @promote spfTokenId: TokenId,
  http: HttpConfig,
  @promote mempool: MempoolConfig
)

object ConfigBundle extends WithContext.Companion[ConfigBundle] with ConfigBundleCompanion[ConfigBundle] {

  implicit val loggable: Loggable[ConfigBundle] = Loggable.empty
}
