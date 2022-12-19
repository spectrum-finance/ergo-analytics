package fi.spectrum.indexer.config

import derevo.derive
import derevo.pureconfig.pureconfigReader
import fi.spectrum.core.config.{ConfigBundleCompanion, ProtocolConfig}
import fi.spectrum.core.db.PgConfig
import fi.spectrum.core.domain.TokenId
import fi.spectrum.streaming.kafka.config.{ConsumerConfig, KafkaConfig, ProducerConfig}
import tofu.logging.Loggable

@derive(pureconfigReader)
final case class ConfigBundle(
  postgres: PgConfig,
  protocol: ProtocolConfig,
  application: ApplicationConfig,
  spfTokenId: TokenId,
  txConsumer: ConsumerConfig,
  ordersConsumer: ConsumerConfig,
  poolsConsumer: ConsumerConfig,
  ordersProducer: ProducerConfig,
  poolsProducer: ProducerConfig,
  kafka: KafkaConfig
)

object ConfigBundle extends ConfigBundleCompanion[ConfigBundle] {

  implicit val loggable: Loggable[ConfigBundle] = Loggable.empty
}
