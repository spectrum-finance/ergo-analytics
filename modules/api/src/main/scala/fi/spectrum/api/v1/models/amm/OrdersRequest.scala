package fi.spectrum.api.v1.models.amm

import derevo.circe.{decoder, encoder}
import derevo.derive
import fi.spectrum.core.domain.{Address, HexString, TokenId, TxId}
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

  implicit val schema: Schema[OrdersRequest] =
    Schema
      .derived[OrdersRequest]
      .modify(_.addresses)(_.description("Corresponding addresses"))
      .modify(_.orderType)(_.description("Order type filter (swap, deposit, redeem, lock)"))
      .modify(_.orderStatus)(_.description("Status filter (orders only with given status)"))
      .modify(_.assetId)(_.description("Asset filter (orders only with given asset)"))
      .modify(_.txId)(_.description("Transaction filter (orders only in given tx)"))
      .encodedExample(
        OrdersRequest(
          Address.fromStringUnsafe("9gEwsJePmqhCXwdtCWVhvoRUgNsnpgWkFQ2kFhLwYhRwW7tMc61") :: Nil,
          Some(OrderType.Swap),
          Some(OrderStatus.Mempool),
          Some(TokenId(HexString.unsafeFromString("0000000000000000000000000000000000000000000000000000000000000000"))),
          Some(TxId("00000111ba9e273590f73830aaeb9ccbb7e75fb57d9d2d3fb1b6482013b2c38f"))
        )
      )
}
