package fi.spectrum.api.processes

import cats.Monad
import fi.spectrum.api.services.{Network, Snapshots, Volumes24H}
import fi.spectrum.cache.middleware.HttpResponseCaching
import fi.spectrum.streaming.kafka.BlocksConsumer
import tofu.lift.Lift
import tofu.logging.{Logging, Logs}
import tofu.streams.Evals
import tofu.syntax.logging._
import tofu.syntax.monadic._
import tofu.syntax.lift._
import tofu.syntax.streams.all._

trait BlocksProcess[S[_]] {
  def run: S[Unit]
}

object BlocksProcess {

  def make[
    I[_]: Monad,
    F[_]: Monad,
    S[_]: Evals[*[_], F]
  ](implicit
    events: BlocksConsumer[S, F],
    snapshots: Snapshots[F],
    volumes24H: Volumes24H[F],
    network: Network[F],
    caching: HttpResponseCaching[F],
    lift: Lift[F, I],
    logs: Logs[I, F]
  ): I[BlocksProcess[S]] = logs.forService[BlocksProcess[S]].flatMap { implicit __ =>
    network.getCurrentNetworkHeight.lift[I].map { height =>
      new Live[F, S](height)
    }
  }

  final private class Live[
    F[_]: Monad: Logging,
    S[_]: Evals[*[_], F]
  ](height: Int)(implicit
    events: BlocksConsumer[S, F],
    snapshots: Snapshots[F],
    volumes24H: Volumes24H[F],
    caching: HttpResponseCaching[F]
  ) extends BlocksProcess[S] {

    def run: S[Unit] =
      events.stream.evalMap { event =>
        for {
          _ <- info"Got next block event: ${event.message.map(_.id)}"
          _ <- Monad[F].whenA(event.message.exists(_.height > height))(
                 snapshots.update >>
                 volumes24H.update >>
                 caching.invalidateAll
               )
          _ <- event.commit
          _ <- info"Block ${event.message.map(_.id)} processed successfully"
        } yield ()
      }
  }
}
