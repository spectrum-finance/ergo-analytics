package fi.spectrum.indexer.config

import derevo.derive
import derevo.pureconfig.pureconfigReader
import tofu.WithContext

import scala.concurrent.duration.FiniteDuration

@derive(pureconfigReader)
final case class ApplicationConfig(
  blocksBatchSize: Int,
  blocksGroupTime: FiniteDuration,
  transactionsBatchSize: Int,
  transactionsGroupTime: FiniteDuration
)

object ApplicationConfig extends WithContext.Companion[ApplicationConfig]
