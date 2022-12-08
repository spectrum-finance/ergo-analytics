package fi.spectrum.core.domain.order

import cats.Show
import cats.syntax.either._
import cats.syntax.eq._
import cats.syntax.functor._
import cats.syntax.show._
import io.circe.syntax._
import io.circe.{Decoder, Encoder}

sealed trait Operation

object Operation {

  sealed trait Swap extends Operation
  sealed trait Deposit extends Operation
  sealed trait Redeem extends Operation
  sealed trait Lock extends Operation

  implicit val swapShow: Show[Swap]       = _ => "swap"
  implicit val depositShow: Show[Deposit] = _ => "deposit"
  implicit val redeemShow: Show[Redeem]   = _ => "redeem"
  implicit val lockShow: Show[Lock]       = _ => "lock"

  implicit val swapEncoder: Encoder[Swap]       = Encoder[String].contramap(_ => make.swap.show)
  implicit val depositEncoder: Encoder[Deposit] = Encoder[String].contramap(_ => make.deposit.show)
  implicit val redeemEncoder: Encoder[Redeem]   = Encoder[String].contramap(_ => make.redeem.show)
  implicit val lockEncoder: Encoder[Lock]       = Encoder[String].contramap(_ => make.lock.show)

  implicit val swapDecoder: Decoder[Swap] = Decoder[String].emap {
    case swap if swap === make.swap.show => make.swap.asRight
    case invalid                         => s"Invalid swap operation decoder: $invalid".asLeft
  }

  implicit val depositDecoder: Decoder[Deposit] = Decoder[String].emap {
    case deposit if deposit === make.deposit.show => make.deposit.asRight
    case invalid                                  => s"Invalid deposit operation decoder: $invalid".asLeft
  }

  implicit val redeemDecoder: Decoder[Redeem] = Decoder[String].emap {
    case redeem if redeem === make.redeem.show => make.redeem.asRight
    case invalid                               => s"Invalid redeem operation decoder: $invalid".asLeft
  }

  implicit val lockDecoder: Decoder[Lock] = Decoder[String].emap {
    case lock if lock === make.lock.show => make.lock.asRight
    case invalid                         => s"Invalid lock operation decoder: $invalid".asLeft
  }

  implicit val operationEncoder: Encoder[Operation] = {
    case swap: Swap       => swap.asJson
    case redeem: Redeem   => redeem.asJson
    case deposit: Deposit => deposit.asJson
    case lock: Lock       => lock.asJson
  }

  implicit val operationDecoder: Decoder[Operation] =
    List[Decoder[Operation]](
      Decoder[Swap].widen,
      Decoder[Redeem].widen,
      Decoder[Deposit].widen,
      Decoder[Lock].widen
    ).reduceLeft(_ or _)

  object make {
    def deposit: Deposit = new Deposit {}

    def redeem: Redeem = new Redeem {}

    def swap: Swap = new Swap {}

    def lock: Lock = new Lock {}
  }
}
