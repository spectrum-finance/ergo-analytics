package fi.spectrum.indexer.db.v2

import cats.Monad
import cats.data.NonEmptyList
import fi.spectrum.core.domain.pool.Pool
import fi.spectrum.core.domain.pool.Pool.AmmPool
import fi.spectrum.indexer.models.PoolDB
import glass.Subset
import glass.classic.Optional
import org.ergoplatform.ErgoBox.BoxId
import tofu.syntax.monadic._
import fi.spectrum.indexer.classes.syntax._
import fi.spectrum.indexer.models.PoolDB.toSchema
import tofu.doobie.transactor.Txr
import tofu.syntax.doobie.txr._

trait PoolsProcessor[F[_]] {
  def resolve(nel: NonEmptyList[Pool.Any]): F[Unit]

  def insert(nel: NonEmptyList[Pool.Any]): F[Unit]
}

object PoolsProcessor {

  def make[F[_]: Monad, D[_]: Monad](bundle: PersistBundle[D])(implicit txr: Txr[F, D]): PoolsProcessor[F] =
    new Live[F, D](bundle)

  final private class Live[F[_]: Monad, D[_]: Monad](bundle: PersistBundle[D])(implicit txr: Txr[F, D])
    extends PoolsProcessor[F] {

    def resolve(nel: NonEmptyList[Pool.Any]): F[Unit] =
      bundle.pools.delete(nel.toList.map(_.box.boxId)).trans.void

    def insert(nel: NonEmptyList[Pool.Any]): F[Unit] =
      bundle.pools
        .insert(
          nel.toList.flatMap(Subset[Pool.Any, AmmPool].getOption).map(_.transform)
        )
        .trans
        .void

  }
}
