package fi.spectrum.api.v1.routes

import cats.effect.kernel.Async
import cats.syntax.semigroupk._
import fi.spectrum.api.v1.endpoints.LmStatsEndpoints
import fi.spectrum.api.v1.services.LmStatsApi
import fi.spectrum.common.http.AdaptThrowable.AdaptThrowableEitherT
import fi.spectrum.common.http.HttpError
import fi.spectrum.common.http.syntax.toAdaptThrowableOps
import org.http4s.HttpRoutes
import sttp.tapir.server.http4s.{Http4sServerInterpreter, Http4sServerOptions}

final class LmStatsRoutes[
  F[_]: Async: AdaptThrowableEitherT[*[_], HttpError]
](stats: LmStatsApi[F])(implicit
  opts: Http4sServerOptions[F]
) {
  private val endpoints = new LmStatsEndpoints

  import endpoints._

  private val interpreter = Http4sServerInterpreter(opts)

  def routes: HttpRoutes[F] =
    lmPoolsStatsR <+>
    lmUserStatsR

  def lmPoolsStatsR: HttpRoutes[F] =
    interpreter
      .toRoutes(lmPoolsStatsE.serverLogic(_ => stats.lmStatsApi.adaptThrowable.value))

  def lmUserStatsR: HttpRoutes[F] =
    interpreter
      .toRoutes(lmUserStatsE.serverLogic(addresses => stats.userLmStats(addresses).adaptThrowable.value))
}

object LmStatsRoutes {

  def make[
    F[_]: Async: AdaptThrowableEitherT[*[_], HttpError]
  ](implicit
    stats: LmStatsApi[F],
    opts: Http4sServerOptions[F]
  ): HttpRoutes[F] =
    new LmStatsRoutes[F](stats).routes
}
