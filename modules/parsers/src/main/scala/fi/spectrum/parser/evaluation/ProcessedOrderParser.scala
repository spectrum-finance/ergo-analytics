package fi.spectrum.parser.evaluation

import cats.syntax.option._
import cats.{Applicative, Monad}
import derevo.derive
import fi.spectrum.core.domain.analytics.OrderEvaluation.{LockEvaluation, SwapEvaluation}
import fi.spectrum.core.domain.analytics.Processed._
import fi.spectrum.core.domain.analytics.{Processed, Version}
import fi.spectrum.core.domain.order.Order.Lock.LockV1
import fi.spectrum.core.domain.order.{Order, OrderState, OrderStatus}
import fi.spectrum.core.domain.pool.Pool
import fi.spectrum.core.domain.transaction.Transaction
import fi.spectrum.parser.lm.compound.CompoundParser
import fi.spectrum.parser.lm.compound.v1.CompoundParserV1._
import fi.spectrum.parser.{OrderParser, PoolParser}
import org.ergoplatform.ErgoAddressEncoder
import tofu.higherKind.Mid
import tofu.higherKind.derived.representableK
import tofu.logging.Logging
import tofu.syntax.logging._
import tofu.syntax.monadic._

@derive(representableK)
sealed trait ProcessedOrderParser[F[_]] {

  def registered(tx: Transaction, timestamp: Long): F[Option[Processed[Order]]]

  def evaluated(
    tx: Transaction,
    timestamp: Long,
    order: Processed.Any,
    pool: Pool,
    height: Int
  ): F[Option[Processed[Order]]]

  def refunded(tx: Transaction, timestamp: Long, order: Processed.Any): F[Processed[Order]]

  def reLock(tx: Transaction, order: Processed[LockV1], original: Option[Processed[LockV1]]): F[Processed[Order]]

  def withdraw(tx: Transaction, timestamp: Long, order: Processed[LockV1]): F[Processed[Order]]

}

object ProcessedOrderParser {

  def make[F[_]: Monad](implicit e: ErgoAddressEncoder, logs: Logging.Make[F]): ProcessedOrderParser[F] =
    logs.forService[ProcessedOrderParser[F]].map { implicit __ =>
      implicit val parser: CompoundParser[Version] = implicitly[CompoundParser[Version]]
      new Tracing[F] attach new Live[F]
    }

  /** Parses processed order
    *
    * @param orderParser - orders parser
    * @param feeParser   - off-chain fee parser
    * @param poolParser  - pools parser
    * @param evalParser  - evaluation result parser
    */
  final private class Live[F[_]: Applicative](implicit
    orderParser: OrderParser,
    feeParser: OffChainFeeParser,
    poolParser: PoolParser,
    evalParser: OrderEvaluationParser
  ) extends ProcessedOrderParser[F] {

    def reLock(tx: Transaction, order: Processed[LockV1], original: Option[Processed[LockV1]]): F[Processed[Order]] =
      original
        .find(_.order.amount.tokenId == order.order.amount.tokenId)
        .map { in =>
          order
            .copy(
              evaluation = LockEvaluation(in.order.id).some,
              state      = order.state.copy(status = OrderStatus.ReLock)
            )
            .widenOrder
        }
        .getOrElse(order)
        .widenOrder
        .pure

    def withdraw(tx: Transaction, timestamp: Long, order: Processed[LockV1]): F[Processed[Order]] =
      order
        .copy(state = OrderState(tx.id, timestamp, OrderStatus.Withdraw))
        .widenOrder
        .pure

    def registered(tx: Transaction, timestamp: Long): F[Option[Processed[Order]]] =
      tx.outputs
        .map(orderParser.parse)
        .collectFirst { case Some(order) => order }
        .map(Processed.make(OrderState(tx.id, timestamp, OrderStatus.Registered), _))
        .pure

    def evaluated(
      tx: Transaction,
      timestamp: Long,
      order: Processed.Any,
      pool: Pool,
      height: Int
    ): F[Option[Processed[Order]]] =
      tx.outputs.toList
        .map(poolParser.parse(_, timestamp, height))
        .collectFirst { case Some(v) => v }
        .map { next =>
          val eval = evalParser.parse(order.order, tx.outputs.toList, pool, next)
          val fee  = feeParser.parse(tx.outputs.toList, order, eval, pool.poolId)
          val swapEvalWithFee = fee
            .flatMap { fee =>
              eval.flatMap(_.widen[SwapEvaluation]).map(_.copy(fee = fee.fee))
            }
            .orElse(eval)
          order
            .copy(
              state       = OrderState(tx.id, timestamp, OrderStatus.Evaluated),
              evaluation  = swapEvalWithFee,
              offChainFee = fee,
              poolBoxId   = pool.box.boxId.some
            )
        }
        .pure

    def refunded(tx: Transaction, timestamp: Long, order: Processed.Any): F[Processed[Order]] =
      order.copy(state = OrderState(tx.id, timestamp, OrderStatus.Refunded)).pure
  }

  final private class Tracing[F[_]: Monad: Logging] extends ProcessedOrderParser[Mid[F, *]] {

    def reLock(tx: Transaction, order: Processed[LockV1], input: Option[Processed[LockV1]]): Mid[F, Processed[Order]] =
      for {
        _ <- info"reLock(${tx.id}, ${order.order.id}, ${input.map(_.order.id)})"
        r <- _
        _ <- info"reLock(${tx.id}, ${order.order.id}, ${input.map(_.order.id)}) -> (${r.evaluation}, ${r.state})"
      } yield r

    def registered(tx: Transaction, timestamp: Long): Mid[F, Option[Processed[Order]]] =
      for {
        _ <- info"registered(${tx.id})"
        r <- _
        _ <- info"registered(${tx.id}) -> ${r.map(_.order.id)}"
      } yield r

    def evaluated(
      tx: Transaction,
      timestamp: Long,
      order: Any,
      pool: Pool,
      height: Int
    ): Mid[F, Option[Processed[Order]]] =
      for {
        _ <- info"evaluated(${tx.id}, ${order.order.id}, ${pool.box.boxId})"
        r <- _
        _ <- info"evaluated(${tx.id}, ${order.order.id}, ${pool.box.boxId}) -> ${r.map(_.order.id)}"
      } yield r

    def refunded(tx: Transaction, timestamp: Long, order: Any): Mid[F, Processed[Order]] =
      for {
        _ <- info"refunded(${tx.id}, ${order.order.id})"
        r <- _
        _ <- info"refunded(${tx.id}, ${order.order.id}) -> ${r.state}"
      } yield r

    def withdraw(tx: Transaction, timestamp: Long, order: Processed[LockV1]): Mid[F, Processed[Order]] =
      for {
        _ <- info"withdraw(${tx.id}, ${order.order.id})"
        r <- _
        _ <- info"withdraw(${tx.id}, ${order.order.id}) -> ${r.state}"
      } yield r
  }
}
