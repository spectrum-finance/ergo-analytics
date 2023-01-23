package fi.spectrum.dex.markets.api.v1.models.amm

import derevo.circe.{decoder, encoder}
import derevo.derive
import fi.spectrum.core.common.FiatUnits
import sttp.tapir.Schema

@derive(encoder, decoder)
case class FiatEquiv(value: BigDecimal, units: FiatUnits)

object FiatEquiv {
  implicit val schemaFiatEquiv: Schema[FiatEquiv] = Schema.derived
  def empty(units: FiatUnits): FiatEquiv          = FiatEquiv(BigDecimal(0), units)
}
