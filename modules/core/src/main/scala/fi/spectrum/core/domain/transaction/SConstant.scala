package fi.spectrum.core.domain.transaction

import derevo.circe.encoder
import derevo.derive
import eu.timepit.refined.api.Refined
import fi.spectrum.core.domain.{HexString, PubKey}
import fi.spectrum.core.protocol.SigmaType
import fi.spectrum.core.protocol.SigmaType.SCollection
import fi.spectrum.core.protocol.SigmaType.SimpleKindSigmaType._
import io.circe.Decoder
import io.circe.refined._
import tofu.logging.derivation.{loggable, show}

@derive(encoder, loggable, show)
sealed trait SConstant

object SConstant {

  @derive(encoder, loggable, show)
  final case class IntConstant(value: Int) extends SConstant

  @derive(encoder, loggable, show)
  final case class LongConstant(value: Long) extends SConstant

  @derive(encoder, loggable, show)
  final case class ByteaConstant(value: HexString) extends SConstant

  @derive(encoder, loggable, show)
  final case class SigmaPropConstant(value: PubKey) extends SConstant

  @derive(encoder, loggable, show)
  final case class UnresolvedConstant(raw: String) extends SConstant

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
