package fi.spectrum.api.v1.models.amm

import derevo.circe.{decoder, encoder}
import derevo.derive
import fi.spectrum.api.currencies.UsdCurrency
import fi.spectrum.api.models.FiatUnits
import sttp.tapir.Schema
import tofu.logging.derivation.loggable

@derive(encoder, decoder, loggable)
case class FiatEquiv(value: BigDecimal, units: FiatUnits)

object FiatEquiv {

  implicit val schemaFiatEquiv: Schema[FiatEquiv] =
    Schema
      .derived[FiatEquiv]
      .modify(_.value)(_.description("Converted value"))
      .modify(_.units)(_.description("Measuring fiat units"))
      .encodedExample(FiatEquiv(BigDecimal(34000), FiatUnits(UsdCurrency)))

  def empty(units: FiatUnits): FiatEquiv = FiatEquiv(BigDecimal(0), units)
}
