package fi.spectrum.dex.markets.configs

import derevo.derive
import derevo.pureconfig.pureconfigReader
import fi.spectrum.core.streaming.config.ConsumerConfig
import tofu.WithContext

@derive(pureconfigReader)
final case class Consumers(
  blocks: ConsumerConfig
)

object Consumers extends WithContext.Companion[Consumers]
