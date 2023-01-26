package fi.spectrum.cache.middleware

import fi.spectrum.streaming.kafka.BlocksConsumer
import tofu.streams.Evals
import tofu.syntax.streams.evals._

trait HttpCacheInvalidator[F[_]] {
  def run: F[Unit]
}

object HttpCacheInvalidator {

  def make[S[_]: Evals[*[_], F], F[_]](implicit
    caching: HttpResponseCaching[F],
    consumer: BlocksConsumer[S, F]
  ): HttpCacheInvalidator[S] =
    new Invalidator[S, F]

  final class Invalidator[S[_]: Evals[*[_], F], F[_]](implicit
    caching: HttpResponseCaching[F],
    blocks: BlocksConsumer[S, F]
  ) extends HttpCacheInvalidator[S] {

    def run: S[Unit] = blocks.stream.evalMap(_ => caching.invalidateAll)
  }
}
