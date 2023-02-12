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

  @derive(loggable, show)
  final case class IntsConstant(value: List[Int]) extends SConstant

  implicit val encoder: Encoder[SConstant] = { c =>
    val (renderedValue, sigmaType: SigmaType) = c match {
      case IntConstant(value)       => value.toString                    -> SInt
      case LongConstant(value)      => value.toString                    -> SLong
      case ByteaConstant(value)     => value.value.value                 -> SCollection(SByte)
      case IntsConstant(value)      => "[" ++ value.mkString(",") ++ "]" -> SCollection(SInt)
      case SigmaPropConstant(value) => value.value.value.value           -> SSigmaProp
      case UnresolvedConstant(raw)  => raw                               -> SAny
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
        case SCollection(SInt)  => parseSInt(value)
        case _                  => UnresolvedConstant(value)
      }
    }
  }

  def fromRenderValue(sType: SigmaType, value: String): SConstant =
    sType match {
      case SInt               => IntConstant(value.toInt)
      case SLong              => LongConstant(value.toLong)
      case SSigmaProp         => SigmaPropConstant(PubKey.unsafeFromString(value))
      case SCollection(SByte) => ByteaConstant(HexString.unsafeFromString(value))
      case SCollection(SInt)  => parseSInt(value)
      case _                  => UnresolvedConstant(value)
    }

  private def parseSInt(value: String): IntsConstant = {
    val split      = value.split(",")
    val splitHead  = split.headOption.map(_.drop(1)).getOrElse("")
    val splitTail  = split.lastOption.map(_.dropRight(1)).getOrElse("")
    val splitTotal = split.drop(1).dropRight(1).prepended(splitHead).appended(splitTail).toList
    IntsConstant(List.from(splitTotal).map(_.toInt))
  }
}
