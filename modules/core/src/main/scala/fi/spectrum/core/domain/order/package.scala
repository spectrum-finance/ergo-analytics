package fi.spectrum.core.domain

import derevo.circe.{decoder, encoder}
import derevo.derive
import doobie.{Get, Put}
import io.estatico.newtype.macros.newtype
import scodec.bits.ByteVector
import sttp.tapir.{Codec, Schema, Validator}
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

  @derive(show, loggable, encoder, decoder)
  @newtype final case class PoolId(value: TokenId) {
    def unwrapped: String = value.unwrapped
  }

  object PoolId {

    implicit val get: Get[PoolId] = deriving
    implicit val put: Put[PoolId] = deriving

    implicit def plainCodec: Codec.PlainCodec[PoolId] = deriving

    implicit def codec: scodec.Codec[PoolId] =
      scodec.codecs
        .bytes(32)
        .xmap(xs => fromBytes(xs.toArray), pid => ByteVector(pid.value.value.toBytes))

    implicit def schema: Schema[PoolId] =
      Schema.schemaForString.description("Pool ID").asInstanceOf[Schema[PoolId]]

    implicit def validator: Validator[PoolId] =
      implicitly[Validator[TokenId]].contramap[PoolId](_.value)

    def unsafeFromString(s: String): PoolId = PoolId(TokenId.unsafeFromString(s))

    def fromBytes(bytes: Array[Byte]): PoolId =
      PoolId(TokenId.fromBytes(bytes))
  }
}
