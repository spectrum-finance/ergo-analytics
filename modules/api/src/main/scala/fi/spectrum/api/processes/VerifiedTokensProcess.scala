package fi.spectrum.api.processes

import cats.Monad
import cats.effect.{Ref, Temporal}
import fi.spectrum.api.configs.NetworkConfig
import fi.spectrum.api.services.Network
import fi.spectrum.core.domain.TokenId
import fi.spectrum.graphite.Metrics
import tofu.lift.Lift
import tofu.logging.{Logging, Logs}
import tofu.syntax.lift._
import tofu.syntax.logging._
import tofu.syntax.monadic._

import scala.concurrent.duration.FiniteDuration

trait VerifiedTokensProcess[F[_]] {
  def run: F[Unit]
}

object VerifiedTokensProcess {

  def make[I[_]: Monad, F[_]: Temporal: NetworkConfig.Has](implicit
    network: Network[F],
    metrics: Metrics[F],
    cache: Ref[F, List[TokenId]],
    logs: Logs[I, F],
    lift: Lift[F, I]
  ): I[VerifiedTokensProcess[F]] =
    for {
      implicit0(logging: Logging[F]) <- logs.forService[VerifiedTokensProcess[F]]
      config                         <- NetworkConfig.access.lift
      tokens                         <- network.getVerifiedTokenList.lift
      _                              <- cache.set(tokens).lift
    } yield new Live[F](config.verifiedTokenListRequestTime)

  final private class Live[F[_]: Temporal: Logging](requestTime: FiniteDuration)(implicit
    network: Network[F],
    metrics: Metrics[F],
    cache: Ref[F, List[TokenId]]
  ) extends VerifiedTokensProcess[F] {

    def run: F[Unit] = {
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
