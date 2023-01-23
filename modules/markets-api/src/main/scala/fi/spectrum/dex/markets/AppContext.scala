package fi.spectrum.dex.markets

import derevo.derive
import fi.spectrum.core.domain.TraceId
import fi.spectrum.dex.markets.configs.ConfigBundle
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
    AppContext(configs, "<Root>".coerce[TraceId])
}
