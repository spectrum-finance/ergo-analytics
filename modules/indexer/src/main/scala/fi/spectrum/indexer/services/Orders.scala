package fi.spectrum.indexer.services

import cats.Monad
import cats.data.NonEmptyList
import cats.instances.all._
import cats.syntax.traverse._
import derevo.derive
import fi.spectrum.core.syntax.ProcessedOps._
import fi.spectrum.graphite.Metrics
import fi.spectrum.indexer.db.persist.PersistBundle
import fi.spectrum.indexer.models.BlockChainEvent._
import tofu.doobie.transactor.Txr
import tofu.higherKind.Mid
import tofu.higherKind.derived.representableK
import tofu.logging.Logging
import tofu.syntax.doobie.txr._
import tofu.syntax.logging._
import tofu.syntax.monadic._
import tofu.syntax.time.now.millis
import tofu.time.Clock

@derive(representableK)
trait Orders[F[_]] {
  def process(events: NonEmptyList[OrderEvent]): F[Unit]
}

object Orders {

  def make[F[_]: Monad, D[_]: Monad: Clock](implicit
    bundle: PersistBundle[D],
    txr: Txr[F, D],
    metrics: Metrics[D],
    logs: Logging.Make[F]
  ): Orders[F] =
    logs.forService[Orders[F]].map(implicit __ => new Tracing[F] attach new Live[F, D])

  final private class Live[F[_]: Monad, D[_]: Monad: Clock](implicit
    bundle: PersistBundle[D],
    txr: Txr[F, D],
    metrics: Metrics[D]
  ) extends Orders[F] {

    def process(events: NonEmptyList[OrderEvent]): F[Unit] =
      events
        .traverse {
          case Apply(order) =>
            handleWithMetrics(
              s"apply.${order.metric}",
              bundle.insertAnyOrder.traverse(_(order)).map(_.sum)
            )
          case Unapply(order) =>
            handleWithMetrics(
              s"unapply.${order.metric}",
              bundle.resolveAnyOrder.traverse(_(order)).map(_.sum)
            )
        }
        .map(_.toList.sum)
        .trans
        .void

    private def handleWithMetrics(metric: String, f: D[Int]): D[Int] =
      for {
        _      <- metrics.sendCount(s"db.order.$metric", 1)
        start  <- millis
        r      <- f
        finish <- millis
        _      <- metrics.sendTs(s"db.order.$metric", (finish - start).toDouble)
      } yield r
  }

  final private class Tracing[F[_]: Monad: Logging] extends Orders[Mid[F, *]] {

    def process(events: NonEmptyList[OrderEvent]): Mid[F, Unit] = {
      val log = events.toList.map {
        case Apply(event)   => s"Apply(${event.order.id})"
        case Unapply(event) => s"Unapply(${event.order.id})"
      }
      for {
        _ <- info"process($log)"
        r <- _
        _ <- info"process($log) -> finish"
      } yield r
    }
  }
}
