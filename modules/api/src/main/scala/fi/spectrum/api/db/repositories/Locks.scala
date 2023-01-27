package fi.spectrum.api.db.repositories

import cats.tagless.syntax.functorK._
import cats.{FlatMap, Functor}
import doobie.ConnectionIO
import fi.spectrum.api.db.models.locks.LiquidityLockStats
import fi.spectrum.api.db.sql.LiquidityLocksSql
import fi.spectrum.core.domain.order.PoolId
import tofu.doobie.LiftConnectionIO
import tofu.doobie.log.EmbeddableLogHandler
import tofu.higherKind.{Mid, RepresentableK}
import tofu.logging.{Logging, Logs}
import tofu.syntax.logging._
import tofu.syntax.monadic._

trait Locks[F[_]] {

  /** Get liquidity locks by the pool with the given `id`.
    */
  def byPool(poolId: PoolId, leastDeadline: Int): F[List[LiquidityLockStats]]
}

object Locks {

  implicit def representableK: RepresentableK[Locks] =
    tofu.higherKind.derived.genRepresentableK

  def make[I[_]: Functor, D[_]: FlatMap: LiftConnectionIO](implicit
    elh: EmbeddableLogHandler[D],
    logs: Logs[I, D]
  ): I[Locks[D]] =
    logs.forService[Locks[D]].map { implicit l =>
      elh.embed(implicit lh =>
        new LocksTracing[D] attach new Live(new LiquidityLocksSql()).mapK(LiftConnectionIO[D].liftF)
      )
    }

  final class Live(sql: LiquidityLocksSql) extends Locks[ConnectionIO] {

    def byPool(poolId: PoolId, leastDeadline: Int): ConnectionIO[List[LiquidityLockStats]] =
      sql.getLocksByPool(poolId, leastDeadline).to[List]
  }

  final class LocksTracing[F[_]: FlatMap: Logging] extends Locks[Mid[F, *]] {

    def byPool(poolId: PoolId, leastDeadline: Int): Mid[F, List[LiquidityLockStats]] =
      for {
        _ <- trace"byPool(poolId=$poolId, leastDeadline=$leastDeadline)"
        r <- _
        _ <- trace"byPool(poolId=$poolId, leastDeadline=$leastDeadline) -> ${r.size} info entities selected"
      } yield r
  }
}
