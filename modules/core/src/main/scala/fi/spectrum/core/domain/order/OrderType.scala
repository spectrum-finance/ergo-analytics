package fi.spectrum.core.domain.order

import cats.{Eq, Show}
import cats.syntax.either._
import cats.syntax.eq._
import cats.syntax.functor._
import cats.syntax.show._
import io.circe.syntax._
import io.circe.{Decoder, Encoder}
import tofu.logging.Loggable

/** Represents order type ,e.g. AMM, LOCK, LM
  */
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

  def anyDecoder[R: Show](r: R): Decoder[R] = Decoder[String].emap {
    case result if result === r.show => r.asRight
    case invalid                     => s"Invalid order type ${r.show} decoder: $invalid".asLeft
  }

  implicit val ammDecoder: Decoder[AMM]   = anyDecoder[AMM](make.amm)
  implicit val lockDecoder: Decoder[LOCK] = anyDecoder[LOCK](make.lock)
  implicit val lmDecoder: Decoder[LM]     = anyDecoder[LM](make.lm)

  implicit val orderTypeEncoder: Encoder[OrderType] = {
    case amm: AMM   => amm.asJson
    case lock: LOCK => lock.asJson
    case lm: LM     => lm.asJson
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

  implicit val loggableAMM: Loggable[AMM]   = Loggable.show
  implicit val loggableLOCK: Loggable[LOCK] = Loggable.show
  implicit val loggableLM: Loggable[LM]     = Loggable.show

  implicit val eqAMM: Eq[AMM]   = (x: AMM, y: AMM) => x.show === y.show
  implicit val eqLOCK: Eq[LOCK] = (x: LOCK, y: LOCK) => x.show === y.show
  implicit val eqLM: Eq[LM]     = (x: LM, y: LM) => x.show === y.show
}
