package fi.spectrum.api.services

import cats.Monad
import cats.effect.Ref
import cats.effect.kernel.Sync
import tofu.higherKind.{Mid, RepresentableK}
import tofu.logging.{Logging, Logs}
import tofu.syntax.logging._
import tofu.syntax.monadic._

trait Height[F[_]] {
  def getCurrent: F[Int]
  def update(newHeight: Int): F[Int]
}

object Height {

  implicit def representableK: RepresentableK[Height] =
    tofu.higherKind.derived.genRepresentableK

  def make[I[_]: Sync, F[_]: Sync](implicit
    logs: Logs[I, F]
  ): I[Height[F]] =
    for {
      implicit0(logging: Logging[F]) <- logs.forService[Height[F]]
      cache                          <- Ref.in[I, F, Int](0)
    } yield new Tracing[F] attach new Live[F](cache)

  final private class Live[F[_]: Monad](cache: Ref[F, Int]) extends Height[F] {

    def update(newHeight: Int): F[Int] = for {
      _ <- cache.set(newHeight)
    } yield newHeight

    def getCurrent: F[Int] = cache.get
  }

  final private class Tracing[F[_]: Monad: Logging] extends Height[Mid[F, *]] {
    def update(newHeight: Int): Mid[F, Int] = info"It's time to update height: $newHeight!" >> _

    def getCurrent: Mid[F, Int] = trace"Get current height" >> _
  }
}
