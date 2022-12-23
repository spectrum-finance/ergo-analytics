package fi.spectrum.indexer.services

import cats.Monad
import cats.data.NonEmptyList
import cats.instances.all._
import cats.syntax.traverse._
import derevo.derive
import fi.spectrum.core.domain.analytics.ProcessedOrder
import fi.spectrum.indexer.db.persist.PersistBundle
import fi.spectrum.streaming.domain.OrderEvent
import tofu.doobie.transactor.Txr
import tofu.higherKind.Mid
import tofu.higherKind.derived.representableK
import tofu.logging.Logging
import tofu.syntax.doobie.txr._
import tofu.syntax.logging._
import tofu.syntax.monadic._

@derive(representableK)
trait Orders[F[_]] {
  def process(events: NonEmptyList[OrderEvent]): F[Unit]
}

object Orders {

  def make[F[_]: Monad, D[_]: Monad](implicit
    bundle: PersistBundle[D],
    mempool: OrdersMempool[F],
    txr: Txr[F, D],
    logs: Logging.Make[F]
  ): Orders[F] =
    logs.forService[Orders[F]].map(implicit __ => new Tracing[F] attach new Live[F, D])

  final private class Live[F[_]: Monad, D[_]: Monad](implicit
    bundle: PersistBundle[D],
    mempool: OrdersMempool[F],
    txr: Txr[F, D]
  ) extends Orders[F] {

    def process(events: NonEmptyList[OrderEvent]): F[Unit] = {

      def run: D[Int] = events
        .traverse {
          case OrderEvent.Apply(order)   => bundle.insertAnyOrder.traverse(_(order)).map(_.sum)
          case OrderEvent.Unapply(order) => bundle.resolveAnyOrder.traverse(_(order)).map(_.sum)
        }
        .map(_.toList.sum)

      val (toInsert, toResolve) =
        events
          .foldLeft((List.empty[ProcessedOrder.Any], List.empty[ProcessedOrder.Any])) { case ((acc1, acc2), next) =>
            next match {
              case OrderEvent.Apply(order)   => (order :: acc1) -> acc2
              case OrderEvent.Unapply(order) => acc1            -> (order :: acc2)
            }
          }

      for {
        _ <- run.trans
        _ <- mempool.del(toInsert)
        _ <- toResolve.traverse(mempool.put)
      } yield ()

    }
  }

  final private class Tracing[F[_]: Monad: Logging] extends Orders[Mid[F, *]] {

    def process(events: NonEmptyList[OrderEvent]): Mid[F, Unit] =
      for {
        _ <- info"Going to process next events: $events"
        r <- _
        _ <- info"All processed events inserted successfully."
      } yield r
  }
}
