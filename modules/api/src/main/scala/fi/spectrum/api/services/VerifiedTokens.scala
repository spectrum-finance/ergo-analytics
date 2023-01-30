package fi.spectrum.api.services

import cats.Monad
import cats.effect.Ref
import cats.effect.kernel.Sync
import fi.spectrum.core.domain.TokenId
import tofu.higherKind.{Mid, RepresentableK}
import tofu.logging.{Logging, Logs}
import tofu.syntax.logging._
import tofu.syntax.monadic._

trait VerifiedTokens[F[_]] {
  def update: F[Unit]

  def get: F[List[TokenId]]
}

object VerifiedTokens {

  implicit def representableK: RepresentableK[VerifiedTokens] =
    tofu.higherKind.derived.genRepresentableK

  def make[I[_]: Sync, F[_]: Sync](implicit
    network: Network[F],
    logs: Logs[I, F]
  ): I[VerifiedTokens[F]] =
    for {
      implicit0(logging: Logging[F]) <- logs.forService[VerifiedTokens[F]]
      cache                          <- Ref.in[I, F, List[TokenId]](List.empty)
    } yield new Tracing[F] attach new Live[F](cache)

  final private class Live[F[_]: Monad](cache: Ref[F, List[TokenId]])(implicit
    network: Network[F]
  ) extends VerifiedTokens[F] {

    def update: F[Unit] = for {
      ids <- network.getVerifiedTokenList
      _   <- cache.set(ids)
    } yield ()

    def get: F[List[TokenId]] = cache.get
  }

  final private class Tracing[F[_]: Monad: Logging] extends VerifiedTokens[Mid[F, *]] {
    def update: Mid[F, Unit] = _ <* info"It's time to update verified tokens list!"

    def get: Mid[F, List[TokenId]] =
      for {
        _ <- info"Get current verified tokens list"
        r <- _
        _ <- info"Verified tokens list is: $r"
      } yield r
  }
}
