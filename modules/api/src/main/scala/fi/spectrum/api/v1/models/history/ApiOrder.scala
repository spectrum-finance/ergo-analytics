package fi.spectrum.api.v1.models.history

import cats.syntax.option._
import cats.syntax.show._
import derevo.circe.{decoder, encoder}
import derevo.derive
import fi.spectrum.api.classes.ToAPI
import fi.spectrum.api.db.models.OrderDB._
import fi.spectrum.api.db.models._
import fi.spectrum.api.v1.models.{AssetAmountApi, history}
import fi.spectrum.core.domain._
import fi.spectrum.core.domain.address._
import fi.spectrum.core.domain.analytics.OrderEvaluation._
import fi.spectrum.core.domain.analytics.Processed
import fi.spectrum.core.domain.order.Order.Deposit.{AmmDeposit, LmDeposit}
import fi.spectrum.core.domain.order.Order.Lock.LockV1
import fi.spectrum.core.domain.order.Order.Redeem.LmRedeem.LmRedeemV1
import fi.spectrum.core.domain.order.Order.Redeem.{AmmRedeem, LmRedeem}
import fi.spectrum.core.domain.order.OrderOptics._
import fi.spectrum.core.domain.order.OrderStatus._
import fi.spectrum.core.domain.order.Redeemer.PublicKeyRedeemer
import fi.spectrum.core.domain.order._
import glass.classic.{Equality, Optional, Prism}
import org.ergoplatform.ErgoAddressEncoder
import sttp.tapir.Schema
import io.circe.syntax._
import tofu.logging.derivation.loggable

@derive(encoder, decoder, loggable)
sealed trait ApiOrder {
  val id: OrderId
  val status: history.OrderStatus
  val registerTx: TxData
}

object ApiOrder {

  implicit def schemaApiOrder: Schema[ApiOrder] =
    Schema
      .derived[ApiOrder]
      .encodedExample(
        AmmRedeemApi(
          OrderId("82720e7581cadb1f2bd13d41ffd7d443247dd5d88082b9405c6b361e4a528621"),
          PoolId(
            TokenId(HexString.unsafeFromString("9916d75132593c8b07fe18bd8d583bda1652eed7565cf41a4738ddd90fc992ec"))
          ),
          history.OrderStatus.Pending,
          AssetAmountApi(
            TokenId(HexString.unsafeFromString("03faf2cb329f2e90d6d23b58d91bbb6c046aa143261cc21f52fbe2824bfcbf04")),
            378294.toString
          ),
          None,
          None,
          "ERG",
          9876282394L,
          Some(Address.fromStringUnsafe("9gEwsJePmqhCXwdtCWVhvoRUgNsnpgWkFQ2kFhLwYhRwW7tMc61")),
          TxData(TxId("00000111ba9e273590f73830aaeb9ccbb7e75fb57d9d2d3fb1b6482013b2c38f"), 1675258629000L),
          None,
          Some(TxData(TxId("0000001eba7d278324f83097oopa9hsor7e32ur57d9d2d3fb1b962013b6f87i"), 1675126828000L))
        ).asJson
      )

  implicit val toApiAny: ToAPI[Processed.Any, ApiOrder, Any] = new ToAPI[Processed.Any, ApiOrder, Any] {

    def toAPI(a: Processed.Any, now: Long)(implicit e: ErgoAddressEncoder): Option[ApiOrder] =
      a.wined[AmmDeposit]
        .flatMap(AmmDepositApi.toApiDeposit.toAPI(_, now))
        .orElse(a.wined[AmmRedeem].flatMap(AmmRedeemApi.toApiRedeem.toAPI(_, now)))
        .orElse(a.wined[order.Order.Swap].flatMap(Swap.toApiSwap.toAPI(_, now)))
        .orElse(a.wined[order.Order.Lock].flatMap(Lock.toApiLock.toAPI(_, now)))
        .orElse(a.wined[LmDeposit].flatMap(LmDepositApi.toApiLmDeposit.toAPI(_, now)))
        .orElse(a.wined[LmRedeem].flatMap(LmRedeemApi.toApiLmRedeem.toAPI(_, now)))

    def toAPI(a: Processed.Any, c: Any, now: Long)(implicit e: ErgoAddressEncoder): Option[ApiOrder] = none
  }

  implicit val toApi2Any: ToAPI[Processed.Any, ApiOrder, Processed.Any] =
    new ToAPI[Processed.Any, ApiOrder, Processed.Any] {

      def toAPI(a: Processed.Any, now: Long)(implicit e: ErgoAddressEncoder): Option[ApiOrder] = none

      def toAPI(a: Processed.Any, c: Processed.Any, now: Long)(implicit e: ErgoAddressEncoder): Option[ApiOrder] =
        a.wined[AmmDeposit]
          .flatMap(AmmDepositApi.toApiDeposit2.toAPI(_, c, now))
          .orElse(a.wined[AmmRedeem].flatMap(AmmRedeemApi.toApiRedeem2.toAPI(_, c, now)))
          .orElse(a.wined[order.Order.Swap].flatMap(Swap.toApiSwap2.toAPI(_, c, now)))
          .orElse(a.wined[LmDeposit].flatMap(LmDepositApi.toApiLmDeposit2.toAPI(_, c, now)))
          .orElse(a.wined[LmRedeem].flatMap(LmRedeemApi.toApiLmRedeem2.toAPI(_, c, now)))
    }

