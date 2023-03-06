package fi.spectrum.api.v1.models.amm

import derevo.circe.{decoder, encoder}
import derevo.derive
import fi.spectrum.api.domain.{TotalValueLocked, Volume}
import fi.spectrum.api.models.{Currency, CurrencyId, FiatUnits}
import fi.spectrum.api.v1.endpoints.models.TimeWindow
import io.circe.syntax._
import sttp.tapir.Schema

@derive(encoder, decoder)
final case class PlatformStats(tvl: TotalValueLocked, volume: Volume)

object PlatformStats {

  implicit def schemaPlatformStats: Schema[PlatformStats] =
    Schema
      .derived[PlatformStats]
      .encodedExample(
        PlatformStats(
          TotalValueLocked(BigDecimal(10.0), FiatUnits(Currency(CurrencyId("USD"), 2))),
          Volume(
            BigDecimal(10000),
            FiatUnits(Currency(CurrencyId("USD"), 2)),
            TimeWindow(Some(1674758543L), Some(1674758544L))
          )
        ).asJson
      )

}
