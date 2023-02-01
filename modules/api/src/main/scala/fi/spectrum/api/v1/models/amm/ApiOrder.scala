package fi.spectrum.api.v1.models.amm

import fi.spectrum.core.domain.analytics.OrderEvaluation.{DepositEvaluation, RedeemEvaluation, SwapEvaluation}
import fi.spectrum.core.domain.analytics.Processed
import fi.spectrum.core.domain.order.OrderStatus.{mapToMempool, WaitingEvaluation, WaitingRefund, WaitingRegistration}
import fi.spectrum.core.domain.{address, order, Address, AssetAmount, TxId}
import fi.spectrum.core.domain.order.{DepositParams, Fee, OrderId, OrderState, PoolId, RedeemParams, SwapParams}
import glass.classic.{Optional, Prism}
import org.ergoplatform.ErgoAddressEncoder
import order.OrderOptics._
import cats.syntax.option._
import derevo.circe.{decoder, encoder}
import derevo.derive
import fi.spectrum.api.classes.ToAPI
import fi.spectrum.api.models.{RegisterDeposit, RegisterRedeem, RegisterSwap}
import fi.spectrum.core.domain.order.Order.Lock.LockV1
import sttp.tapir.Schema

@derive(encoder, decoder)
sealed trait ApiOrder {
  val id: OrderId
  val status: OrderStatus
}

object ApiOrder {

  implicit val schema: Schema[ApiOrder] = Schema.derived

  implicit val toApiAny: ToAPI[Processed.Any, ApiOrder, Any] = new ToAPI[Processed.Any, ApiOrder, Any] {

    def toAPI(a: Processed.Any)(implicit e: ErgoAddressEncoder): Option[ApiOrder] =
      a.wined[order.Order.Deposit]
        .flatMap(Deposit.toApiDeposit.toAPI(_))
        .orElse(a.wined[order.Order.Redeem].flatMap(Redeem.toApiRedeem.toAPI(_)))
        .orElse(a.wined[order.Order.Swap].flatMap(Swap.toApiSwap.toAPI(_)))
        .orElse(a.wined[order.Order.Lock].flatMap(Lock.toApiLock.toAPI(_)))

    def toAPI(a: Processed.Any, c: Any)(implicit e: ErgoAddressEncoder): Option[ApiOrder] = none
  }

  implicit val toApi2Any: ToAPI[Processed.Any, ApiOrder, Processed.Any] =
    new ToAPI[Processed.Any, ApiOrder, Processed.Any] {

      def toAPI(a: Processed.Any)(implicit e: ErgoAddressEncoder): Option[ApiOrder] = none

      def toAPI(a: Processed.Any, c: Processed.Any)(implicit e: ErgoAddressEncoder): Option[ApiOrder] =
        a.wined[order.Order.Deposit]
          .flatMap(Deposit.toApiDeposit2.toAPI(_, c))
          .orElse(a.wined[order.Order.Redeem].flatMap(Redeem.toApiRedeem2.toAPI(_, c)))
          .orElse(a.wined[order.Order.Swap].flatMap(Swap.toApiSwap2.toAPI(_, c)))
    }

  @derive(encoder, decoder)
  final case class Deposit(
    id: OrderId,
    poolId: PoolId,
    status: OrderStatus,
    inputX: AssetAmount,
    inputY: AssetAmount,
    actualX: Option[Long],
    actualY: Option[Long],
    outputLp: Option[AssetAmount],
    fee: Fee,
    address: Option[Address],
    registerTx: TxData,
    refundTx: Option[TxData],
    evaluateTx: Option[TxData]
  ) extends ApiOrder

  object Deposit {

    implicit val toApiDeposit: ToAPI[Processed[order.Order.Deposit], ApiOrder, RegisterDeposit] =
      new ToAPI[Processed[order.Order.Deposit], ApiOrder, RegisterDeposit] {

        def toAPI(o: Processed[order.Order.Deposit])(implicit e: ErgoAddressEncoder): Option[ApiOrder] =
          Optional[order.Order, DepositParams].getOption(o.order).map { params =>
            Deposit(
              o.order.id,
              o.order.poolId,
              OrderStatus.Mempool,
              params.inX,
              params.inY,
              none,
              none,
              none,
              o.order.fee,
              address.formAddress(o.order.redeemer),
              TxData(o.state.txId, o.state.timestamp),
              none,
              none
            )
          }

        def toAPI(o: Processed[order.Order.Deposit], c: RegisterDeposit)(implicit
          e: ErgoAddressEncoder
        ): Option[ApiOrder] =
          o.evaluation.flatMap(_.widen[DepositEvaluation]).map { eval =>
            Deposit(
              o.order.id,
              o.order.poolId,
              OrderStatus.Mempool,
              c.inX,
              c.inY,
              eval.actualX.some,
              eval.actualY.some,
              eval.outputLP.some,
              o.order.fee,
              address.formAddress(o.order.redeemer),
              TxData(c.info.id, c.info.timestamp),
              if (o.state.status.in(WaitingEvaluation)) TxData(o.state.txId, o.state.timestamp).some else none,
              if (o.state.status.in(WaitingRefund)) TxData(o.state.txId, o.state.timestamp).some else none
            )
          }
      }

