package fi.spectrum.core.domain.analytics

import cats.{Eq, Show}
import cats.syntax.either._
import cats.syntax.eq._
import cats.syntax.functor._
import cats.syntax.show._
import doobie.util.{Get, Put}
import io.circe.syntax._
import io.circe.{Decoder, Encoder}
import tofu.logging.Loggable

sealed trait Version

object Version {
  sealed trait V1 extends Version
  sealed trait V2 extends Version
  sealed trait V3 extends Version

  sealed trait LegacyV1 extends Version
  sealed trait LegacyV2 extends Version

  implicit val v1Show: Show[V1] = _ => "v1"
  implicit val v2Show: Show[V2] = _ => "v2"
  implicit val v3Show: Show[V3] = _ => "v3"

  implicit val legacyV1Show: Show[LegacyV1] = _ => "legacyV1"
  implicit val legacyV2Show: Show[LegacyV2] = _ => "legacyV2"

  implicit val v1Encoder: Encoder[V1] = Encoder[String].contramap(_ => make.v1.show)
  implicit val v2Encoder: Encoder[V2] = Encoder[String].contramap(_ => make.v2.show)
  implicit val v3Encoder: Encoder[V3] = Encoder[String].contramap(_ => make.v3.show)

  implicit val legacyV1Encoder: Encoder[LegacyV1] = Encoder[String].contramap(_ => make.legacyV1.show)
  implicit val legacyV2Encoder: Encoder[LegacyV2] = Encoder[String].contramap(_ => make.legacyV2.show)

  def anyDecoder[R: Show](r: R): Decoder[R] = Decoder[String].emap {
    case result if result === r.show => r.asRight
    case invalid                     => s"Invalid version ${r.show} decoder: $invalid".asLeft
  }

  implicit val v1Decoder: Decoder[V1]             = anyDecoder[V1](make.v1)
  implicit val v2Decoder: Decoder[V2]             = anyDecoder[V2](make.v2)
  implicit val v3Decoder: Decoder[V3]             = anyDecoder[V3](make.v3)
  implicit val legacyV1Decoder: Decoder[LegacyV1] = anyDecoder[LegacyV1](make.legacyV1)
  implicit val legacyV2Decoder: Decoder[LegacyV2] = anyDecoder[LegacyV2](make.legacyV2)

  implicit val orderTypeEncoder: Encoder[Version] = {
    case v1: V1             => v1.asJson
    case v2: V2             => v2.asJson
    case v3: V3             => v3.asJson
    case legacyV1: LegacyV1 => legacyV1.asJson
    case legacyV2: LegacyV2 => legacyV2.asJson
  }

  implicit val orderTypeDecoder: Decoder[Version] =
    List[Decoder[Version]](
      Decoder[V1].widen,
      Decoder[V2].widen,
      Decoder[V3].widen,
      Decoder[LegacyV1].widen,
      Decoder[LegacyV2].widen
    ).reduceLeft(_ or _)

  object make {
    def v3: V3             = new V3 {}
    def v2: V2             = new V2 {}
    def v1: V1             = new V1 {}
    def legacyV2: LegacyV2 = new LegacyV2 {}
    def legacyV1: LegacyV1 = new LegacyV1 {}
  }

  implicit def put: Put[Version] = Put[String].contramap {
    case _: V1       => make.v1.show
    case _: V2       => make.v2.show
    case _: V3       => make.v3.show
    case _: LegacyV1 => make.legacyV1.show
    case _: LegacyV2 => make.legacyV2.show
  }

  implicit def get: Get[Version] = Get[String].temap {
    case s if s == make.v1.show       => make.v1.asRight[String]
    case s if s == make.v2.show       => make.v2.asRight[String]
    case s if s == make.v3.show       => make.v3.asRight[String]
    case s if s == make.legacyV1.show => make.legacyV1.asRight[String]
    case s if s == make.legacyV2.show => make.legacyV2.asRight[String]
    case err                          => s"Invalid contract version: $err".asLeft[Version]
  }

  implicit val loggableV1: Loggable[V1]             = Loggable.show
  implicit val loggableV2: Loggable[V2]             = Loggable.show
  implicit val loggableV3: Loggable[V3]             = Loggable.show
  implicit val loggableLegacyV1: Loggable[LegacyV1] = Loggable.show
  implicit val loggableLegacyV2: Loggable[LegacyV2] = Loggable.show

  implicit val eqV1: Eq[V1]             = (x: V1, y: V1) => x.show === y.show
  implicit val eqV2: Eq[V2]             = (x: V2, y: V2) => x.show === y.show
  implicit val eqV3: Eq[V3]             = (x: V3, y: V3) => x.show === y.show
  implicit val eqLegacyV1: Eq[LegacyV1] = (x: LegacyV1, y: LegacyV1) => x.show === y.show
  implicit val eqLegacyV2: Eq[LegacyV2] = (x: LegacyV2, y: LegacyV2) => x.show === y.show
}
