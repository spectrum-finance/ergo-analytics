package fi.spectrum.core.domain.order

import cats.Show
import cats.syntax.either._
import cats.syntax.eq._
import cats.syntax.functor._
import cats.syntax.show._
import io.circe.syntax._
import io.circe.{Decoder, Encoder}

sealed trait OrderType

object OrderType {
  sealed trait AMM extends OrderType
  sealed trait LOCK extends OrderType
  sealed trait LM extends OrderType

  implicit val ammShow: Show[AMM]   = _ => "amm"
  implicit val lockShow: Show[LOCK] = _ => "lock"
  implicit val lmShow: Show[LM]     = _ => "lm"

  implicit val ammEncoder: Encoder[AMM]   = Encoder[String].contramap(_ => make.amm.show)
  implicit val lockEncoder: Encoder[LOCK] = Encoder[String].contramap(_ => make.lock.show)
  implicit val lmEncoder: Encoder[LM]     = Encoder[String].contramap(_ => make.lm.show)

  implicit val ammDecoder: Decoder[AMM] = Decoder[String].emap {
    case amm if amm === make.amm.show => make.amm.asRight
    case invalid                      => s"Invalid amm type decoder: $invalid".asLeft
  }

  implicit val lockDecoder: Decoder[LOCK] = Decoder[String].emap {
    case lock if lock === make.lock.show => make.lock.asRight
    case invalid                         => s"Invalid lock type decoder: $invalid".asLeft
  }

  implicit val lmDecoder: Decoder[LM] = Decoder[String].emap {
    case lm if lm === make.lm.show => make.lm.asRight
    case invalid                   => s"Invalid lm type decoder: $invalid".asLeft
  }

  implicit val orderTypeEncoder: Encoder[OrderType] = {
    case amm: AMM => amm.asJson
    case lock: LOCK => lock.asJson
    case lm: LM => lm.asJson
  }

  implicit val orderTypeDecoder: Decoder[OrderType] =
    List[Decoder[OrderType]](
      Decoder[AMM].widen,
      Decoder[LOCK].widen,
      Decoder[LM].widen
    ).reduceLeft(_ or _)

  object make {
    def amm: AMM   = new AMM {}
    def lock: LOCK = new LOCK {}
    def lm: LM     = new LM {}
  }
}
