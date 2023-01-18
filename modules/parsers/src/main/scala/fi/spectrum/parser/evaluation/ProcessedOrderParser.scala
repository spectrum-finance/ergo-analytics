package fi.spectrum.parser.evaluation

import cats.syntax.option._
import cats.{Applicative, Monad}
import derevo.derive
import fi.spectrum.core.domain.TokenId
import fi.spectrum.core.domain.analytics.Processed
import fi.spectrum.core.domain.analytics.Processed._
import fi.spectrum.core.domain.order.{Order, OrderState, OrderStatus}
import fi.spectrum.core.domain.pool.Pool
import fi.spectrum.core.domain.transaction.Transaction
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

  def evaluated(tx: Transaction, timestamp: Long, order: Processed.Any, pool: Pool): F[Option[Processed[Order]]]

  def refunded(tx: Transaction, timestamp: Long, order: Processed.Any): F[Processed[Order]]

}

object ProcessedOrderParser {

  def make[F[_]: Monad](
    spf: TokenId
  )(implicit e: ErgoAddressEncoder, logs: Logging.Make[F]): ProcessedOrderParser[F] =
    logs.forService[ProcessedOrderParser[F]].map { implicit __ =>
      implicit val fee: OffChainFeeParser = OffChainFeeParser.make(spf)
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

    def registered(tx: Transaction, timestamp: Long): F[Option[Processed[Order]]] =
      tx.outputs
        .map(orderParser.parse)
        .collectFirst { case Some(order) => order }
        .map(Processed.make(OrderState(tx.id, timestamp, OrderStatus.Registered), _))
        .pure

    def evaluated(tx: Transaction, timestamp: Long, order: Processed.Any, pool: Pool): F[Option[Processed[Order]]] =
      tx.outputs.toList
        .map(poolParser.parse(_, timestamp))
        .collectFirst { case Some(v) => v }
        .map { _ =>
          order
            .copy(
              state       = OrderState(tx.id, timestamp, OrderStatus.Evaluated),
              evaluation  = evalParser.parse(order.order, tx.outputs.toList, pool),
              offChainFee = feeParser.parse(tx.outputs.toList, order.order, pool.poolId),
              poolBoxId   = pool.box.boxId.some
            )
        }
        .pure

    def refunded(tx: Transaction, timestamp: Long, order: Processed.Any): F[Processed[Order]] =
      order.copy(state = OrderState(tx.id, timestamp, OrderStatus.Refunded)).pure

  }

  final private class Tracing[F[_]: Monad: Logging] extends ProcessedOrderParser[Mid[F, *]] {

    def registered(tx: Transaction, timestamp: Long): Mid[F, Option[Processed[Order]]] =
      for {
        _ <- info"registered(${tx.id})"
        r <- _
        _ <- info"registered(${tx.id}) -> ${r.map(_.order.id)}"
      } yield r

    def evaluated(tx: Transaction, timestamp: Long, order: Any, pool: Pool): Mid[F, Option[Processed[Order]]] =
      for {
        _ <- info"evaluated(${tx.id})"
        r <- _
        _ <- info"evaluated(${tx.id}) -> ${r.map(_.order.id)}"
      } yield r

    def refunded(tx: Transaction, timestamp: Long, order: Any): Mid[F, Processed[Order]] =
      for {
        _ <- info"refunded(${tx.id})"
        r <- _
        _ <- info"refunded(${tx.id}) -> ${r.order.id}"
      } yield r
  }
}
