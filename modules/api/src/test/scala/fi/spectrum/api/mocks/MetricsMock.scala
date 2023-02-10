package fi.spectrum.api.mocks

import cats.Applicative
import cats.effect.IO
import fi.spectrum.graphite.Metrics

object MetricsMock {

  def make[F[_]: Applicative]: Metrics[F] = new Metrics[F] {
    def sendTs(key: String, value: Double): F[Unit] = Applicative[F].unit

    def sendCount(key: String, value: Double): F[Unit] = Applicative[F].unit
  }

}
