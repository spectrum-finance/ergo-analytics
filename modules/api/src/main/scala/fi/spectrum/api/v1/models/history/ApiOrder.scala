package fi.spectrum.api.v1.models.history

import cats.syntax.option._
import cats.syntax.show._
import derevo.circe.{decoder, encoder}
import derevo.derive
import fi.spectrum.api.classes.ToAPI
import fi.spectrum.api.db.models.OrderDB._
import fi.spectrum.api.db.models._
import fi.spectrum.api.v1.models.history
import fi.spectrum.core.domain._
import fi.spectrum.core.domain.address._
import fi.spectrum.core.domain.analytics.OrderEvaluation._
import fi.spectrum.core.domain.analytics.Processed
import fi.spectrum.core.domain.order.Order.Compound
import fi.spectrum.core.domain.order.Order.Compound.CompoundV1
import fi.spectrum.core.domain.order.Order.Deposit.{AmmDeposit, LmDeposit}
import fi.spectrum.core.domain.order.Order.Lock.LockV1
import fi.spectrum.core.domain.order.Order.Redeem.LmRedeem.LmRedeemV1
import fi.spectrum.core.domain.order.Order.Redeem.{AmmRedeem, LmRedeem}
import fi.spectrum.core.domain.order.OrderOptics._
import fi.spectrum.core.domain.order.OrderStatus._
import fi.spectrum.core.domain.order.Redeemer.PublicKeyRedeemer
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
      a.wined[AmmDeposit]
        .flatMap(AmmDepositApi.toApiDeposit.toAPI(_))
        .orElse(a.wined[AmmRedeem].flatMap(AmmRedeemApi.toApiRedeem.toAPI(_)))
        .orElse(a.wined[order.Order.Swap].flatMap(Swap.toApiSwap.toAPI(_)))
        .orElse(a.wined[order.Order.Lock].flatMap(Lock.toApiLock.toAPI(_)))
        .orElse(a.wined[LmDeposit].flatMap(LmDepositApi.toApiLmDeposit.toAPI(_)))
        .orElse(a.wined[order.Order.Compound].flatMap(LmCompoundApi.toApiCompound.toAPI(_)))
        .orElse(a.wined[LmRedeem].flatMap(LmRedeemApi.toApiLmRedeem.toAPI(_)))

    def toAPI(a: Processed.Any, c: Any)(implicit e: ErgoAddressEncoder): Option[ApiOrder] = none
  }

  implicit val toApi2Any: ToAPI[Processed.Any, ApiOrder, Processed.Any] =
    new ToAPI[Processed.Any, ApiOrder, Processed.Any] {

      def toAPI(a: Processed.Any)(implicit e: ErgoAddressEncoder): Option[ApiOrder] = none

      def toAPI(a: Processed.Any, c: Processed.Any)(implicit e: ErgoAddressEncoder): Option[ApiOrder] =
        a.wined[AmmDeposit]
          .flatMap(AmmDepositApi.toApiDeposit2.toAPI(_, c))
          .orElse(a.wined[AmmRedeem].flatMap(AmmRedeemApi.toApiRedeem2.toAPI(_, c)))
          .orElse(a.wined[order.Order.Swap].flatMap(Swap.toApiSwap2.toAPI(_, c)))
          .orElse(a.wined[LmDeposit].flatMap(LmDepositApi.toApiLmDeposit2.toAPI(_, c)))
          .orElse(a.wined[order.Order.Compound].flatMap(LmCompoundApi.toApiCompound2.toAPI(_, c)))
          .orElse(a.wined[LmRedeem].flatMap(LmRedeemApi.toApiLmRedeem2.toAPI(_, c)))
    }

  implicit val toApiAnyOrder: ToAPI[AnyOrderDB, ApiOrder, Any] = new ToAPI[AnyOrderDB, ApiOrder, Any] {

    def toAPI(a: AnyOrderDB)(implicit e: ErgoAddressEncoder): Option[ApiOrder] =
      AmmDepositApi.toApiDepositDBAny
        .toAPI(a)
        .orElse(AmmRedeemApi.toApiRedeemDBAny.toAPI(a))
        .orElse(Swap.toApSwapDBAny.toAPI(a))
        .orElse(Lock.toApiLockDBAny.toAPI(a))
        .orElse(LmDepositApi.toApiLmDepositDBAny.toAPI(a))
        .orElse(LmCompoundApi.toApiLmCompoundDBAny.toAPI(a))
        .orElse(LmRedeemApi.toApiLmRedeemDBAny.toAPI(a))

    def toAPI(a: AnyOrderDB, c: Any)(implicit e: ErgoAddressEncoder): Option[ApiOrder] = none
  }

  @derive(encoder, decoder, loggable)
  final case class AmmDepositApi(
    id: OrderId,
    poolId: PoolId,
    status: history.OrderStatus,
    inputX: AssetAmount,
    inputY: AssetAmount,
    actualX: Option[Long],
    actualY: Option[Long],
    outputLp: Option[AssetAmount],
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

        def toAPI(o: Processed[AmmDeposit])(implicit e: ErgoAddressEncoder): Option[ApiOrder] =
          Optional[order.Order, AmmDepositParams].getOption(o.order).map { params =>
            AmmDepositApi(
              o.order.id,
              o.order.poolId,
              history.OrderStatus.Mempool,
              params.inX,
              params.inY,
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

        def toAPI(o: Processed[AmmDeposit], c: RegisterDeposit)(implicit
          e: ErgoAddressEncoder
        ): Option[ApiOrder] =
          o.evaluation.flatMap(_.widen[AmmDepositEvaluation]).map { eval =>
            AmmDepositApi(
              o.order.id,
              o.order.poolId,
              history.OrderStatus.Mempool,
              c.inX,
              c.inY,
              eval.actualX.some,
              eval.actualY.some,
              eval.outputLP.some,
              o.order.fee.show,
              o.order.fee.amount,
              address.formAddress(o.order.redeemer),
              TxData(c.info.id, c.info.timestamp),
              if (o.state.status.in(WaitingEvaluation)) TxData(o.state.txId, o.state.timestamp).some else none,
              if (o.state.status.in(WaitingRefund)) TxData(o.state.txId, o.state.timestamp).some else none
            )
          }
      }

    implicit val toApiDeposit2: ToAPI[Processed[AmmDeposit], ApiOrder, Processed.Any] =
      new ToAPI[Processed[AmmDeposit], ApiOrder, Processed.Any] {

        def toAPI(a: Processed[AmmDeposit])(implicit e: ErgoAddressEncoder): Option[ApiOrder] =
          toApiDeposit.toAPI(a)

        def toAPI(x: Processed[AmmDeposit], c: Processed.Any)(implicit
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
              history.OrderStatus.Mempool,
              params.inX,
              params.inY,
              eval.map(_.actualX),
              eval.map(_.actualY),
              eval.map(_.outputLP),
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

      def toAPI(d: AmmDepositDB)(implicit e: ErgoAddressEncoder): Option[AmmDepositApi] =
        d.address.flatMap(r => formAddress(PublicKeyRedeemer(r))).map { a =>
          AmmDepositApi(
            d.orderId,
            d.poolId,
            history.OrderStatus.Ledger,
            d.inputX,
            d.inputY,
            d.actualX,
            d.actualY,
            d.outputLp,
            d.fee.show,
            d.fee.amount,
            a.some,
            d.registerTx,
            d.refundTx,
            d.evaluateTx
          )
        }

      def toAPI(a: AmmDepositDB, c: Any)(implicit e: ErgoAddressEncoder): Option[AmmDepositApi] = none
    }

    implicit val toApiDepositDBAny: ToAPI[AnyOrderDB, AmmDepositApi, Any] = new ToAPI[AnyOrderDB, AmmDepositApi, Any] {

      def toAPI(d: AnyOrderDB)(implicit e: ErgoAddressEncoder): Option[AmmDepositApi] =
        for {
          poolId <- d.poolId
          inX    <- d.depositX
          inY    <- d.depositY
          fee    <- d.fee
          address = d.redeemer.flatMap(r => formAddress(Redeemer.PublicKeyRedeemer(r)))
        } yield AmmDepositApi(
          d.orderId,
          poolId,
          history.OrderStatus.Ledger,
          inX,
          inY,
          d.depositActualX,
          d.depositActualY,
          d.depositLp,
          fee.show,
          fee.amount,
          address,
          d.registerTx,
          d.refundedTx,
          d.executedTx
        )

      def toAPI(a: AnyOrderDB, c: Any)(implicit e: ErgoAddressEncoder): Option[AmmDepositApi] = none
    }
  }

  @derive(encoder, decoder, loggable)
  final case class AmmRedeemApi(
    id: OrderId,
    poolId: PoolId,
    status: history.OrderStatus,
    lp: AssetAmount,
    outX: Option[AssetAmount],
    outY: Option[AssetAmount],
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

        def toAPI(o: Processed[AmmRedeem])(implicit e: ErgoAddressEncoder): Option[ApiOrder] =
          Optional[order.Order, RedeemParams].getOption(o.order).map { params =>
            AmmRedeemApi(
              o.order.id,
              o.order.poolId,
              history.OrderStatus.Mempool,
              params.lp,
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

        def toAPI(o: Processed[AmmRedeem], c: RegisterRedeem)(implicit
          e: ErgoAddressEncoder
        ): Option[ApiOrder] =
          o.evaluation.flatMap(_.widen[AmmRedeemEvaluation]).map { eval =>
            AmmRedeemApi(
              o.order.id,
              o.order.poolId,
              history.OrderStatus.Mempool,
              c.lp,
              eval.outputX.some,
              eval.outputY.some,
              o.order.fee.show,
              o.order.fee.amount,
              address.formAddress(o.order.redeemer),
              TxData(c.info.id, c.info.timestamp),
              if (o.state.status.in(WaitingEvaluation)) TxData(o.state.txId, o.state.timestamp).some else none,
              if (o.state.status.in(WaitingRefund)) TxData(o.state.txId, o.state.timestamp).some else none
            )
          }
      }

    implicit val toApiRedeem2: ToAPI[Processed[AmmRedeem], ApiOrder, Processed.Any] =
      new ToAPI[Processed[AmmRedeem], ApiOrder, Processed.Any] {

        def toAPI(a: Processed[AmmRedeem])(implicit e: ErgoAddressEncoder): Option[ApiOrder] =
          toApiRedeem.toAPI(a)

        def toAPI(x: Processed[AmmRedeem], c: Processed.Any)(implicit
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
              history.OrderStatus.Mempool,
              params.lp,
              eval.map(_.outputX),
              eval.map(_.outputY),
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

      def toAPI(d: AmmRedeemDB)(implicit e: ErgoAddressEncoder): Option[AmmRedeemApi] =
        d.address.flatMap(r => formAddress(PublicKeyRedeemer(r))).map { a =>
          AmmRedeemApi(
            d.id,
            d.poolId,
            history.OrderStatus.Ledger,
            d.lp,
            d.outX,
            d.outY,
            d.fee.show,
            d.fee.amount,
            a.some,
            d.registerTx,
            d.refundTx,
            d.evaluateTx
          )
        }

      def toAPI(a: AmmRedeemDB, c: Any)(implicit e: ErgoAddressEncoder): Option[AmmRedeemApi] = none
    }

    implicit val toApiRedeemDBAny: ToAPI[AnyOrderDB, AmmRedeemApi, Any] = new ToAPI[AnyOrderDB, AmmRedeemApi, Any] {

      def toAPI(d: AnyOrderDB)(implicit e: ErgoAddressEncoder): Option[AmmRedeemApi] =
        for {
          poolId <- d.poolId
          lp     <- d.redeemLp
          fee    <- d.fee
          address = d.redeemer.flatMap(r => formAddress(Redeemer.PublicKeyRedeemer(r)))
        } yield AmmRedeemApi(
          d.orderId,
          poolId,
          history.OrderStatus.Ledger,
          lp,
          d.redeemX,
          d.redeemY,
          fee.show,
          fee.amount,
          address,
          d.registerTx,
          d.refundedTx,
          d.executedTx
        )

      def toAPI(a: AnyOrderDB, c: Any)(implicit e: ErgoAddressEncoder): Option[AmmRedeemApi] = none
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
              eval.fee.show.some,
              eval.fee.amount.some,
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

      def toAPI(d: SwapDB)(implicit e: ErgoAddressEncoder): Option[Swap] =
        d.address.flatMap(r => formAddress(PublicKeyRedeemer(r))).map { a =>
          Swap(
            d.id,
            d.poolId,
            history.OrderStatus.Ledger,
            d.base,
            d.minQuote,
            d.quote,
            d.fee.map(_.show),
            d.fee.map(_.amount),
            a.some,
            d.registerTx,
            d.refundTx,
            d.evaluateTx
          )
        }

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
          d.fee.map(_.show),
          d.fee.map(_.amount),
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
        d.address.flatMap(r => formAddress(PublicKeyRedeemer(r))).map { a =>
          Lock(
            d.id,
            history.OrderStatus.Ledger,
            d.registerTx,
            d.deadline,
            d.asset,
            a.some,
            d.evalTxId,
            d.evalType
          )
        }

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

  @derive(encoder, decoder, loggable)
  final case class LmDepositApi(
    id: OrderId,
    status: history.OrderStatus,
    poolId: PoolId,
    expectedNumEpochs: Int,
    input: AssetAmount,
    out: Option[AssetAmount],
    compoundId: Option[TokenId],
    registerTx: TxData,
    refundTx: Option[TxData],
    evaluateTx: Option[TxData]
  ) extends ApiOrder

  object LmDepositApi extends LmDepositApiInstances {

    implicit val toApiLmDeposit: ToAPI[Processed[LmDeposit], ApiOrder, RegisterLmDeposit] =
      new ToAPI[Processed[LmDeposit], ApiOrder, RegisterLmDeposit] {

        def toAPI(o: Processed[LmDeposit])(implicit e: ErgoAddressEncoder): Option[ApiOrder] =
          Optional[order.Order, LmDepositParams].getOption(o.order).map { params =>
            LmDepositApi(
              o.order.id,
              history.OrderStatus.Mempool,
              o.order.poolId,
              params.expectedNumEpochs,
              params.tokens,
              none,
              none,
              TxData(o.state.txId, o.state.timestamp),
              none,
              none
            )
          }

        def toAPI(o: Processed[LmDeposit], c: RegisterLmDeposit)(implicit
          e: ErgoAddressEncoder
        ): Option[ApiOrder] =
          o.evaluation.flatMap(_.widen[LmDepositCompoundEvaluation]).map { eval =>
            LmDepositApi(
              o.order.id,
              history.OrderStatus.Mempool,
              o.order.poolId,
              c.epochs,
              c.in,
              eval.tokens.some,
              TokenId.unsafeFromString(eval.bundle.id.value).some,
              TxData(c.info.id, c.info.timestamp),
              if (o.state.status.in(WaitingRefund)) TxData(o.state.txId, o.state.timestamp).some else none,
              if (o.state.status.in(WaitingEvaluation)) TxData(o.state.txId, o.state.timestamp).some else none
            )
          }
      }

    implicit val toApiLmDeposit2: ToAPI[Processed[LmDeposit], ApiOrder, Processed.Any] =
      new ToAPI[Processed[LmDeposit], ApiOrder, Processed.Any] {

        def toAPI(a: Processed[LmDeposit])(implicit e: ErgoAddressEncoder): Option[ApiOrder] =
          toApiLmDeposit.toAPI(a)

        def toAPI(x: Processed[LmDeposit], c: Processed.Any)(implicit
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
              history.OrderStatus.Mempool,
              x.order.poolId,
              params.expectedNumEpochs,
              params.tokens,
              eval.map(_.tokens),
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

      def toAPI(d: LmDepositDB)(implicit e: ErgoAddressEncoder): Option[LmDepositApi] =
        LmDepositApi(
          d.orderId,
          history.OrderStatus.Ledger,
          d.poolId,
          d.expectedNumEpochs,
          d.input,
          d.lp,
          d.compoundId,
          d.registerTx,
          d.refundTx,
          d.evaluateTx
        ).some

      def toAPI(a: LmDepositDB, c: Any)(implicit e: ErgoAddressEncoder): Option[LmDepositApi] = none
    }

    implicit val toApiLmDepositDBAny: ToAPI[AnyOrderDB, LmDepositApi, Any] = new ToAPI[AnyOrderDB, LmDepositApi, Any] {

      def toAPI(d: AnyOrderDB)(implicit e: ErgoAddressEncoder): Option[LmDepositApi] =
        for {
          expectedNumEpochs <- d.lmDepositExpectedNumEpochs
          input             <- d.lmDepositInput
          poolId            <- d.poolId
        } yield LmDepositApi(
          d.orderId,
          history.OrderStatus.Ledger,
          poolId,
          expectedNumEpochs,
          input,
          d.lmDepositLp,
          d.lmDepositCompoundId,
          d.registerTx,
          d.refundedTx,
          d.executedTx
        )

      def toAPI(a: AnyOrderDB, c: Any)(implicit e: ErgoAddressEncoder): Option[LmDepositApi] = none
    }
  }

  @derive(encoder, decoder, loggable)
  final case class LmCompoundApi(
    id: OrderId,
    status: history.OrderStatus,
    poolId: PoolId,
    vLq: AssetAmount,
    tmp: Option[AssetAmount],
    compoundId: TokenId,
    registerTx: TxData,
    evaluateTx: Option[TxData]
  ) extends ApiOrder

  object LmCompoundApi extends LmCompoundApiInstances {

    implicit val toApiCompound: ToAPI[Processed[Compound], ApiOrder, RegisterCompound] =
      new ToAPI[Processed[Compound], ApiOrder, RegisterCompound] {

        def toAPI(o: Processed[Compound])(implicit e: ErgoAddressEncoder): Option[ApiOrder] =
          Prism[Compound, CompoundV1].getOption(o.order).map { o1 =>
            LmCompoundApi(
              o.order.id,
              history.OrderStatus.Mempool,
              o.order.poolId,
              o1.params.vLq,
              o1.params.tmp,
              o1.params.bundleKeyId,
              TxData(o.state.txId, o.state.timestamp),
              none
            )
          }

        def toAPI(o: Processed[Compound], c: RegisterCompound)(implicit
          e: ErgoAddressEncoder
        ): Option[ApiOrder] =
          Prism[Compound, CompoundV1].getOption(o.order).map { o1 =>
            LmCompoundApi(
              o.order.id,
              history.OrderStatus.Mempool,
              o.order.poolId,
              o1.params.vLq,
              o1.params.tmp,
              o1.params.bundleKeyId,
              TxData(c.info.id, c.info.timestamp),
              if (o.state.status.in(WaitingEvaluation)) TxData(o.state.txId, o.state.timestamp).some else none
            )
          }
      }

    implicit val toApiCompound2: ToAPI[Processed[Compound], ApiOrder, Processed.Any] =
      new ToAPI[Processed[Compound], ApiOrder, Processed.Any] {

        def toAPI(a: Processed[Compound])(implicit e: ErgoAddressEncoder): Option[ApiOrder] =
          toApiCompound.toAPI(a)

        def toAPI(x: Processed[Compound], c: Processed.Any)(implicit
          e: ErgoAddressEncoder
        ): Option[ApiOrder] =
          for {
            x1 <- Prism[Compound, CompoundV1].getOption(x.order)
            y  <- c.wined[Compound]
          } yield LmCompoundApi(
            x.order.id,
            history.OrderStatus.Mempool,
            x.order.poolId,
            x1.params.vLq,
            x1.params.tmp,
            x1.params.bundleKeyId,
            mkTxData(x.state, WaitingRegistration).getOrElse(TxData(y.state.txId, y.state.timestamp)),
            mkTxData(x.state, WaitingEvaluation).orElse(mkTxData(y.state, WaitingEvaluation))
          )
      }
  }

  trait LmCompoundApiInstances {

    implicit val toApiCompoundDB: ToAPI[LmCompoundDB, LmCompoundApi, Any] =
      new ToAPI[LmCompoundDB, LmCompoundApi, Any] {

        def toAPI(d: LmCompoundDB)(implicit e: ErgoAddressEncoder): Option[LmCompoundApi] =
          LmCompoundApi(
            d.orderId,
            history.OrderStatus.Ledger,
            d.poolId,
            d.vLq,
            d.tmp,
            d.bundleKeyId,
            d.registerTx,
            d.evaluateTx
          ).some

        def toAPI(a: LmCompoundDB, c: Any)(implicit e: ErgoAddressEncoder): Option[LmCompoundApi] = none
      }

    implicit val toApiLmCompoundDBAny: ToAPI[AnyOrderDB, LmCompoundApi, Any] =
      new ToAPI[AnyOrderDB, LmCompoundApi, Any] {

        def toAPI(d: AnyOrderDB)(implicit e: ErgoAddressEncoder): Option[LmCompoundApi] =
          for {
            vLq    <- d.lmCompoundVLq
            key    <- d.lmCompoundBundleKeyId
            poolId <- d.poolId
            tmp = d.lmCompoundTmp
          } yield LmCompoundApi(
            d.orderId,
            history.OrderStatus.Ledger,
            poolId,
            vLq,
            tmp,
            key,
            d.registerTx,
            d.executedTx
          )

        def toAPI(a: AnyOrderDB, c: Any)(implicit e: ErgoAddressEncoder): Option[LmCompoundApi] = none
      }
  }

  @derive(encoder, decoder, loggable)
  final case class LmRedeemApi(
    id: OrderId,
    status: history.OrderStatus,
    poolId: Option[PoolId],
    bundleKeyId: TokenId,
    expectedLq: AssetAmount,
    out: Option[AssetAmount],
    boxId: Option[TokenId],
    registerTx: TxData,
    refundTx: Option[TxData],
    evaluateTx: Option[TxData]
  ) extends ApiOrder

  object LmRedeemApi extends LmRedeemApiInstances {

    implicit val toApiLmRedeem: ToAPI[Processed[LmRedeem], ApiOrder, RegisterLmRedeem] =
      new ToAPI[Processed[LmRedeem], ApiOrder, RegisterLmRedeem] {

        def toAPI(o: Processed[LmRedeem])(implicit e: ErgoAddressEncoder): Option[ApiOrder] =
          Prism[LmRedeem, LmRedeemV1].getOption(o.order).map { o1 =>
            LmRedeemApi(
              o.order.id,
              history.OrderStatus.Mempool,
              none,
              o1.bundleKeyId,
              o1.expectedLq,
              none,
              none,
              TxData(o.state.txId, o.state.timestamp),
              none,
              none
            )
          }

        def toAPI(o: Processed[LmRedeem], c: RegisterLmRedeem)(implicit
          e: ErgoAddressEncoder
        ): Option[ApiOrder] =
          o.evaluation.flatMap(_.widen[LmRedeemEvaluation]).map { eval =>
            LmRedeemApi(
              o.order.id,
              history.OrderStatus.Mempool,
              eval.poolId.some,
              c.bundleKeyId,
              c.expectedLq,
              eval.out.some,
              eval.boxId.some.map(r => TokenId.unsafeFromString(r.value)),
              TxData(c.info.id, c.info.timestamp),
              if (o.state.status.in(WaitingRefund)) TxData(o.state.txId, o.state.timestamp).some else none,
              if (o.state.status.in(WaitingEvaluation)) TxData(o.state.txId, o.state.timestamp).some else none
            )
          }
      }

    implicit val toApiLmRedeem2: ToAPI[Processed[LmRedeem], ApiOrder, Processed.Any] =
      new ToAPI[Processed[LmRedeem], ApiOrder, Processed.Any] {

        def toAPI(a: Processed[LmRedeem])(implicit e: ErgoAddressEncoder): Option[ApiOrder] =
          toApiLmRedeem.toAPI(a)

        def toAPI(x: Processed[LmRedeem], c: Processed.Any)(implicit
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
              history.OrderStatus.Mempool,
              eval.map(_.poolId),
              x1.bundleKeyId,
              x1.expectedLq,
              eval.map(_.out),
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

        def toAPI(d: LmRedeemsDB)(implicit e: ErgoAddressEncoder): Option[LmRedeemApi] =
          LmRedeemApi(
            d.orderId,
            history.OrderStatus.Ledger,
            d.poolId,
            d.bundleKeyId,
            d.expectedLq,
            d.out,
            d.boxId,
            d.registerTx,
            d.refundTx,
            d.evaluateTx
          ).some

        def toAPI(a: LmRedeemsDB, c: Any)(implicit e: ErgoAddressEncoder): Option[LmRedeemApi] = none
      }

    implicit val toApiLmRedeemDBAny: ToAPI[AnyOrderDB, LmRedeemApi, Any] =
      new ToAPI[AnyOrderDB, LmRedeemApi, Any] {

        def toAPI(d: AnyOrderDB)(implicit e: ErgoAddressEncoder): Option[LmRedeemApi] =
          for {
            lmRedeemExpectedLq  <- d.lmRedeemExpectedLq
            lmRedeemBundleKeyId <- d.lmRedeemBundleKeyId
            poolId = d.poolId
          } yield LmRedeemApi(
            d.orderId,
            history.OrderStatus.Ledger,
            poolId,
            lmRedeemBundleKeyId,
            lmRedeemExpectedLq,
            d.lmRedeemOut,
            d.lmRedeemBoxId,
            d.registerTx,
            d.refundedTx,
            d.executedTx
          )

        def toAPI(a: AnyOrderDB, c: Any)(implicit e: ErgoAddressEncoder): Option[LmRedeemApi] = none
      }
  }

  private def mkTxData(state: OrderState, status: order.OrderStatus): Option[TxData] =
    if (mapToMempool(state.status).in(status)) TxData(state.txId, state.timestamp).some else none
}
