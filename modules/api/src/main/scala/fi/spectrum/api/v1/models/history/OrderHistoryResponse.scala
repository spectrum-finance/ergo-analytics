package fi.spectrum.api.v1.models.history

import derevo.circe.{decoder, encoder}
import derevo.derive
import sttp.tapir.Schema
import tofu.logging.derivation.loggable

@derive(loggable, encoder, decoder)
final case class OrderHistoryResponse(orders: List[ApiOrder], total: Long)

object OrderHistoryResponse {
  implicit val schema: Schema[OrderHistoryResponse] = Schema.derived
}