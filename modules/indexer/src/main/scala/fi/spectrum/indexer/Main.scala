package fi.spectrum.indexer

import cats.Monad
import fi.spectrum.core.domain.TokenId
import fi.spectrum.indexer.db.persist.PersistBundle
import fi.spectrum.indexer.processes.{OrdersProcessor, PoolsProcessor, TransactionsProcessor}
import fi.spectrum.parser.PoolParser
import fi.spectrum.parser.evaluation.ProcessedOrderParser
import fi.spectrum.streaming._
import fs2.Chunk
import org.ergoplatform.ErgoAddressEncoder
import tofu.doobie.LiftConnectionIO
import tofu.doobie.log.EmbeddableLogHandler
import tofu.doobie.transactor.Txr
import tofu.logging.Logs
import tofu.streams.{Chunks, Evals}
import tofu.syntax.monadic._

object Main {

//  def run[S[_]: Evals[*[_], F]: Chunks[*[_], Chunk]: Monad, F[_]: Monad, D[_]: Monad: LiftConnectionIO](spf: TokenId)(
//    implicit
//    elh: EmbeddableLogHandler[D],
//    txr: Txr[F, D],
//    e: ErgoAddressEncoder,
//    logs: Logs[F, F]
//  ) = {
//    implicit val txConsumer: TxEventsConsumer[S, F]       = ???
//    implicit val orderProducer: OrderEventsProducer[S]    = ???
//    implicit val orderConsumer: OrderEventsConsumer[S, F] = ???
//    implicit val poolsConsumer: PoolsEventsConsumer[S, F] = ???
//    implicit val poolsProducer: PoolsEventsProducer[S]    = ???
//
//    implicit val ordersParser: ProcessedOrderParser = ProcessedOrderParser.make(spf)
//    implicit val poolsParser: PoolParser            = PoolParser.make
//
//    implicit val persistBundle: PersistBundle[D] = PersistBundle.make[D]
//
//    implicit val ordersProcessor = OrdersProcessor.make[Chunk, F, S]
//    implicit val poolsProcessor  = PoolsProcessor.make[Chunk, F, S]
//
//    for {
//      tx <- TransactionsProcessor.make[Chunk, F, S]
//    } yield (tx.run, ordersProcessor.run, poolsProcessor.run)
//  }
}
