package fi.spectrum.indexer.db.persistence

import cats.{FlatMap, Monad}
import fi.spectrum.indexer.db.schema.UpdateSchema._
import fi.spectrum.indexer.models.{DepositDB, LockDB, RedeemDB, SwapDB}
import tofu.doobie.LiftConnectionIO
import tofu.doobie.log.EmbeddableLogHandler

final case class UpdateBundle[F[_]](
  swaps: Update[SwapDB, F],
  redeems: Update[RedeemDB, F],
  deposits: Update[DepositDB, F],
  locks: Update[LockDB, F]
)

object UpdateBundle {

  def make[D[_]: Monad: LiftConnectionIO](implicit
    elh: EmbeddableLogHandler[D],
    persistBundle: PersistBundle[D]
  ): UpdateBundle[D] =
    UpdateBundle[D](
      Update.make[SwapDB, D](persistBundle.swaps),
      Update.make[RedeemDB, D](persistBundle.redeems),
      Update.make[DepositDB, D](persistBundle.deposits),
      Update.makeNonUpdatable[LockDB, D](persistBundle.locks)
    )
}
