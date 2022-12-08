package fi.spectrum.core.domain.order

import derevo.circe.{decoder, encoder}
import derevo.derive
import io.circe.derivation.deriveEncoder
import io.circe.{Decoder, Encoder}
import io.circe.syntax._
import io.circe.parser.parse
import cats.syntax.functor._
import cats.syntax.either._

object Test extends App {

  sealed trait Y

  sealed trait YImpl extends Y

  implicit val encoderY: Encoder[YImpl] = Encoder[String].contramap(s => "test")

  implicit val decoderY: Decoder[YImpl] = Decoder[String].emap {
    case "test" => new YImpl {}.asRight
    case r      => s"Invalid: $r".asLeft
  }

  sealed trait YImpl2 extends Y

  implicit val encoderY2: Encoder[YImpl2] = Encoder[String].contramap(s => "test2")

  implicit val decoderY2: Decoder[YImpl2] = Decoder[String].emap {
    case "test2" => new YImpl2 {}.asRight
    case r       => s"Invalid: $r".asLeft
  }

  sealed trait X[+O <: Y] {
    val o: O
  }

  implicit val encoderX: Encoder[X[Y]] = {
    case x: XYImpl  => x.asJson
    case x: XYImpl2 => x.asJson
  }

  implicit def decoderX: Decoder[X[Y]] =
    List[Decoder[X[Y]]](
      Decoder[XYImpl].widen,
      Decoder[XYImpl2].widen
    ).reduceLeft(_ or _)

  @derive(encoder, decoder)
  final case class XYImpl(o: YImpl) extends X[YImpl]

  @derive(encoder, decoder)
  final case class XYImpl2(o: YImpl2) extends X[YImpl2]

  val a = XYImpl(new YImpl {}).asJson
  val a1 = XYImpl2(new YImpl2 {}).asJson

  println(a)

  val b = a.as[XYImpl2]

  println(b)

  println(a.as[X[Y]])
  println(a1.as[X[Y]])

}
