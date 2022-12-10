package fi.spectrum.core.domain.pool

import cats.Show
import io.circe.{Decoder, Encoder}
import cats.syntax.show._
import cats.syntax.functor._
import cats.syntax.eq._
import cats.syntax.either._
import io.circe.syntax._

sealed trait PoolType

object PoolType {
  sealed trait AMM extends PoolType

  sealed trait LM extends PoolType

  implicit val ammShow: Show[AMM] = _ => "amm"
  implicit val lmShow: Show[LM]   = _ => "lm"

  implicit val ammEncoder: Encoder[AMM] = Encoder[String].contramap(_ => make.amm.show)
  implicit val lmEncoder: Encoder[LM]   = Encoder[String].contramap(_ => make.lm.show)

  def anyDecoder[R: Show](r: R): Decoder[R] = Decoder[String].emap {
    case result if result === r.show => r.asRight
    case invalid                     => s"Invalid order type ${r.show} decoder: $invalid".asLeft
  }

  implicit val ammDecoder: Decoder[AMM] = anyDecoder[AMM](make.amm)
  implicit val lmDecoder: Decoder[LM]   = anyDecoder[LM](make.lm)

  implicit val poolTypeEncoder: Encoder[PoolType] = {
    case amm: AMM => amm.asJson
    case lm: LM   => lm.asJson
  }

  implicit val orderTypeDecoder: Decoder[PoolType] =
    List[Decoder[PoolType]](
      Decoder[AMM].widen,
      Decoder[LM].widen
    ).reduceLeft(_ or _)

  object make {
    def amm: AMM = new AMM {}

    def lm: LM = new LM {}
  }
}