    implicit val toApiDeposit2: ToAPI[Processed[order.Order.Deposit], ApiOrder, Processed.Any] =
      new ToAPI[Processed[order.Order.Deposit], ApiOrder, Processed.Any] {

        def toAPI(a: Processed[order.Order.Deposit])(implicit e: ErgoAddressEncoder): Option[ApiOrder] =
          toApiDeposit.toAPI(a)

        def toAPI(x: Processed[order.Order.Deposit], c: Processed.Any)(implicit
          e: ErgoAddressEncoder
        ): Option[ApiOrder] =
          for {
            y      <- c.wined[order.Order.Deposit]
            params <- Optional[order.Order, DepositParams].getOption(x.order)
          } yield {
            val eval: Option[DepositEvaluation] =
              x.evaluation
                .flatMap(_.widen[DepositEvaluation])
                .orElse(y.evaluation.flatMap(_.widen[DepositEvaluation]))
            Deposit(
              x.order.id,
              x.order.poolId,
              OrderStatus.Mempool,
              params.inX,
              params.inY,
              eval.map(_.actualX),
              eval.map(_.actualY),
              eval.map(_.outputLP),
              x.order.fee,
              address.formAddress(x.order.redeemer),
              mkTxData(x.state, WaitingRegistration).getOrElse(TxData(y.state.txId, y.state.timestamp)),
              mkTxData(x.state, WaitingRefund).orElse(mkTxData(y.state, WaitingRefund)),
              mkTxData(x.state, WaitingRegistration).orElse(mkTxData(y.state, WaitingRegistration))
            )
          }
      }
  }

  @derive(encoder, decoder)
  final case class Redeem(
    id: OrderId,
    poolId: PoolId,
    status: OrderStatus,
    lp: AssetAmount,
    outX: Option[AssetAmount],
    outY: Option[AssetAmount],
    fee: Fee,
    address: Option[Address],
    registerTx: TxData,
    refundTx: Option[TxData],
    evaluateTx: Option[TxData]
  ) extends ApiOrder

  object Redeem {

    implicit val toApiRedeem: ToAPI[Processed[order.Order.Redeem], ApiOrder, RegisterRedeem] =
      new ToAPI[Processed[order.Order.Redeem], ApiOrder, RegisterRedeem] {

        def toAPI(o: Processed[order.Order.Redeem])(implicit e: ErgoAddressEncoder): Option[ApiOrder] =
          Optional[order.Order, RedeemParams].getOption(o.order).map { params =>
            Redeem(
              o.order.id,
              o.order.poolId,
              OrderStatus.Mempool,
              params.lp,
              none,
              none,
              o.order.fee,
              address.formAddress(o.order.redeemer),
              TxData(o.state.txId, o.state.timestamp),
              none,
              none
            )
          }

        def toAPI(o: Processed[order.Order.Redeem], c: RegisterRedeem)(implicit
          e: ErgoAddressEncoder
        ): Option[ApiOrder] =
          o.evaluation.flatMap(_.widen[RedeemEvaluation]).map { eval =>
            Redeem(
              o.order.id,
              o.order.poolId,
              OrderStatus.Mempool,
              c.lp,
              eval.outputX.some,
              eval.outputY.some,
              o.order.fee,
              address.formAddress(o.order.redeemer),
              TxData(c.info.id, c.info.timestamp),
              if (o.state.status.in(WaitingEvaluation)) TxData(o.state.txId, o.state.timestamp).some else none,
              if (o.state.status.in(WaitingRefund)) TxData(o.state.txId, o.state.timestamp).some else none
            )
          }
      }

    implicit val toApiRedeem2: ToAPI[Processed[order.Order.Redeem], ApiOrder, Processed.Any] =
      new ToAPI[Processed[order.Order.Redeem], ApiOrder, Processed.Any] {

        def toAPI(a: Processed[order.Order.Redeem])(implicit e: ErgoAddressEncoder): Option[ApiOrder] =
          toApiRedeem.toAPI(a)

        def toAPI(x: Processed[order.Order.Redeem], c: Processed.Any)(implicit
          e: ErgoAddressEncoder
        ): Option[ApiOrder] =
          for {
            y      <- c.wined[order.Order.Redeem]
            params <- Optional[order.Order, RedeemParams].getOption(x.order)
          } yield {
            val eval: Option[RedeemEvaluation] =
              x.evaluation
                .flatMap(_.widen[RedeemEvaluation])
                .orElse(y.evaluation.flatMap(_.widen[RedeemEvaluation]))
            Redeem(
              x.order.id,
              x.order.poolId,
              OrderStatus.Mempool,
              params.lp,
              eval.map(_.outputX),
              eval.map(_.outputY),
              x.order.fee,
              address.formAddress(x.order.redeemer),
              mkTxData(x.state, WaitingRegistration).getOrElse(TxData(y.state.txId, y.state.timestamp)),
              mkTxData(x.state, WaitingRefund).orElse(mkTxData(y.state, WaitingRefund)),
              mkTxData(x.state, WaitingRegistration).orElse(mkTxData(y.state, WaitingRegistration))
            )
          }
      }
  }

