package fi.spectrum.core.domain

import cats.FlatMap

trait ConstrainedEmbed[U[f[_]], C[g[_]]] {
  def embed[F[_]: FlatMap: C](ft: F[U[F]]): U[F]
}
