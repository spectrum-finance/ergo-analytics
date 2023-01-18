package fi.spectrum.indexer.services

import cats.Monad
import cats.data.NonEmptyList
import cats.effect.Ref
import cats.effect.kernel.Sync
import fi.spectrum.core.domain.TokenId
import fi.spectrum.indexer.db.repositories.AssetsRepo
import tofu.doobie.transactor.Txr
import tofu.syntax.monadic._
import tofu.syntax.logging._
import tofu.syntax.doobie.txr._
import cats.syntax.traverse._
import fi.spectrum.indexer.classes.syntax._
import fi.spectrum.indexer.db.models.AssetDB._
import fi.spectrum.indexer.models.TokenInfo
import cats.syntax.semigroup._
import derevo.derive
import tofu.higherKind.Mid
import tofu.higherKind.derived.representableK
import tofu.logging.Logging

@derive(representableK)
trait Assets[F[_]] {
  def process(nel: NonEmptyList[TokenId]): F[Unit]
}

object Assets {

  def make[F[_]: Sync, D[_]](implicit
    assetRepo: AssetsRepo[D],
    explorer: Explorer[F],
    txr: Txr[F, D],
    logs: Logging.Make[F]
  ): F[Assets[F]] =
    for {
      tokens <- assetRepo.getAll.trans
      containsErg = tokens.contains(TokenInfo.ErgoTokenInfo.id)
      _ <-
        Monad[F].whenA(!containsErg)(assetRepo.insertNoConflict(NonEmptyList.one(TokenInfo.ErgoTokenInfo.toDB)).trans)
      ref <- Ref.of[F, List[TokenId]](if (!containsErg) TokenInfo.ErgoTokenInfo.id :: tokens else tokens)
      implicit0(logging: Logging[F]) = logs.forService[Assets[F]]
    } yield new Tracing[F] attach new Live[F, D](ref)

  def make[F[_]: Monad, D[_]](cache: Ref[F, List[TokenId]])(implicit
    assetRepo: AssetsRepo[D],
    explorer: Explorer[F],
    txr: Txr[F, D],
    logs: Logging.Make[F]
  ): F[Assets[F]] =
    for {
      tokens <- assetRepo.getAll.trans
      containsErg = tokens.contains(TokenInfo.ErgoTokenInfo.id)
      _ <-
        Monad[F].whenA(!containsErg)(assetRepo.insertNoConflict(NonEmptyList.one(TokenInfo.ErgoTokenInfo.toDB)).trans)
      _ <- cache.set(if (!containsErg) TokenInfo.ErgoTokenInfo.id :: tokens else tokens)
      implicit0(logging: Logging[F]) = logs.forService[Assets[F]]
    } yield new Tracing[F] attach new Live[F, D](cache)

  final private class Live[F[_]: Monad, D[_]](cache: Ref[F, List[TokenId]])(implicit
    assetRepo: AssetsRepo[D],
    explorer: Explorer[F],
    txr: Txr[F, D]
  ) extends Assets[F] {

    def process(nel: NonEmptyList[TokenId]): F[Unit] =
      for {
        exists <- cache.get
        toResolve = nel.distinct.toList.diff(exists)
        info <- toResolve.filterNot(_ == TokenInfo.ErgoTokenInfo.id).traverse(explorer.getTokenInfo).map(_.flatten)
        _ <- NonEmptyList.fromList(info) match {
               case Some(value) => assetRepo.insertNoConflict(value.map(_.toDB)).trans.void
               case None        => unit[F]
             }
        _ <- cache.update(_ |+| info.map(_.id))
      } yield ()

  }

  final private class Tracing[F[_]: Monad: Logging] extends Assets[Mid[F, *]] {

    def process(nel: NonEmptyList[TokenId]): Mid[F, Unit] =
      for {
        _ <- info"process(${nel.toList})"
        _ <- _
        _ <- info"process(${nel.toList}) -> finish"
      } yield ()
  }
}
