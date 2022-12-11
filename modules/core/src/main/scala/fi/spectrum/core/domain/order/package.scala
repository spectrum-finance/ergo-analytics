package fi.spectrum.core.domain

import derevo.circe.{decoder, encoder}
import derevo.derive
import doobie.{Get, Put}
import fi.spectrum.core.domain.order.OrderId
import io.estatico.newtype.macros.newtype

package object order {

  @derive(encoder, decoder)
  @newtype final case class OrderId(value: String)

  object OrderId {
    implicit val get: Get[OrderId] = deriving
    implicit val put: Put[OrderId] = deriving
  }

  @derive(encoder, decoder)
  @newtype final case class PoolId(value: TokenId)

  object PoolId {

    implicit val get: Get[PoolId] = deriving
    implicit val put: Put[PoolId] = deriving

    def fromBytes(bytes: Array[Byte]): PoolId =
      PoolId(TokenId.fromBytes(bytes))
  }
}
