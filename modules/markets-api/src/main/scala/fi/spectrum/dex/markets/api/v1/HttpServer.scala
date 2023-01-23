package fi.spectrum.dex.markets.api.v1

import cats.effect.kernel.Async
import cats.effect.{Concurrent, ExitCode}
import cats.syntax.semigroupk._
import fi.spectrum.core.domain.TraceId
import fi.spectrum.core.http.cache.CacheMiddleware.CachingMiddleware
import fi.spectrum.dex.markets.api.v1.routes.{AmmStatsRoutes, DocsRoutes}
import fi.spectrum.dex.markets.api.v1.services.{AmmStats, LqLocks}
import fi.spectrum.dex.markets.configs.RequestConfig
import fi.spectrum.dex.markets.api.v1.http.syntax.routes.unliftRoutes
import fi.spectrum.dex.markets.api.v1.routes.{AmmStatsRoutes, DocsRoutes}
import fi.spectrum.dex.markets.api.v1.services.{AmmStats, LqLocks}
import fi.spectrum.dex.markets.configs.{HttpConfig, RequestConfig}
import org.http4s.blaze.server.BlazeServerBuilder
import org.http4s.server.Router
import org.http4s.server.middleware.CORS
import sttp.tapir.server.http4s.Http4sServerOptions
import tofu.lift.Unlift

import scala.concurrent.ExecutionContext

object HttpServer {

  def make[
    I[_]: Async,
    F[_]: Async: Unlift[*[_], I]: TraceId.Local
  ](conf: HttpConfig, ec: ExecutionContext, requestConf: RequestConfig)(implicit
    stats: AmmStats[F],
    locks: LqLocks[F],
    opts: Http4sServerOptions[F],
    cache: CachingMiddleware[F],
    //metrics: MetricsMiddleware[F]
  ): fs2.Stream[I, ExitCode] = {
    val ammStatsR  = AmmStatsRoutes.make[F](requestConf)
    val docsR      = DocsRoutes.make[F](requestConf)
    val routes     = unliftRoutes[F, I](cache.middleware(ammStatsR <+> docsR))
    val corsRoutes = CORS.policy.withAllowOriginAll(routes)
    val api        = Router("/" -> corsRoutes).orNotFound
    BlazeServerBuilder[I].bindHttp(conf.port, conf.host).withHttpApp(api).serve
  }
}
