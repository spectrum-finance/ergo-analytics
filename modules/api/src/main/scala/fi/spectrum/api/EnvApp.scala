package fi.spectrum.api

import cats.data.ReaderT
import cats.effect.{IO, IOApp}
import fs2.Stream
import tofu.lift.{IsoK, Unlift}
import tofu.logging.{Loggable, LoggableContext, Logging, Logs}
import tofu.{Delay, WithRun}
import zio.Task
import zio.interop.catz._

abstract class EnvApp[C: Loggable] extends IOApp {

  type I[+A] = IO[A]
  type F[A]  = ReaderT[I, C, A]
  type S[+A] = Stream[F, A]

  implicit def logs: Logs[I, F]                    = Logs.withContext[I, F]
  implicit def logsInit: Logs[I, I]                = Logs.sync[I, I]
  implicit def logsRun: Logs[F, F]                 = Logs.sync[F, F]
  implicit def loggableContext: LoggableContext[F] = LoggableContext.of[F].instance[C]

  val wr: WithRun[F, I, C] = implicitly

  implicit def logsMakeF[F[_]: Delay]: Logging.Make[F] = Logging.Make.plain[F]

  def isoKRunByContext(ctx: C): IsoK[F, I] = IsoK.byFunK(wr.runContextK(ctx))(wr.liftF)

  implicit val unlift: Unlift[I, F] = wr
  implicit val isoK: IsoK[S, S]     = IsoK.id[S]
}
