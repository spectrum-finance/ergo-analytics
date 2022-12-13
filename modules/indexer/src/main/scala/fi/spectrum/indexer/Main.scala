package fi.spectrum.indexer

import cats.{FlatMap, Monad}
import fi.spectrum.core.domain.TokenId
import fi.spectrum.indexer.db.persistence.{PersistBundle, UpdateBundle}
import fi.spectrum.indexer.processes.{OrdersProcessor, TransactionsProcessor}
import fi.spectrum.indexer.services.ProcessedOrderHandlers
import fi.spectrum.parser.evaluation.ProcessedOrderParser
import fi.spectrum.streaming.{OrderEventsConsumer, OrderEventsProducer, TxEventsConsumer}
import fs2.Chunk
import org.ergoplatform.ErgoAddressEncoder
import tofu.doobie.LiftConnectionIO
import tofu.doobie.log.EmbeddableLogHandler
import tofu.doobie.transactor.Txr
import tofu.logging.Logs
import tofu.streams.{Chunks, Evals}
import tofu.syntax.monadic._

object Main {

  def run[S[_]: Evals[*[_], F]: Chunks[*[_], Chunk]: Monad, F[_]: Monad, D[_]: FlatMap: LiftConnectionIO](spf: TokenId)(
    implicit
    elh: EmbeddableLogHandler[D],
    txr: Txr[F, D],
    e: ErgoAddressEncoder,
    logs: Logs[F, F]
  ) = {
    implicit val parser: ProcessedOrderParser       = ProcessedOrderParser.make(spf)
    implicit val events: TxEventsConsumer[S, F]     = ???
    implicit val orders: OrderEventsProducer[S]     = ???
    implicit val orders2: OrderEventsConsumer[S, F] = ???
    implicit val persistBundle: PersistBundle[F]    = PersistBundle.make[D, F]
    implicit val updateBundle: UpdateBundle[F]      = UpdateBundle.make[D, F]
    val processors: ProcessedOrderHandlers[F]       = ProcessedOrderHandlers.make[F](updateBundle)
    val ordersProcessor: OrdersProcessor[S]         = OrdersProcessor.make[Chunk, F, S](orders2, processors.toList)
    for {
      tx <- TransactionsProcessor.make[Chunk, F, S]
    } yield (tx, ordersProcessor)
  }
}
