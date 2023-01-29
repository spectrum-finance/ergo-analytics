package fi.spectrum.api.models

import cats.Show
import derevo.circe.{decoder, encoder}
import derevo.derive
import sttp.tapir.Schema
import tofu.logging.derivation.loggable

@derive(encoder, decoder, loggable)
final case class Currency(id: CurrencyId, decimals: Int)

object Currency {
  implicit val schema: Schema[Currency] = Schema.derived

  implicit val show: Show[Currency] =
    Show.show(c => s"Currency{id=${c.id}, decimals=${c.decimals}]")
}
