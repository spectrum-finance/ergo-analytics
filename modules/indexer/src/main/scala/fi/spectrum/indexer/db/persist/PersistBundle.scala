package fi.spectrum.indexer.db.persist

import cats.FlatMap
import fi.spectrum.core.domain.analytics.OrderEvaluation._
import fi.spectrum.core.domain.analytics.ProcessedOrderOptics._
import fi.spectrum.core.domain.analytics.{OffChainFee, OrderEvaluation, Processed}
import fi.spectrum.core.domain.order.Order.Compound
import fi.spectrum.core.domain.order.Order.Deposit.{AmmDeposit, LmDeposit}
import fi.spectrum.core.domain.order.{Order, OrderId}
import fi.spectrum.core.domain.pool.Pool
import fi.spectrum.core.domain.pool.PoolOptics._
import fi.spectrum.core.domain.pool.Pool.LmPool._
import fi.spectrum.core.domain.pool.Pool.{AmmPool, LmPool}
import fi.spectrum.core.domain.pool.PoolOptics._
import fi.spectrum.core.domain.{BlockId, BoxId}
import fi.spectrum.indexer.db.models.LmCompoundDB._
import fi.spectrum.indexer.db.models.LmDepositDB._
import fi.spectrum.indexer.db.models.LmPoolDB._
import fi.spectrum.indexer.db.models._
import fi.spectrum.indexer.models.Block
import tofu.doobie.LiftConnectionIO
import tofu.doobie.log.EmbeddableLogHandler

final case class PersistBundle[D[_]](
  swaps: Persist[Processed.Any, D],
  ammDeposits: Persist[Processed.Any, D],
  lmDeposits: Persist[Processed.Any, D],
  compounds: Persist[Processed.Any, D],
  redeems: Persist[Processed.Any, D],
  locks: Persist[Processed.Any, D],
  offChainFees: Persist[Processed.Any, D],
  ammPools: Persist[Pool, D],
  lmPools: Persist[Pool, D],
  blocks: Persist[Block, D]
) {

  def insertAnyOrder: List[Processed.Any => D[Int]] =
    List(
      swaps.insert,
      ammDeposits.insert,
      lmDeposits.insert,
      compounds.insert,
      redeems.insert,
      locks.insert,
      offChainFees.insert
    )

  def resolveAnyOrder: List[Processed.Any => D[Int]] =
    List(
      swaps.resolve,
      ammDeposits.resolve,
      lmDeposits.resolve,
      compounds.resolve,
      redeems.resolve,
      locks.resolve,
      offChainFees.resolve
    )

  def insertAnyPool: List[Pool => D[Int]] =
    List(ammPools.insert, lmPools.insert)

  def resolveAnyPool: List[Pool => D[Int]] =
    List(ammPools.resolve, lmPools.resolve)
}

object PersistBundle {

  def make[D[_]: LiftConnectionIO: FlatMap](implicit elh: EmbeddableLogHandler[D]): PersistBundle[D] =
    PersistBundle(
      Persist.makeUpdatable[D, Order.Swap, SwapEvaluation, SwapDB],
      Persist.makeUpdatable[D, AmmDeposit, AmmDepositEvaluation, AmmDepositDB],
      Persist.makeUpdatable[D, LmDeposit, LmDepositCompoundEvaluation, LmDepositDB],
      Persist.makeUpdatable[D, Compound, LmDepositCompoundEvaluation, LmCompoundDB],
      Persist.makeUpdatable[D, Order.Redeem, RedeemEvaluation, RedeemDB],
      Persist.makeUpdatable[D, Order.Lock, OrderEvaluation, LockDB],
      Persist.makeNonUpdatable[D, Processed.Any, OffChainFee, OffChainFeeDB, OrderId],
      Persist.makeNonUpdatable[D, Pool, AmmPool, PoolDB, BoxId],
      Persist.makeNonUpdatable[D, Pool, LmPool, LmPoolDB, BoxId],
      Persist.makeNonUpdatable[D, Block, Block, BlockDB, BlockId]
    )
}