  implicit val toApiAnyOrder: ToAPI[AnyOrderDB, ApiOrder, Any] = new ToAPI[AnyOrderDB, ApiOrder, Any] {

    def toAPI(a: AnyOrderDB, now: Long)(implicit e: ErgoAddressEncoder): Option[ApiOrder] =
      AmmDepositApi.toApiDepositDBAny
        .toAPI(a, now)
        .orElse(AmmRedeemApi.toApiRedeemDBAny.toAPI(a, now))
        .orElse(Swap.toApSwapDBAny.toAPI(a, now))
        .orElse(Lock.toApiLockDBAny.toAPI(a, now))
        .orElse(LmDepositApi.toApiLmDepositDBAny.toAPI(a, now))
        .orElse(LmRedeemApi.toApiLmRedeemDBAny.toAPI(a, now))

    def toAPI(a: AnyOrderDB, c: Any, now: Long)(implicit e: ErgoAddressEncoder): Option[ApiOrder] = none
  }

  @derive(encoder, decoder, loggable)
  final case class AmmDepositApi(
    id: OrderId,
    poolId: PoolId,
    status: history.OrderStatus,
    inputX: AssetAmountApi,
    inputY: AssetAmountApi,
    actualX: Option[String],
    actualY: Option[String],
    outputLp: Option[AssetAmountApi],
    feeType: String,
    feeAmount: Long,
    address: Option[Address],
    registerTx: TxData,
    refundTx: Option[TxData],
    evaluateTx: Option[TxData]
  ) extends ApiOrder

  object AmmDepositApi extends DepositInstances {

    implicit val toApiDeposit: ToAPI[Processed[AmmDeposit], ApiOrder, RegisterDeposit] =
      new ToAPI[Processed[AmmDeposit], ApiOrder, RegisterDeposit] {

        def toAPI(o: Processed[AmmDeposit], now: Long)(implicit e: ErgoAddressEncoder): Option[ApiOrder] =
          Optional[order.Order, AmmDepositParams].getOption(o.order).map { params =>
            AmmDepositApi(
              o.order.id,
              o.order.poolId,
              history.OrderStatus.Pending,
              AssetAmountApi.fromAssetAmount(params.inX),
              AssetAmountApi.fromAssetAmount(params.inY),
              none,
              none,
              none,
              o.order.fee.show,
              o.order.fee.amount,
              address.formAddress(o.order.redeemer),
              TxData(o.state.txId, o.state.timestamp),
              none,
              none
            )
          }

        def toAPI(o: Processed[AmmDeposit], c: RegisterDeposit, now: Long)(implicit
          e: ErgoAddressEncoder
        ): Option[ApiOrder] =
          o.evaluation.flatMap(_.widen[AmmDepositEvaluation]).map { eval =>
            AmmDepositApi(
              o.order.id,
              o.order.poolId,
              history.OrderStatus.Pending,
              AssetAmountApi.fromAssetAmount(c.inX),
              AssetAmountApi.fromAssetAmount(c.inY),
              s"${eval.actualX}".some,
              s"${eval.actualY}".some,
              AssetAmountApi.fromAssetAmount(eval.outputLP).some,
              o.order.fee.show,
              o.order.fee.amount,
              address.formAddress(o.order.redeemer),
              TxData(c.info.id, c.info.timestamp),
              if (o.state.status.in(Evaluated, WaitingEvaluation)) TxData(o.state.txId, o.state.timestamp).some else none,
              if (o.state.status.in(Refunded,WaitingRefund)) TxData(o.state.txId, o.state.timestamp).some else none
            )
          }
      }

    implicit val toApiDeposit2: ToAPI[Processed[AmmDeposit], ApiOrder, Processed.Any] =
      new ToAPI[Processed[AmmDeposit], ApiOrder, Processed.Any] {

        def toAPI(a: Processed[AmmDeposit], now: Long)(implicit e: ErgoAddressEncoder): Option[ApiOrder] =
          toApiDeposit.toAPI(a, now)

        def toAPI(x: Processed[AmmDeposit], c: Processed.Any, now: Long)(implicit
          e: ErgoAddressEncoder
        ): Option[ApiOrder] =
          for {
            y      <- c.wined[AmmDeposit]
            params <- Optional[order.Order, AmmDepositParams].getOption(x.order)
          } yield {
            val eval: Option[AmmDepositEvaluation] =
              x.evaluation
                .flatMap(_.widen[AmmDepositEvaluation])
                .orElse(y.evaluation.flatMap(_.widen[AmmDepositEvaluation]))
            AmmDepositApi(
              x.order.id,
              x.order.poolId,
              history.OrderStatus.Pending,
              AssetAmountApi.fromAssetAmount(params.inX),
              AssetAmountApi.fromAssetAmount(params.inY),
              eval.map(s => s"${s.actualX}"),
              eval.map(s => s"${s.actualY}"),
              eval.map(s => AssetAmountApi.fromAssetAmount(s.outputLP)),
              x.order.fee.show,
              x.order.fee.amount,
              address.formAddress(x.order.redeemer),
              mkTxData(x.state, WaitingRegistration).getOrElse(TxData(y.state.txId, y.state.timestamp)),
              mkTxData(x.state, WaitingRefund).orElse(mkTxData(y.state, WaitingRefund)),
              mkTxData(x.state, WaitingEvaluation).orElse(mkTxData(y.state, WaitingEvaluation))
            )
          }
      }
  }

