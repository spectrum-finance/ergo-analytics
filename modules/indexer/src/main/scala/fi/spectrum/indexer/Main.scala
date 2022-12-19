package fi.spectrum.indexer

import cats.Monad
import cats.effect.kernel.{Async, Resource}
import cats.effect.std.Dispatcher
import cats.effect.syntax.resource._
import cats.effect.{ExitCode, IO, IOApp}
import fi.spectrum.core.db.PostgresTransactor
import fi.spectrum.core.db.doobieLogging.makeEmbeddableHandler
import fi.spectrum.core.domain.TxId
import fi.spectrum.core.domain.order.{OrderId, PoolId}
import fi.spectrum.indexer.config.ConfigBundle
import fi.spectrum.indexer.db.persist.PersistBundle
import fi.spectrum.indexer.processes.{OrdersProcessor, PoolsProcessor, TransactionsProcessor}
import fi.spectrum.indexer.services.{Orders, Pools, Transactions}
import fi.spectrum.parser.PoolParser
import fi.spectrum.parser.evaluation.ProcessedOrderParser
import fi.spectrum.streaming._
import fi.spectrum.streaming.domain.{OrderEvent, PoolEvent, TxEvent}
import fi.spectrum.streaming.kafka.Consumer._
import fi.spectrum.streaming.kafka.config.{ConsumerConfig, KafkaConfig}
import fi.spectrum.streaming.kafka.serde._
import fi.spectrum.streaming.kafka.serde.tx._
import fi.spectrum.streaming.kafka.{Consumer, MakeKafkaConsumer, Producer}
import fs2.kafka.RecordDeserializer
import fs2.{Chunk, Stream}
import org.apache.kafka.clients.consumer.OffsetAndMetadata
import org.apache.kafka.common.TopicPartition
import org.ergoplatform.ErgoAddressEncoder
import tofu.WithContext
import tofu.doobie.log.EmbeddableLogHandler
import tofu.doobie.transactor.Txr
import tofu.fs2.LiftStream
import tofu.fs2Instances._
import tofu.lift.IsoK
import tofu.logging.Logging
import tofu.streams.{Chunks, Evals}

object Main extends IOApp {

  type S[A] = fs2.Stream[IO, A]

  def run(args: List[String]): IO[ExitCode] =
    Dispatcher[IO].use { dispatcher =>
      implicit val isoKF: IsoK[S, Stream[IO, *]]        = IsoK.id[S]
      implicit val cxt: WithContext[IO, Dispatcher[IO]] = WithContext.const(dispatcher)
      init[S, IO](args.headOption).use { processes =>
        val appF = fs2.Stream(processes: _*).parJoinUnbounded.compile.drain
        appF as ExitCode.Success
      }
    }

  def init[
    S[_]: Evals[*[_], F]: Chunks[*[_], Chunk]: Monad: LiftStream[*[_], F],
    F[_]: Async: WithContext[*[_], Dispatcher[F]]
  ](configPathOpt: Option[String])(implicit iso: IsoK[S, Stream[F, *]]): Resource[F, List[S[Unit]]] = {
    def makeConsumer[
      K: RecordDeserializer[F, *],
      V: RecordDeserializer[F, *]
    ](kafka: KafkaConfig, conf: ConsumerConfig): Aux[K, V, (TopicPartition, OffsetAndMetadata), S, F] = {
      implicit val maker = MakeKafkaConsumer.make[F, K, V](kafka)
      Consumer.make[S, F, K, V](conf)
    }

    implicit val isoKF: IsoK[F, F] = IsoK.id[F]

    for {
      config <- ConfigBundle.load[F](configPathOpt).toResource
      implicit0(e: ErgoAddressEncoder) = config.protocol.networkType.addressEncoder
      transactor <- PostgresTransactor.make[F]("markets-index", config.postgres)
      implicit0(xa: Txr.Continuational[F]) = Txr.continuational[F](transactor)
      implicit0(logs: Logging.Make[F])     = Logging.Make.plain[F]
      implicit0(elh: EmbeddableLogHandler[xa.DB]) = makeEmbeddableHandler[F, xa.DB](
                                                      "markets-index-db-logging"
                                                    )
      implicit0(txEvents: TxEventsConsumer[S, F]) = makeConsumer[TxId, Option[TxEvent]](config.kafka, config.txConsumer)
      implicit0(orderEvents: OrderEventsConsumer[S, F]) =
        makeConsumer[OrderId, Option[OrderEvent]](config.kafka, config.ordersConsumer)
      implicit0(poolEvents: PoolsEventsConsumer[S, F]) =
        makeConsumer[PoolId, Option[PoolEvent]](config.kafka, config.poolsConsumer)
      implicit0(ordersProducer: OrderEventsProducer[S]) <-
        Producer.make[F, S, F, OrderId, OrderEvent](config.ordersProducer, config.kafka)
      implicit0(poolProducer: PoolsEventsProducer[S]) <-
        Producer.make[F, S, F, PoolId, PoolEvent](config.poolsProducer, config.kafka)
      implicit0(ordersParser: ProcessedOrderParser)  = ProcessedOrderParser.make(config.spfTokenId)
      implicit0(poolsParser: PoolParser)             = PoolParser.make
      implicit0(persistBundle: PersistBundle[xa.DB]) = PersistBundle.make[xa.DB]
      implicit0(orders: Orders[F])                   = Orders.make[F, xa.DB]
      implicit0(pools: Pools[F])                     = Pools.make[F, xa.DB]
      implicit0(transactions: Transactions[F])       = Transactions.make[F]
      ordersProcessor                                = OrdersProcessor.make[Chunk, F, S](config.application)
      poolsProcessor                                 = PoolsProcessor.make[Chunk, F, S](config.application)
      tx                                             = TransactionsProcessor.make[Chunk, F, S](config.application)
    } yield List(tx.run, ordersProcessor.run, poolsProcessor.run)
  }

}