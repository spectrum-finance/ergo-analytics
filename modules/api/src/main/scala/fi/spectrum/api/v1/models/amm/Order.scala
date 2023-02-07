package fi.spectrum.api.v1.models.amm

import derevo.cats.show
import derevo.circe.{decoder, encoder}
import derevo.derive
import fi.spectrum.core.domain.{AssetAmount, HexString, TokenId, TxId}
import fi.spectrum.core.domain.order.PoolId
import sttp.tapir.Schema
import tofu.logging.derivation.loggable

@derive(show, encoder, decoder, loggable)
sealed trait Order {
  val timestamp: Option[Long]
  val registerTransactionId: Option[TxId]
  val fee: Option[Long]
  val status: Option[OrderStatus]
}

object Order {

  @derive(show, encoder, decoder, loggable)
  case class Swap(
    baseAmount: AssetAmount,
    timestamp: Option[Long],
    registerTransactionId: Option[TxId],
    poolId: PoolId,
    fee: Option[Long],
    quoteAmount: Option[AssetAmount],
    executedTransactionId: Option[TxId],
    status: Option[OrderStatus]
  ) extends Order

  @derive(show, encoder, decoder, loggable)
  case class Deposit(
    x: AssetAmount,
    y: AssetAmount,
    timestamp: Option[Long],
    registerTransactionId: Option[TxId],
    poolId: PoolId,
    fee: Option[Long],
    executedTransactionId: Option[TxId],
    lpReward: Option[AssetAmount],
    status: Option[OrderStatus]
  ) extends Order

  @derive(show, encoder, decoder, loggable)
  case class Redeem(
    lp: AssetAmount,
    timestamp: Option[Long],
    registerTransactionId: Option[TxId],
    poolId: PoolId,
    fee: Option[Long],
    executedTransactionId: Option[TxId],
    x: Option[AssetAmount],
    y: Option[AssetAmount],
    status: Option[OrderStatus]
  ) extends Order

  @derive(show, encoder, decoder, loggable)
  case class Lock(
    lockedAmount: AssetAmount,
    timestamp: Option[Long],
    registerTransactionId: Option[TxId],
    deadline: Long,
    fee: Option[Long],
    status: Option[OrderStatus]
  ) extends Order

  @derive(show, encoder, decoder, loggable)
  case class AnyOrder(
    x: Option[AssetAmount],
    y: Option[AssetAmount],
    lp: Option[AssetAmount],
    lockedAmount: Option[AssetAmount],
    deadline: Option[Long],
    timestamp: Option[Long],
    registerTransactionId: Option[TxId],
    poolId: Option[PoolId],
    fee: Option[Long],
    executedTransactionId: Option[TxId],
    status: Option[OrderStatus]
  ) extends Order

  implicit val schema: Schema[Order] =
    Schema
      .derived[Order]
      .modify(_.timestamp)(_.description("Timestamp of order creation"))
      .modify(_.registerTransactionId)(_.description("Id of transaction created the order"))
      .modify(_.fee)(_.description("Order fee"))
      .modify(_.status)(_.description("Order status (e.g Mempool, Ledger)"))
      .encodedExample(
        Swap(
          AssetAmount(
            TokenId(HexString.unsafeFromString("0000000000000000000000000000000000000000000000000000000000000000")),
            236293
          ),
          Some(1675258629000L),
          Some(TxId("00000111ba9e273590f73830aaeb9ccbb7e75fb57d9d2d3fb1b6482013b2c38f")),
          PoolId(
            TokenId(HexString.unsafeFromString("9916d75132593c8b07fe18bd8d583bda1652eed7565cf41a4738ddd90fc992ec"))
          ),
          Some(14732L),
          Some(
            AssetAmount(
              TokenId(HexString.unsafeFromString("03faf2cb329f2e90d6d23b58d91bbb6c046aa143261cc21f52fbe2824bfcbf04")),
              378294
            )
          ),
          Some(TxId("00000111dt7q273268f85930jjsr9mmka6w58fb89x6a2d3fb1b6482013b5a86h")),
          Some(OrderStatus.Ledger)
        )
      )
}
