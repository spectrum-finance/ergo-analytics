package fi.spectrum.api.services

import cats.Monad
import cats.effect.kernel.Sync
import fi.spectrum.api.db.models.lm.LmPoolSnapshot
import fi.spectrum.api.db.repositories.{AppCache, LM}
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
    cache: AppCache[F],
    logs: Logs[I, F]
  ): I[LMSnapshots[F]] =
    for {
      implicit0(logging: Logging[F]) <- logs.forService[LMSnapshots[F]]
    } yield new Tracing[F] attach new Live[F, D]

  final private class Live[F[_]: Monad, D[_]](implicit
    txr: Txr[F, D],
    lm: LM[D],
    cache: AppCache[F]
  ) extends LMSnapshots[F] {

    def update: F[List[LmPoolSnapshot]] = for {
      snapshots <- lm.lmPoolsSnapshots.trans
      _         <- cache.setLmPoolSnapshots(snapshots)
    } yield snapshots

    def get: F[List[LmPoolSnapshot]] = cache.getLmPoolsSnapshots
  }

  final private class Tracing[F[_]: Monad: Logging] extends LMSnapshots[Mid[F, *]] {
    def update: Mid[F, List[LmPoolSnapshot]] = info"It's time to update LM snapshots!" >> _

    def get: Mid[F, List[LmPoolSnapshot]] = trace"Get current LM snapshots" >> _
  }
}
