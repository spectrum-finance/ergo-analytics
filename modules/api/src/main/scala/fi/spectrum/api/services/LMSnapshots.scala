package fi.spectrum.api.services

import cats.Monad
import cats.effect.Ref
import cats.effect.kernel.Sync
import fi.spectrum.api.db.models.lm.LmPoolSnapshot
import fi.spectrum.api.db.repositories.LM
import tofu.doobie.transactor.Txr
import tofu.higherKind.{Mid, RepresentableK}
import tofu.logging.{Logging, Logs}
import tofu.syntax.doobie.txr._
import tofu.syntax.logging._
import tofu.syntax.monadic._

trait LMSnapshots[F[_]] {
  def get: F[List[LmPoolSnapshot]]

  def update: F[List[LmPoolSnapshot]]
}

object LMSnapshots {

  implicit def representableK: RepresentableK[LMSnapshots] =
    tofu.higherKind.derived.genRepresentableK

  def make[I[_]: Sync, F[_]: Sync, D[_]](implicit
    txr: Txr[F, D],
    lm: LM[D],
    logs: Logs[I, F]
  ): I[LMSnapshots[F]] =
    for {
      implicit0(logging: Logging[F]) <- logs.forService[LMSnapshots[F]]
      cache                          <- Ref.in[I, F, List[LmPoolSnapshot]](List.empty)
    } yield new Tracing[F] attach new Live[F, D](cache)

  final private class Live[F[_]: Monad, D[_]](cache: Ref[F, List[LmPoolSnapshot]])(implicit
    txr: Txr[F, D],
    lm: LM[D]
  ) extends LMSnapshots[F] {

    def update: F[List[LmPoolSnapshot]] = for {
      snapshots <- lm.lmPoolsSnapshots.trans
      _         <- cache.set(snapshots)
    } yield snapshots

    def get: F[List[LmPoolSnapshot]] = cache.get
  }

  final private class Tracing[F[_]: Monad: Logging] extends LMSnapshots[Mid[F, *]] {
    def update: Mid[F, List[LmPoolSnapshot]] = info"It's time to update LM snapshots!" >> _

    def get: Mid[F, List[LmPoolSnapshot]] = trace"Get current LM snapshots" >> _
  }
}
