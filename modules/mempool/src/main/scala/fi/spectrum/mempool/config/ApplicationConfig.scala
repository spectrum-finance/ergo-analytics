package fi.spectrum.mempool.config

import derevo.derive
import derevo.pureconfig.pureconfigReader
import tofu.WithContext

import scala.concurrent.duration.FiniteDuration

@derive(pureconfigReader)
final case class ApplicationConfig(
  mempoolBatchSize: Int,
  mempoolGroupTime: FiniteDuration,
  csBatchSize: Int,
  csGroupTime: FiniteDuration
)

object ApplicationConfig extends WithContext.Companion[ApplicationConfig]
