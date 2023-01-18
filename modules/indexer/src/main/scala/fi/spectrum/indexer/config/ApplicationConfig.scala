package fi.spectrum.indexer.config

import derevo.derive
import derevo.pureconfig.pureconfigReader
import tofu.WithContext

@derive(pureconfigReader)
final case class ApplicationConfig(
  blocksBatchSize: Int,
  transactionsBatchSize: Int,
  mempoolBatchSize: Int
)

object ApplicationConfig extends WithContext.Companion[ApplicationConfig]
