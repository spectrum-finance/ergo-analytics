package fi.spectrum.api.db.repositories

import cats.tagless.syntax.functorK._
import cats.{FlatMap, Functor, Monad}
import doobie.ConnectionIO
import fi.spectrum.api.db.models.amm._
import fi.spectrum.api.db.models.{PoolSnapshotDB, PoolTraceDB, PoolVolumeSnapshotDB}
import fi.spectrum.api.db.sql.AnalyticsSql
import fi.spectrum.api.v1.endpoints.models.TimeWindow
import fi.spectrum.core.domain.order.PoolId
import fi.spectrum.graphite.Metrics
import tofu.doobie.LiftConnectionIO
import tofu.doobie.log.EmbeddableLogHandler
import tofu.higherKind.{Mid, RepresentableK}
import tofu.logging.{Logging, Logs}
import tofu.syntax.foption._
import tofu.syntax.logging._
import tofu.syntax.monadic._
import tofu.time.Clock

trait Pools[F[_]] {

  /** Get general info about the pool with the given `id`.
    */
  def getFirstPoolSwapTime(id: PoolId): F[Option[PoolInfo]]

  /** Get snapshots of all pools.
    */
  def snapshots: F[List[PoolSnapshotDB]]

  /** Get recent volumes by all pools.
    */
  def volumes(window: TimeWindow): F[List[PoolVolumeSnapshotDB]]

  /** Get volumes by a given pool.
    */
  def volume(id: PoolId, window: TimeWindow): F[Option[PoolVolumeSnapshotDB]]

  /** Get fees by a given pool.
    */
  def fees(id: PoolSnapshot, window: TimeWindow): F[Option[PoolFeesSnapshot]]

  /** Get snapshots of a given pool within given depth.
    */
  def trace(id: PoolId, depth: Int, currHeight: Int): F[List[PoolTraceDB]]

  /** Get most recent snapshot of a given pool below given depth.
    */
  def prevTrace(id: PoolId, depth: Int, currHeight: Int): F[Option[PoolTraceDB]]

  /** Get average asset amounts in a given pool within given height window.
    */
  def avgAmounts(id: PoolId, window: TimeWindow, resolution: Int): F[List[AvgAssetAmounts]]
}

object Pools {

  implicit def representableK: RepresentableK[Pools] =
    tofu.higherKind.derived.genRepresentableK

  def make[I[_]: Functor, D[_]: Monad: LiftConnectionIO: Clock](implicit
    elh: EmbeddableLogHandler[D],
    metrics: Metrics[D],
    logs: Logs[I, D]
  ): I[Pools[D]] =
    logs.forService[Pools[D]].map { implicit l =>
      elh.embed { implicit lh =>
        new PoolsMetrics[D] attach (
          new PoolsTracing[D] attach new Live(new AnalyticsSql()).mapK(LiftConnectionIO[D].liftF)
        )
      }
    }

  final class Live(sql: AnalyticsSql) extends Pools[ConnectionIO] {

    def getFirstPoolSwapTime(id: PoolId): ConnectionIO[Option[PoolInfo]] =
      sql.getFirstPoolSwapTime(id).option

    def snapshots: ConnectionIO[List[PoolSnapshotDB]] =
      sql.getPoolSnapshots.to[List]

    def volumes(window: TimeWindow): ConnectionIO[List[PoolVolumeSnapshotDB]] =
      sql.getPoolVolumes(window).to[List]

    def volume(id: PoolId, window: TimeWindow): ConnectionIO[Option[PoolVolumeSnapshotDB]] =
      sql.getPoolVolumes(id, window).option

    def fees(pool: PoolSnapshot, window: TimeWindow): ConnectionIO[Option[PoolFeesSnapshot]] =
      sql.getPoolFees(pool, window).option.mapIn(_.tooPoolFeesSnapshot(pool))

    def trace(id: PoolId, depth: Int, currHeight: Int): ConnectionIO[List[PoolTraceDB]] =
      sql.getPoolTrace(id, depth, currHeight).to[List]

    def prevTrace(id: PoolId, depth: Int, currHeight: Int): ConnectionIO[Option[PoolTraceDB]] =
      sql.getPrevPoolTrace(id, depth, currHeight).option

    def avgAmounts(id: PoolId, window: TimeWindow, resolution: Int): ConnectionIO[List[AvgAssetAmounts]] =
      sql.getAvgPoolSnapshot(id, window, resolution).to[List]

  }

