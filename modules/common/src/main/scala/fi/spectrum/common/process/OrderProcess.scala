package fi.spectrum.common.process

import cats.Monad
import cats.syntax.option._
import fi.spectrum.core.domain.BoxId
import fi.spectrum.core.domain.analytics.Processed
import fi.spectrum.core.domain.order.Order.Lock.LockV1
import fi.spectrum.core.domain.pool.Pool
import fi.spectrum.core.domain.transaction.Transaction
import fi.spectrum.parser.PoolParser
import fi.spectrum.parser.evaluation.ProcessedOrderParser
import tofu.syntax.foption._
import tofu.syntax.monadic._
import cats.syntax.traverse._

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
  ): F[Option[Processed.Any]] =
    orderParser
      .registered(tx, timestamp)
      .semiflatMap {
        case lock @ Processed(order: LockV1, _, _, _, _) =>
          getOrder(tx.inputs.toList).flatMap { maybeOrder =>
            orderParser.reLock(tx, lock.widen(order), maybeOrder.flatMap(_.wined[LockV1]))
          }
        case order => order.pure
      }
      .orElseF {
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

  def processPool[F[_]: Monad](tx: Transaction, timestamp: Long, height: Int, log: Pool => F[Unit])(implicit
    poolsParser: PoolParser
  ): F[Option[Pool]] =
    tx.outputs
      .map(poolsParser.parse(_, timestamp, height))
      .collectFirst { case Some(p) => p }
      .pure
      .flatTap(_.traverse(log))

}
