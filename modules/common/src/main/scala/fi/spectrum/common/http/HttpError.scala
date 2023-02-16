package fi.spectrum.common.http

import cats.MonadError
import derevo.circe.{decoder, encoder}
import derevo.derive
import fi.spectrum.common.http.AdaptThrowable.AdaptThrowableEitherT
import tofu.logging.Logging
import tofu.syntax.logging._
import tofu.syntax.monadic._

@derive(encoder, decoder)
sealed trait HttpError

object HttpError {

  @derive(encoder, decoder)
  final case class NotFound(what: String) extends HttpError

  @derive(encoder, decoder)
  final case class Unauthorized(realm: String) extends HttpError

  @derive(encoder, decoder)
  final case class Unknown(code: Int, msg: String) extends HttpError

  @derive(encoder, decoder)
  case object NoContent extends HttpError

  implicit def adaptThrowable[F[_]](implicit
    F: MonadError[F, Throwable], L: Logging[F]
  ): AdaptThrowableEitherT[F, HttpError] =
    new AdaptThrowableEitherT[F, HttpError] {

      final def adapter: Throwable => F[HttpError] = e =>
        info"API error is: ${e.getMessage}".as(Unknown(500, "Something went wrong"))
    }
}
