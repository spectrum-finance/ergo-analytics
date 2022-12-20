package fi.spectrum.indexer.processes

import cats.effect.kernel.Clock
import cats.syntax.foldable._
import cats.syntax.traverse._
import cats.{Foldable, Functor, Monad}
import fi.spectrum.core.domain.analytics.ProcessedOrder
import fi.spectrum.core.domain.order.OrderStatus
import fi.spectrum.core.domain.transaction.Transaction
import fi.spectrum.indexer.config.ApplicationConfig
import fi.spectrum.indexer.services.OrdersMempool
import fi.spectrum.parser.evaluation.ProcessedOrderParser
import fi.spectrum.streaming.MempoolEventsConsumer
import glass.classic.Lens
import tofu.logging.Logging
import tofu.streams.{Chunks, Evals}
import tofu.syntax.logging._
import tofu.syntax.monadic._
import tofu.syntax.streams.all._
import tofu.syntax.time.now.millis
import fi.spectrum.core.domain.analytics.ProcessedOrderOptics._

trait MempoolProcessor[S[_]] {
  def run: S[Unit]
}

object MempoolProcessor {

  def make[
    C[_]: Functor: Foldable,
    F[_]: Monad: ApplicationConfig.Has: Clock,
    S[_]: Evals[*[_], F]: Chunks[*[_], C]: Monad
  ](implicit
    events: MempoolEventsConsumer[S, F],
    mempoolCache: OrdersMempool[F],
    orderParser: ProcessedOrderParser,
    logs: Logging.Make[F]
  ): MempoolProcessor[S] = logs.forService[MempoolProcessor[S]].map(implicit __ => new Live[C, F, S])

  final private class Live[
    C[_]: Functor: Foldable,
    F[_]: Monad: ApplicationConfig.Has: Logging: Clock,
    S[_]: Evals[*[_], F]: Chunks[*[_], C]: Monad
  ](implicit events: MempoolEventsConsumer[S, F], mempoolCache: OrdersMempool[F], orderParser: ProcessedOrderParser)
    extends MempoolProcessor[S] {

    def run: S[Unit] = eval(ApplicationConfig.access).flatMap { config =>
      events.stream
        .chunksN(config.mempoolBatchSize)
        .evalTap(batch => info"Got next mempool batch of size: ${batch.size}.")
        .flatMap { committableBatch =>
          def orders(ts: Long): List[ProcessedOrder.Any] = committableBatch.toList
            .flatMap(_.message)
            .map(tx => Transaction.fromErgoLike(tx.tx))
            .flatMap(tx => orderParser.parse(tx, ts))
            .map { order =>
              val status = OrderStatus.mapToMempool(order.state.status)
              Lens[ProcessedOrder.Any, OrderStatus].set(order, status)
            }

          for {
            ts <- eval(millis[F])
            _  <- eval(orders(ts).traverse(mempoolCache.put))
            _  <- eval(committableBatch.toList.lastOption.traverse_(_.commit))
          } yield ()
        }
    }
  }
}
