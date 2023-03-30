package fi.spectrum.api

import cats.effect.Resource
import cats.effect.std.Dispatcher
import cats.effect.syntax.resource._
import dev.profunktor.redis4cats.RedisCommands
import fi.spectrum.api.configs.ConfigBundle
import fi.spectrum.api.db.repositories._
import fi.spectrum.api.models.TraceId
import fi.spectrum.api.modules.AmmStatsMath
import fi.spectrum.api.modules.PriceSolver.{CryptoPriceSolver, FiatPriceSolver}
import fi.spectrum.api.processes.{BlocksProcess, ErgPriceProcess, VerifiedTokensProcess}
import fi.spectrum.api.services._
import fi.spectrum.api.v1.HttpServer
import fi.spectrum.api.v1.services._
import fi.spectrum.cache.Cache
import fi.spectrum.cache.Cache.Plain
import fi.spectrum.cache.middleware.CacheMiddleware.CachingMiddleware
import fi.spectrum.cache.middleware.{CacheMiddleware, HttpResponseCaching}
import fi.spectrum.cache.redis.codecs._
import fi.spectrum.cache.redis.mkRedis
import fi.spectrum.core.db.PostgresTransactor
import fi.spectrum.core.db.doobieLogging.makeEmbeddableHandler
import fi.spectrum.core.domain.BlockId
import fi.spectrum.core.syntax.WithContextOps.WithContextOps
import fi.spectrum.graphite.MetricsMiddleware.MetricsMiddleware
import fi.spectrum.graphite.{GraphiteClient, Metrics, MetricsMiddleware}
import fi.spectrum.streaming.domain.BlockEvent
import fi.spectrum.streaming.kafka.Consumer.Aux
import fi.spectrum.streaming.kafka.config.{ConsumerConfig, KafkaConfig}
import fi.spectrum.streaming.kafka.serde.json._
import fi.spectrum.streaming.kafka.serde.string._
import fi.spectrum.streaming.kafka.{BlocksConsumer, Consumer, MakeKafkaConsumer}
import fs2.Chunk
import fs2.kafka.RecordDeserializer
import glass.Contains
import org.apache.kafka.clients.consumer.OffsetAndMetadata
import org.apache.kafka.common.TopicPartition
import org.ergoplatform.ErgoAddressEncoder
import sttp.client3.SttpBackend
import sttp.client3.httpclient.fs2.HttpClientFs2Backend
import sttp.tapir.server.http4s.Http4sServerOptions
import tofu.doobie.log.EmbeddableLogHandler
import tofu.doobie.transactor.Txr
import tofu.fs2Instances._
import tofu.lift.{IsoK, Unlift}
import tofu.logging.Logs
import tofu.{In, WithContext, WithLocal}
import zio.ExitCode
import zio.interop.catz._

object Main extends EnvApp[AppContext] {

  implicit val serverOptions: Http4sServerOptions[F] = Http4sServerOptions.default[F]

  override def run(args: List[String]): zio.URIO[Any, zio.ExitCode] =
    Dispatcher
      .parallel[I]
      .use { dispatcher =>
        implicit val cxt: WithContext[I, Dispatcher[I]] = WithContext.const(dispatcher)
        init(args.headOption).use { case (processes, ctx) =>
          val appF = fs2.Stream(processes: _*).parJoinUnbounded.compile.drain
          appF.run(ctx) as ExitCode.success
        }
      }
      .orDie

