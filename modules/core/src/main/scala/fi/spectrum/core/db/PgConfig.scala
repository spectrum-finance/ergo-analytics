package fi.spectrum.core.db

import derevo.derive
import derevo.pureconfig.pureconfigReader
import tofu.WithContext

import scala.concurrent.duration.FiniteDuration

@derive(pureconfigReader)
final case class PgConfig(
  url: String,
  user: String,
  pass: String,
  connectionTimeout: FiniteDuration,
  minConnections: Int,
  maxConnections: Int
)

object PgConfig extends WithContext.Companion[PgConfig]