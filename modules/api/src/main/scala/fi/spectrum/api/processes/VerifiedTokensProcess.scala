package fi.spectrum.api.processes

import cats.Monad
import cats.effect.Temporal
import fi.spectrum.api.configs.NetworkConfig
import fi.spectrum.api.services.VerifiedTokens
import fi.spectrum.graphite.Metrics
import tofu.Catches
import tofu.lift.Lift
import tofu.logging.{Logging, Logs}
import tofu.streams.Evals
import tofu.syntax.lift._
import tofu.syntax.logging._
import tofu.syntax.handle._
import tofu.syntax.monadic._
import tofu.syntax.streams.evals._
import scala.concurrent.duration.FiniteDuration

trait VerifiedTokensProcess[S[_]] {
  def run: S[Unit]
}

object VerifiedTokensProcess {

  def make[I[_]: Monad, F[_]: Temporal: NetworkConfig.Has, S[_]: Monad: Evals[*[_], F]: Catches](implicit
    verifiedTokens: VerifiedTokens[F],
    metrics: Metrics[F],
    logs: Logs[I, F],
    lift: Lift[F, I]
  ): I[VerifiedTokensProcess[S]] =
    for {
      implicit0(logging: Logging[F]) <- logs.forService[VerifiedTokensProcess[F]]
      config                         <- NetworkConfig.access.lift[I]
    } yield new Live[S, F](config.verifiedTokenListRequestTime)

  final private class Live[S[_]: Monad: Evals[*[_], F]: Catches, F[_]: Temporal: Logging](
    requestTime: FiniteDuration
  )(implicit
    verifiedTokens: VerifiedTokens[F],
    metrics: Metrics[F]
  ) extends VerifiedTokensProcess[S] {

    def run: S[Unit] = (eval {
      for {
        _ <- verifiedTokens.update
        _ <- metrics.sendCount("fetch.verified", 1.0)
        _ <- info"Going to sleep $requestTime"
        _ <- Temporal[F].sleep(requestTime)
      } yield ()
    } >> run).handleWith { err: Throwable =>
      eval(
        warn"The error ${err.getMessage} occurred in VerifiedTokensProcess stream. Going to restore process.." >>
        metrics.sendCount("warn.verified.tokens.process", 1.0)
      ) >> run
    }
  }
}
