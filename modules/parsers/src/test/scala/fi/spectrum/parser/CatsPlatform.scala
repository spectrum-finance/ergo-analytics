package fi.spectrum.parser

import cats.Id
import cats.effect.{Clock, IO, IOApp}
import tofu.logging.{Logging, Logs}

trait CatsPlatform extends IOApp.Simple {
  override def run: IO[Unit] = IO.unit
  implicit val clockIO: Clock[IO] = Clock[IO]
  implicit val logs: Logging.Make[IO] = Logging.Make.plain[IO]
}
