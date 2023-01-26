package fi.spectrum.api.processes

import cats.Monad
import cats.effect.{Ref, Temporal}
import fi.spectrum.api.services.Network
import tofu.lift.Lift
import tofu.logging.{Logging, Logs}
import tofu.streams.Evals
import tofu.syntax.logging._
import tofu.syntax.monadic._
import tofu.syntax.streams.evals.eval

import scala.concurrent.duration.{DurationInt, FiniteDuration}

trait ErgPriceProcess[S[_]] {
  def run: S[Unit]
}

object ErgPriceProcess {

  val MemoTtl: FiniteDuration = 2.minutes

  def make[I[_]: Monad, F[_]: Temporal, S[_]: Monad: Evals[*[_], F]](implicit
    network: Network[F],
    cache: Ref[F, Option[BigDecimal]],
    logs: Logs[I, F],
    lift: Lift[F, I]
  ): I[ErgPriceProcess[S]] =
    for {
      implicit0(logging: Logging[F]) <- logs.forService[ErgPriceProcess[S]]
      rate                           <- lift.lift(network.getErgPriceCMC)
      _                              <- lift.lift(cache.set(rate))
    } yield new Live[S, F]

  final private class Live[S[_]: Monad: Evals[*[_], F], F[_]: Temporal: Logging](implicit
    network: Network[F],
    cache: Ref[F, Option[BigDecimal]]
  ) extends ErgPriceProcess[S] {

    def run: S[Unit] =
      eval {
        for {
          rate <- network.getErgPriceCMC
          _    <- cache.set(rate)
          _    <- info"Going to sleep $MemoTtl"
          _    <- Temporal[F].sleep(MemoTtl)
        } yield ()
      } >> run
  }
}
