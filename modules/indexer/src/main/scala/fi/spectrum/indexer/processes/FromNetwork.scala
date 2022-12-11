package fi.spectrum.indexer.processes

import cats.{Foldable, Functor, Monad}
import fi.spectrum.core.domain.transaction.Transaction
import fi.spectrum.parser.evaluation.{OrderEvaluationParser, ProcessedOrderParser}
import fi.spectrum.streaming.domain.TxEvent
import fi.spectrum.streaming.domain.TxEvent.Apply
import tofu.streams.{Chunks, Evals}
import tofu.syntax.streams.all._
import tofu.syntax.monadic._

trait FromNetwork[S[_]] {
  def run: S[Unit]
}

object FromNetwork {

  def make[C[_]: Functor: Foldable, F[_]: Monad, S[_]: Evals[*[_], F]: Chunks[*[_], C]: Monad](
    stream: S[TxEvent],
    parser: ProcessedOrderParser
  ): FromNetwork[S] = new Live[C, F, S](stream, parser)

  final private class Live[C[_]: Functor: Foldable, F[_]: Monad, S[_]: Evals[*[_], F]: Chunks[*[_], C]: Monad](
    stream: S[TxEvent],
    parser: ProcessedOrderParser
  ) extends FromNetwork[S] {

    override def run: S[Unit] = stream
      .map { case Apply(timestamp, tx) =>
        parser.parse(Transaction.fromErgoLike(tx), timestamp)
      }
  }
}
