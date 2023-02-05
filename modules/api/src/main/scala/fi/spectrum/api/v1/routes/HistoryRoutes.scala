package fi.spectrum.api.v1.routes

import cats.effect.kernel.Async
import fi.spectrum.api.services.MempoolApi
import fi.spectrum.api.v1.endpoints.HistoryEndpoints
import fi.spectrum.common.http.AdaptThrowable.AdaptThrowableEitherT
import fi.spectrum.common.http.HttpError
import fi.spectrum.common.http.syntax.toAdaptThrowableOps
import org.http4s.HttpRoutes
import sttp.tapir.server.http4s.{Http4sServerInterpreter, Http4sServerOptions}

final class HistoryRoutes[
  F[_]: Async: AdaptThrowableEitherT[*[_], HttpError]
](mempool: MempoolApi[F])(implicit
  opts: Http4sServerOptions[F]
) {

  private val endpoints = new HistoryEndpoints
  import endpoints._

  private val interpreter = Http4sServerInterpreter(opts)

  def routes: HttpRoutes[F] = mempoolHistoryR

  def mempoolHistoryR: HttpRoutes[F] = interpreter.toRoutes(mempoolHistoryE.serverLogic { addresses =>
    mempool.ordersByAddress(addresses).adaptThrowable.value
  })

}

object HistoryRoutes {

  def make[
    F[_]: Async: AdaptThrowableEitherT[*[_], HttpError]
  ](implicit
    mempool: MempoolApi[F],
    opts: Http4sServerOptions[F]
  ): HttpRoutes[F] =
    new HistoryRoutes[F](mempool).routes
}
