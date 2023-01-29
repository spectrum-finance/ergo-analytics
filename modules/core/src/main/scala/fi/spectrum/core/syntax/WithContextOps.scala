package fi.spectrum.core.syntax

import cats.Applicative
import tofu.WithContext

object WithContextOps {

  implicit final class WithContextOps[A](val value: A) extends AnyVal {
    def makeContext[F[_]: Applicative]: WithContext[F, A] = WithContext.const[F, A](value)}
}