  trait DepositInstances {

    implicit val toApiDepositDB: ToAPI[AmmDepositDB, AmmDepositApi, Any] = new ToAPI[AmmDepositDB, AmmDepositApi, Any] {

      def toAPI(d: AmmDepositDB, now: Long)(implicit e: ErgoAddressEncoder): Option[AmmDepositApi] =
        d.address.flatMap(r => formAddress(PublicKeyRedeemer(r))).map { a =>
          AmmDepositApi(
            d.orderId,
            d.poolId,
            history.OrderStatus.status(d.registerTx, d.evaluateTx, d.refundTx, now),
            AssetAmountApi.fromAssetAmount(d.inputX),
            AssetAmountApi.fromAssetAmount(d.inputY),
            d.actualX.map(s => s"$s"),
            d.actualY.map(s => s"$s"),
            d.outputLp.map(AssetAmountApi.fromAssetAmount),
            d.fee.show,
            d.fee.amount,
            a.some,
            d.registerTx,
            d.refundTx,
            d.evaluateTx
          )
        }

      def toAPI(a: AmmDepositDB, c: Any, now: Long)(implicit e: ErgoAddressEncoder): Option[AmmDepositApi] = none
    }

    implicit val toApiDepositDBAny: ToAPI[AnyOrderDB, AmmDepositApi, Any] = new ToAPI[AnyOrderDB, AmmDepositApi, Any] {

      def toAPI(d: AnyOrderDB, now: Long)(implicit e: ErgoAddressEncoder): Option[AmmDepositApi] =
        for {
          poolId <- d.poolId
          inX    <- d.depositX
          inY    <- d.depositY
          fee    <- d.fee
          address = d.redeemer.flatMap(r => formAddress(Redeemer.PublicKeyRedeemer(r)))
        } yield AmmDepositApi(
          d.orderId,
          poolId,
          history.OrderStatus.status(d.registerTx, d.executedTx, d.refundedTx, now),
          AssetAmountApi.fromAssetAmount(inX),
          AssetAmountApi.fromAssetAmount(inY),
          d.depositActualX.map(s => s"$s"),
          d.depositActualY.map(s => s"$s"),
          d.depositLp.map(AssetAmountApi.fromAssetAmount),
          fee.show,
          fee.amount,
          address,
          d.registerTx,
          d.refundedTx,
          d.executedTx
        )

      def toAPI(a: AnyOrderDB, c: Any, now: Long)(implicit e: ErgoAddressEncoder): Option[AmmDepositApi] = none
    }
  }

  @derive(encoder, decoder, loggable)
  final case class AmmRedeemApi(
    id: OrderId,
    poolId: PoolId,
    status: history.OrderStatus,
    lp: AssetAmountApi,
    outX: Option[AssetAmountApi],
    outY: Option[AssetAmountApi],
    feeType: String,
    feeAmount: Long,
    address: Option[Address],
    registerTx: TxData,
    refundTx: Option[TxData],
    evaluateTx: Option[TxData]
  ) extends ApiOrder

  object AmmRedeemApi extends RedeemInstances {

    implicit val toApiRedeem: ToAPI[Processed[AmmRedeem], ApiOrder, RegisterRedeem] =
      new ToAPI[Processed[AmmRedeem], ApiOrder, RegisterRedeem] {

        def toAPI(o: Processed[AmmRedeem], now: Long)(implicit e: ErgoAddressEncoder): Option[ApiOrder] =
          Optional[order.Order, RedeemParams].getOption(o.order).map { params =>
            AmmRedeemApi(
              o.order.id,
              o.order.poolId,
              history.OrderStatus.Pending,
              AssetAmountApi.fromAssetAmount(params.lp),
              none,
              none,
              o.order.fee.show,
              o.order.fee.amount,
              address.formAddress(o.order.redeemer),
              TxData(o.state.txId, o.state.timestamp),
              none,
              none
            )
          }

        def toAPI(o: Processed[AmmRedeem], c: RegisterRedeem, now: Long)(implicit
          e: ErgoAddressEncoder
        ): Option[ApiOrder] =
          o.evaluation.flatMap(_.widen[AmmRedeemEvaluation]).map { eval =>
            AmmRedeemApi(
              o.order.id,
              o.order.poolId,
              history.OrderStatus.Pending,
              AssetAmountApi.fromAssetAmount(c.lp),
              AssetAmountApi.fromAssetAmount(eval.outputX).some,
              AssetAmountApi.fromAssetAmount(eval.outputY).some,
              o.order.fee.show,
              o.order.fee.amount,
              address.formAddress(o.order.redeemer),
              TxData(c.info.id, c.info.timestamp),
              if (o.state.status.in(Evaluated, WaitingEvaluation)) TxData(o.state.txId, o.state.timestamp).some else none,
              if (o.state.status.in(Refunded,WaitingRefund)) TxData(o.state.txId, o.state.timestamp).some else none
            )
          }
      }

