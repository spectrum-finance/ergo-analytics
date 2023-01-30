package fi.spectrum.api.configs

import derevo.derive
import derevo.pureconfig.pureconfigReader
import tofu.WithContext
import tofu.logging.derivation.loggable

import scala.concurrent.duration.FiniteDuration

@derive(pureconfigReader, loggable)
final case class BlocksProcessConfig(blocksBatchSize: Int, blocksGroupTime: FiniteDuration)

object BlocksProcessConfig extends WithContext.Companion[BlocksProcessConfig]