  @derive(encoder, decoder)
  final case class Swap(
    id: OrderId,
    poolId: PoolId,
    status: OrderStatus,
    base: AssetAmount,
    minQuote: AssetAmount,
    quote: Option[AssetAmount],
    fee: Option[Fee],
    address: Option[Address],
    registerTx: TxData,
    refundTx: Option[TxData],
    evaluateTx: Option[TxData]
  ) extends ApiOrder

  object Swap {

    implicit val toApiSwap: ToAPI[Processed[order.Order.Swap], ApiOrder, RegisterSwap] =
      new ToAPI[Processed[order.Order.Swap], ApiOrder, RegisterSwap] {

        def toAPI(o: Processed[order.Order.Swap])(implicit e: ErgoAddressEncoder): Option[ApiOrder] =
          Optional[order.Order, SwapParams].getOption(o.order).map { params =>
            Swap(
              o.order.id,
              o.order.poolId,
              OrderStatus.Mempool,
              params.base,
              params.minQuote,
              none,
              none,
              address.formAddress(o.order.redeemer),
              TxData(o.state.txId, o.state.timestamp),
              none,
              none
            )
          }

        def toAPI(o: Processed[order.Order.Swap], c: RegisterSwap)(implicit
          e: ErgoAddressEncoder
        ): Option[ApiOrder] =
          o.evaluation.flatMap(_.widen[SwapEvaluation]).map { eval =>
            Swap(
              o.order.id,
              o.order.poolId,
              OrderStatus.Mempool,
              c.base,
              c.minQuote,
              eval.output.some,
              eval.fee.some,
              address.formAddress(o.order.redeemer),
              TxData(c.info.id, c.info.timestamp),
              if (o.state.status.in(WaitingRefund)) TxData(o.state.txId, o.state.timestamp).some else none,
              if (o.state.status.in(WaitingEvaluation)) TxData(o.state.txId, o.state.timestamp).some else none
            )
          }
      }

    implicit val toApiSwap2: ToAPI[Processed[order.Order.Swap], ApiOrder, Processed.Any] =
      new ToAPI[Processed[order.Order.Swap], ApiOrder, Processed.Any] {

        def toAPI(a: Processed[order.Order.Swap])(implicit e: ErgoAddressEncoder): Option[ApiOrder] =
          toApiSwap.toAPI(a)

        def toAPI(x: Processed[order.Order.Swap], c: Processed.Any)(implicit
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
              OrderStatus.Mempool,
              params.base,
              params.minQuote,
              eval.map(_.output),
              eval.map(_.fee),
              address.formAddress(x.order.redeemer),
              mkTxData(x.state, WaitingRegistration).getOrElse(TxData(y.state.txId, y.state.timestamp)),
              mkTxData(x.state, WaitingRefund).orElse(mkTxData(y.state, WaitingRefund)),
              mkTxData(x.state, WaitingEvaluation).orElse(mkTxData(y.state, WaitingEvaluation))
            )
          }
      }
  }

  @derive(encoder, decoder)
  final case class Lock(
    id: OrderId,
    status: OrderStatus,
    txId: TxId,
    ts: Long,
    deadline: Int,
    asset: AssetAmount,
    address: Option[Address],
    evalTxId: Option[TxId],
    evalType: Option[String]
  ) extends ApiOrder

  object Lock {

    implicit val toApiLock: ToAPI[Processed[order.Order.Lock], ApiOrder, Any] =
      new ToAPI[Processed[order.Order.Lock], ApiOrder, Any] {

        def toAPI(o: Processed[order.Order.Lock])(implicit e: ErgoAddressEncoder): Option[ApiOrder] =
          Prism[order.Order.Lock, LockV1].getOption(o.order).map { lock =>
            Lock(
              o.order.id,
              OrderStatus.Mempool,
              o.state.txId,
              o.state.timestamp,
              lock.deadline,
              lock.amount,
              address.formAddress(o.order.redeemer),
              none,
              none
            )
          }

        def toAPI(o: Processed[order.Order.Lock], c: Any)(implicit
          e: ErgoAddressEncoder
        ): Option[ApiOrder] = none
      }
  }

  private def mkTxData(state: OrderState, status: order.OrderStatus): Option[TxData] =
    if (mapToMempool(state.status).in(status)) TxData(state.txId, state.timestamp).some else none
}
