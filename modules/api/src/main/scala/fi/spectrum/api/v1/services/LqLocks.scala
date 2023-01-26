package fi.spectrum.api.v1.services

import cats.Functor
import fi.spectrum.api.repositories.Locks
import fi.spectrum.api.v1.models.locks.LiquidityLockInfo
import fi.spectrum.core.domain.order.PoolId
import tofu.doobie.transactor.Txr
import tofu.syntax.doobie.txr._
import tofu.syntax.monadic._

trait LqLocks[F[_]] {

  def byPool(poolId: PoolId, leastDeadline: Int): F[List[LiquidityLockInfo]]
}

object LqLocks {

  def make[F[_], D[_]: Functor](implicit txr: Txr[F, D], locks: Locks[D]): LqLocks[F] =
    new Live()

  final class Live[F[_], D[_]: Functor](implicit txr: Txr[F, D], locks: Locks[D]) extends LqLocks[F] {

    def byPool(poolId: PoolId, leastDeadline: Int): F[List[LiquidityLockInfo]] =
      locks.byPool(poolId, leastDeadline).map(_.map(LiquidityLockInfo(_))).trans
  }
}
