package fi.spectrum.core.syntax

import cats.Monad
import cats.syntax.either._
import fi.spectrum.core.common.errors.ResponseError
import sttp.client3.{Response, ResponseException}
import tofu.Throws
import tofu.syntax.monadic._
import tofu.syntax.raise._

object HttpOps {
  implicit class ResponseOps[F[_], A, E](private val fr: F[Response[Either[ResponseException[String, E], A]]])
    extends AnyVal {

    def absorbError(implicit R: Throws[F], A: Monad[F]): F[A] =
      fr.flatMap(_.body.leftMap(resEx => ResponseError(resEx.getMessage)).toRaise)

    def absorbServerError(implicit R: Throws[F], A: Monad[F]): F[Option[A]] =
      fr.flatMap[Option[A]] { res =>
        if (res.code.isClientError) Option.empty[A].pure[F]
        else res.body.leftMap(resEx => ResponseError(resEx.getMessage)).toRaise.map(Some(_))
      }
  }

  implicit class PlainResponseOps[F[_], A](private val fr: F[Response[Either[String, A]]]) extends AnyVal {

    def absorbError(implicit R: Throws[F], A: Monad[F]): F[A] =
      fr.flatMap(_.body.leftMap(ResponseError(_)).toRaise)
  }
}
