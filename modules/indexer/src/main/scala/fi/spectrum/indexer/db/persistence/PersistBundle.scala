package fi.spectrum.indexer.db.persistence

import cats.{Applicative, FlatMap}
import fi.spectrum.indexer.models.{DepositDB, LockDB, OffChainFeeDB, RedeemDB, SwapDB}
import tofu.doobie.LiftConnectionIO
import tofu.doobie.log.EmbeddableLogHandler
import tofu.doobie.transactor.Txr

final case class PersistBundle[F[_]](
  swaps: Persist[SwapDB, F],
  redeems: Persist[RedeemDB, F],
  deposits: Persist[DepositDB, F],
  locks: Persist[LockDB, F],
  offChainFee: Persist[OffChainFeeDB, F]
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
      Persist.create[LockDB, D, F],
      Persist.create[OffChainFeeDB, D, F]
    )

  def make[D[_]: FlatMap: LiftConnectionIO](implicit
    elh: EmbeddableLogHandler[D]
  ): PersistBundle[D] =
    PersistBundle(
      Persist.create[SwapDB, D],
      Persist.create[RedeemDB, D],
      Persist.create[DepositDB, D],
      Persist.create[LockDB, D],
      Persist.create[OffChainFeeDB, D]
    )
}
