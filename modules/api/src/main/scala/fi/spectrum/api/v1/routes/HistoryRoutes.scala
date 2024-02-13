package fi.spectrum.api.v1.routes

import cats.effect.kernel.Async
import fi.spectrum.api.v1.endpoints.HistoryEndpoints
import fi.spectrum.api.v1.services.{HistoryApi, MempoolApi}
import fi.spectrum.common.http.AdaptThrowable.AdaptThrowableEitherT
import fi.spectrum.common.http.HttpError
import fi.spectrum.common.http.syntax.toAdaptThrowableOps
import org.http4s.HttpRoutes
import sttp.tapir.server.http4s.{Http4sServerInterpreter, Http4sServerOptions}
import cats.syntax.semigroupk._
import io.circe.generic.auto._

final class HistoryRoutes[
  F[_]: Async: AdaptThrowableEitherT[*[_], HttpError]
](mempool: MempoolApi[F], history: HistoryApi[F])(implicit
  opts: Http4sServerOptions[F]
) {

  private val endpoints = new HistoryEndpoints[F]
  import endpoints._

  private val interpreter = Http4sServerInterpreter(opts)

  def routes: HttpRoutes[F] = mempoolHistoryR <+> orderHistoryR <+> addressesHistoryR <+> streamOrderHistoryR

  def mempoolHistoryR: HttpRoutes[F] = interpreter.toRoutes(mempoolHistoryE.serverLogic { addresses =>
    mempool.ordersByAddress(addresses).adaptThrowable.value
  })

  def orderHistoryR: HttpRoutes[F] = interpreter.toRoutes(orderHistoryE.serverLogic { case (paging, window, query) =>
    history.orderHistory(paging, window, query).adaptThrowable.value
  })

  def streamOrderHistoryR: HttpRoutes[F] =
    interpreter.toRoutes(streamOrderHistoryE.serverLogic { case (paging, window, query) =>
      streaming.bytesStream(history.streamOrderHistory(paging, window, query))
    })

  def addressesHistoryR: HttpRoutes[F] = interpreter.toRoutes(addressesHistoryE.serverLogic { paging =>
    history.addressesHistory(paging).adaptThrowable.value
  })

}

object HistoryRoutes {

  def make[
    F[_]: Async: AdaptThrowableEitherT[*[_], HttpError]
  ](implicit
    mempool: MempoolApi[F],
    history: HistoryApi[F],
    opts: Http4sServerOptions[F]
  ): HttpRoutes[F] =
    new HistoryRoutes[F](mempool, history).routes
}
