package fi.spectrum.api.processes

import cats.{Foldable, Functor, Monad, Parallel}
import fi.spectrum.api.configs.BlocksProcessConfig
import fi.spectrum.api.services.{Assets, Network, Snapshots, Volumes24H}
import fi.spectrum.cache.middleware.HttpResponseCaching
import fi.spectrum.streaming.kafka.BlocksConsumer
import tofu.lift.Lift
import tofu.logging.{Logging, Logs}
import tofu.streams.{Evals, Temporal}
import tofu.syntax.logging._
import tofu.syntax.monadic._
import tofu.syntax.lift._
import tofu.syntax.streams.all._
import cats.syntax.foldable._
import cats.syntax.traverse._
import cats.syntax.parallel._

trait BlocksProcess[S[_]] {
  def run: S[Unit]
}

object BlocksProcess {

  def make[
    I[_]: Monad,
    F[_]: Monad: BlocksProcessConfig.Has: Parallel,
    S[_]: Evals[*[_], F]: Temporal[*[_], C]: Monad,
    C[_]: Functor: Foldable
  ](implicit
    events: BlocksConsumer[S, F],
    snapshots: Snapshots[F],
    volumes24H: Volumes24H[F],
    assets: Assets[F],
    network: Network[F],
    caching: HttpResponseCaching[F],
    lift: Lift[F, I],
    logs: Logs[I, F]
  ): I[BlocksProcess[S]] = logs.forService[BlocksProcess[S]].flatMap { implicit __ =>
    for {
      assetsL <- assets.update.lift[I]
      _       <- snapshots.update(assetsL).lift[I]
      _       <- volumes24H.update(assetsL).lift[I]
      height  <- network.getCurrentNetworkHeight.lift[I]
    } yield new Live[F, S, C](height)
  }

  final private class Live[
    F[_]: Monad: Logging: BlocksProcessConfig.Has: Parallel,
    S[_]: Evals[*[_], F]: Temporal[*[_], C]: Monad,
    C[_]: Functor: Foldable
  ](height: Int)(implicit
    events: BlocksConsumer[S, F],
    snapshots: Snapshots[F],
    volumes24H: Volumes24H[F],
    assets: Assets[F],
    caching: HttpResponseCaching[F]
  ) extends BlocksProcess[S] {

    def run: S[Unit] =
      eval(BlocksProcessConfig.access).flatMap { config =>
        events.stream
          .groupWithin(config.blocksBatchSize, config.blocksGroupTime)
          .evalMap { batch =>
            for {
              _ <- info"Got next block event: ${batch.toList.map(_.message.map(_.id))}"
              _ <- Monad[F].whenA(batch.toList.lastOption.flatMap(_.message).exists(_.height > height)) {
                     assets.update.flatMap { assets =>
                       List(snapshots.update(assets), volumes24H.update(assets)).parSequence
                     }.void >> caching.invalidateAll
                   }
              _ <- batch.toList.lastOption.traverse(_.commit)
              blocks       = batch.toList.map(_.message.map(_.id))
              latestHeight = batch.toList.lastOption.flatMap(_.message).map(_.height)
              _ <- info"Block $blocks processed successfully. height is: $latestHeight. Height on app start is: $height"
            } yield ()
          }
      }
  }
}
