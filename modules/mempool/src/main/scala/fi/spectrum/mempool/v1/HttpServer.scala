package fi.spectrum.mempool.v1

import cats.effect.ExitCode
import cats.effect.kernel.Async
import fi.spectrum.graphite.MetricsMiddleware.MetricsMiddleware
import fi.spectrum.mempool.config.HttpConfig
import fi.spectrum.mempool.services.Mempool
import org.http4s.blaze.server.BlazeServerBuilder
import org.http4s.server.Router
import org.http4s.server.middleware.CORS
import sttp.tapir.server.http4s.Http4sServerOptions
import tofu.logging.Logs

object HttpServer {

  def make[F[_]: Async](conf: HttpConfig)(implicit
    mempool: Mempool[F],
    opts: Http4sServerOptions[F],
    metrics: MetricsMiddleware[F],
    logs: Logs[F, F]
  ): fs2.Stream[F, ExitCode] =
    fs2.Stream.eval(logs.forService[HttpServer.type]).flatMap { implicit __ =>
      val routes     = metrics.middleware(MempoolRoutes.make[F])
      val corsRoutes = CORS.policy.withAllowOriginAll(routes)
      val api        = Router("/" -> corsRoutes).orNotFound
      BlazeServerBuilder[F].bindHttp(conf.port, conf.host).withHttpApp(api).serve
    }
}
