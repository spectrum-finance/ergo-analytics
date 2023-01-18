package fi.spectrum.indexer.mocks

import cats.Applicative
import fi.spectrum.graphite.Metrics
import tofu.syntax.monadic._

object MetricsMock {

  def make[F[_]: Applicative]: Metrics[F] = new Metrics[F] {
    def sendTs(key: String, value: Double): F[Unit] = unit

    def sendCount(key: String, value: Double): F[Unit] = unit
  }
}
