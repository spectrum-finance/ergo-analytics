package fi.spectrum.api.v1.models.history

import cats.syntax.option._
import derevo.circe.{decoder, encoder}
import derevo.derive
import fi.spectrum.api.classes.ToAPI
import fi.spectrum.api.db.models.OrderDB._
import fi.spectrum.api.db.models.{RegisterDeposit, RegisterRedeem, RegisterSwap}
import fi.spectrum.api.v1.models.history
import fi.spectrum.core.domain._
import fi.spectrum.core.domain.address._
import fi.spectrum.core.domain.analytics.OrderEvaluation.{DepositEvaluation, RedeemEvaluation, SwapEvaluation}
import fi.spectrum.core.domain.analytics.Processed
import fi.spectrum.core.domain.order.Order.Lock.LockV1
import fi.spectrum.core.domain.order.OrderOptics._
import fi.spectrum.core.domain.order.OrderStatus.{WaitingEvaluation, WaitingRefund, WaitingRegistration, mapToMempool}
import fi.spectrum.core.domain.order._
import glass.classic.{Optional, Prism}
import org.ergoplatform.ErgoAddressEncoder
import sttp.tapir.Schema
import tofu.logging.derivation.loggable

@derive(encoder, decoder, loggable)
sealed trait ApiOrder {
  val id: OrderId
  val status: history.OrderStatus
  val registerTx: TxData
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

  implicit val toApiAnyOrder: ToAPI[AnyOrderDB, ApiOrder, Any] = new ToAPI[AnyOrderDB, ApiOrder, Any] {

    def toAPI(a: AnyOrderDB)(implicit e: ErgoAddressEncoder): Option[ApiOrder] =
      Deposit.toApiDepositDBAny
        .toAPI(a)
        .orElse(Redeem.toApiRedeemDBAny.toAPI(a))
        .orElse(Swap.toApSwapDBAny.toAPI(a))
        .orElse(Lock.toApiLockDBAny.toAPI(a))

    def toAPI(a: AnyOrderDB, c: Any)(implicit e: ErgoAddressEncoder): Option[ApiOrder] = none
  }

