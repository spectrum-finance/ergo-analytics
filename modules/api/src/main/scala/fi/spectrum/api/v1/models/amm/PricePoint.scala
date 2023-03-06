package fi.spectrum.api.v1.models.amm

import derevo.circe.{decoder, encoder}
import derevo.derive
import fi.spectrum.api.v1.models.amm.types.RealPrice
import io.circe.syntax.EncoderOps
import sttp.tapir.Schema
import tofu.logging.derivation.loggable

@derive(encoder, decoder, loggable)
case class PricePoint(
  timestamp: Long,
  price: RealPrice
)

object PricePoint {

  implicit def schemaPricePoint: Schema[PricePoint] = Schema
    .derived[PricePoint]
    .modify(_.timestamp)(_.description("Timestamp of a point"))
    .modify(_.price)(_.description("Price at given timestamp"))
    .encodedExample(PricePoint(1675586229000L, RealPrice(BigDecimal(150))).asJson)
}
