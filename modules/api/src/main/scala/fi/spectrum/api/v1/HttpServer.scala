package fi.spectrum.api.v1

import cats.effect.ExitCode
import cats.effect.kernel.Async
import cats.syntax.semigroupk._
import fi.spectrum.api.configs.HttpConfig
import fi.spectrum.api.models.TraceId
import fi.spectrum.api.v1.ErrorsMiddleware.ErrorsMiddleware
import fi.spectrum.api.v1.routes.{AmmStatsRoutes, DocsRoutes, HistoryRoutes, LmStatsRoutes}
import fi.spectrum.api.v1.services._
import fi.spectrum.cache.middleware.CacheMiddleware.CachingMiddleware
import fi.spectrum.common.http.syntax.routes.unliftRoutes
import fi.spectrum.graphite.MetricsMiddleware.MetricsMiddleware
import org.http4s.blaze.server.BlazeServerBuilder
import org.http4s.server.Router
import org.http4s.server.middleware.CORS
import sttp.tapir.server.http4s.Http4sServerOptions
import tofu.lift.Unlift
import tofu.logging.Logs

object HttpServer {

  def make[
    I[_]: Async,
    F[_]: Async: Unlift[*[_], I]: TraceId.Local
  ](conf: HttpConfig)(implicit
    statsAmm: AmmStats[F],
    locks: LqLocks[F],
    mempool: MempoolApi[F],
    statsLM: LmStatsApi[F],
    history: HistoryApi[F],
    opts: Http4sServerOptions[F],
    cache: CachingMiddleware[F],
    metrics: MetricsMiddleware[F],
    errorsMiddleware: ErrorsMiddleware[F],
    logs: Logs[I, F]
  ): fs2.Stream[I, ExitCode] =
    fs2.Stream.eval(logs.forService[HttpServer.type]).flatMap { implicit __ =>
      val ammStatsR = AmmStatsRoutes.make[F]
      val lmStatsR  = LmStatsRoutes.make[F]
      val historyR  = HistoryRoutes.make[F]
      val docsR     = DocsRoutes.make[F]
      val routes = unliftRoutes[F, I](
        errorsMiddleware.middleware(metrics.middleware(historyR <+> cache.middleware(ammStatsR <+> lmStatsR <+> docsR)))
      )
      val corsRoutes = CORS.policy.withAllowOriginAll(routes)
      val api        = Router("/" -> corsRoutes).orNotFound
      BlazeServerBuilder[I].bindHttp(conf.port, conf.host).withHttpApp(api).serve
    }
}
