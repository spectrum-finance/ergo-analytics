package fi.spectrum.indexer.services

import cats.Monad
import cats.data.NonEmptyList
import cats.effect.Ref
import cats.effect.kernel.Sync
import fi.spectrum.core.domain.TokenId
import fi.spectrum.indexer.db.repositories.AssetsRepo
import tofu.doobie.transactor.Txr
import tofu.syntax.monadic._
import tofu.syntax.doobie.txr._
import cats.syntax.traverse._
import fi.spectrum.indexer.classes.syntax._
import fi.spectrum.indexer.db.models.AssetDB._
import fi.spectrum.indexer.models.TokenInfo
import cats.syntax.semigroup._

trait AssetsService[F[_]] {
  def process(nel: NonEmptyList[TokenId]): F[Unit]
}

object AssetsService {

  def make[F[_]: Sync, D[_]](implicit
    assetRepo: AssetsRepo[D],
    explorer: Explorer[F],
    txr: Txr[F, D]
  ): F[AssetsService[F]] =
    for {
      tokens <- assetRepo.getAll.trans
      ref    <- Ref.of[F, List[TokenId]](tokens)
    } yield new Live[F, D](ref)

  final private class Live[F[_]: Monad, D[_]](cache: Ref[F, List[TokenId]])(implicit
    assetRepo: AssetsRepo[D],
    explorer: Explorer[F],
    txr: Txr[F, D]
  ) extends AssetsService[F] {

    def process(nel: NonEmptyList[TokenId]): F[Unit] =
      for {
        exists <- cache.get
        toResolve = nel.distinct.toList.diff(exists)
        info <- toResolve.filterNot(_ == TokenInfo.ErgoTokenInfo.id).traverse(explorer.getTokenInfo).map(_.flatten)
        _ <- NonEmptyList.fromList(info) match {
               case Some(value) => assetRepo.insertNoConflict(value.map(_.toDB)).trans.void
               case None        => unit[F]
             }
        ergo = if (toResolve.contains(TokenInfo.ErgoTokenInfo.id)) List(TokenInfo.ErgoTokenInfo.id) else List.empty
        _ <- cache.update(_ |+| info.map(_.id) |+| ergo)
      } yield ()

  }
}
