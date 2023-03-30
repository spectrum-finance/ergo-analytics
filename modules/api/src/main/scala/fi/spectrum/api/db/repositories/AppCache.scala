package fi.spectrum.api.db.repositories

import cats.Monad
import dev.profunktor.redis4cats.RedisCommands
import fi.spectrum.api.db.models.amm.{PoolFeesSnapshot, PoolSnapshot, PoolVolumeSnapshot}
import fi.spectrum.api.db.models.lm.LmPoolSnapshot
import fi.spectrum.api.v1.models.amm.PoolStats
import fi.spectrum.api.v1.models.lm.LMPoolStat
import io.circe.parser.decode
import io.circe.syntax._
import tofu.syntax.foption._
import tofu.syntax.monadic._

trait AppCache[F[_]] {
  def setPoolFeeSnapshots(fees: List[PoolFeesSnapshot]): F[Unit]
  def getPoolFeeSnapshots: F[List[PoolFeesSnapshot]]

  def getLmPoolsSnapshots: F[List[LmPoolSnapshot]]
  def setLmPoolSnapshots(pools: List[LmPoolSnapshot]): F[Unit]

  def getPoolsSnapshots: F[List[PoolSnapshot]]
  def setPoolsSnapshots(pools: List[PoolSnapshot]): F[Unit]

  def getVolume24: F[List[PoolVolumeSnapshot]]
  def setVolume24(snapshots: List[PoolVolumeSnapshot]): F[Unit]

  def getPoolsStat24H: F[List[PoolStats]]
  def setPoolsStat24H(in: List[PoolStats]): F[Unit]

  def getLmPoolsStats: F[List[LMPoolStat]]
  def setLmPoolsStats(in: List[LMPoolStat]): F[Unit]
}

object AppCache {

  def make[F[_]: Monad](implicit cmd: RedisCommands[F, String, String]): AppCache[F] =
    new Live[F]

  final private class Live[F[_]: Monad](implicit cmd: RedisCommands[F, String, String]) extends AppCache[F] {

    def setPoolFeeSnapshots(fees: List[PoolFeesSnapshot]): F[Unit] =
      cmd.set(keyPoolFeeSnapshots, fees.asJson.noSpaces)

    def getPoolFeeSnapshots: F[List[PoolFeesSnapshot]] =
      cmd
        .get(keyPoolFeeSnapshots)
        .flatMapIn(r => decode[List[PoolFeesSnapshot]](r).toOption)
        .map(_.getOrElse(List.empty))

    def getLmPoolsSnapshots: F[List[LmPoolSnapshot]] =
      cmd
        .get(keyLmPoolsSnapshots)
        .flatMapIn(r => decode[List[LmPoolSnapshot]](r).toOption)
        .map(_.getOrElse(List.empty))

    def setLmPoolSnapshots(pools: List[LmPoolSnapshot]): F[Unit] =
      cmd.set(keyLmPoolsSnapshots, pools.asJson.noSpaces)

    def getPoolsSnapshots: F[List[PoolSnapshot]] =
      cmd
        .get(keyPoolsSnapshots)
        .flatMapIn(r => decode[List[PoolSnapshot]](r).toOption)
        .map(_.getOrElse(List.empty))

    def setPoolsSnapshots(pools: List[PoolSnapshot]): F[Unit] =
      cmd.set(keyPoolsSnapshots, pools.asJson.noSpaces)

    def getVolume24: F[List[PoolVolumeSnapshot]] =
      cmd
        .get(keyVolume24)
        .flatMapIn(r => decode[List[PoolVolumeSnapshot]](r).toOption)
        .map(_.getOrElse(List.empty))

    def setVolume24(snapshots: List[PoolVolumeSnapshot]): F[Unit] =
      cmd.set(keyVolume24, snapshots.asJson.noSpaces)

    def getPoolsStat24H: F[List[PoolStats]] =
      cmd
        .get(keyVolume24)
        .flatMapIn(r => decode[List[PoolStats]](r).toOption)
        .map(_.getOrElse(List.empty))

    def setPoolsStat24H(in: List[PoolStats]): F[Unit] =
      cmd.set(keyPoolStats24, in.asJson.noSpaces)

    def getLmPoolsStats: F[List[LMPoolStat]] =
      cmd
        .get(keyLmPoolStats)
        .flatMapIn(r => decode[List[LMPoolStat]](r).toOption)
        .map(_.getOrElse(List.empty))

    def setLmPoolsStats(in: List[LMPoolStat]): F[Unit] =
      cmd.set(keyLmPoolStats, in.asJson.noSpaces)

    val keyPoolFeeSnapshots = "api.pool.fee.snapshots"
    val keyLmPoolsSnapshots = "api.lm.pools.snapshots"
    val keyPoolsSnapshots   = "api.pools.snapshots"
    val keyVolume24         = "api.volume.24"
    val keyPoolStats24      = "api.pools.stats.24"
    val keyLmPoolStats      = "api.lm.pools.stats"
  }
}
