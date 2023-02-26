package fi.spectrum.api.v1.models.history

import derevo.circe.{decoder, encoder}
import derevo.derive
import fi.spectrum.core.domain.{Address, HexString, TokenId, TxId}
import sttp.tapir.Schema
import tofu.logging.derivation.loggable
import io.circe.syntax._

@derive(encoder, decoder, loggable)
final case class HistoryApiQuery(
  addresses: List[Address],
  orderType: Option[OrderType],
  orderStatus: Option[OrderStatusApi],
  txId: Option[TxId],
  tokenIds: Option[List[TokenId]],
  tokenPair: Option[TokenPair]
)

object HistoryApiQuery {

  implicit def schemaHistoryApiQuery: Schema[HistoryApiQuery] =
    Schema
      .derived[HistoryApiQuery]
      .modify(_.addresses)(_.description("Corresponding addresses"))
      .modify(_.orderType)(_.description("Order type filter (swap, deposit, redeem, lock)"))
      .modify(_.orderStatus)(_.description("Status filter (orders only with given status)"))
      .modify(_.txId)(_.description("Transaction filter (orders only in given tx)"))
      .modify(_.tokenIds)(_.description("Token filter (orders only with given tokens)"))
      .encodedExample(
        HistoryApiQuery(
          Address.fromStringUnsafe("9gEwsJePmqhCXwdtCWVhvoRUgNsnpgWkFQ2kFhLwYhRwW7tMc61") :: Nil,
          Some(OrderType.Swap),
          Some(OrderStatusApi.Evaluated),
          Some(TxId("00000111ba9e273590f73830aaeb9ccbb7e75fb57d9d2d3fb1b6482013b2c38f")),
          Some(
            TokenId(
              HexString.unsafeFromString("0000000000000000000000000000000000000000000000000000000000000000")
            ) :: Nil
          ),
          Some(
            TokenPair(
              TokenId(HexString.unsafeFromString("0000000000000000000000000000000000000000000000000000000000000000")),
              TokenId(HexString.unsafeFromString("03faf2cb329f2e90d6d23b58d91bbb6c046aa143261cc21f52fbe2824bfcbf04"))
            )
          )
        ).asJson
      )
}
