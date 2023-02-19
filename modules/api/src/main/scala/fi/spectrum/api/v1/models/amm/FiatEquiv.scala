package fi.spectrum.api.v1.models.amm

import derevo.circe.{decoder, encoder}
import derevo.derive
import fi.spectrum.api.models.FiatUnits
import fi.spectrum.api.models.FiatUnits
import sttp.tapir.Schema
import tofu.logging.derivation.loggable

@derive(encoder, decoder)
case class FiatEquiv(value: BigDecimal, units: FiatUnits)

object FiatEquiv {
  implicit val schemaFiatEquiv: Schema[FiatEquiv] = Schema.derived
  def empty(units: FiatUnits): FiatEquiv          = FiatEquiv(BigDecimal(0), units)
}
