package fi.spectrum.graphite

import cats.Monad
import derevo.derive
import tofu.Catches
import tofu.higherKind.Mid
import tofu.higherKind.derived.representableK
import tofu.logging.Logging
import tofu.syntax.handle._
import tofu.syntax.monadic._
import tofu.syntax.logging._

@derive(representableK)
trait Metrics[F[_]] {
  def sendTs(key: String, value: Double): F[Unit]
  def sendCount(key: String, value: Double): F[Unit]
}

object Metrics {

  def make[F[_]: Catches: Monad](implicit
    client: GraphiteClient[F],
    logs: Logging.Make[F]
  ): Metrics[F] =
    logs.forService[Metrics[F]].map { implicit log =>
      new Errors[F] attach new Metrics[F] {
        def sendTs(key: String, value: Double): F[Unit] =
          client.send(GraphitePoint.GraphitePointTs(key, value))

        def sendCount(key: String, value: Double): F[Unit] =
          client.send(GraphitePoint.GraphitePointCount(key, value))
      }
    }

  final private class Errors[F[_]: Catches: Logging] extends Metrics[Mid[F, *]] {

    def sendTs(key: String, value: Double): Mid[F, Unit] =
      _.handleWith[Throwable](err => error"While sending ts metrics error ${err.getMessage} has occurred.")

    def sendCount(key: String, value: Double): Mid[F, Unit] =
      _.handleWith[Throwable](err => error"While sending count metrics error ${err.getMessage} has occurred.")
  }
}
