package fi.spectrum.api.v1.models.amm

import derevo.circe.{decoder, encoder}
import derevo.derive
import fi.spectrum.core.domain.{Address, TokenId, TxId}
import sttp.tapir.Schema
import tofu.logging.derivation.loggable

@derive(loggable, encoder, decoder)
case class OrdersRequest(
  addresses: List[Address],
  orderType: Option[OrderType],
  orderStatus: Option[OrderStatus],
  assetId: Option[TokenId],
  txId: Option[TxId]
)

object OrdersRequest {
  implicit val schema: Schema[OrdersRequest] = Schema.derived
}
