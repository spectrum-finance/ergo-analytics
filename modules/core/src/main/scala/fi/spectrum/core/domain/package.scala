package fi.spectrum.core

import cats.instances.either._
import cats.syntax.either._
import cats.syntax.eq._
import cats.syntax.functor._
import cats.{Applicative, Eq, Order, Show}
import derevo.circe.{decoder, encoder}
import derevo.derive
import derevo.pureconfig.pureconfigReader
import doobie.util.{Get, Put}
import eu.timepit.refined.api.Refined
import eu.timepit.refined.refineV
import eu.timepit.refined.string.HexStringSpec
import fi.spectrum.core.common.errors.RefinementFailed
import fi.spectrum.core.domain.TypeConstraints.{AddressType, Base58Spec, HexStringType}
import fi.spectrum.core.syntax.PubKeyOps
import io.circe.refined._
import io.circe.{Decoder, Encoder}
import io.estatico.newtype.macros.newtype
import org.ergoplatform.ErgoBox
import pureconfig.ConfigReader
import scodec.bits.ByteVector
import scorex.util.encode.Base16
import sttp.tapir.{Codec, CodecFormat, DecodeResult, Schema, Validator}
import tofu.Raise
import tofu.logging.Loggable
import tofu.logging.derivation.{loggable, show}
import tofu.syntax.raise._

import scala.util.Try

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

    implicit val configReader: ConfigReader[HexString] = ConfigReader.stringConfigReader.map(unsafeFromString)

    implicit val order: Order[HexString] = (x: HexString, y: HexString) => x.value.value.compare(y.value.value)

    implicit def schema: Schema[HexString] =
      Schema.schemaForString.description("Hex-encoded string").asInstanceOf[Schema[HexString]]

    implicit def validator: Validator[HexString] =
      Schema.schemaForString.validator.contramap[HexString](_.unwrapped)

    implicit def plainCodec: Codec.PlainCodec[HexString] =
      deriveCodec[String, CodecFormat.TextPlain, HexString](
        fromString[Either[Throwable, *]](_),
        _.unwrapped
      )

    def fromBytes(bytes: Array[Byte]): HexString =
      unsafeFromString(scorex.util.encode.Base16.encode(bytes))

    def fromString[F[_]: Raise[*[_], RefinementFailed]: Applicative](
      s: String
    ): F[HexString] =
      refineV[HexStringSpec](s)
        .leftMap(RefinementFailed)
        .toRaise[F]
        .map(HexString.apply)

    def unsafeFromString(s: String): HexString = HexString(Refined.unsafeApply(s))
  }

  @derive(encoder, decoder, loggable, show)
  @newtype final case class ProtocolVersion(value: Int)

  object ProtocolVersion {

    val init = ProtocolVersion(1)

    implicit val get: Get[ProtocolVersion] = deriving
    implicit val put: Put[ProtocolVersion] = deriving
  }

  @derive(encoder, decoder, loggable, show)
  @newtype final case class TxId(value: String)

  object TxId {
    implicit val get: Get[TxId] = deriving
    implicit val put: Put[TxId] = deriving

    implicit def codec: scodec.Codec[TxId] =
      scodec.codecs
        .bytes(32)
        .xmap(xs => TxId(new String(xs.toArray)), pid => ByteVector(pid.value.getBytes()))
  }

  @derive(encoder, decoder, loggable, show)
  @newtype final case class BoxId(value: String)

  object BoxId {
    implicit val get: Get[BoxId] = deriving
    implicit val put: Put[BoxId] = deriving

    def fromErgo(ergoBoxId: ErgoBox.BoxId): BoxId =
      BoxId(Base16.encode(ergoBoxId))

    implicit def codec: scodec.Codec[BoxId] =
      scodec.codecs
        .bytes(64)
        .xmap(xs => BoxId(new String(xs.toArray)), pid => ByteVector(pid.value.getBytes()))
  }

  @derive(encoder, decoder, loggable, show)
  @newtype case class BlockId(value: String)

  object BlockId {
    implicit val get: Get[BlockId] = deriving
    implicit val put: Put[BlockId] = deriving
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

  @derive(encoder, decoder, loggable, show, pureconfigReader)
  @newtype final case class TokenId(value: HexString) {
    def unwrapped: String = value.unwrapped
  }

  object TokenId {

    implicit val get: Get[TokenId] = deriving

    implicit val put: Put[TokenId] = deriving

    def fromBytes(bytes: Array[Byte]): TokenId = TokenId(HexString.fromBytes(bytes))

    def unsafeFromString(s: String): TokenId = TokenId(HexString.unsafeFromString(s))

    implicit val order: Order[TokenId] = deriving

    implicit def plainCodec: Codec.PlainCodec[TokenId] = deriving

    implicit def validator: Validator[TokenId] =
      implicitly[Validator[HexString]].contramap[TokenId](_.value)

    implicit def schema: Schema[TokenId] =
      Schema.schemaForString.description("Token ID").asInstanceOf[Schema[TokenId]]
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
  @newtype case class Address(value: AddressType) {
    final def unwrapped: String = value.value
  }

  object Address {

    implicit val schema: Schema[Address] = Schema.schemaForString.map(fromString[Try](_).toOption)(_.unwrapped)

    implicit val show: Show[Address]         = _.value.value
    implicit val loggable: Loggable[Address] = Loggable.show

    implicit val get: Get[Address] =
      Get[String]
        .temap(s => refineV[Base58Spec](s))
        .map(rs => Address(rs))

    implicit val put: Put[Address] =
      Put[String].contramap[Address](_.unwrapped)

    def fromString[F[_]: Raise[*[_], RefinementFailed]: Applicative](
      s: String
    ): F[Address] =
      refineV[Base58Spec](s)
        .leftMap(RefinementFailed)
        .toRaise[F]
        .map(Address.apply)

    def fromStringUnsafe(s: String): Address =
      Address(refineV[Base58Spec].unsafeFrom(s))
  }

  private def deriveCodec[A, CF <: CodecFormat, T](
    at: A => Either[Throwable, T],
    ta: T => A
  )(implicit c: Codec[String, A, CF]): Codec[String, T, CF] =
    c.mapDecode { x =>
      at(x).fold(DecodeResult.Error(x.toString, _), DecodeResult.Value(_))
    }(ta)
}
