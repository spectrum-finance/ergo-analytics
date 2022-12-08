package fi.spectrum.core

import derevo.circe.{decoder, encoder}
import derevo.derive
import fi.spectrum.core.domain.TypeConstraints.HexString
import io.circe.refined._
import io.estatico.newtype.macros.newtype

package object domain {

  @derive(encoder, decoder)
  @newtype final case class TxId(value: String)

  object TxId {}

  @derive(encoder, decoder)
  @newtype final case class BoxId(value: String)

  object BoxId {}

  @derive(encoder, decoder)
  @newtype final case class SErgoTree(value: HexString)

  object SErgoTree {}

  @derive(encoder, decoder)
  @newtype final case class TokenId(value: HexString)

  object TokenId {}

  @derive(encoder, decoder)
  @newtype final case class PubKey(value: HexString)

  object PubKey {}
}
