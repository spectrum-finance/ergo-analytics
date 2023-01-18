package fi.spectrum.indexer.db.repositories

import cats.FlatMap
import cats.data.NonEmptyList
import derevo.derive
import doobie.implicits._
import doobie.util.log.LogHandler
import fi.spectrum.core.domain.TokenId
import fi.spectrum.indexer.db.models.AssetDB
import fi.spectrum.indexer.db.repositories.Repository._
import tofu.doobie.LiftConnectionIO
import tofu.doobie.log.EmbeddableLogHandler
import tofu.higherKind.derived.representableK
import tofu.syntax.monadic._

@derive(representableK)
trait AssetsRepo[F[_]] {
  def getAll: F[List[TokenId]]

  def insertNoConflict(nel: NonEmptyList[AssetDB]): F[Unit]
}

object AssetsRepo {

  def make[D[_]: LiftConnectionIO: FlatMap](implicit
    elh: EmbeddableLogHandler[D]
  ): AssetsRepo[D] =
    elh.embed(implicit __ => new Live[D]: AssetsRepo[D])

  final private class Live[F[_]: FlatMap: LiftConnectionIO](implicit insert: AssetInsert, logHandler: LogHandler)
    extends AssetsRepo[F] {

    def getAll: F[List[TokenId]] =
      LiftConnectionIO[F].lift(sql"select id from assets".query[TokenId].to[List])

    def insertNoConflict(nel: NonEmptyList[AssetDB]): F[Unit] =
      LiftConnectionIO[F].lift(insert.insertNoConflict.updateMany(nel)).void
  }
}
