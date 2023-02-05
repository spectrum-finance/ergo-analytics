package fi.spectrum.core.domain.analytics

import derevo.circe.{decoder, encoder}
import derevo.derive
import fi.spectrum.core.domain.order.Fee.ERG
import fi.spectrum.core.domain.order.{Fee, OrderId}
import fi.spectrum.core.domain.{AssetAmount, TxId}
import glass.classic.Prism
import tofu.logging.derivation.loggable

@derive(encoder, decoder, loggable)
sealed trait OrderEvaluation { self =>

  def widen[O <: OrderEvaluation](implicit prism: Prism[OrderEvaluation, O]): Option[O] =
    prism.getOption(self)
}

object OrderEvaluation {

  @derive(encoder, decoder, loggable)
  final case class SwapEvaluation(output: AssetAmount, fee: Fee) extends OrderEvaluation

  object SwapEvaluation {
    def emptyFee(output: AssetAmount): SwapEvaluation = SwapEvaluation(output, ERG(0))
  }

  @derive(encoder, decoder, loggable)
  final case class DepositEvaluation(outputLP: AssetAmount, actualX: Long, actualY: Long)
    extends OrderEvaluation

  @derive(encoder, decoder, loggable)
  final case class RedeemEvaluation(outputX: AssetAmount, outputY: AssetAmount) extends OrderEvaluation

  @derive(encoder, decoder, loggable)
  final case class LockEvaluation(orderId: OrderId) extends OrderEvaluation
}
