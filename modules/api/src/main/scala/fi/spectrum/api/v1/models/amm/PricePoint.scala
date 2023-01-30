package fi.spectrum.api.v1.models.amm

import derevo.circe.{decoder, encoder}
import derevo.derive
import fi.spectrum.api.v1.models.amm.types.RealPrice
import sttp.tapir.Schema
import tofu.logging.derivation.loggable

@derive(encoder, decoder, loggable)
case class PricePoint(
  timestamp: Long,
  price: RealPrice
)

object PricePoint {
  implicit val schema: Schema[PricePoint] = Schema.derived
}
