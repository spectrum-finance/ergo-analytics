package fi.spectrum.api.processes

import cats.Monad
import cats.effect.{Ref, Temporal}
import fi.spectrum.api.configs.NetworkConfig
import fi.spectrum.api.services.Network
import fi.spectrum.core.domain.TokenId
import fi.spectrum.graphite.Metrics
import tofu.lift.Lift
import tofu.logging.{Logging, Logs}
import tofu.streams.Evals
import tofu.syntax.lift._
import tofu.syntax.logging._
import tofu.syntax.monadic._
import tofu.syntax.streams.evals._

import scala.concurrent.duration.FiniteDuration

trait VerifiedTokensProcess[S[_]] {
  def run: S[Unit]
}

object VerifiedTokensProcess {

  def make[I[_]: Monad, F[_]: Temporal: NetworkConfig.Has, S[_]: Monad: Evals[*[_], F]](implicit
    network: Network[F],
    metrics: Metrics[F],
    cache: Ref[F, List[TokenId]],
    logs: Logs[I, F],
    lift: Lift[F, I]
  ): I[VerifiedTokensProcess[S]] =
    for {
      implicit0(logging: Logging[F]) <- logs.forService[VerifiedTokensProcess[F]]
      config                         <- NetworkConfig.access.lift[I]
      tokens                         <- network.getVerifiedTokenList.lift[I]
      _                              <- cache.set(tokens).lift[I]
    } yield new Live[S, F](config.verifiedTokenListRequestTime)

  final private class Live[S[_]: Monad: Evals[*[_], F], F[_]: Temporal: Logging](requestTime: FiniteDuration)(implicit
    network: Network[F],
    metrics: Metrics[F],
    cache: Ref[F, List[TokenId]]
  ) extends VerifiedTokensProcess[S] {

    def run: S[Unit] = eval {
      for {
        _      <- info"It's time to update verified tokens list"
        tokens <- network.getVerifiedTokenList
        _      <- cache.set(tokens)
        _      <- metrics.sendCount("fetch.verified", 1.0)
        _      <- info"Going to sleep $requestTime"
        _      <- Temporal[F].sleep(requestTime)
      } yield ()
    } >> run
  }
}
