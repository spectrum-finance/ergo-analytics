package fi.spectrum.indexer.db.persist

import cats.FlatMap
import fi.spectrum.core.domain.{BlockId, BoxId}
import fi.spectrum.core.domain.analytics.{OffChainFee, ProcessedOrder}
import fi.spectrum.core.domain.order.{Order, OrderId}
import fi.spectrum.core.domain.pool.Pool
import fi.spectrum.core.domain.pool.Pool.AmmPool
import fi.spectrum.core.domain.pool.PoolOptics._
import fi.spectrum.core.domain.analytics.ProcessedOrderOptics._
import fi.spectrum.core.domain.block.Block
import fi.spectrum.indexer.db.models.{BlockDB, DepositDB, LockDB, OffChainFeeDB, PoolDB, RedeemDB, SwapDB}
import tofu.doobie.LiftConnectionIO
import tofu.doobie.log.EmbeddableLogHandler

final case class PersistBundle[D[_]](
  swaps: Persist[ProcessedOrder.Any, D],
  deposits: Persist[ProcessedOrder.Any, D],
  redeems: Persist[ProcessedOrder.Any, D],
  locks: Persist[ProcessedOrder.Any, D],
  offChainFees: Persist[ProcessedOrder.Any, D],
  pools: Persist[Pool, D],
  blocks: Persist[Block, D]
) {

  def insertAnyOrder: List[ProcessedOrder.Any => D[Int]] =
    List(swaps.insert, deposits.insert, redeems.insert, locks.insert, offChainFees.insert)

  def resolveAnyOrder: List[ProcessedOrder.Any => D[Int]] =
    List(swaps.resolve, deposits.resolve, redeems.resolve, locks.resolve, offChainFees.resolve)
}

object PersistBundle {

  def make[D[_]: LiftConnectionIO: FlatMap](implicit elh: EmbeddableLogHandler[D]): PersistBundle[D] =
    PersistBundle(
      Persist.makeUpdatable[D, Order.Swap, SwapDB],
      Persist.makeUpdatable[D, Order.Deposit, DepositDB],
      Persist.makeUpdatable[D, Order.Redeem, RedeemDB],
      Persist.makeUpdatable[D, Order.Lock, LockDB],
      Persist.makeNonUpdatable[D, ProcessedOrder.Any, OffChainFee, OffChainFeeDB, OrderId],
      Persist.makeNonUpdatable[D, Pool, AmmPool, PoolDB, BoxId],
      Persist.makeNonUpdatable[D, Block, Block, BlockDB, BlockId]
    )
}
