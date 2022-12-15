package fi.spectrum.indexer

import cats.{FlatMap, Monad}
import fi.spectrum.core.domain.TokenId
import fi.spectrum.core.domain.analytics.ProcessedOrder
import fi.spectrum.indexer.db.persistence.{PersistBundle, UpdateBundle}
import fi.spectrum.indexer.db.v2.Fork
import fi.spectrum.indexer.processes.{OrdersProcessor, TransactionsProcessor}
import fi.spectrum.indexer.services.InsertOrderBundle
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
import fi.spectrum.indexer.services.{ProcessOrder => PO}
import fi.spectrum.indexer.db.v2.{PersistBundle => PO2}


object Main {

  def run[S[_]: Evals[*[_], F]: Chunks[*[_], Chunk]: Monad, F[_]: Monad, D[_]: Monad: LiftConnectionIO](spf: TokenId)(
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
    implicit val persistBundle: PersistBundle[D]    = PersistBundle.make[D]
    implicit val updateBundle: UpdateBundle[D]      = UpdateBundle.make[D]
    implicit val insertBundle: InsertOrderBundle[D] = InsertOrderBundle.make[D]
    implicit val processors: PO[F]                  = PO.make[F, D](insertBundle.toList, persistBundle.offChainFee)
    implicit val pb = PO2.make
    val fork = Fork.make[F, D]
    val ordersProcessor: OrdersProcessor[S]         = OrdersProcessor.make[Chunk, F, S](orders2, processors, fork)
    for {
      tx <- TransactionsProcessor.make[Chunk, F, S]
    } yield (tx, ordersProcessor)
  }
}
