package fi.spectrum.core.domain

import derevo.circe.{decoder, encoder}
import derevo.derive
import doobie.Get
import doobie.util.Put
import fi.spectrum.core.domain.order.PoolId
import io.estatico.newtype.macros.newtype

package object pool {

  @derive(encoder, decoder)
  @newtype final case class PoolBoxId(id: BoxId)

  object PoolBoxId {
    implicit val get: Get[PoolBoxId] = deriving
    implicit val put: Put[PoolBoxId] = deriving
  }
}
