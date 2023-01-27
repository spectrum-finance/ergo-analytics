package fi.spectrum.api.processes

import cats.Monad
import cats.effect.{Ref, Temporal}
import cats.syntax.traverse._
import fi.spectrum.api.configs.NetworkConfig
import fi.spectrum.api.services.Network
import fi.spectrum.graphite.Metrics
import tofu.lift.Lift
import tofu.logging.{Logging, Logs}
import tofu.syntax.lift._
import tofu.syntax.logging._
import tofu.syntax.monadic._

import scala.concurrent.duration.FiniteDuration

trait ErgPriceProcess[S[_]] {
  def run: S[Unit]
}

object ErgPriceProcess {

  def make[I[_]: Monad, F[_]: Temporal: NetworkConfig.Has](implicit
    network: Network[F],
    metrics: Metrics[F],
    cache: Ref[F, Option[BigDecimal]],
    logs: Logs[I, F],
    lift: Lift[F, I]
  ): I[ErgPriceProcess[F]] =
    for {
      implicit0(logging: Logging[F]) <- logs.forService[ErgPriceProcess[F]]
      config                         <- NetworkConfig.access.lift
      rate                           <- network.getErgPriceCMC.lift
      _                              <- cache.set(rate).lift
    } yield new Live[F](config.cmcRequestTime)

  final private class Live[F[_]: Temporal: Logging](requestTime: FiniteDuration)(implicit
    network: Network[F],
    metrics: Metrics[F],
    cache: Ref[F, Option[BigDecimal]]
  ) extends ErgPriceProcess[F] {

    def run: F[Unit] = {
      for {
        _    <- info"It's time to request new ERG price!"
        rate <- network.getErgPriceCMC
        _    <- cache.set(rate)
        _    <- metrics.sendCount("fetch.erg.price", 1.0)
        _    <- rate.traverse(r => metrics.sendCount("fetch.erg.price", r.toDouble))
        _    <- info"Going to sleep $requestTime"
        _    <- Temporal[F].sleep(requestTime)
      } yield ()
    } >> run
  }
}
