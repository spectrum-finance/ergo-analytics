package fi.spectrum.common.process

import cats.Monad
import cats.syntax.option._
import cats.syntax.traverse._
import fi.spectrum.core.domain.BoxId
import fi.spectrum.core.domain.analytics.Processed
import fi.spectrum.core.domain.order.Order.Compound
import fi.spectrum.core.domain.order.Order.Lock.LockV1
import fi.spectrum.core.domain.order.Order.Redeem.LmRedeem
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
    getOrders: List[BoxId] => F[List[Processed.Any]],
    getPool: List[BoxId] => F[Option[Pool]],
    logIfEmpty: F[Unit]
  )(implicit
    orderParser: ProcessedOrderParser[F]
  ): F[List[Processed.Any]] =
    orderParser
      .registered(tx, timestamp)
      .semiflatMap {
        case lock @ Processed(order: LockV1, _, _, _, _) =>
          getOrders(tx.inputs.toList).map(_.headOption).flatMap { maybeOrder =>
            orderParser.reLock(tx, lock.widen(order), maybeOrder.flatMap(_.wined[LockV1]))
          }
        case order => order.pure
      }
      .mapIn(List(_))
      .orElseF(eval(tx, getOrders, getPool, height, timestamp, logIfEmpty))
      .flatMap {
        case Some((order: Processed.Any) :: Nil) =>
          if (order.wined[Compound].exists(_.state.status.in(Registered))) {
            eval(tx, getOrders, getPool, height, timestamp, logIfEmpty).map { o =>
              List(o.getOrElse(List.empty), order.some).flatten
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
    getOrders: List[BoxId] => F[List[Processed.Any]],
    getPool: List[BoxId] => F[Option[Pool]],
    height: Int,
    timestamp: Long,
    logIfEmpty: F[Unit]
  )(implicit
    orderParser: ProcessedOrderParser[F]
  ): F[Option[List[Processed.Any]]] = {
    def getState: F[(List[Processed.Any], Option[Pool])] = for {
      orders <- getOrders(tx.inputs.toList)
      pool   <- getPool(tx.inputs.toList)
    } yield (orders, pool)

    def lmRedeem(orders: List[Processed.Any], pool: Pool): F[List[Processed.Any]] =
      orders match {
        case head :: next :: Nil =>
          val result = for {
            x <- head.wined[LmRedeem].orElse(next.wined[LmRedeem])
            y <- head.wined[Compound].orElse(next.wined[Compound])
          } yield (x, y)
          result
            .traverse { case (redeem, compound) =>
              orderParser.evaluated(tx, timestamp, redeem.widenOrder, pool, height).flatMap { r =>
                orderParser.evaluated(tx, timestamp, compound.widenOrder, pool, height).map { c =>
                  List(r, c).flatten
                }
              }
            }
            .map(_.getOrElse(List.empty))
        case _ => List.empty[Processed.Any].pure[F]
      }

    def evalOrder: F[Option[Processed.Any]] = getState.flatMap {
      case (order :: Nil, Some(pool)) =>
        orderParser.evaluated(tx, timestamp, order, pool, height)
      case ((lock @ Processed(order: LockV1, _, _, _, _)) :: Nil, _) =>
        orderParser.withdraw(tx, timestamp, lock.widen(order)).someIn
      case (order :: Nil, _) =>
        orderParser.refunded(tx, timestamp, order).someIn
      case _ => //todo arbitrage bots
        logIfEmpty as none[Processed.Any]
    }

    getState
      .flatMap {
        case (value, Some(p)) => lmRedeem(value, p)
        case _                => List.empty[Processed.Any].pure[F]
      }
      .flatMap {
        case xs if xs.nonEmpty => xs.someF
        case _                 => evalOrder.mapIn(List(_))
      }

  }

}
