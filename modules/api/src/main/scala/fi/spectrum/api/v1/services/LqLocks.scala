package fi.spectrum.api.v1.services

import cats.Functor
import fi.spectrum.api.db.repositories.Locks
import fi.spectrum.api.v1.models.locks.LiquidityLockInfo
import fi.spectrum.core.domain.order.PoolId
import org.ergoplatform.ErgoAddressEncoder
import tofu.doobie.transactor.Txr
import tofu.syntax.doobie.txr._
import tofu.syntax.monadic._

trait LqLocks[F[_]] {

  def byPool(poolId: PoolId, leastDeadline: Int): F[List[LiquidityLockInfo]]
}

object LqLocks {

  def make[F[_], D[_]: Functor](implicit txr: Txr[F, D], locks: Locks[D], e: ErgoAddressEncoder): LqLocks[F] =
    new Live()

  final class Live[F[_], D[_]: Functor](implicit txr: Txr[F, D], locks: Locks[D], e: ErgoAddressEncoder)
    extends LqLocks[F] {

    def byPool(poolId: PoolId, leastDeadline: Int): F[List[LiquidityLockInfo]] =
      locks.byPool(poolId, leastDeadline).map(_.flatMap(LiquidityLockInfo.fromLiquidityLockStats)).trans
  }
}