    implicit val toApiRedeem2: ToAPI[Processed[AmmRedeem], ApiOrder, Processed.Any] =
      new ToAPI[Processed[AmmRedeem], ApiOrder, Processed.Any] {

        def toAPI(a: Processed[AmmRedeem], now: Long)(implicit e: ErgoAddressEncoder): Option[ApiOrder] =
          toApiRedeem.toAPI(a, now)

        def toAPI(x: Processed[AmmRedeem], c: Processed.Any, now: Long)(implicit
          e: ErgoAddressEncoder
        ): Option[ApiOrder] =
          for {
            y      <- c.wined[AmmRedeem]
            params <- Optional[order.Order, RedeemParams].getOption(x.order)
          } yield {
            val eval: Option[AmmRedeemEvaluation] =
              x.evaluation
                .flatMap(_.widen[AmmRedeemEvaluation])
                .orElse(y.evaluation.flatMap(_.widen[AmmRedeemEvaluation]))
            AmmRedeemApi(
              x.order.id,
              x.order.poolId,
              history.OrderStatus.Pending,
              AssetAmountApi.fromAssetAmount(params.lp),
              eval.map(_.outputX).map(AssetAmountApi.fromAssetAmount),
              eval.map(_.outputY).map(AssetAmountApi.fromAssetAmount),
              x.order.fee.show,
              x.order.fee.amount,
              address.formAddress(x.order.redeemer),
              mkTxData(x.state, WaitingRegistration).getOrElse(TxData(y.state.txId, y.state.timestamp)),
              mkTxData(x.state, WaitingRefund).orElse(mkTxData(y.state, WaitingRefund)),
              mkTxData(x.state, WaitingEvaluation).orElse(mkTxData(y.state, WaitingEvaluation))
            )
          }
      }
  }

  trait RedeemInstances {

    implicit val toApiRedeemDB: ToAPI[AmmRedeemDB, AmmRedeemApi, Any] = new ToAPI[AmmRedeemDB, AmmRedeemApi, Any] {

      def toAPI(d: AmmRedeemDB, now: Long)(implicit e: ErgoAddressEncoder): Option[AmmRedeemApi] =
        d.address.flatMap(r => formAddress(PublicKeyRedeemer(r))).map { a =>
          AmmRedeemApi(
            d.id,
            d.poolId,
            history.OrderStatus.status(d.registerTx, d.evaluateTx, d.refundTx, now),
            AssetAmountApi.fromAssetAmount(d.lp),
            d.outX.map(AssetAmountApi.fromAssetAmount),
            d.outY.map(AssetAmountApi.fromAssetAmount),
            d.fee.show,
            d.fee.amount,
            a.some,
            d.registerTx,
            d.refundTx,
            d.evaluateTx
          )
        }

      def toAPI(a: AmmRedeemDB, c: Any, now: Long)(implicit e: ErgoAddressEncoder): Option[AmmRedeemApi] = none
    }

    implicit val toApiRedeemDBAny: ToAPI[AnyOrderDB, AmmRedeemApi, Any] = new ToAPI[AnyOrderDB, AmmRedeemApi, Any] {

      def toAPI(d: AnyOrderDB, now: Long)(implicit e: ErgoAddressEncoder): Option[AmmRedeemApi] =
        for {
          poolId <- d.poolId
          lp     <- d.redeemLp
          fee    <- d.fee
          address = d.redeemer.flatMap(r => formAddress(Redeemer.PublicKeyRedeemer(r)))
        } yield AmmRedeemApi(
          d.orderId,
          poolId,
          history.OrderStatus.status(d.registerTx, d.executedTx, d.refundedTx, now),
          AssetAmountApi.fromAssetAmount(lp),
          d.redeemX.map(AssetAmountApi.fromAssetAmount),
          d.redeemY.map(AssetAmountApi.fromAssetAmount),
          fee.show,
          fee.amount,
          address,
          d.registerTx,
          d.refundedTx,
          d.executedTx
        )

      def toAPI(a: AnyOrderDB, c: Any, now: Long)(implicit e: ErgoAddressEncoder): Option[AmmRedeemApi] = none
    }
  }

  @derive(encoder, decoder, loggable)
  final case class Swap(
    id: OrderId,
    poolId: PoolId,
    status: history.OrderStatus,
    base: AssetAmountApi,
    minQuote: AssetAmountApi,
    quote: Option[String],
    feeType: Option[String],
    feeAmount: Option[Long],
    address: Option[Address],
    registerTx: TxData,
    refundTx: Option[TxData],
    evaluateTx: Option[TxData]
  ) extends ApiOrder

  object Swap extends SwapInstances {

