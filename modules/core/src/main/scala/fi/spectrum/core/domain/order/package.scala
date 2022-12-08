package fi.spectrum.core.domain

import derevo.circe.{decoder, encoder}
import derevo.derive
import io.estatico.newtype.macros.newtype

package object order {

  @derive(encoder, decoder)
  @newtype final case class OrderId(value: String)

  object OrderId {}

  @derive(encoder, decoder)
  @newtype final case class PoolId(value: TokenId)

  object PoolId {

    def fromBytes(bytes: Array[Byte]): PoolId =
      PoolId(TokenId.fromBytes(bytes))
  }
}
