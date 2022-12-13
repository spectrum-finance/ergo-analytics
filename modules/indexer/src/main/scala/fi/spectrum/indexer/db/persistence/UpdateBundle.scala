package fi.spectrum.indexer.db.persistence

import cats.{Applicative, FlatMap}
import fi.spectrum.indexer.db.schema.OrderSchema._
import fi.spectrum.indexer.models.{DepositDB, RedeemDB, SwapDB}
import tofu.doobie.LiftConnectionIO
import tofu.doobie.transactor.Txr

final case class UpdateBundle[F[_]](
  swaps: Update[SwapDB, F],
  redeems: Update[RedeemDB, F],
  deposits: Update[DepositDB, F]
)

object UpdateBundle {

  def make[D[_]: FlatMap: LiftConnectionIO, F[_]: Applicative](implicit
    txr: Txr[F, D],
    persistBundle: PersistBundle[F]
  ): UpdateBundle[F] =
    UpdateBundle(
      Update.make[SwapDB, D, F](persistBundle.swaps),
      Update.make[RedeemDB, D, F](persistBundle.redeems),
      Update.make[DepositDB, D, F](persistBundle.deposits)
    )
}