    implicit val toApiSwap: ToAPI[Processed[order.Order.Swap], ApiOrder, RegisterSwap] =
      new ToAPI[Processed[order.Order.Swap], ApiOrder, RegisterSwap] {

        def toAPI(o: Processed[order.Order.Swap], now: Long)(implicit e: ErgoAddressEncoder): Option[ApiOrder] =
          Optional[order.Order, SwapParams].getOption(o.order).map { params =>
            Swap(
              o.order.id,
              o.order.poolId,
              history.OrderStatus.Pending,
              AssetAmountApi.fromAssetAmount(params.base),
              AssetAmountApi.fromAssetAmount(params.minQuote),
              none,
              none,
              none,
              address.formAddress(o.order.redeemer),
              TxData(o.state.txId, o.state.timestamp),
              none,
              none
            )
          }

        def toAPI(o: Processed[order.Order.Swap], c: RegisterSwap, now: Long)(implicit
          e: ErgoAddressEncoder
        ): Option[ApiOrder] =
          o.evaluation.flatMap(_.widen[SwapEvaluation]).map { eval =>
            Swap(
              o.order.id,
              o.order.poolId,
              history.OrderStatus.Pending,
              AssetAmountApi.fromAssetAmount(c.base),
              AssetAmountApi.fromAssetAmount(c.minQuote),
              s"${eval.output.amount}".some,
              eval.fee.show.some,
              eval.fee.amount.some,
              address.formAddress(o.order.redeemer),
              TxData(c.info.id, c.info.timestamp),
              if (o.state.status.in(Refunded, WaitingRefund)) TxData(o.state.txId, o.state.timestamp).some else none,
              if (o.state.status.in(Evaluated, WaitingEvaluation)) TxData(o.state.txId, o.state.timestamp).some else none
            )
          }
      }

    implicit val toApiSwap2: ToAPI[Processed[order.Order.Swap], ApiOrder, Processed.Any] =
      new ToAPI[Processed[order.Order.Swap], ApiOrder, Processed.Any] {

        def toAPI(a: Processed[order.Order.Swap], now: Long)(implicit e: ErgoAddressEncoder): Option[ApiOrder] =
          toApiSwap.toAPI(a, now)

        def toAPI(x: Processed[order.Order.Swap], c: Processed.Any, now: Long)(implicit
          e: ErgoAddressEncoder
        ): Option[ApiOrder] =
          for {
            y      <- c.wined[order.Order.Swap]
            params <- Optional[order.Order, SwapParams].getOption(x.order)
          } yield {
            val eval: Option[SwapEvaluation] =
              x.evaluation
                .flatMap(_.widen[SwapEvaluation])
                .orElse(y.evaluation.flatMap(_.widen[SwapEvaluation]))
            Swap(
              x.order.id,
              x.order.poolId,
              history.OrderStatus.Pending,
              AssetAmountApi.fromAssetAmount(params.base),
              AssetAmountApi.fromAssetAmount(params.minQuote),
              eval.map(_.output.amount).map(s => s"$s"),
              eval.map(_.fee.show),
              eval.map(_.fee.amount),
              address.formAddress(x.order.redeemer),
              mkTxData(x.state, WaitingRegistration).getOrElse(TxData(y.state.txId, y.state.timestamp)),
              mkTxData(x.state, WaitingRefund).orElse(mkTxData(y.state, WaitingRefund)),
              mkTxData(x.state, WaitingEvaluation).orElse(mkTxData(y.state, WaitingEvaluation))
            )
          }
      }
  }

  trait SwapInstances {

    implicit val toApiSwapDB: ToAPI[SwapDB, Swap, Any] = new ToAPI[SwapDB, Swap, Any] {

      def toAPI(d: SwapDB, now: Long)(implicit e: ErgoAddressEncoder): Option[Swap] =
        d.address.flatMap(r => formAddress(PublicKeyRedeemer(r))).map { a =>
          Swap(
            d.id,
            d.poolId,
            history.OrderStatus.status(d.registerTx, d.evaluateTx, d.refundTx, now),
            AssetAmountApi.fromAssetAmount(d.base),
            AssetAmountApi.fromAssetAmount(d.minQuote),
            d.quote.map(s => s"$s"),
            d.fee.map(_.show),
            d.fee.map(_.amount),
            a.some,
            d.registerTx,
            d.refundTx,
            d.evaluateTx
          )
        }

      def toAPI(a: SwapDB, c: Any, now: Long)(implicit e: ErgoAddressEncoder): Option[Swap] = none
    }

    implicit val toApSwapDBAny: ToAPI[AnyOrderDB, Swap, Any] = new ToAPI[AnyOrderDB, Swap, Any] {

      def toAPI(d: AnyOrderDB, now: Long)(implicit e: ErgoAddressEncoder): Option[Swap] =
        for {
          poolId   <- d.poolId
          base     <- d.swapBase
          minQuote <- d.swapMinQuote
          address = d.redeemer.flatMap(r => formAddress(Redeemer.PublicKeyRedeemer(r)))
        } yield Swap(
          d.orderId,
          poolId,
          history.OrderStatus.status(d.registerTx, d.executedTx, d.refundedTx, now),
          AssetAmountApi.fromAssetAmount(base),
          AssetAmountApi.fromAssetAmount(minQuote),
          d.swapQuote.map(s => s"$s"),
          d.fee.map(_.show),
          d.fee.map(_.amount),
          address,
          d.registerTx,
          d.refundedTx,
          d.executedTx
        )

      def toAPI(a: AnyOrderDB, c: Any, now: Long)(implicit e: ErgoAddressEncoder): Option[Swap] = none
    }
  }

