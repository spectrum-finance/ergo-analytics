package fi.spectrum.api.configs

import derevo.derive
import derevo.pureconfig.pureconfigReader
import fi.spectrum.streaming.kafka.config.ConsumerConfig
import tofu.WithContext
import tofu.logging.derivation.loggable

@derive(pureconfigReader, loggable)
final case class Consumers(
  blocks: ConsumerConfig
)

object Consumers extends WithContext.Companion[Consumers]
