package fi.spectrum

import cats.Applicative
import cats.data.NonEmptyList
import cats.syntax.applicative._

package object indexer {

  def foldNel[F[_]: Applicative, A, T](list: List[T], f: NonEmptyList[T] => F[A], default: => A): F[A] =
    NonEmptyList.fromList(list) match {
      case Some(value) => f(value)
      case None        => default.pure[F]
    }
}
