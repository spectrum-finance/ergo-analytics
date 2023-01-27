package fi.spectrum.api.v1

import cats.effect.ExitCode
import cats.effect.kernel.Async
import cats.syntax.semigroupk._
import fi.spectrum.api.configs.{HttpConfig, RequestConfig}
import fi.spectrum.api.models.TraceId
import fi.spectrum.api.v1.http.syntax.routes.unliftRoutes
import fi.spectrum.api.v1.routes.{AmmStatsRoutes, DocsRoutes}
import fi.spectrum.api.v1.services.{AmmStats, LqLocks}
import fi.spectrum.cache.middleware.CacheMiddleware.CachingMiddleware
import fi.spectrum.graphite.MetricsMiddleware.MetricsMiddleware
import org.http4s.blaze.server.BlazeServerBuilder
import org.http4s.server.Router
import org.http4s.server.middleware.CORS
import sttp.tapir.server.http4s.Http4sServerOptions
import tofu.lift.Unlift

object HttpServer {

  def make[
    I[_]: Async,
    F[_]: Async: Unlift[*[_], I]: TraceId.Local
  ](conf: HttpConfig, requestConf: RequestConfig)(implicit
    stats: AmmStats[F],
    locks: LqLocks[F],
    opts: Http4sServerOptions[F],
    cache: CachingMiddleware[F],
    metrics: MetricsMiddleware[F]
  ): fs2.Stream[I, ExitCode] = {
    val ammStatsR  = AmmStatsRoutes.make[F](requestConf)
    val docsR      = DocsRoutes.make[F](requestConf)
    val routes     = unliftRoutes[F, I](metrics.middleware(cache.middleware(ammStatsR <+> docsR)))
    val corsRoutes = CORS.policy.withAllowOriginAll(routes)
    val api        = Router("/" -> corsRoutes).orNotFound
    BlazeServerBuilder[I].bindHttp(conf.port, conf.host).withHttpApp(api).serve
  }
}
