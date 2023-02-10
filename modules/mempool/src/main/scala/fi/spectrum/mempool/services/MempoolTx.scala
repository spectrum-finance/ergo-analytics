package fi.spectrum.mempool.services

import cats.Monad
import cats.effect.MonadCancel
import derevo.derive
import fi.spectrum.common.process.OrderProcess._
import fi.spectrum.core.storage.OrdersStorage
import fi.spectrum.graphite.Metrics
import fi.spectrum.parser.PoolParser
import fi.spectrum.parser.evaluation.ProcessedOrderParser
import fi.spectrum.streaming.domain.ChainSyncEvent.{ApplyChainSync, UnapplyChainSync}
import fi.spectrum.streaming.domain.{ChainSyncEvent, MempoolEvent}
import fi.spectrum.streaming.domain.MempoolEvent.{MempoolApply, MempoolUnapply}
import tofu.higherKind.Mid
import tofu.higherKind.derived.representableK
import tofu.logging.Logging
import tofu.syntax.foption._
import tofu.syntax.logging._
import tofu.syntax.monadic._
import tofu.syntax.time.now.millis
import tofu.time.Clock
import cats.syntax.traverse._

@derive(representableK)
trait MempoolTx[F[_]] {
  def process(event: MempoolEvent): F[Unit]

  def process(event: ChainSyncEvent): F[Unit]
}

object MempoolTx {

  def make[F[_]: MonadCancel[*[_], Throwable]: Clock](implicit
    orderParser: ProcessedOrderParser[F],
    poolsParser: PoolParser,
    storage: OrdersStorage[F],
    mempool: Mempool[F],
    metrics: Metrics[F],
    logs: Logging.Make[F]
  ): MempoolTx[F] =
    logs.forService[MempoolTx[F]].map(implicit __ => new Tracing[F] attach (new MetricsMid[F] attach new Live[F]))

  final private class Live[F[_]: MonadCancel[*[_], Throwable]: Logging: Clock](implicit
    orderParser: ProcessedOrderParser[F],
    poolsParser: PoolParser,
    storage: OrdersStorage[F],
    mempool: Mempool[F]
  ) extends MempoolTx[F] {

    def process(event: MempoolEvent): F[Unit] =
      for {
        now <- millis
        order <- processOrder(
                   event.transaction,
                   now,
                   0,
                   ids =>
                     mempool.getOrder(ids).flatMap {
                       case xs if xs.nonEmpty => xs.pure
                       case _                 => storage.getOrders(ids)
                     },
                   ids => mempool.getPool(ids).orElseF(storage.getPool(ids)),
                   info"Empty pool&order in mempool tx ${event.transaction.id}"
                 )
        pool <- processPool(
                  event.transaction,
                  now,
                  0,
                  pool => info"Found pool ${pool.poolId} in mempool: ${event.transaction.id} in box: ${pool.box.boxId}"
                )
        _ <-
          event match {
            case MempoolApply(_)   => mempool.put(pool, order)
            case MempoolUnapply(_) => mempool.del(pool, order)
          }
      } yield ()

    def process(event: ChainSyncEvent): F[Unit] =
      event match {
        case ApplyChainSync(order, pool) =>
          storage.insertPoolAndOrder(pool, List(order).flatten) >>
            mempool.del(pool, List(order).flatten)
        case UnapplyChainSync(order, pool) =>
          storage.deletePoolAndOrder(pool, List(order).flatten)
      }

  }

  final private class MetricsMid[F[_]: Monad](implicit metrics: Metrics[F]) extends MempoolTx[Mid[F, *]] {

    def process(event: MempoolEvent): Mid[F, Unit] =
      (event match {
        case MempoolApply(_)   => metrics.sendCount("mempool.apply", 1.0)
        case MempoolUnapply(_) => metrics.sendCount("mempool.unapply", 1.0)
      }) *> _

    def process(event: ChainSyncEvent): Mid[F, Unit] =
      (event match {
        case _: ApplyChainSync   => metrics.sendCount("mempool.chain.sync.apply", 1.0)
        case _: UnapplyChainSync => metrics.sendCount("mempool.chain.sync.unapply", 1.0)
      }) *> _
  }

  final private class Tracing[F[_]: Monad: Logging] extends MempoolTx[Mid[F, *]] {

    def process(event: MempoolEvent): Mid[F, Unit] =
      for {
        _ <- trace"process(${event.transaction.id})"
        r <- _
        _ <- trace"process(${event.transaction.id}) -> Success"
      } yield r

    def process(event: ChainSyncEvent): Mid[F, Unit] =
      for {
        _ <- trace"cs: process($event)"
        r <- _
        _ <- trace"cs: process($event) -> Success"
      } yield r
  }
}
