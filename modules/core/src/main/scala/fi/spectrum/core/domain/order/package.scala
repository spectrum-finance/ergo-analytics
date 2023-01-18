package fi.spectrum.core.domain

import derevo.circe.{decoder, encoder}
import derevo.derive
import doobie.{Get, Put}
import io.estatico.newtype.macros.newtype
import scodec.bits.ByteVector
import tofu.logging.derivation.{loggable, show}

package object order {

  @derive(encoder, decoder, loggable, show)
  @newtype final case class OrderId(value: String)

  object OrderId {
    implicit val get: Get[OrderId] = deriving
    implicit val put: Put[OrderId] = deriving

    implicit def codec: scodec.Codec[OrderId] =
      scodec.codecs
        .bytes(64)
        .xmap(xs => OrderId(new String(xs.toArray)), pid => ByteVector(pid.value.getBytes()))
  }

  @derive(encoder, decoder, loggable, show)
  @newtype final case class PoolId(value: TokenId)

  object PoolId {

    implicit val get: Get[PoolId] = deriving
    implicit val put: Put[PoolId] = deriving

    def unsafeFromString(s: String): PoolId = PoolId(TokenId.unsafeFromString(s))

    def fromBytes(bytes: Array[Byte]): PoolId =
      PoolId(TokenId.fromBytes(bytes))
  }
}
