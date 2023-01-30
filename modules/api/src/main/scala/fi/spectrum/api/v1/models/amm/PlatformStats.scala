package fi.spectrum.api.v1.models.amm

import derevo.circe.{decoder, encoder}
import derevo.derive
import fi.spectrum.api.domain.{TotalValueLocked, Volume}
import fi.spectrum.api.models.{Currency, CurrencyId, FiatUnits}
import fi.spectrum.api.v1.endpoints.models.TimeWindow
import io.circe.syntax._
import sttp.tapir.Schema
import tofu.logging.derivation.loggable

@derive(encoder, decoder, loggable)
final case class PlatformStats(tvl: TotalValueLocked, volume: Volume)

object PlatformStats {

  implicit val schemaFees: Schema[PlatformStats] =
    Schema.derived
      .encodedExample(
        PlatformStats(
          TotalValueLocked(BigDecimal(10.0), FiatUnits(Currency(CurrencyId("id"), 3))),
          Volume(
            BigDecimal(10000),
            FiatUnits(Currency(CurrencyId("id"), 3)),
            TimeWindow(Some(1674758543L), Some(1674758544L))
          )
        )
      )
}
