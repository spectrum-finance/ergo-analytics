package fi.spectrum.indexer.config

import derevo.derive
import derevo.pureconfig.pureconfigReader
import glass.macros.ClassyOptics
import tofu.WithContext

@derive(pureconfigReader)
final case class ApplicationConfig(ordersBatchSize: Int, poolsBatchSize: Int, transactionsBatchSize: Int)

object ApplicationConfig extends WithContext.Companion[ApplicationConfig]
