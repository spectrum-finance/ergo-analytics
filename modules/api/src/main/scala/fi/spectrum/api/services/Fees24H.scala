package fi.spectrum.api.services

import cats.effect.Ref
import cats.effect.kernel.Sync
import cats.syntax.traverse._
import cats.syntax.parallel._
import cats.{Applicative, Monad, Parallel}
import derevo.derive
import fi.spectrum.api.db.models.amm.{PoolFeesSnapshot, PoolSnapshot}
import fi.spectrum.api.db.repositories.Pools
import fi.spectrum.api.v1.endpoints.models.TimeWindow
import tofu.doobie.transactor.Txr
import tofu.higherKind.Mid
import tofu.higherKind.derived.representableK
import tofu.logging.{Logging, Logs}
import tofu.syntax.doobie.txr._
import tofu.syntax.logging._
import tofu.syntax.monadic._
import tofu.syntax.time.now.millis
import tofu.time.Clock

@derive(representableK)
trait Fees24H[F[_]] {
  def update(assets: List[PoolSnapshot]): F[List[PoolFeesSnapshot]]

  def get: F[List[PoolFeesSnapshot]]
}

object Fees24H {

  private val day: Int = 3600000 * 24

  def make[I[_]: Sync, F[_]: Sync: Clock: Parallel, D[_]: Applicative](implicit
    txr: Txr[F, D],
    pools: Pools[D],
    logs: Logs[I, F]
  ): I[Fees24H[F]] =
    for {
      implicit0(logging: Logging[F]) <- logs.forService[Fees24H[F]]
      cache                          <- Ref.in[I, F, List[PoolFeesSnapshot]](List.empty)
    } yield new Tracing[F] attach new Live[F, D](cache)

  final private class Live[F[_]: Monad: Clock: Parallel, D[_]: Applicative](cache: Ref[F, List[PoolFeesSnapshot]])(
    implicit
    txr: Txr[F, D],
    pools: Pools[D]
  ) extends Fees24H[F] {

    def update(poolsList: List[PoolSnapshot]): F[List[PoolFeesSnapshot]] =
      millis.flatMap { now =>
        poolsList
          .parTraverse(pools.fees(_, TimeWindow(Some(now - day), Some(now))).trans)
          .map(_.flatten)
          .flatTap(cache.set)
      }

    def get: F[List[PoolFeesSnapshot]] = cache.get
  }

  final private class Tracing[F[_]: Monad: Logging: Clock] extends Fees24H[Mid[F, *]] {

    def update(assets: List[PoolSnapshot]): Mid[F, List[PoolFeesSnapshot]] =
      for {
        _      <- info"It's time to update fees!"
        start  <- millis
        r      <- _
        finish <- millis
        _      <- info"Fees updated. It took: ${finish - start}ms"
      } yield r

    def get: Mid[F, List[PoolFeesSnapshot]] = trace"Get current fees" >> _
  }

}
