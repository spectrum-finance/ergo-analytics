package fi.spectrum.indexer.db.persistence

import cats.{Applicative, FlatMap}
import fi.spectrum.indexer.models.{DepositDB, LockDB, RedeemDB, SwapDB}
import tofu.doobie.LiftConnectionIO
import tofu.doobie.log.EmbeddableLogHandler
import tofu.doobie.transactor.Txr

final case class PersistBundle[F[_]](
  swaps: Persist[SwapDB, F],
  redeems: Persist[RedeemDB, F],
  deposits: Persist[DepositDB, F],
  locks: Persist[LockDB, F]
)

object PersistBundle {

  def make[D[_]: FlatMap: LiftConnectionIO, F[_]: Applicative](implicit
    elh: EmbeddableLogHandler[D],
    txr: Txr[F, D]
  ): PersistBundle[F] =
    PersistBundle(
      Persist.create[SwapDB, D, F],
      Persist.create[RedeemDB, D, F],
      Persist.create[DepositDB, D, F],
      Persist.create[LockDB, D, F]
    )
}
