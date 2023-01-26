package fi.spectrum.api.processes

import cats.Monad
import cats.effect.{Ref, Temporal}
import fi.spectrum.api.services.Network
import fi.spectrum.core.domain.TokenId
import tofu.lift.Lift
import tofu.logging.{Logging, Logs}
import tofu.streams.Evals
import tofu.syntax.logging._
import tofu.syntax.monadic._
import tofu.syntax.streams.evals.eval

import scala.concurrent.duration.{DurationInt, FiniteDuration}

trait VerifiedTokensProcess[S[_]] {
  def run: S[Unit]
}

object VerifiedTokensProcess {

  val MemoTtl: FiniteDuration = 480.minutes

  def make[I[_]: Monad, F[_]: Temporal, S[_]: Monad: Evals[*[_], F]](implicit
    network: Network[F],
    cache: Ref[F, List[TokenId]],
    logs: Logs[I, F],
    lift: Lift[F, I]
  ): I[VerifiedTokensProcess[S]] =
    for {
      implicit0(logging: Logging[F]) <- logs.forService[VerifiedTokensProcess[S]]
      tokens                         <- lift.lift(network.getVerifiedTokenList)
      _                              <- lift.lift(cache.set(tokens))
    } yield new Live[S, F]

  final private class Live[S[_]: Monad: Evals[*[_], F], F[_]: Temporal: Logging](implicit
    network: Network[F],
    cache: Ref[F, List[TokenId]]
  ) extends VerifiedTokensProcess[S] {

    def run: S[Unit] =
      eval {
        for {
          tokens <- network.getVerifiedTokenList
          _      <- cache.set(tokens)
          _      <- info"Going to sleep $MemoTtl"
          _      <- Temporal[F].sleep(MemoTtl)
        } yield ()
      } >> run
  }
}
