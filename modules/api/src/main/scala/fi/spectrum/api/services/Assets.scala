package fi.spectrum.api.services

import cats.Monad
import cats.effect.Ref
import cats.effect.kernel.Sync
import derevo.derive
import fi.spectrum.api.db.models.amm.AssetInfo
import fi.spectrum.api.db.repositories.Asset
import tofu.doobie.transactor.Txr
import tofu.higherKind.Mid
import tofu.higherKind.derived.representableK
import tofu.logging.{Logging, Logs}
import tofu.syntax.doobie.txr._
import tofu.syntax.logging._
import tofu.syntax.monadic._

@derive(representableK)
trait Assets[F[_]] {
  def update: F[Unit]

  def get: F[List[AssetInfo]]
}

object Assets {

  def make[I[_]: Sync, F[_]: Sync, D[_]](implicit
    txr: Txr[F, D],
    asset: Asset[D],
    logs: Logs[I, F]
  ): I[Assets[F]] =
    for {
      implicit0(logging: Logging[F]) <- logs.forService[Assets[F]]
      cache                          <- Ref.in[I, F, List[AssetInfo]](List.empty)
    } yield new Tracing[F] attach new Live[F, D](cache)

  final private class Live[F[_]: Monad, D[_]](cache: Ref[F, List[AssetInfo]])(implicit
    txr: Txr[F, D],
    asset: Asset[D]
  ) extends Assets[F] {

    def update: F[Unit] = for {
      assets <- asset.getAll.trans
      _      <- cache.set(assets)
    } yield ()

    def get: F[List[AssetInfo]] = cache.get
  }

  final private class Tracing[F[_]: Monad: Logging] extends Assets[Mid[F, *]] {
    def update: Mid[F, Unit] = info"It's time to update assets cache!" >> _

    def get: Mid[F, List[AssetInfo]] = trace"Get current assets" >> _
  }
}