  @derive(encoder, decoder, loggable)
  final case class Lock(
    id: OrderId,
    status: history.OrderStatus,
    registerTx: TxData,
    deadline: Int,
    asset: AssetAmountApi,
    address: Option[Address],
    evalTxId: Option[TxId],
    evalType: Option[String]
  ) extends ApiOrder

  object Lock extends LockInstances {

    implicit val toApiLock: ToAPI[Processed[order.Order.Lock], ApiOrder, Any] =
      new ToAPI[Processed[order.Order.Lock], ApiOrder, Any] {

        def toAPI(o: Processed[order.Order.Lock], now: Long)(implicit e: ErgoAddressEncoder): Option[ApiOrder] =
          Prism[order.Order.Lock, LockV1].getOption(o.order).map { lock =>
            Lock(
              o.order.id,
              history.OrderStatus.Pending,
              TxData(o.state.txId, o.state.timestamp),
              lock.deadline,
              AssetAmountApi.fromAssetAmount(lock.amount),
              address.formAddress(o.order.redeemer),
              none,
              none
            )
          }

        def toAPI(o: Processed[order.Order.Lock], c: Any, now: Long)(implicit
          e: ErgoAddressEncoder
        ): Option[ApiOrder] = none
      }
  }

  trait LockInstances {

    implicit val toApiLockDB: ToAPI[LockDB, Lock, Any] = new ToAPI[LockDB, Lock, Any] {

      def toAPI(d: LockDB, now: Long)(implicit e: ErgoAddressEncoder): Option[Lock] =
        d.address.flatMap(r => formAddress(PublicKeyRedeemer(r))).map { a =>
          Lock(
            d.id,
            if (d.evalType.isEmpty) history.OrderStatus.Register else  history.OrderStatus.Evaluated,
            d.registerTx,
            d.deadline,
            AssetAmountApi.fromAssetAmount(d.asset),
            a.some,
            d.evalTxId,
            d.evalType
          )
        }

      def toAPI(a: LockDB, c: Any, now: Long)(implicit e: ErgoAddressEncoder): Option[Lock] = none
    }

    implicit val toApiLockDBAny: ToAPI[AnyOrderDB, Lock, Any] = new ToAPI[AnyOrderDB, Lock, Any] {

      def toAPI(d: AnyOrderDB, now: Long)(implicit e: ErgoAddressEncoder): Option[Lock] =
        for {
          deadline <- d.lockDeadline
          asset    <- d.lockAsset
          address = d.redeemer.flatMap(r => formAddress(Redeemer.PublicKeyRedeemer(r)))
        } yield Lock(
          d.orderId,
          if (d.lockEvalType.isEmpty) history.OrderStatus.Register else  history.OrderStatus.Evaluated,
          d.registerTx,
          deadline,
          AssetAmountApi.fromAssetAmount(asset),
          address,
          d.lockEvalTxId,
          d.lockEvalType
        )

      def toAPI(a: AnyOrderDB, c: Any, now: Long)(implicit e: ErgoAddressEncoder): Option[Lock] = none
    }
  }

  @derive(encoder, decoder, loggable)
  final case class LmDepositApi(
    id: OrderId,
    status: history.OrderStatus,
    poolId: PoolId,
    expectedNumEpochs: Int,
    input: AssetAmountApi,
    out: Option[AssetAmountApi],
    compoundId: Option[TokenId],
    registerTx: TxData,
    refundTx: Option[TxData],
    evaluateTx: Option[TxData]
  ) extends ApiOrder

  object LmDepositApi extends LmDepositApiInstances {

    implicit val toApiLmDeposit: ToAPI[Processed[LmDeposit], ApiOrder, RegisterLmDeposit] =
      new ToAPI[Processed[LmDeposit], ApiOrder, RegisterLmDeposit] {

        def toAPI(o: Processed[LmDeposit], now: Long)(implicit e: ErgoAddressEncoder): Option[ApiOrder] =
          Optional[order.Order, LmDepositParams].getOption(o.order).map { params =>
            LmDepositApi(
              o.order.id,
              history.OrderStatus.Pending,
              o.order.poolId,
              params.expectedNumEpochs,
              AssetAmountApi.fromAssetAmount(params.tokens),
              none,
              none,
              TxData(o.state.txId, o.state.timestamp),
              none,
              none
            )
          }

        def toAPI(o: Processed[LmDeposit], c: RegisterLmDeposit, now: Long)(implicit
          e: ErgoAddressEncoder
        ): Option[ApiOrder] =
          o.evaluation.flatMap(_.widen[LmDepositCompoundEvaluation]).map { eval =>
            LmDepositApi(
              o.order.id,
              history.OrderStatus.Pending,
              o.order.poolId,
              c.epochs,
              AssetAmountApi.fromAssetAmount(c.in),
              AssetAmountApi.fromAssetAmount(eval.tokens).some,
              TokenId.unsafeFromString(eval.bundle.id.value).some,
              TxData(c.info.id, c.info.timestamp),
              if (o.state.status.in(Refunded, WaitingRefund)) TxData(o.state.txId, o.state.timestamp).some else none,
              if (o.state.status.in(Evaluated, WaitingEvaluation)) TxData(o.state.txId, o.state.timestamp).some else none
            )
          }
      }

