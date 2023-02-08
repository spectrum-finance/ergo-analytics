package fi.spectrum.common.process

import cats.Monad
import cats.syntax.option._
import cats.syntax.traverse._
import fi.spectrum.core.domain.BoxId
import fi.spectrum.core.domain.analytics.Processed
import fi.spectrum.core.domain.order.Order.Compound
import fi.spectrum.core.domain.order.Order.Lock.LockV1
import fi.spectrum.core.domain.order.OrderStatus.Registered
import fi.spectrum.core.domain.pool.Pool
import fi.spectrum.core.domain.transaction.Transaction
import fi.spectrum.parser.PoolParser
import fi.spectrum.parser.evaluation.ProcessedOrderParser
import tofu.syntax.foption._
import tofu.syntax.monadic._

object OrderProcess {

  def processOrder[F[_]: Monad](
    tx: Transaction,
    timestamp: Long,
    height: Int,
    getOrder: List[BoxId] => F[Option[Processed.Any]],
    getPool: List[BoxId] => F[Option[Pool]],
    logIfEmpty: F[Unit]
  )(implicit
    orderParser: ProcessedOrderParser[F]
  ): F[List[Processed.Any]] =
    orderParser
      .registered(tx, timestamp)
      .semiflatMap {
        case lock @ Processed(order: LockV1, _, _, _, _) =>
          getOrder(tx.inputs.toList).flatMap { maybeOrder =>
            orderParser.reLock(tx, lock.widen(order), maybeOrder.flatMap(_.wined[LockV1]))
          }
        case order => order.pure
      }
      .orElseF(eval(tx, getOrder, getPool, height, timestamp, logIfEmpty))
      .flatMap {
        case Some(order: Processed.Any) =>
          if (order.wined[Compound].exists(_.state.status.in(Registered))) {
            eval(tx, getOrder, getPool, height, timestamp, logIfEmpty).map { o =>
              List(o, order.some).flatten
            }
          } else List(order).pure
        case _ => List.empty[Processed.Any].pure
      }

  def processPool[F[_]: Monad](tx: Transaction, timestamp: Long, height: Int, log: Pool => F[Unit])(implicit
    poolsParser: PoolParser
  ): F[Option[Pool]] =
    tx.outputs
      .map(poolsParser.parse(_, timestamp, height))
      .collectFirst { case Some(p) => p }
      .pure
      .flatTap(_.traverse(log))

  private def eval[F[_]: Monad](
    tx: Transaction,
    getOrder: List[BoxId] => F[Option[Processed.Any]],
    getPool: List[BoxId] => F[Option[Pool]],
    height: Int,
    timestamp: Long,
    logIfEmpty: F[Unit]
  )(implicit
    orderParser: ProcessedOrderParser[F]
  ): F[Option[Processed.Any]] = {
    def getState: F[(Option[Processed.Any], Option[Pool])] = for {
      order <- getOrder(tx.inputs.toList)
      pool  <- getPool(tx.inputs.toList)
    } yield (order, pool)

    getState.flatMap {
      case (Some(order), Some(pool)) =>
        orderParser.evaluated(tx, timestamp, order, pool, height)
      case (Some(lock @ Processed(order: LockV1, _, _, _, _)), _) =>
        orderParser.withdraw(tx, timestamp, lock.widen(order)).someIn
      case (Some(order), _) =>
        orderParser.refunded(tx, timestamp, order).someIn
      case _ => //todo arbitrage bots
        logIfEmpty as none[Processed.Any]
    }
  }

}
