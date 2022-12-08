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

  def anyDecoder[R: Show](r: R): Decoder[R] = Decoder[String].emap {
    case result if result === r.show => r.asRight
    case invalid                     => s"Invalid operation ${r.show} decoder: $invalid".asLeft
  }

  implicit val swapDecoder: Decoder[Swap] = anyDecoder(make.swap)
  implicit val depositDecoder: Decoder[Deposit] = anyDecoder(make.deposit)
  implicit val redeemDecoder: Decoder[Redeem] = anyDecoder(make.redeem)
  implicit val lockDecoder: Decoder[Lock] = anyDecoder(make.lock)

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
