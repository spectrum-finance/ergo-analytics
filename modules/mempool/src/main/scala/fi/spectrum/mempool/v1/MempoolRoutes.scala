package fi.spectrum.mempool.v1

import cats.effect.kernel.Async
import fi.spectrum.common.http.AdaptThrowable.AdaptThrowableEitherT
import fi.spectrum.common.http.HttpError
import fi.spectrum.mempool.services.Mempool
import org.http4s.HttpRoutes
import sttp.tapir.server.http4s.{Http4sServerInterpreter, Http4sServerOptions}
import tofu.syntax.monadic._
import cats.syntax.either._

import scala.annotation.unused

final class MempoolRoutes[
  F[_]: Async: AdaptThrowableEitherT[*[_], HttpError]
](mempool: Mempool[F])(implicit
  opts: Http4sServerOptions[F]
) {

  private val interpreter = Http4sServerInterpreter(opts)

  def routes: HttpRoutes[F] = mempoolR

  def mempoolR: HttpRoutes[F] =
    interpreter.toRoutes(Endpoints.getMempool.serverLogic(a => mempool.getOrders(a).map(_.asRight[HttpError])))

}

object MempoolRoutes {

  def make[
    F[_]: Async: AdaptThrowableEitherT[*[_], HttpError]
  ](implicit
    mempool: Mempool[F],
    opts: Http4sServerOptions[F]
  ): HttpRoutes[F] =
    new MempoolRoutes[F](mempool).routes
}