  final class PoolsTracing[F[_]: FlatMap: Logging] extends Pools[Mid[F, *]] {

    def getFirstPoolSwapTime(poolId: PoolId): Mid[F, Option[PoolInfo]] =
      for {
        _ <- trace"info(poolId=$poolId)"
        r <- _
        _ <- trace"info(poolId=$poolId) -> ${r.size} info entities selected"
      } yield r

    def volumes(window: TimeWindow): Mid[F, List[PoolVolumeSnapshotDB]] =
      for {
        _ <- trace"volumes(window=$window)"
        r <- _
        _ <- trace"volumes(window=$window) -> ${r.size} volume snapshots selected"
      } yield r

    def volume(poolId: PoolId, window: TimeWindow): Mid[F, Option[PoolVolumeSnapshotDB]] =
      for {
        _ <- trace"volume(poolId=$poolId, window=$window)"
        r <- _
        _ <- trace"volume(poolId=$poolId, window=$window) -> ${r.size} volume snapshots selected"
      } yield r

    def fees(pool: PoolSnapshot, window: TimeWindow): Mid[F, Option[PoolFeesSnapshot]] =
      for {
        _ <- trace"fees(poolId=${pool.id}, window=$window)"
        r <- _
        _ <- trace"fees(poolId=${pool.id}, window=$window) -> ${r.size} fees snapshots selected"
      } yield r

    def trace(id: PoolId, depth: Int, currHeight: Int): Mid[F, List[PoolTraceDB]] =
      for {
        _ <- trace"trace(poolId=$id, depth=$depth, currHeight=$currHeight)"
        r <- _
        _ <- trace"trace(poolId=$id, depth=$depth, currHeight=$currHeight) -> ${r.size} trace snapshots selected"
      } yield r

    def prevTrace(id: PoolId, depth: Int, currHeight: Int): Mid[F, Option[PoolTraceDB]] =
      for {
        _ <- trace"prevTrace(poolId=$id, depth=$depth, currHeight=$currHeight)"
        r <- _
        _ <- trace"prevTrace(poolId=$id, depth=$depth, currHeight=$currHeight) -> ${r.size} trace snapshots selected"
      } yield r

    def avgAmounts(id: PoolId, window: TimeWindow, resolution: Int): Mid[F, List[AvgAssetAmounts]] =
      for {
        _ <- trace"avgAmounts(poolId=$id, window=$window, resolution=$resolution)"
        r <- _
        _ <- trace"avgAmounts(poolId=$id, window=$window, resolution=$resolution) -> ${r.size} trace snapshots selected"
      } yield r

    def snapshots: Mid[F, List[PoolSnapshotDB]] =
      for {
        _ <- trace"snapshots()"
        r <- _
        _ <- trace"snapshots() -> ${r.size} trace snapshots selected"
      } yield r
  }

  final class PoolsMetrics[F[_]: Monad: Clock](implicit metrics: Metrics[F]) extends Pools[Mid[F, *]] {

    def getFirstPoolSwapTime(id: PoolId): Mid[F, Option[PoolInfo]] =
      processMetric(_, s"db.pools.get.first.pool.swap.time.$id")

    def snapshots: Mid[F, List[PoolSnapshotDB]] =
      processMetric(_, s"db.pools.snapshots")

    def volumes(window: TimeWindow): Mid[F, List[PoolVolumeSnapshotDB]] =
      processMetric(_, s"db.pools.volumes")

    def volume(id: PoolId, window: TimeWindow): Mid[F, Option[PoolVolumeSnapshotDB]] =
      processMetric(_, s"db.pools.volume.$id")

    def fees(id: PoolSnapshot, window: TimeWindow): Mid[F, Option[PoolFeesSnapshot]] =
      processMetric(_, s"db.pools.fees.$id")

    def trace(id: PoolId, depth: Int, currHeight: Int): Mid[F, List[PoolTraceDB]] =
      processMetric(_, s"db.pools.trace.$id")

    def prevTrace(id: PoolId, depth: Int, currHeight: Int): Mid[F, Option[PoolTraceDB]] =
      processMetric(_, s"db.pools.prev.trace.$id")

    def avgAmounts(id: PoolId, window: TimeWindow, resolution: Int): Mid[F, List[AvgAssetAmounts]] =
      processMetric(_, s"db.pools.avg.amounts.$id")
  }
}
