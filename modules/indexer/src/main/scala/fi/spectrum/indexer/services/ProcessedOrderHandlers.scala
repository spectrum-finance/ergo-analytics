package fi.spectrum.indexer.services

import cats.Applicative
import fi.spectrum.core.domain.order.Order
import fi.spectrum.core.domain.order.Order.Swap
import fi.spectrum.indexer.models.{DepositDB, RedeemDB, SwapDB}
import fi.spectrum.core.domain.analytics.ProcessedOrderOptics._
import fi.spectrum.indexer.db.persistence.UpdateBundle

final case class ProcessedOrderHandlers[F[_]](
  swaps: ProcessedOrderHandler[F],
  redeems: ProcessedOrderHandler[F],
  deposits: ProcessedOrderHandler[F]
)

object ProcessedOrderHandlers {

  def make[F[_]: Applicative](bundle: UpdateBundle[F]): ProcessedOrderHandlers[F] =
    ProcessedOrderHandlers(
      ProcessedOrderHandler.make[Order.AnySwap, SwapDB, F](bundle.swaps),
      ProcessedOrderHandler.make[Order.AnyRedeem, RedeemDB, F](bundle.redeems),
      ProcessedOrderHandler.make[Order.AnyDeposit, DepositDB, F](bundle.deposits)
    )
}
