package fi.spectrum.indexer.services

import cats.{Applicative, Monad}
import fi.spectrum.core.domain.analytics.ProcessedOrderOptics._
import fi.spectrum.core.domain.order.{Operation, Order}
import fi.spectrum.indexer.db.persistence.UpdateBundle
import fi.spectrum.indexer.models.DepositDB._
import fi.spectrum.indexer.models.{DepositDB, LockDB, RedeemDB, SwapDB}

final case class InsertOrderBundle[F[_]](
  swaps: InsertOrder[F],
  redeems: InsertOrder[F],
  deposits: InsertOrder[F],
  locks: InsertOrder[F]
) {
  def toList: List[InsertOrder[F]] = List(swaps, redeems, deposits, locks)
}

object InsertOrderBundle {

  def make[F[_]: Monad](implicit bundle: UpdateBundle[F]): InsertOrderBundle[F] =
    InsertOrderBundle(
      InsertOrder.make[Order.AnySwap, SwapDB, F](Operation.Swap, bundle.swaps),
      InsertOrder.make[Order.AnyRedeem, RedeemDB, F](Operation.Redeem, bundle.redeems),
      InsertOrder.make[Order.AnyDeposit, DepositDB, F](Operation.Deposit, bundle.deposits),
      InsertOrder.make[Order.AnyLock, LockDB, F](Operation.Lock, bundle.locks)
    )
}
