package fi.spectrum.indexer.config

import derevo.derive
import derevo.pureconfig.pureconfigReader
import fi.spectrum.core.common.redis.RedisConfig
import fi.spectrum.core.config.{ConfigBundleCompanion, ProtocolConfig}
import fi.spectrum.core.db.PgConfig
import fi.spectrum.core.domain.TokenId
import fi.spectrum.graphite.GraphiteSettings
import fi.spectrum.streaming.kafka.config.{ConsumerConfig, KafkaConfig, ProducerConfig}
import glass.macros.{ClassyPOptics, promote}
import tofu.WithContext
import tofu.logging.Loggable

@derive(pureconfigReader)
@ClassyPOptics
final case class ConfigBundle(
  @promote postgres: PgConfig,
  @promote protocol: ProtocolConfig,
  @promote application: ApplicationConfig,
  @promote spfTokenId: TokenId,
  @promote mempool: MempoolConfig,
  @promote network: NetworkConfig,
  txConsumer: ConsumerConfig,
  mempoolTxConsumer: ConsumerConfig,
  blocksConsumer: ConsumerConfig,
  @promote kafka: KafkaConfig,
  @promote redis: RedisConfig,
  rocks: RocksConfig,
  graphite: GraphiteSettings
)

object ConfigBundle extends WithContext.Companion[ConfigBundle] with ConfigBundleCompanion[ConfigBundle] {

  implicit val loggable: Loggable[ConfigBundle] = Loggable.empty
}