    implicit val toApiLmDeposit2: ToAPI[Processed[LmDeposit], ApiOrder, Processed.Any] =
      new ToAPI[Processed[LmDeposit], ApiOrder, Processed.Any] {

        def toAPI(a: Processed[LmDeposit], now: Long)(implicit e: ErgoAddressEncoder): Option[ApiOrder] =
          toApiLmDeposit.toAPI(a, now)

        def toAPI(x: Processed[LmDeposit], c: Processed.Any, now: Long)(implicit
          e: ErgoAddressEncoder
        ): Option[ApiOrder] =
          for {
            y      <- c.wined[LmDeposit]
            params <- Optional[order.Order, LmDepositParams].getOption(x.order)
          } yield {
            val eval: Option[LmDepositCompoundEvaluation] =
              x.evaluation
                .flatMap(_.widen[LmDepositCompoundEvaluation])
                .orElse(y.evaluation.flatMap(_.widen[LmDepositCompoundEvaluation]))
            LmDepositApi(
              x.order.id,
              history.OrderStatus.Pending,
              x.order.poolId,
              params.expectedNumEpochs,
              AssetAmountApi.fromAssetAmount(params.tokens),
              eval.map(_.tokens).map(AssetAmountApi.fromAssetAmount),
              eval.map(_.bundle.id).map(r => TokenId.unsafeFromString(r.value)),
              mkTxData(x.state, WaitingRegistration).getOrElse(TxData(y.state.txId, y.state.timestamp)),
              mkTxData(x.state, WaitingRefund).orElse(mkTxData(y.state, WaitingRefund)),
              mkTxData(x.state, WaitingEvaluation).orElse(mkTxData(y.state, WaitingEvaluation))
            )
          }
      }
  }

  trait LmDepositApiInstances {

    implicit val toApiLmDepositDB: ToAPI[LmDepositDB, LmDepositApi, Any] = new ToAPI[LmDepositDB, LmDepositApi, Any] {

      def toAPI(d: LmDepositDB, now: Long)(implicit e: ErgoAddressEncoder): Option[LmDepositApi] =
        LmDepositApi(
          d.orderId,
          history.OrderStatus.status(d.registerTx, d.evaluateTx, d.refundTx, now),
          d.poolId,
          d.expectedNumEpochs,
          AssetAmountApi.fromAssetAmount(d.input),
          d.lp.map(AssetAmountApi.fromAssetAmount),
          d.compoundId,
          d.registerTx,
          d.refundTx,
          d.evaluateTx
        ).some

      def toAPI(a: LmDepositDB, c: Any, now: Long)(implicit e: ErgoAddressEncoder): Option[LmDepositApi] = none
    }

    implicit val toApiLmDepositDBAny: ToAPI[AnyOrderDB, LmDepositApi, Any] = new ToAPI[AnyOrderDB, LmDepositApi, Any] {

      def toAPI(d: AnyOrderDB, now: Long)(implicit e: ErgoAddressEncoder): Option[LmDepositApi] =
        for {
          expectedNumEpochs <- d.lmDepositExpectedNumEpochs
          input             <- d.lmDepositInput
          poolId            <- d.poolId
        } yield LmDepositApi(
          d.orderId,
          history.OrderStatus.status(d.registerTx, d.executedTx, d.refundedTx, now),
          poolId,
          expectedNumEpochs,
          AssetAmountApi.fromAssetAmount(input),
          d.lmDepositLp.map(AssetAmountApi.fromAssetAmount),
          d.lmDepositCompoundId,
          d.registerTx,
          d.refundedTx,
          d.executedTx
        )

      def toAPI(a: AnyOrderDB, c: Any, now: Long)(implicit e: ErgoAddressEncoder): Option[LmDepositApi] = none
    }
  }

  @derive(encoder, decoder, loggable)
  final case class LmRedeemApi(
    id: OrderId,
    status: history.OrderStatus,
    poolId: Option[PoolId],
    bundleKeyId: TokenId,
    expectedLq: AssetAmountApi,
    out: Option[AssetAmountApi],
    boxId: Option[TokenId],
    registerTx: TxData,
    refundTx: Option[TxData],
    evaluateTx: Option[TxData]
  ) extends ApiOrder

  object LmRedeemApi extends LmRedeemApiInstances {

