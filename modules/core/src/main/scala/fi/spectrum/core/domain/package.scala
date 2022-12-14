package fi.spectrum.core

import cats.{Eq, Show}

import derevo.circe.{decoder, encoder}
import derevo.derive
import doobie.util.{Get, Put}
import eu.timepit.refined.api.Refined
import eu.timepit.refined.refineV
import eu.timepit.refined.string.HexStringSpec
import fi.spectrum.core.domain.TypeConstraints.{AddressType, Base58Spec, HexStringType}
import fi.spectrum.core.syntax.PubKeyOps
import io.circe.refined._
import io.circe.{Decoder, Encoder}
import io.estatico.newtype.macros.newtype
import scorex.util.encode.Base16
import tofu.logging.Loggable
import tofu.logging.derivation.{loggable, show}
import cats.syntax.eq._

package object domain {

  @newtype case class HexString(value: HexStringType) {
    final def unwrapped: String = value.value

    final def toBytes: Array[Byte] = Base16.decode(unwrapped).get
  }

  object HexString {
    // circe instances
    implicit val encoder: Encoder[HexString] = deriving
    implicit val decoder: Decoder[HexString] = deriving

    implicit val show: Show[HexString]         = _.unwrapped
    implicit val loggable: Loggable[HexString] = Loggable.show

    implicit val eq: Eq[HexString] = (x: HexString, y: HexString) => x.value.value === y.value.value

    implicit val get: Get[HexString] =
      Get[String]
        .temap(s => refineV[HexStringSpec](s))
        .map(rs => HexString(rs))

    implicit val put: Put[HexString] =
      Put[String].contramap[HexString](_.unwrapped)

    def fromBytes(bytes: Array[Byte]): HexString =
      unsafeFromString(scorex.util.encode.Base16.encode(bytes))

    def unsafeFromString(s: String): HexString = HexString(Refined.unsafeApply(s))
  }

  @derive(encoder, decoder, loggable, show)
  @newtype final case class ProtocolVersion(value: Int)

  object ProtocolVersion {
    implicit val get: Get[ProtocolVersion] = deriving
    implicit val put: Put[ProtocolVersion] = deriving
  }

  @derive(encoder, decoder, loggable, show)
  @newtype final case class TxId(value: String)

  object TxId {
    implicit val get: Get[TxId] = deriving
    implicit val put: Put[TxId] = deriving
  }

  @derive(encoder, decoder, loggable, show)
  @newtype final case class BoxId(value: String)

  object BoxId {
    implicit val get: Get[BoxId] = deriving
    implicit val put: Put[BoxId] = deriving
  }

  @derive(encoder, decoder, loggable, show)
  @newtype final case class SErgoTree(value: HexString) {
    def toBytea: Array[Byte] = value.toBytes
  }

  object SErgoTree {

    implicit val get: Get[SErgoTree] = deriving

    implicit val put: Put[SErgoTree] = deriving

    def unsafeFromString(s: String): SErgoTree = SErgoTree(HexString.unsafeFromString(s))

    def fromBytes(bytes: Array[Byte]): SErgoTree = SErgoTree(
      HexString.fromBytes(bytes)
    )
  }

  @derive(encoder, decoder, loggable, show)
  @newtype final case class TokenId(value: HexString)

  object TokenId {

    implicit val get: Get[TokenId] = deriving

    implicit val put: Put[TokenId] = deriving

    def fromBytes(bytes: Array[Byte]): TokenId = TokenId(HexString.fromBytes(bytes))

    def unsafeFromString(s: String): TokenId = TokenId(HexString.unsafeFromString(s))

  }

  @derive(encoder, decoder, loggable, show)
  @newtype final case class PubKey(value: HexString) {
    def toBytes: Array[Byte] = value.toBytes

    def ergoTree: SErgoTree = SErgoTree.fromBytes(PubKey(value).toErgoTree.bytes)
  }

  object PubKey {

    implicit val get: Get[PubKey] = deriving

    implicit val put: Put[PubKey] = deriving

    def unsafeFromString(s: String): PubKey = PubKey(HexString.unsafeFromString(s))

    def fromBytes(bytes: Array[Byte]): PubKey = PubKey(HexString.fromBytes(bytes))

  }

  @newtype case class ErgoTreeTemplate(value: HexString) {
    def toBytes: Array[Byte] = value.toBytes
  }

  object ErgoTreeTemplate {

    def fromBytes(bytes: Array[Byte]): ErgoTreeTemplate = ErgoTreeTemplate(
      HexString.fromBytes(bytes)
    )
    def unsafeFromString(s: String): ErgoTreeTemplate = ErgoTreeTemplate(HexString.unsafeFromString(s))
  }

  @derive(encoder, decoder)
  @newtype case class Address(value: AddressType)

  object Address {

    implicit val show: Show[Address]         = _.value.value
    implicit val loggable: Loggable[Address] = Loggable.show

    def fromStringUnsafe(s: String): Address =
      Address(refineV[Base58Spec].unsafeFrom(s))
  }
}
