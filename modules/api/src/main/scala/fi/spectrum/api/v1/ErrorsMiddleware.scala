package fi.spectrum.api.v1

import cats.Functor
import cats.data.{Kleisli, OptionT}
import cats.effect.Sync
import org.http4s.Status.InternalServerError
import org.http4s.{HttpRoutes, Request}
import tofu.Catches
import tofu.logging.{Logging, Logs}
import tofu.syntax.handle._
import tofu.syntax.logging._
import tofu.syntax.monadic._

object ErrorsMiddleware {

  def make[I[_]: Functor, F[_]: Sync: Catches](implicit logs: Logs[I, F]): I[ErrorsMiddleware[F]] =
    logs.forService[ErrorsMiddleware[F]].map { implicit __ =>
      new ErrorsMiddleware[F]
    }

  final class ErrorsMiddleware[F[_]: Sync: Logging: Catches] {

    def middleware(routes: HttpRoutes[F]): HttpRoutes[F] = Kleisli { req: Request[F] =>
      routes(req)
        .handleWith { err: Throwable =>
          OptionT
            .liftF(info"API error is: ${err.getMessage}")
            .as(org.http4s.Response(InternalServerError))
        }
    }
  }

}
