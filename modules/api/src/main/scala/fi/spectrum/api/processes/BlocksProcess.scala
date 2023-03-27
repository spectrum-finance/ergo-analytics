package fi.spectrum.api.processes

import cats.syntax.foldable._
import cats.syntax.traverse._
import cats.{Foldable, Functor, Monad, Parallel}
import fi.spectrum.api.configs.BlocksProcessConfig
import fi.spectrum.api.services._
import fi.spectrum.cache.middleware.HttpResponseCaching
import fi.spectrum.graphite.Metrics
import fi.spectrum.streaming.kafka.BlocksConsumer
import tofu.Catches
import tofu.lift.Lift
import tofu.logging.{Logging, Logs}
import tofu.streams.{Evals, Temporal}
import tofu.syntax.handle._
import tofu.syntax.lift._
import tofu.syntax.logging._
import tofu.syntax.monadic._
import tofu.syntax.streams.all._
import tofu.syntax.streams.evals.eval
import tofu.syntax.time.now.millis
import tofu.time.Clock

trait BlocksProcess[S[_]] {
  def run: S[Unit]
}

object BlocksProcess {

  def make[
    I[_]: Monad,
    F[_]: Clock: Monad: BlocksProcessConfig.Has: Parallel,
    S[_]: Evals[*[_], F]: Temporal[*[_], C]: Monad: Catches,
    C[_]: Functor: Foldable
  ](implicit
    events: BlocksConsumer[S, F],
    snapshots: Snapshots[F],
    volumes24H: Volumes24H[F],
    fees24H: Fees24H[F],
    poolsStats: PoolsStats24H[F],
    assets: Assets[F],
    network: Network[F],
    caching: HttpResponseCaching[F],
    heightService: Height[F],
    lMSnapshots: LMSnapshots[F],
    lmStats: LmStats[F],
    lift: Lift[F, I],
    metrics: Metrics[F],
    logs: Logs[I, F]
  ): I[BlocksProcess[S]] = logs.forService[BlocksProcess[S]].flatMap { implicit __ =>
    for {
      assetsL      <- assets.update.lift[I]
      poolsL       <- snapshots.update(assetsL).lift[I]
      volumesL     <- volumes24H.update(poolsL).lift[I]
      feesL        <- fees24H.update(poolsL).lift[I]
      _            <- poolsStats.update(poolsL, volumesL, feesL).lift[I]
      height       <- network.getCurrentNetworkHeight.lift[I]
      newHeight    <- heightService.update(height).lift[I]
      lmSnapshotsL <- lMSnapshots.update.lift[I]
      _            <- lmStats.update(poolsL, newHeight, lmSnapshotsL).lift[I]
    } yield new Live[F, S, C](height)
  }

  final private class Live[
    F[_]: Clock: Monad: Logging: BlocksProcessConfig.Has: Parallel,
    S[_]: Evals[*[_], F]: Temporal[*[_], C]: Monad: Catches,
    C[_]: Functor: Foldable
  ](height: Int)(implicit
    events: BlocksConsumer[S, F],
    snapshots: Snapshots[F],
    volumes24H: Volumes24H[F],
    assets: Assets[F],
    fees24H: Fees24H[F],
    poolsStats: PoolsStats24H[F],
    caching: HttpResponseCaching[F],
    heightService: Height[F],
    lMSnapshots: LMSnapshots[F],
    lmStats: LmStats[F],
    metrics: Metrics[F]
  ) extends BlocksProcess[S] {

    def run: S[Unit] =
      eval(BlocksProcessConfig.access)
        .flatMap { config =>
          events.stream
            .groupWithin(config.blocksBatchSize, config.blocksGroupTime)
            .evalMap { batch =>
              for {
                _ <- info"Got next block event: ${batch.toList.map(_.message.map(_.id))}"
                _ <- Monad[F].whenA(batch.toList.lastOption.flatMap(_.message).exists(_.height > height)) {
                       val lastHeight = batch.toList.lastOption.flatMap(_.message).map(_.height).getOrElse(0)
                       for {
                         assetsL      <- assets.update
                         snapshotsL   <- snapshots.update(assetsL)
                         volumesL     <- volumes24H.update(snapshotsL)
                         feesL        <- fees24H.update(snapshotsL)
                         _            <- poolsStats.update(snapshotsL, volumesL, feesL)
                         newHeight    <- heightService.update(lastHeight)
                         lmSnapshotsL <- lMSnapshots.update
                         _            <- lmStats.update(snapshotsL, newHeight, lmSnapshotsL)
                         _            <- caching.invalidateAll
                       } yield ()
                     }
                _ <- batch.toList.lastOption.traverse(_.commit)
                blocks       = batch.toList.map(_.message.map(_.id))
                latestHeight = batch.toList.lastOption.flatMap(_.message).map(_.height)
                _ <-
                  info"Block $blocks processed successfully. height is: $latestHeight. Height on app start is: $height"
              } yield ()
            }
        }
        .handleWith { err: Throwable =>
          eval(
            warn"The error ${err.getMessage} occurred in BlocksProcess stream. Going to restore process..." >>
            millis.flatMap(ts => metrics.sendTs("warn.BlocksProcess", ts.toDouble))
          ) >> run
        }

  }
}
