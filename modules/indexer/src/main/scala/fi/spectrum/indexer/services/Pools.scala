package fi.spectrum.indexer.services

import cats.Monad
import cats.data.NonEmptyList
import derevo.derive
import fi.spectrum.core.domain.pool.Pool.AmmPool
import fi.spectrum.core.syntax.PoolsOps._
import fi.spectrum.graphite.Metrics
import fi.spectrum.indexer.db.persist.PersistBundle
import fi.spectrum.indexer.models.BlockChainEvent._
import tofu.doobie.transactor.Txr
import tofu.higherKind.Mid
import tofu.higherKind.derived.representableK
import tofu.logging.Logging
import tofu.syntax.doobie.txr._
import tofu.syntax.logging._
import tofu.syntax.monadic._

@derive(representableK)
trait Pools[F[_]] {
  def process(events: NonEmptyList[PoolEvent]): F[Unit]
}

object Pools {

  def make[F[_]: Monad, D[_]: Monad](implicit
    bundle: PersistBundle[D],
    txr: Txr[F, D],
    assets: Assets[F],
    metrics: Metrics[D],
    logs: Logging.Make[F]
  ): Pools[F] =
    logs.forService[Pools[F]].map(implicit __ => new Tracing[F] attach new Live[F, D])

  final private class Live[F[_]: Monad, D[_]: Monad](implicit
    bundle: PersistBundle[D],
    txr: Txr[F, D],
    assets: Assets[F],
    metrics: Metrics[D]
  ) extends Pools[F] {

    def process(events: NonEmptyList[PoolEvent]): F[Unit] = {
      def run: D[Int] = events
        .traverse {
          case Apply(pool) =>
            metrics.sendCount(s"pool.apply.${pool.metric}", 1) >>
              bundle.pools.insert(pool)
          case Unapply(pool) =>
            metrics.sendCount(s"pool.unapply.${pool.metric}", 1) >>
              bundle.pools.resolve(pool)
        }
        .map(_.toList.sum)

      def tokens: F[Unit] = NonEmptyList.fromList(
        events
          .collect { case Apply(pool: AmmPool) =>
            List(pool.lp.tokenId, pool.x.tokenId, pool.y.tokenId)
          }
          .flatten
          .distinct
      ) match {
        case Some(value) => assets.process(value)
        case None        => unit[F]
      }

      run.trans >> tokens
    }
  }

  final private class Tracing[F[_]: Monad: Logging] extends Pools[Mid[F, *]] {

    def process(events: NonEmptyList[PoolEvent]): Mid[F, Unit] =
      for {
        _ <- info"Going to process next pool events: ${events.toList.mkString(",")}"
        r <- _
        _ <- info"Pools result is: $r"
      } yield r
  }
}
