package fi.spectrum.parser

import cats.Show
import derevo.circe.{decoder, encoder}
import derevo.derive
import doobie.util.{Get, Put}
import io.circe.{Decoder, Encoder}
import io.estatico.newtype.macros.newtype
import tofu.logging.Loggable

package object models {
  @derive(encoder, decoder)
  @newtype case class TokenType(value: String)

  object TokenType

  @derive(encoder, decoder)
  @newtype case class BlockId(value: String)

  object BlockId
}
