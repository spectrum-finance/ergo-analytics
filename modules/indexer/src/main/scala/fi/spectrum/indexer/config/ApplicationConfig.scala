package fi.spectrum.indexer.config

import derevo.derive
import derevo.pureconfig.pureconfigReader

@derive(pureconfigReader)
final case class ApplicationConfig(ordersBatchSize: Int, poolsBatchSize: Int, transactionsBatchSize: Int)