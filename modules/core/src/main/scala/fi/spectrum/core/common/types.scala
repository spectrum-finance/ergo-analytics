package fi.spectrum.core.common

import cats.Show
import doobie.util.{Get, Put}
import io.circe.{Decoder, Encoder}
import io.estatico.newtype.macros.newtype
import scodec.Codec
import scodec.codecs.{uint16, utf8, variableSizeBits}
import sttp.tapir.{Schema, Validator}
import tofu.logging.Loggable
import derevo.cats.show
import derevo.circe.{decoder, encoder}
import derevo.derive
import tofu.logging.derivation.loggable

object types {

  @derive(show, encoder, decoder, loggable)
  @newtype case class CurrencyId(value: String)

  object CurrencyId {
    implicit val schema: Schema[CurrencyId] = deriving
  }

  @newtype
  final case class Ticker(value: String)

  object Ticker {

    implicit val get: Get[Ticker] = deriving
    implicit val put: Put[Ticker] = deriving

    implicit val encoder: Encoder[Ticker] = deriving
    implicit val decoder: Decoder[Ticker] = deriving

    implicit val show: Show[Ticker]         = _.value
    implicit val loggable: Loggable[Ticker] = Loggable.show

    implicit val codec: Codec[Ticker] = variableSizeBits(uint16, utf8).xmap[Ticker](Ticker(_), _.value)

    implicit val schema: Schema[Ticker]       = deriving
    implicit val validator: Validator[Ticker] = schema.validator
  }
}
