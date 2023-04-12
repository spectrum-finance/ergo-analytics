package fi.spectrum.indexer.db.persist

import cats.FlatMap
import fi.spectrum.core.domain.analytics.OrderEvaluation._
import fi.spectrum.core.domain.analytics.ProcessedOrderOptics._
import fi.spectrum.core.domain.analytics.{OffChainFee, Processed}
import fi.spectrum.core.domain.order.Order.Compound
import fi.spectrum.core.domain.order.Order.Deposit.{AmmDeposit, LmDeposit}
import fi.spectrum.core.domain.order.Order.Redeem.{AmmRedeem, LmRedeem}
import fi.spectrum.core.domain.order.{Order, OrderId}
import fi.spectrum.core.domain.pool.Pool
import fi.spectrum.core.domain.pool.Pool.LmPool._
import fi.spectrum.core.domain.pool.Pool.{AmmPool, LmPool}
import fi.spectrum.core.domain.pool.PoolOptics._
import fi.spectrum.core.domain.{BlockId, BoxId}
import fi.spectrum.indexer.db.models.LmCompoundDB._
import fi.spectrum.indexer.db.models.LmDepositDB._
import fi.spectrum.indexer.db.models.LmPoolDB._
import fi.spectrum.indexer.db.models.LmRedeemDB._
import fi.spectrum.indexer.db.models._
import fi.spectrum.indexer.db.repositories._
import fi.spectrum.indexer.models.Block
import tofu.doobie.LiftConnectionIO
import tofu.doobie.log.EmbeddableLogHandler

final case class PersistBundle[D[_]](
  swaps: Persist[Processed.Any, D],
  ammDeposits: Persist[Processed.Any, D],
  lmDeposits: Persist[Processed.Any, D],
  compounds: Persist[Processed.Any, D],
  ammRedeems: Persist[Processed.Any, D],
  lmRedeems: Persist[Processed.Any, D],
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
      ammRedeems.insert,
      lmRedeems.insert,
      locks.insert,
      offChainFees.insert
    )

  def resolveAnyOrder: List[Processed.Any => D[Int]] =
    List(
      swaps.resolve,
      ammDeposits.resolve,
      lmDeposits.resolve,
      compounds.resolve,
      ammRedeems.resolve,
      lmRedeems.resolve,
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
      Persist.makeOrderRepo[D, Order.Swap, SwapEvaluation, SwapDB],
      Persist.makeOrderRepo[D, AmmDeposit, AmmDepositEvaluation, AmmDepositDB],
      Persist.makeOrderRepo[D, LmDeposit, LmDepositCompoundEvaluation, LmDepositDB],
      Persist.makeOrderRepo[D, Compound, LmDepositCompoundEvaluation, LmCompoundDB],
      Persist.makeOrderRepo[D, AmmRedeem, AmmRedeemEvaluation, AmmRedeemDB],
      Persist.makeOrderRepo[D, LmRedeem, LmRedeemEvaluation, LmRedeemDB],
      Persist.makeLockRepo[D, Order.Lock],
      Persist.makeRepo[D, Processed.Any, OffChainFee, OffChainFeeDB, OrderId],
      Persist.makeRepo[D, Pool, AmmPool, PoolDB, BoxId],
      Persist.makeRepo[D, Pool, LmPool, LmPoolDB, BoxId],
      Persist.makeRepo[D, Block, Block, BlockDB, BlockId]
    )
}
