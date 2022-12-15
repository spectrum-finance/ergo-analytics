package fi.spectrum.indexer.processes

import cats.syntax.foldable._
import cats.syntax.option._
import cats.{Foldable, Functor, Monad}
import fi.spectrum.core.domain
import fi.spectrum.core.domain.transaction.Transaction
import fi.spectrum.parser.evaluation.ProcessedOrderParser
import fi.spectrum.streaming.domain.TxEvent.Apply
import fi.spectrum.streaming.domain.{OrderEvent, TxEvent}
import fi.spectrum.streaming.kafka.Record
import fi.spectrum.streaming.{OrderEventsProducer, TxEventsConsumer}
import mouse.all._
import tofu.logging.{Logging, Logs}
import tofu.streams.{Chunks, Evals}
import tofu.syntax.monadic._
import tofu.syntax.logging._
import tofu.syntax.streams.all._

trait TransactionsProcessor[S[_]] {
  def run: S[Unit]
}

object TransactionsProcessor {

  def make[
    C[_]: Functor: Foldable,
    F[_]: Monad,
    S[_]: Evals[*[_], F]: Chunks[*[_], C]: Monad
  ](implicit
    events: TxEventsConsumer[S, F],
    orders: OrderEventsProducer[S],
    parser: ProcessedOrderParser,
    logs: Logs[F, F]
  ): F[TransactionsProcessor[S]] = logs.forService[TransactionsProcessor[S]].map(implicit __ => new Live[C, F, S])

  final private class Live[
    C[_]: Functor: Foldable,
    F[_]: Monad: Logging,
    S[_]: Evals[*[_], F]: Chunks[*[_], C]: Monad
  ](implicit
    events: TxEventsConsumer[S, F],
    orders: OrderEventsProducer[S],
    parser: ProcessedOrderParser
  ) extends TransactionsProcessor[S] {

    override def run: S[Unit] =
      events.stream
        .chunksN(10) //todo to config
        .evalTap(batch => info"Got next transaction batch of size: ${batch.size}.")
        .flatMap { committableBatch =>
          val events: List[Record[domain.TxId, OrderEvent]] = committableBatch.toList.flatMap { committable =>
            val tx = Transaction.fromErgoLike(committable.message.tx)
            committable.message match {
              case Apply(timestamp, _) =>
                parser
                  .parse(tx, timestamp)
                  .map(OrderEvent.Apply)
                  .map(Record(tx.id, _))
              case TxEvent.Unapply(_) =>
                parser
                  .parse(tx, 0)
                  .map(OrderEvent.Unapply)
                  .map(Record(tx.id, _))
            }
          }
          eval(info"New orders are: ${events.map(_.value).collect { case OrderEvent.Apply(order) => order }}") >>
          emits(events) thrush orders.produce
        }

  }
}
