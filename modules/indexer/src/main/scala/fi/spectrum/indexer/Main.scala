package fi.spectrum.indexer

import cats.Monad
import cats.effect.kernel.{Async, Clock, Resource}
import cats.effect.std.Dispatcher
import cats.effect.syntax.resource._
import cats.effect.{ExitCode, IO, IOApp}
import fi.spectrum.core.config.ProtocolConfig
import fi.spectrum.core.db.PostgresTransactor
import fi.spectrum.core.db.doobieLogging.makeEmbeddableHandler
import fi.spectrum.core.domain.{BlockId, BoxId, TokenId, TxId}
import fi.spectrum.core.storage.OrdersStorage
import fi.spectrum.core.syntax.WithContextOps._
import fi.spectrum.graphite.{GraphiteClient, Metrics}
import fi.spectrum.indexer.config.ConfigBundle
import fi.spectrum.indexer.config.ConfigBundle._
import fi.spectrum.indexer.db.persist.PersistBundle
import fi.spectrum.indexer.db.repositories.AssetsRepo
import fi.spectrum.indexer.processes.{BlockProcessor, TransactionsProcessor}
import fi.spectrum.indexer.services._
import fi.spectrum.parser.PoolParser
import fi.spectrum.parser.evaluation.ProcessedOrderParser
import fi.spectrum.streaming.domain.TransactionEvent._
import fi.spectrum.streaming.domain.{BlockEvent, ChainSyncEvent, TransactionEvent}
import fi.spectrum.streaming.kafka.Consumer._
import fi.spectrum.streaming.kafka.KafkaDecoder._
import fi.spectrum.streaming.kafka._
import fi.spectrum.streaming.kafka.config.{ConsumerConfig, KafkaConfig}
import fi.spectrum.streaming.kafka.serde.json._
import fi.spectrum.streaming.kafka.serde.string._
import fs2.{Chunk, Stream}
import fs2.kafka.RecordDeserializer
import io.github.oskin1.rocksdb.scodec.TxRocksDB
import org.apache.kafka.clients.consumer.OffsetAndMetadata
import org.apache.kafka.common.TopicPartition
import org.ergoplatform.ErgoAddressEncoder
import sttp.client3.SttpBackend
import sttp.client3.httpclient.fs2.HttpClientFs2Backend
import tofu.doobie.log.EmbeddableLogHandler
import tofu.doobie.transactor.Txr
import tofu.fs2.LiftStream
import tofu.fs2Instances._
import tofu.lift.IsoK
import tofu.logging.Logging
import tofu.streams.{Chunks, Evals, Temporal}
import tofu.syntax.monadic._
import tofu.{In, WithContext}

object Main extends IOApp {

  type S[A] = fs2.Stream[IO, A]

  implicit val isoKS: IsoK[S, S] = IsoK.id[S]

  def run(args: List[String]): IO[ExitCode] =
    Dispatcher.parallel[IO].use { dispatcher =>
      implicit val cxt: WithContext[IO, Dispatcher[IO]] = WithContext.const(dispatcher)
      init[S, IO](args.headOption).use { processes =>
        val appF = fs2.Stream(processes: _*).parJoinUnbounded.compile.drain
        appF as ExitCode.Success
      }
    }

  def init[
    S[_]: Evals[*[_], F]: Chunks[*[_], Chunk]: Monad: LiftStream[*[_], F]: Temporal[*[_], Chunk],
    F[_]: Async: In[Dispatcher[F], *[_]]: Clock
  ](configPathOpt: Option[String])(implicit isoKFG: IsoK[S, Stream[F, *]]): Resource[F, List[S[Unit]]] = {
    def makeConsumer[
      K: RecordDeserializer[F, *],
      V: RecordDeserializer[F, *]
    ](conf: ConsumerConfig)(implicit
      context: KafkaConfig.Has[F]
    ): Aux[K, V, (TopicPartition, OffsetAndMetadata), S, F] = {
      implicit val maker: MakeKafkaConsumer[F, K, V] = MakeKafkaConsumer.make[F, K, V]
      Consumer.make[S, F, K, V](conf)
    }

    implicit val isoKF: IsoK[F, F] = IsoK.id[F]

    def makeBackend: Resource[F, SttpBackend[F, Any]] = HttpClientFs2Backend.resource[F]()

    for {
      config <- ConfigBundle.load[F](configPathOpt).toResource
      implicit0(context: WithContext[F, ConfigBundle]) = config.makeContext[F]
      implicit0(e: ErgoAddressEncoder) <- ProtocolConfig.access[F].map(_.networkType.addressEncoder).toResource
      implicit0(spf: TokenId) = config.spfTokenId
      transactor                       <- PostgresTransactor.make[F]("indexer")
      implicit0(xa: Txr.Continuational[F])        = Txr.continuational[F](transactor)
      implicit0(logsF: Logging.Make[F])           = Logging.Make.plain[F]
      implicit0(logsD: Logging.Make[xa.DB])       = Logging.Make.plain[xa.DB]
      implicit0(elh: EmbeddableLogHandler[xa.DB]) = makeEmbeddableHandler[F, xa.DB]("indexer-db")
      implicit0(txC: TxConsumer[S, F])            = makeConsumer[TxId, Option[TransactionEvent]](config.txConsumer)
      implicit0(txB: BlocksConsumer[S, F])        = makeConsumer[BlockId, Option[BlockEvent]](config.blocksConsumer)
      implicit0(csP: CSProducer[S])               <- Producer.make[F, S, F, String, ChainSyncEvent](config.chainSync)
      implicit0(graphiteD: GraphiteClient[xa.DB]) <- GraphiteClient.make[F, xa.DB](config.graphite)
      implicit0(graphiteF: GraphiteClient[F])     <- GraphiteClient.make[F, F](config.graphite)
      implicit0(sttp: SttpBackend[F, Any])        <- makeBackend
      implicit0(rocks: TxRocksDB[F])              <- TxRocksDB.make[F, F](config.rocks.path)
      implicit0(assetsRepo: AssetsRepo[xa.DB]) = AssetsRepo.make[xa.DB]
      implicit0(metricsD: Metrics[xa.DB])      = Metrics.make[xa.DB]
      implicit0(metricsF: Metrics[F])          = Metrics.make[F]
      implicit0(storage: OrdersStorage[F])     = OrdersStorage.make[F]
      implicit0(explorer: Explorer[F]) <- Explorer.make[F].toResource
      implicit0(assets: Assets[F])     <- Assets.make[F, xa.DB].toResource
      implicit0(ordersParser: ProcessedOrderParser[F]) = ProcessedOrderParser.make[F]
      implicit0(poolsParser: PoolParser)               = PoolParser.make
      implicit0(persistBundle: PersistBundle[xa.DB])   = PersistBundle.make[xa.DB]
      implicit0(blocks: Blocks[F])                     = Blocks.make[F, xa.DB]
      implicit0(orders: Orders[F])                     = Orders.make[F, xa.DB]
      implicit0(pools: Pools[F])                       = Pools.make[F, xa.DB]
      implicit0(transactions: Events[F])               = Events.make[F]
      txProcessor                                      = TransactionsProcessor.make[Chunk, F, S]
      blockProcessor                                   = BlockProcessor.make[Chunk, F, S]
    } yield List(txProcessor.run, blockProcessor.run)
  }

}
