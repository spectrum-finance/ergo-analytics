package fi.spectrum.api.db.repositories

import cats.tagless.syntax.functorK._
import cats.{FlatMap, Functor, Monad}
import doobie.ConnectionIO
import fi.spectrum.api.db.models.amm.AssetInfo
import fi.spectrum.api.db.sql.AssetsSql
import fi.spectrum.core.domain.TokenId
import fi.spectrum.graphite.Metrics
import tofu.doobie.LiftConnectionIO
import tofu.doobie.log.EmbeddableLogHandler
import tofu.higherKind.{Mid, RepresentableK}
import tofu.logging.{Logging, Logs}
import tofu.syntax.logging._
import tofu.syntax.monadic._
import tofu.time.Clock

trait Asset[F[_]] {
  def assetById(id: TokenId): F[Option[AssetInfo]]
}

object Asset {

  implicit def representableK: RepresentableK[Asset] =
    tofu.higherKind.derived.genRepresentableK

  def make[I[_]: Functor, D[_]: Monad: LiftConnectionIO: Clock](implicit
    elh: EmbeddableLogHandler[D],
    metrics: Metrics[D],
    logs: Logs[I, D]
  ): I[Asset[D]] =
    logs.forService[Asset[D]].map { implicit l =>
      elh.embed(implicit lh =>
        new AssetsMetrics[D] attach (
          new Tracing[D] attach new Live(new AssetsSql()).mapK(LiftConnectionIO[D].liftF)
        )
      )
    }

  final class Live(sql: AssetsSql) extends Asset[ConnectionIO] {

    def assetById(id: TokenId): ConnectionIO[Option[AssetInfo]] =
      sql.getAssetById(id).option
  }

  final class AssetsMetrics[F[_]: Monad: Clock](implicit metrics: Metrics[F]) extends Asset[Mid[F, *]] {

    def assetById(id: TokenId): Mid[F, Option[AssetInfo]] =
      processMetric(_, s"db.assets.by.id.$id")
  }

  final class Tracing[F[_]: FlatMap: Logging] extends Asset[Mid[F, *]] {

    def assetById(id: TokenId): Mid[F, Option[AssetInfo]] =
      for {
        _ <- trace"assetById(id=$id)"
        r <- _
        _ <- trace"assetById(id=$id) -> ${r.map(_.toString)}"
      } yield r
  }
}
