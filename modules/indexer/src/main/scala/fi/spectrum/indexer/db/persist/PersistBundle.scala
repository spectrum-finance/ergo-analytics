package fi.spectrum.indexer.db.persist

import cats.FlatMap
import fi.spectrum.core.domain.analytics.OrderEvaluation._
import fi.spectrum.core.domain.analytics.ProcessedOrderOptics._
import fi.spectrum.core.domain.analytics.{OffChainFee, OrderEvaluation, Processed}
import fi.spectrum.core.domain.order.OrderOptics._
import fi.spectrum.core.domain.order.{Fee, Order, OrderId}
import fi.spectrum.core.domain.order.Fee._
import fi.spectrum.core.domain.pool.Pool
import fi.spectrum.core.domain.pool.Pool.AmmPool
import fi.spectrum.core.domain.pool.PoolOptics._
import fi.spectrum.core.domain.{BlockId, BoxId}
import fi.spectrum.indexer.db.models._
import fi.spectrum.indexer.models.Block
import tofu.doobie.LiftConnectionIO
import tofu.doobie.log.EmbeddableLogHandler

final case class PersistBundle[D[_]](
  swaps: Persist[Processed.Any, D],
  deposits: Persist[Processed.Any, D],
  redeems: Persist[Processed.Any, D],
  locks: Persist[Processed.Any, D],
  offChainFees: Persist[Processed.Any, D],
  pools: Persist[Pool, D],
  blocks: Persist[Block, D]
) {

  def insertAnyOrder: List[Processed.Any => D[Int]] =
    List(swaps.insert, deposits.insert, redeems.insert, locks.insert, offChainFees.insert)

  def resolveAnyOrder: List[Processed.Any => D[Int]] =
    List(swaps.resolve, deposits.resolve, redeems.resolve, locks.resolve, offChainFees.resolve)
}

object PersistBundle {

  def make[D[_]: LiftConnectionIO: FlatMap](implicit elh: EmbeddableLogHandler[D]): PersistBundle[D] =
    PersistBundle(
      Persist.makeUpdatable[D, Order.Swap, SwapEvaluation, SwapDB],
      Persist.makeUpdatable[D, Order.Deposit, DepositEvaluation, DepositDB],
      Persist.makeUpdatable[D, Order.Redeem, RedeemEvaluation, RedeemDB],
      Persist.makeUpdatable[D, Order.Lock, OrderEvaluation, LockDB],
      Persist.makeNonUpdatable[D, Processed.Any, OffChainFee, OffChainFeeDB, OrderId],
      Persist.makeNonUpdatable[D, Pool, AmmPool, PoolDB, BoxId],
      Persist.makeNonUpdatable[D, Block, Block, BlockDB, BlockId]
    )
}
