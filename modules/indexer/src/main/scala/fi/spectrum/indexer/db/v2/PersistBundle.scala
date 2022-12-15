package fi.spectrum.indexer.db.v2

import cats.FlatMap
import fi.spectrum.core.domain.BoxId
import fi.spectrum.core.domain.analytics.OffChainFee
import fi.spectrum.core.domain.order.{Order, OrderId}
import fi.spectrum.indexer.models.{DepositDB, LockDB, OffChainFeeDB, RedeemDB, SwapDB}
import tofu.doobie.LiftConnectionIO
import fi.spectrum.indexer.db.v2.Repository._
import tofu.doobie.log.EmbeddableLogHandler

final case class PersistBundle[F[_]](
  swaps: Persist[SwapDB, OrderId, F],
  deposits: Persist[DepositDB, OrderId, F],
  redeems: Persist[RedeemDB, OrderId, F],
  locks: Persist[LockDB, OrderId, F],
  offChainFee: Persist[OffChainFeeDB, BoxId, F]
)

object PersistBundle {

  def make[F[_]: LiftConnectionIO: FlatMap](implicit elh: EmbeddableLogHandler[F]): PersistBundle[F] = PersistBundle(
    Persist.make[F, SwapDB, OrderId],
    Persist.make[F, DepositDB, OrderId],
    Persist.make[F, RedeemDB, OrderId],
    Persist.make[F, LockDB, OrderId],
    Persist.make[F, OffChainFeeDB, BoxId]
  )
}
