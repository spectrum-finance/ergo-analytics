package fi.spectrum.graphite

import derevo.derive
import derevo.pureconfig.pureconfigReader
import tofu.WithContext

@derive(pureconfigReader)
final case class GraphiteSettings(
  host: String,
  port: Int,
  prefix: String
)

object GraphiteSettings extends WithContext.Companion[GraphiteSettings]