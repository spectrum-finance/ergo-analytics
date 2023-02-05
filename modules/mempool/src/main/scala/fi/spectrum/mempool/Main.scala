package fi.spectrum.mempool

import cats.Monad
import cats.effect.kernel.{Async, Clock, Resource}
import cats.effect.std.Dispatcher
import cats.effect.syntax.resource._
import cats.effect.{ExitCode, IO, IOApp}
import dev.profunktor.redis4cats.RedisCommands
import fi.spectrum.cache.redis.codecs._
import fi.spectrum.cache.redis.mkRedis
import fi.spectrum.core.config.ProtocolConfig
import fi.spectrum.core.domain.TxId
import fi.spectrum.core.storage.OrdersStorage
import fi.spectrum.core.syntax.WithContextOps._
import fi.spectrum.graphite.MetricsMiddleware.MetricsMiddleware
import fi.spectrum.graphite.{GraphiteClient, Metrics, MetricsMiddleware}
import fi.spectrum.mempool.cache.RedisCache
import fi.spectrum.mempool.config.ConfigBundle
import fi.spectrum.mempool.config.ConfigBundle._
import fi.spectrum.mempool.processes.{ChainSyncProcessor, MempoolProcessor}
import fi.spectrum.mempool.services.{Mempool, MempoolTx}
import fi.spectrum.mempool.v1.HttpServer
import fi.spectrum.parser.PoolParser
import fi.spectrum.parser.evaluation.ProcessedOrderParser
import fi.spectrum.streaming.domain.{ChainSyncEvent, MempoolEvent}
import fi.spectrum.streaming.kafka.Consumer._
import fi.spectrum.streaming.kafka.KafkaDecoder._
import fi.spectrum.streaming.domain.MempoolEvent._
import fi.spectrum.streaming.kafka._
import fi.spectrum.streaming.kafka.config.{ConsumerConfig, KafkaConfig}
import fi.spectrum.streaming.kafka.serde.json._
import fi.spectrum.streaming.kafka.serde.string._
import fs2.Chunk
import fs2.kafka.RecordDeserializer
import io.github.oskin1.rocksdb.scodec.TxRocksDB
import org.apache.kafka.clients.consumer.OffsetAndMetadata
import org.apache.kafka.common.TopicPartition
import org.ergoplatform.ErgoAddressEncoder
import sttp.tapir.server.http4s.Http4sServerOptions
import tofu.fs2.LiftStream
import tofu.fs2Instances._
import tofu.lift.IsoK
import tofu.logging.Logging
import tofu.streams.{Chunks, Evals, Temporal}
import tofu.syntax.monadic._
import tofu.{In, WithContext}
import mouse.all._

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
  ](configPathOpt: Option[String]): Resource[F, List[S[Unit]]] = {
    def makeConsumer[
      K: RecordDeserializer[F, *],
      V: RecordDeserializer[F, *]
    ](conf: ConsumerConfig)(implicit
      context: KafkaConfig.Has[F]
    ): Aux[K, V, (TopicPartition, OffsetAndMetadata), S, F] = {
      implicit val maker: MakeKafkaConsumer[F, K, V] = MakeKafkaConsumer.make[F, K, V]
      Consumer.make[S, F, K, V](conf)
    }

    implicit val serverOptions: Http4sServerOptions[F] = Http4sServerOptions.default[F]

    for {
      config <- ConfigBundle.load[F](configPathOpt).toResource
      implicit0(context: WithContext[F, ConfigBundle]) = config.makeContext[F]
      implicit0(e: ErgoAddressEncoder) <- ProtocolConfig.access[F].map(_.networkType.addressEncoder).toResource
      implicit0(logsF: Logging.Make[F])    = Logging.Make.plain[F]
      implicit0(csC: CSConsumer[S, F])     = makeConsumer[String, Option[ChainSyncEvent]](config.csConsumer)
      implicit0(mC: MempoolConsumer[S, F]) = makeConsumer[TxId, Option[MempoolEvent]](config.mempoolConsumer)
      implicit0(graphiteF: GraphiteClient[F]) <- GraphiteClient.make[F, F](config.graphite)
      implicit0(rocks: TxRocksDB[F])          <- TxRocksDB.make[F, F](config.rocks.path)
      implicit0(metricsF: Metrics[F])                  = Metrics.make[F]
      implicit0(storage: OrdersStorage[F])             = OrdersStorage.make[F]
      implicit0(ordersParser: ProcessedOrderParser[F]) = ProcessedOrderParser.make[F]
      implicit0(poolsParser: PoolParser)               = PoolParser.make
      implicit0(redis: RedisCommands[F, String, String]) <- mkRedis[String, String, F]
      implicit0(redisCache: RedisCache[F]) = RedisCache.make[F]
      implicit0(mempool: Mempool[F]) <- Mempool.make[F].toResource
      implicit0(mempoolTx: MempoolTx[F])                 = MempoolTx.make[F]
      implicit0(metricsMiddleware: MetricsMiddleware[F]) = MetricsMiddleware.make[F]
      csProcessor                                        = ChainSyncProcessor.make[Chunk, F, S]
      mempoolProcessor                                   = MempoolProcessor.make[Chunk, F, S]
      serverProcessor                                    = HttpServer.make[F](config.http)
    } yield List(csProcessor.run, mempoolProcessor.run, serverProcessor.drain.void.thrushK(LiftStream[S, F].liftF))
  }
}
