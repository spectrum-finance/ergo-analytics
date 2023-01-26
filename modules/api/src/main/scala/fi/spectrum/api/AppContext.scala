package fi.spectrum.api

import derevo.derive
import fi.spectrum.api
import fi.spectrum.api.configs.ConfigBundle
import fi.spectrum.api.models.TraceId
import glass.macros.{ClassyOptics, promote}
import io.estatico.newtype.ops._
import tofu.WithContext
import tofu.logging.derivation.{hidden, loggable}

@ClassyOptics
@derive(loggable)
final case class AppContext(
  @promote @hidden config: ConfigBundle,
  @promote traceId: TraceId
)

object AppContext extends WithContext.Companion[AppContext] {

  def init(configs: ConfigBundle): AppContext =
    api.AppContext(configs, "<Root>".coerce[TraceId])
}
