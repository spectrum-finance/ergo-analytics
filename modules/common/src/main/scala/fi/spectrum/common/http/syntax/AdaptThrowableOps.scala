package fi.spectrum.common.http.syntax

import fi.spectrum.common.http.AdaptThrowable

final class AdaptThrowableOps[F[_], G[_, _], E, A](fa: F[A])(implicit
  A: AdaptThrowable[F, G, E]
) {
  def adaptThrowable: G[E, A] = A.adaptThrowable(fa)
}