    implicit val toApiLmRedeem: ToAPI[Processed[LmRedeem], ApiOrder, RegisterLmRedeem] =
      new ToAPI[Processed[LmRedeem], ApiOrder, RegisterLmRedeem] {

        def toAPI(o: Processed[LmRedeem], now: Long)(implicit e: ErgoAddressEncoder): Option[ApiOrder] =
          Prism[LmRedeem, LmRedeemV1].getOption(o.order).map { o1 =>
            LmRedeemApi(
              o.order.id,
              history.OrderStatus.Pending,
              none,
              o1.bundleKeyId,
              AssetAmountApi.fromAssetAmount(o1.expectedLq),
              none,
              none,
              TxData(o.state.txId, o.state.timestamp),
              none,
              none
            )
          }

        def toAPI(o: Processed[LmRedeem], c: RegisterLmRedeem, now: Long)(implicit
          e: ErgoAddressEncoder
        ): Option[ApiOrder] =
          o.evaluation.flatMap(_.widen[LmRedeemEvaluation]).map { eval =>
            LmRedeemApi(
              o.order.id,
              history.OrderStatus.Pending,
              eval.poolId.some,
              c.bundleKeyId,
              AssetAmountApi.fromAssetAmount(c.expectedLq),
              AssetAmountApi.fromAssetAmount(eval.out).some,
              eval.boxId.some.map(r => TokenId.unsafeFromString(r.value)),
              TxData(c.info.id, c.info.timestamp),
              if (o.state.status.in(Refunded, WaitingRefund)) TxData(o.state.txId, o.state.timestamp).some else none,
              if (o.state.status.in(Evaluated, WaitingEvaluation)) TxData(o.state.txId, o.state.timestamp).some else none
            )
          }
      }

    implicit val toApiLmRedeem2: ToAPI[Processed[LmRedeem], ApiOrder, Processed.Any] =
      new ToAPI[Processed[LmRedeem], ApiOrder, Processed.Any] {

        def toAPI(a: Processed[LmRedeem], now: Long)(implicit e: ErgoAddressEncoder): Option[ApiOrder] =
          toApiLmRedeem.toAPI(a, now)

        def toAPI(x: Processed[LmRedeem], c: Processed.Any, now: Long)(implicit
          e: ErgoAddressEncoder
        ): Option[ApiOrder] =
          for {
            x1 <- Prism[LmRedeem, LmRedeemV1].getOption(x.order)
            y  <- c.wined[LmRedeem]
          } yield {
            val eval: Option[LmRedeemEvaluation] =
              x.evaluation
                .flatMap(_.widen[LmRedeemEvaluation])
                .orElse(y.evaluation.flatMap(_.widen[LmRedeemEvaluation]))
            LmRedeemApi(
              x.order.id,
              history.OrderStatus.Pending,
              eval.map(_.poolId),
              x1.bundleKeyId,
              AssetAmountApi.fromAssetAmount(x1.expectedLq),
              eval.map(_.out).map(AssetAmountApi.fromAssetAmount),
              eval.map(_.boxId).map(r => TokenId.unsafeFromString(r.value)),
              mkTxData(x.state, WaitingRegistration).getOrElse(TxData(y.state.txId, y.state.timestamp)),
              mkTxData(x.state, WaitingRefund).orElse(mkTxData(y.state, WaitingRefund)),
              mkTxData(x.state, WaitingEvaluation).orElse(mkTxData(y.state, WaitingEvaluation))
            )
          }
      }
  }

  trait LmRedeemApiInstances {

    implicit val toApiLmRedeemDB: ToAPI[LmRedeemsDB, LmRedeemApi, Any] =
      new ToAPI[LmRedeemsDB, LmRedeemApi, Any] {

        def toAPI(d: LmRedeemsDB, now: Long)(implicit e: ErgoAddressEncoder): Option[LmRedeemApi] =
          LmRedeemApi(
            d.orderId,
            history.OrderStatus.status(d.registerTx, d.evaluateTx, d.refundTx, now),
            d.poolId,
            d.bundleKeyId,
            AssetAmountApi.fromAssetAmount(d.expectedLq),
            d.out.map(AssetAmountApi.fromAssetAmount),
            d.boxId,
            d.registerTx,
            d.refundTx,
            d.evaluateTx
          ).some

        def toAPI(a: LmRedeemsDB, c: Any, now: Long)(implicit e: ErgoAddressEncoder): Option[LmRedeemApi] = none
      }

    implicit val toApiLmRedeemDBAny: ToAPI[AnyOrderDB, LmRedeemApi, Any] =
      new ToAPI[AnyOrderDB, LmRedeemApi, Any] {

        def toAPI(d: AnyOrderDB, now: Long)(implicit e: ErgoAddressEncoder): Option[LmRedeemApi] =
          for {
            lmRedeemExpectedLq  <- d.lmRedeemExpectedLq
            lmRedeemBundleKeyId <- d.lmRedeemBundleKeyId
            poolId = d.poolId
          } yield LmRedeemApi(
            d.orderId,
            history.OrderStatus.status(d.registerTx, d.executedTx, d.refundedTx, now),
            poolId,
            lmRedeemBundleKeyId,
            AssetAmountApi.fromAssetAmount(lmRedeemExpectedLq),
            d.lmRedeemOut.map(AssetAmountApi.fromAssetAmount),
            d.lmRedeemBoxId,
            d.registerTx,
            d.refundedTx,
            d.executedTx
          )

        def toAPI(a: AnyOrderDB, c: Any, now: Long)(implicit e: ErgoAddressEncoder): Option[LmRedeemApi] = none
      }
  }

  private def mkTxData(state: OrderState, status: order.OrderStatus): Option[TxData] =
    if (mapToMempool(state.status).in(status)) TxData(state.txId, state.timestamp).some else none
}
