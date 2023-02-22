package fi.spectrum.api.v1.endpoints.models

import cats.Monoid
import cats.syntax.semigroup._
import cats.syntax.option._
import derevo.circe.{decoder, encoder}
import derevo.derive
import sttp.tapir.Schema
import tofu.logging.derivation.loggable

@derive(encoder, decoder, loggable)
final case class TimeWindow(from: Option[Long], to: Option[Long])

object TimeWindow {

  def apply(from: Long, to: Long): TimeWindow = TimeWindow(from.some, to.some)

  val empty: TimeWindow = TimeWindow(None, None)

  implicit val monoid: Monoid[TimeWindow] =
    Monoid.instance(empty, (w0, w1) => w0.copy(w0.to |+| w1.to, w0.from |+| w1.from))

  implicit val schema: Schema[TimeWindow] = Schema.derived
}
