package fi.spectrum.dex.markets.api.v1.models.amm

import derevo.circe.{decoder, encoder}
import derevo.derive
import fi.spectrum.dex.markets.api.v1.models.amm.types.RealPrice
import fi.spectrum.dex.markets.api.v1.models.amm.types.RealPrice
import sttp.tapir.Schema

@derive(encoder, decoder)
case class PricePoint(
  timestamp: Long,
  price: RealPrice
)

object PricePoint {
  implicit val schema: Schema[PricePoint] = Schema.derived
}
