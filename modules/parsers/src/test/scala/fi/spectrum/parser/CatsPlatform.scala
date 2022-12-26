package fi.spectrum.parser

import cats.effect.{Clock, IO, IOApp}

trait CatsPlatform extends IOApp.Simple {
  override def run: IO[Unit] = IO.unit
  implicit val clockIO: Clock[IO] = Clock[IO]
}
