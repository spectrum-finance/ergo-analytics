package fi.spectrum.api.processes

import cats.Monad
import cats.effect.Temporal
import cats.syntax.traverse._
import fi.spectrum.api.configs.NetworkConfig
import fi.spectrum.api.services.ErgRate
import fi.spectrum.graphite.Metrics
import tofu.Catches
import tofu.lift.Lift
import tofu.logging.{Logging, Logs}
import tofu.streams.Evals
import tofu.syntax.lift._
import tofu.syntax.logging._
import tofu.syntax.monadic._
import tofu.syntax.handle._
import tofu.syntax.streams.all.eval
import tofu.syntax.streams.evals._
import scala.concurrent.duration.FiniteDuration

trait ErgPriceProcess[S[_]] {
  def run: S[Unit]
}

object ErgPriceProcess {

  def make[I[_]: Monad, F[_]: Temporal: NetworkConfig.Has, S[_]: Monad: Evals[*[_], F]: Catches](implicit
    ergRate: ErgRate[F],
    metrics: Metrics[F],
    logs: Logs[I, F],
    lift: Lift[F, I]
  ): I[ErgPriceProcess[S]] =
    for {
      implicit0(logging: Logging[F]) <- logs.forService[ErgPriceProcess[F]]
      config                         <- NetworkConfig.access.lift[I]
      _                              <- ergRate.update.lift[I]
    } yield new Live[S, F](config.cmcRequestTime)

  final private class Live[S[_]: Monad: Evals[*[_], F]: Catches, F[_]: Temporal: Logging](
    requestTime: FiniteDuration
  )(implicit
    ergRate: ErgRate[F],
    metrics: Metrics[F]
  ) extends ErgPriceProcess[S] {

    def run: S[Unit] = (eval {
      for {
        rate <- ergRate.update
        _    <- metrics.sendCount("fetch.erg.price", 1.0)
        _    <- rate.traverse(r => metrics.sendCount("fetch.erg.price", r.toDouble))
        _    <- info"Going to sleep $requestTime"
        _    <- Temporal[F].sleep(requestTime)
      } yield ()
    } >> run).handleWith { err: Throwable =>
      eval(
        warn"The error ${err.getMessage} occurred in ErgPriceProcess stream. Going to restore process.." >>
        metrics.sendCount("warn.erg.price.process", 1.0)
      ) >> run
    }
  }
}
