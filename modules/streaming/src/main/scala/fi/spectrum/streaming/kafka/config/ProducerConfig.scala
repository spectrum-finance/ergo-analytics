package fi.spectrum.streaming.kafka.config

import derevo.derive
import derevo.pureconfig.pureconfigReader
import fi.spectrum.core.config.ProtocolConfig
import fi.spectrum.streaming.kafka.TopicId
import tofu.WithContext
import tofu.logging.derivation.loggable

@derive(pureconfigReader, loggable)
final case class ProducerConfig(topicId: TopicId, parallelism: Int)

object ProducerConfig extends WithContext.Companion[ProducerConfig]