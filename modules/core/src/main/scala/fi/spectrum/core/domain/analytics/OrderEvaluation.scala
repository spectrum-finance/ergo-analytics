package fi.spectrum.core.domain.analytics

import derevo.circe.{decoder, encoder}
import derevo.derive
import fi.spectrum.core.domain.order.Fee.ERG
import fi.spectrum.core.domain.order.Order.Compound
import fi.spectrum.core.domain.order.{Fee, OrderId, PoolId}
import fi.spectrum.core.domain.{AssetAmount, BoxId}
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
  final case class AmmDepositEvaluation(outputLP: AssetAmount, actualX: Long, actualY: Long) extends OrderEvaluation

  @derive(encoder, decoder, loggable)
  final case class AmmRedeemEvaluation(outputX: AssetAmount, outputY: AssetAmount) extends OrderEvaluation

  @derive(encoder, decoder, loggable)
  final case class LockEvaluation(orderId: OrderId) extends OrderEvaluation

  @derive(encoder, decoder, loggable)
  final case class LmDepositCompoundEvaluation(tokens: AssetAmount, boxId: BoxId, bundle: Compound)
    extends OrderEvaluation

  @derive(encoder, decoder, loggable)
  final case class LmRedeemEvaluation(out: AssetAmount, boxId: BoxId, poolId: PoolId)
    extends OrderEvaluation

}
