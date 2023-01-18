package fi.spectrum.graphite

import cats.effect.Resource
import cats.effect.syntax.resource._
import cats.syntax.show._
import cats.tagless.implicits.toFunctorKOps
import cats.{Apply, Monad}
import fs2.io.net.Network
import tofu.higherKind.{Mid, RepresentableK}
import tofu.lift.Lift
import tofu.logging.Logging
import tofu.syntax.logging._
import tofu.syntax.monadic._

trait GraphiteClient[F[_]] {

  def send(point: GraphitePoint): F[Unit]
}

object GraphiteClient {

  implicit val representableK: RepresentableK[GraphiteClient] =
    tofu.higherKind.derived.genRepresentableK

  def make[F[_]: Lift[*[_], D]: Network, D[_]: Monad](settings: GraphiteSettings)(implicit
    logs: Logging.Make[D]
  ): Resource[F, GraphiteClient[D]] =
    Client
      .make[F](settings)
      .map(_.mapK(Lift[F, D].liftF))
      .map { client =>
        implicit val __ = logs.forService[GraphiteClient[D]]
        new GraphiteClientTracing[D] attach new Live[D](client, settings.prefix)
      }

  final private class Live[F[_]: Monad](
    client: Client[F],
    prefix: String
  ) extends GraphiteClient[F] {

    def send(point: GraphitePoint): F[Unit] =
      client.send(
        point
          .transformation(prefix)
          .format
          .getBytes(Encoding)
      )
  }

  final private class GraphiteClientTracing[F[_]: Apply: Logging] extends GraphiteClient[Mid[F, *]] {

    def send(point: GraphitePoint): Mid[F, Unit] =
      trace"Sending graphite point: ${point.show}" *> _
  }

  private val Encoding = "UTF-8"
}