  @derive(encoder, decoder, loggable)
  final case class Deposit(
    id: OrderId,
    poolId: PoolId,
    status: history.OrderStatus,
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

  object Deposit extends DepositInstances {

    implicit val toApiDeposit: ToAPI[Processed[order.Order.Deposit], ApiOrder, RegisterDeposit] =
      new ToAPI[Processed[order.Order.Deposit], ApiOrder, RegisterDeposit] {

        def toAPI(o: Processed[order.Order.Deposit])(implicit e: ErgoAddressEncoder): Option[ApiOrder] =
          Optional[order.Order, DepositParams].getOption(o.order).map { params =>
            Deposit(
              o.order.id,
              o.order.poolId,
              history.OrderStatus.Mempool,
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
              history.OrderStatus.Mempool,
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
              history.OrderStatus.Mempool,
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

  trait DepositInstances {

    implicit val toApiDepositDB: ToAPI[DepositDB, Deposit, Any] = new ToAPI[DepositDB, Deposit, Any] {

      def toAPI(d: DepositDB)(implicit e: ErgoAddressEncoder): Option[Deposit] =
        Deposit(
          d.orderId,
          d.poolId,
          history.OrderStatus.Ledger,
          d.inputX,
          d.inputY,
          d.actualX,
          d.actualY,
          d.outputLp,
          d.fee,
          d.address,
          d.registerTx,
          d.refundTx,
          d.evaluateTx
        ).some

      def toAPI(a: DepositDB, c: Any)(implicit e: ErgoAddressEncoder): Option[Deposit] = none
    }

    implicit val toApiDepositDBAny: ToAPI[AnyOrderDB, Deposit, Any] = new ToAPI[AnyOrderDB, Deposit, Any] {

      def toAPI(d: AnyOrderDB)(implicit e: ErgoAddressEncoder): Option[Deposit] =
        for {
          poolId <- d.poolId
          inX    <- d.depositX
          inY    <- d.depositY
          fee    <- d.fee
          address = d.redeemer.flatMap(r => formAddress(Redeemer.PublicKeyRedeemer(r)))
        } yield Deposit(
          d.orderId,
          poolId,
          history.OrderStatus.Ledger,
          inX,
          inY,
          d.depositActualX,
          d.depositActualY,
          d.depositLp,
          fee,
          address,
          d.registerTx,
          d.refundedTx,
          d.executedTx
        )

      def toAPI(a: AnyOrderDB, c: Any)(implicit e: ErgoAddressEncoder): Option[Deposit] = none
    }
  }

  @derive(encoder, decoder, loggable)
  final case class Redeem(
    id: OrderId,
    poolId: PoolId,
    status: history.OrderStatus,
    lp: AssetAmount,
    outX: Option[AssetAmount],
    outY: Option[AssetAmount],
    fee: Fee,
    address: Option[Address],
    registerTx: TxData,
    refundTx: Option[TxData],
    evaluateTx: Option[TxData]
  ) extends ApiOrder

  object Redeem extends RedeemInstances {

    implicit val toApiRedeem: ToAPI[Processed[order.Order.Redeem], ApiOrder, RegisterRedeem] =
      new ToAPI[Processed[order.Order.Redeem], ApiOrder, RegisterRedeem] {

        def toAPI(o: Processed[order.Order.Redeem])(implicit e: ErgoAddressEncoder): Option[ApiOrder] =
          Optional[order.Order, RedeemParams].getOption(o.order).map { params =>
            Redeem(
              o.order.id,
              o.order.poolId,
              history.OrderStatus.Mempool,
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
              history.OrderStatus.Mempool,
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
              history.OrderStatus.Mempool,
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

  trait RedeemInstances {

    implicit val toApiRedeemDB: ToAPI[RedeemDB, Redeem, Any] = new ToAPI[RedeemDB, Redeem, Any] {

      def toAPI(d: RedeemDB)(implicit e: ErgoAddressEncoder): Option[Redeem] =
        Redeem(
          d.id,
          d.poolId,
          history.OrderStatus.Ledger,
          d.lp,
          d.outX,
          d.outY,
          d.fee,
          d.address,
          d.registerTx,
          d.refundTx,
          d.evaluateTx
        ).some

      def toAPI(a: RedeemDB, c: Any)(implicit e: ErgoAddressEncoder): Option[Redeem] = none
    }

    implicit val toApiRedeemDBAny: ToAPI[AnyOrderDB, Redeem, Any] = new ToAPI[AnyOrderDB, Redeem, Any] {

      def toAPI(d: AnyOrderDB)(implicit e: ErgoAddressEncoder): Option[Redeem] =
        for {
          poolId <- d.poolId
          lp     <- d.redeemLp
          fee    <- d.fee
          address = d.redeemer.flatMap(r => formAddress(Redeemer.PublicKeyRedeemer(r)))
        } yield Redeem(
          d.orderId,
          poolId,
          history.OrderStatus.Ledger,
          lp,
          d.redeemX,
          d.redeemY,
          fee,
          address,
          d.registerTx,
          d.refundedTx,
          d.executedTx
        )

      def toAPI(a: AnyOrderDB, c: Any)(implicit e: ErgoAddressEncoder): Option[Redeem] = none
    }
  }

  @derive(encoder, decoder, loggable)
  final case class Swap(
    id: OrderId,
    poolId: PoolId,
    status: history.OrderStatus,
    base: AssetAmount,
    minQuote: AssetAmount,
    quote: Option[Long],
    fee: Option[Fee],
    address: Option[Address],
    registerTx: TxData,
    refundTx: Option[TxData],
    evaluateTx: Option[TxData]
  ) extends ApiOrder

  object Swap extends SwapInstances {

    implicit val toApiSwap: ToAPI[Processed[order.Order.Swap], ApiOrder, RegisterSwap] =
      new ToAPI[Processed[order.Order.Swap], ApiOrder, RegisterSwap] {

        def toAPI(o: Processed[order.Order.Swap])(implicit e: ErgoAddressEncoder): Option[ApiOrder] =
          Optional[order.Order, SwapParams].getOption(o.order).map { params =>
            Swap(
              o.order.id,
              o.order.poolId,
              history.OrderStatus.Mempool,
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
              history.OrderStatus.Mempool,
              c.base,
              c.minQuote,
              eval.output.amount.some,
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
              history.OrderStatus.Mempool,
              params.base,
              params.minQuote,
              eval.map(_.output.amount),
              eval.map(_.fee),
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

      def toAPI(d: SwapDB)(implicit e: ErgoAddressEncoder): Option[Swap] =
        Swap(
          d.id,
          d.poolId,
          history.OrderStatus.Ledger,
          d.base,
          d.minQuote,
          d.quote,
          d.fee,
          d.address,
          d.registerTx,
          d.refundTx,
          d.evaluateTx
        ).some

      def toAPI(a: SwapDB, c: Any)(implicit e: ErgoAddressEncoder): Option[Swap] = none
    }

    implicit val toApSwapDBAny: ToAPI[AnyOrderDB, Swap, Any] = new ToAPI[AnyOrderDB, Swap, Any] {

      def toAPI(d: AnyOrderDB)(implicit e: ErgoAddressEncoder): Option[Swap] =
        for {
          poolId   <- d.poolId
          base     <- d.swapBase
          minQuote <- d.swapMinQuote
          address = d.redeemer.flatMap(r => formAddress(Redeemer.PublicKeyRedeemer(r)))
        } yield Swap(
          d.orderId,
          poolId,
          history.OrderStatus.Ledger,
          base,
          minQuote,
          d.swapQuote,
          d.fee,
          address,
          d.registerTx,
          d.refundedTx,
          d.executedTx
        )

      def toAPI(a: AnyOrderDB, c: Any)(implicit e: ErgoAddressEncoder): Option[Swap] = none
    }
  }

  @derive(encoder, decoder, loggable)
  final case class Lock(
    id: OrderId,
    status: history.OrderStatus,
    registerTx: TxData,
    deadline: Int,
    asset: AssetAmount,
    address: Option[Address],
    evalTxId: Option[TxId],
    evalType: Option[String]
  ) extends ApiOrder

  object Lock extends LockInstances {

    implicit val toApiLock: ToAPI[Processed[order.Order.Lock], ApiOrder, Any] =
      new ToAPI[Processed[order.Order.Lock], ApiOrder, Any] {

        def toAPI(o: Processed[order.Order.Lock])(implicit e: ErgoAddressEncoder): Option[ApiOrder] =
          Prism[order.Order.Lock, LockV1].getOption(o.order).map { lock =>
            Lock(
              o.order.id,
              history.OrderStatus.Mempool,
              TxData(o.state.txId, o.state.timestamp),
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

  trait LockInstances {

    implicit val toApiLockDB: ToAPI[LockDB, Lock, Any] = new ToAPI[LockDB, Lock, Any] {

      def toAPI(d: LockDB)(implicit e: ErgoAddressEncoder): Option[Lock] =
        Lock(
          d.id,
          history.OrderStatus.Ledger,
          d.registerTx,
          d.deadline,
          d.asset,
          d.address,
          d.evalTxId,
          d.evalType
        ).some

      def toAPI(a: LockDB, c: Any)(implicit e: ErgoAddressEncoder): Option[Lock] = none
    }

    implicit val toApiLockDBAny: ToAPI[AnyOrderDB, Lock, Any] = new ToAPI[AnyOrderDB, Lock, Any] {

      def toAPI(d: AnyOrderDB)(implicit e: ErgoAddressEncoder): Option[Lock] =
        for {
          deadline <- d.lockDeadline
          asset    <- d.lockAsset
          address = d.redeemer.flatMap(r => formAddress(Redeemer.PublicKeyRedeemer(r)))
        } yield Lock(
          d.orderId,
          history.OrderStatus.Ledger,
          d.registerTx,
          deadline,
          asset,
          address,
          d.lockEvalTxId,
          d.lockEvalType
        )

      def toAPI(a: AnyOrderDB, c: Any)(implicit e: ErgoAddressEncoder): Option[Lock] = none
    }
  }

  private def mkTxData(state: OrderState, status: order.OrderStatus): Option[TxData] =
    if (mapToMempool(state.status).in(status)) TxData(state.txId, state.timestamp).some else none
}