  private def init(
    configPathOpt: Option[String]
  )(implicit in: In[Dispatcher[I], I]): Resource[I, (List[S[Unit]], AppContext)] =
    for {
      config <- ConfigBundle.load[I](configPathOpt).toResource
      appContext                                     = AppContext.init(config)
      implicit0(context: WithContext[F, AppContext]) = appContext.makeContext[F]
      implicit0(e: ErgoAddressEncoder)               = appContext.config.protocol.networkType.addressEncoder
      implicit0(context2: WithLocal[F, TraceId])     = wr.subcontext(implicitly[Contains[AppContext, TraceId]])
      implicit0(iso: IsoK[F, I])                     = IsoK.byFunK(wr.runContextK(appContext))(wr.liftF)
      implicit0(ul: Unlift[F, I])                    = Unlift.byIso(IsoK.byFunK(wr.runContextK(appContext))(wr.liftF))
      transactor <- PostgresTransactor.make[F]("api-postgres-pool").mapK(iso.tof)
      implicit0(xa: Txr.Continuational[F])        = Txr.continuational[F](transactor)
      implicit0(elh: EmbeddableLogHandler[xa.DB]) = makeEmbeddableHandler[F, xa.DB]("api-db")
      implicit0(graphiteD: GraphiteClient[xa.DB]) <- GraphiteClient.make[F, xa.DB](config.graphite).mapK(iso.tof)
      implicit0(graphiteF: GraphiteClient[F])     <- GraphiteClient.make[F, F](config.graphite).mapK(iso.tof)
      implicit0(metricsD: Metrics[xa.DB])      = Metrics.make[xa.DB]
      implicit0(metricsF: Metrics[F])          = Metrics.make[F]
      implicit0(blocksC: BlocksConsumer[S, F]) = makeConsumer[BlockId, Option[BlockEvent]](config.blockConsumer)
      implicit0(logs2: Logs[I, F])             = Logs.withContext[I, F]
      implicit0(logs: Logs[I, xa.DB])          = Logs.sync[I, xa.DB]
      implicit0(sttp: SttpBackend[F, Any])     <- makeBackend
      implicit0(ammStatsMath: AmmStatsMath[F]) <- AmmStatsMath.make[I, F].toResource
      implicit0(asset: Asset[xa.DB])           <- Asset.make[I, xa.DB].toResource
      implicit0(blocks: Blocks[xa.DB])         <- Blocks.make[I, xa.DB].toResource
      implicit0(pools: Pools[xa.DB])           <- Pools.make[I, xa.DB].toResource
      implicit0(locks: Locks[xa.DB])           <- Locks.make[I, xa.DB].toResource
      implicit0(history: History[xa.DB])       <- History.make[I, xa.DB].toResource
      implicit0(lm: LM[xa.DB])                 <- LM.make[I, xa.DB].toResource
      implicit0(redis: Plain[F])               <- mkRedis[Array[Byte], Array[Byte], F](config.redisApiCache).mapK(iso.tof)
      implicit0(redis2: RedisCommands[F, String, String]) <-
        mkRedis[String, String, F](config.redisAppCache).mapK(iso.tof)
      implicit0(apiCache: Cache[F]) <- Cache.make[I, F].toResource
      implicit0(appCache: AppCache[F]) = AppCache.make[F]
      implicit0(httpRespCache: HttpResponseCaching[F])   <- HttpResponseCaching.make[I, F].toResource
      implicit0(network: Network[F])                     <- Network.make[I, F].toResource
      implicit0(assets: Assets[F])                       <- Assets.make[I, F, xa.DB].toResource
      implicit0(ergRate: ErgRate[F])                     <- ErgRate.make[I, F].toResource
      implicit0(snapshots: Snapshots[F])                 <- Snapshots.make[I, F, xa.DB].toResource
      implicit0(volumes: Volumes24H[F])                  <- Volumes24H.make[I, F, xa.DB].toResource
      implicit0(ergProcess: ErgPriceProcess[S])          <- ErgPriceProcess.make[I, F, S].toResource
      implicit0(tokens: VerifiedTokens[F])               <- VerifiedTokens.make[I, F].toResource
      implicit0(tokensProcess: VerifiedTokensProcess[S]) <- VerifiedTokensProcess.make[I, F, S].toResource
      implicit0(cryptoSolver: CryptoPriceSolver[F])      <- CryptoPriceSolver.make[I, F].toResource
      implicit0(fiatSolver: FiatPriceSolver[F])          <- FiatPriceSolver.make[I, F].toResource
      implicit0(fees24H: Fees24H[F])                     <- Fees24H.make[I, F, xa.DB].toResource
      implicit0(lmSnapshots: LMSnapshots[F])             <- LMSnapshots.make[I, F, xa.DB].toResource
      implicit0(height: Height[F])                       <- Height.make[I, F].toResource
      implicit0(lmStats: LmStats[F])                     <- LmStats.make[I, F].toResource
      implicit0(poolsStats: PoolsStats24H[F])            <- PoolsStats24H.make[I, F, xa.DB].toResource
      implicit0(blocksProcess: BlocksProcess[S])         <- BlocksProcess.make[I, F, S, Chunk].toResource
      implicit0(locks: LqLocks[F])                       = LqLocks.make[F, xa.DB]
      implicit0(httpCache: CachingMiddleware[F])         = CacheMiddleware.make[F]
      implicit0(metricsMiddleware: MetricsMiddleware[F]) = MetricsMiddleware.make[F]
      implicit0(ammStats: AmmStats[F])     <- AmmStats.make[I, F, xa.DB].toResource
      implicit0(lmStats: LmStatsApi[F])    <- LmStatsApi.make[I, F, xa.DB].toResource
      implicit0(mempool: MempoolApi[F])    <- MempoolApi.make[I, F, xa.DB].toResource
      implicit0(historyApi: HistoryApi[F]) <- HistoryApi.make[I, F, xa.DB].toResource
      serverProc = HttpServer.make[I, F](config.http)
    } yield List(
      ergProcess.run,
      tokensProcess.run,
      blocksProcess.run,
      serverProc.translate(wr.liftF).drain
    ) -> appContext

  private def makeConsumer[
    K: RecordDeserializer[F, *],
    V: RecordDeserializer[F, *]
  ](conf: ConsumerConfig)(implicit
    context: KafkaConfig.Has[F]
  ): Aux[K, V, (TopicPartition, OffsetAndMetadata), S, F] = {
    implicit val maker: MakeKafkaConsumer[F, K, V] = MakeKafkaConsumer.make[F, K, V]
    Consumer.make[S, F, K, V](conf)
  }

  private def emptyConsumer[
    K: RecordDeserializer[F, *],
    V: RecordDeserializer[F, *]
  ](conf: ConsumerConfig)(implicit
    context: KafkaConfig.Has[F]
  ): Aux[K, V, (TopicPartition, OffsetAndMetadata), S, F] =
    Consumer.empty[S, F, K, V]

  private def makeBackend(implicit iso: IsoK[F, I]): Resource[I, SttpBackend[F, Any]] =
    HttpClientFs2Backend
      .resource[F]()
      .mapK(iso.tof)
}
