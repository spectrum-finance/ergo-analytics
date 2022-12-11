package fi.spectrum.core

import derevo.circe.{decoder, encoder}
import derevo.derive
import doobie.util.{Get, Put}
import eu.timepit.refined.api.Refined
import eu.timepit.refined.refineV
import eu.timepit.refined.string.HexStringSpec
import fi.spectrum.core.domain.TypeConstraints.{AddressType, Base58Spec, HexString}
import fi.spectrum.core.syntax.PubKeyOps
import io.circe.refined._
import io.estatico.newtype.macros.newtype
import scorex.util.encode.Base16

package object domain {

  @derive(encoder, decoder)
  @newtype final case class ProtocolVersion(value: Int)

  object ProtocolVersion {
    implicit val get: Get[ProtocolVersion] = deriving
    implicit val put: Put[ProtocolVersion] = deriving
  }

  @derive(encoder, decoder)
  @newtype final case class TxId(value: String)

  object TxId {
    implicit val get: Get[TxId] = deriving
    implicit val put: Put[TxId] = deriving
  }

  @derive(encoder, decoder)
  @newtype final case class BoxId(value: String)

  object BoxId {
    implicit val get: Get[BoxId] = deriving
    implicit val put: Put[BoxId] = deriving
  }

  @derive(encoder, decoder)
  @newtype final case class SErgoTree(value: HexString) {
    def toBytea: Array[Byte] = Base16.decode(value.value).get
  }

  object SErgoTree {

    implicit val get: Get[SErgoTree] =
      Get[String]
        .temap(s => refineV[HexStringSpec](s))
        .map(rs => SErgoTree(rs))

    implicit val put: Put[SErgoTree] =
      Put[String].contramap[SErgoTree](_.value.value)

    def unsafeFromString(s: String): SErgoTree = SErgoTree(Refined.unsafeApply(s))

    def fromBytes(bytes: Array[Byte]): SErgoTree = SErgoTree(
      Refined.unsafeApply(scorex.util.encode.Base16.encode(bytes))
    )
  }

  @derive(encoder, decoder)
  @newtype final case class TokenId(value: HexString)

  object TokenId {

    implicit val get: Get[TokenId] =
      Get[String]
        .temap(s => refineV[HexStringSpec](s))
        .map(rs => TokenId(rs))

    implicit val put: Put[TokenId] =
      Put[String].contramap[TokenId](_.value.value)

    def fromBytes(bytes: Array[Byte]): TokenId =
      TokenId(Refined.unsafeApply(scorex.util.encode.Base16.encode(bytes)))
  }

  @derive(encoder, decoder)
  @newtype final case class PubKey(value: HexString) {
    def toBytes: Array[Byte] = Base16.decode(value.value).get

    def ergoTree: SErgoTree = SErgoTree.fromBytes(PubKey(value).toErgoTree.bytes)
  }

  object PubKey {

    implicit val get: Get[PubKey] =
      Get[String]
        .temap(s => refineV[HexStringSpec](s))
        .map(rs => PubKey(rs))

    implicit val put: Put[PubKey] =
      Put[String].contramap[PubKey](_.value.value)

    def unsafeFromString(s: String): PubKey = PubKey(Refined.unsafeApply(s))

    def fromBytes(bytes: Array[Byte]): PubKey =
      PubKey(Refined.unsafeApply(scorex.util.encode.Base16.encode(bytes)))

  }

  @newtype case class ErgoTreeTemplate(value: HexString) {
    def toBytes: Array[Byte] = Base16.decode(value.value).get
  }

  object ErgoTreeTemplate {

    def fromBytes(bytes: Array[Byte]): ErgoTreeTemplate = ErgoTreeTemplate(
      Refined.unsafeApply(scorex.util.encode.Base16.encode(bytes))
    )
    def unsafeFromString(s: String): ErgoTreeTemplate = ErgoTreeTemplate(Refined.unsafeApply(s))
  }

  @derive(encoder, decoder)
  @newtype case class Address(value: AddressType)

  object Address {

    def fromStringUnsafe(s: String): Address =
      Address(refineV[Base58Spec].unsafeFrom(s))
  }
}
