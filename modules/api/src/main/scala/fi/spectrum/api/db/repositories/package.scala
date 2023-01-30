package fi.spectrum.api.db

import cats.Monad
import fi.spectrum.graphite.Metrics
import tofu.syntax.time.now.millis
import tofu.time.Clock
import tofu.syntax.monadic._

package object repositories {

  def processMetric[F[_]: Clock: Monad, A](f: F[A], key: String)(implicit metrics: Metrics[F]): F[A] =
    for {
      start  <- millis[F]
      r      <- f
      finish <- millis[F]
      _      <- metrics.sendTs(key, (finish - start).toDouble)
      _      <- metrics.sendCount(key, 1)
    } yield r
}
