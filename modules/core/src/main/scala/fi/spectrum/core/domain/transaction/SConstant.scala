package fi.spectrum.core.domain.transaction

import derevo.derive
import fi.spectrum.core.domain.{HexString, PubKey}
import fi.spectrum.core.protocol.SigmaType
import fi.spectrum.core.protocol.SigmaType.SCollection
import fi.spectrum.core.protocol.SigmaType.SimpleKindSigmaType._
import io.circe.{Decoder, Encoder, Json}
import io.circe.refined._
import io.circe.syntax._
import tofu.logging.derivation.{loggable, show}

@derive(loggable, show)
sealed trait SConstant

object SConstant {

  @derive(loggable, show)
  final case class IntConstant(value: Int) extends SConstant

  @derive(loggable, show)
  final case class LongConstant(value: Long) extends SConstant

  @derive(loggable, show)
  final case class ByteaConstant(value: HexString) extends SConstant

  @derive(loggable, show)
  final case class SigmaPropConstant(value: PubKey) extends SConstant

  @derive(loggable, show)
  final case class UnresolvedConstant(raw: String) extends SConstant

  implicit val encoder: Encoder[SConstant] = { c =>
    val (renderedValue, sigmaType: SigmaType) = c match {
      case IntConstant(value)       => value.toString          -> SInt
      case LongConstant(value)      => value.toString          -> SLong
      case ByteaConstant(value)     => value.value.value       -> SCollection(SByte)
      case SigmaPropConstant(value) => value.value.value.value -> SSigmaProp
      case UnresolvedConstant(raw)  => raw                     -> SAny
    }
    Json.obj("renderedValue" -> Json.fromString(renderedValue), "sigmaType" -> sigmaType.asJson)
  }

  implicit val decoder: Decoder[SConstant] = { c =>
    c.downField("renderedValue").as[String].flatMap { value =>
      c.downField("sigmaType").as[SigmaType].map {
        case SInt               => IntConstant(value.toInt)
        case SLong              => LongConstant(value.toLong)
        case SSigmaProp         => SigmaPropConstant(PubKey.unsafeFromString(value))
        case SCollection(SByte) => ByteaConstant(HexString.unsafeFromString(value))
        case _                  => UnresolvedConstant(value)
      }
    }
  }
}
