package fi.spectrum.parser.evaluation

import cats.syntax.option._
import fi.spectrum.core.domain.analytics.OrderEvaluation.SwapEvaluation
import fi.spectrum.core.domain.analytics.Version.{LegacyV1, V1, V2}
import fi.spectrum.core.domain.analytics.{OffChainFee, OrderEvaluation, Processed}
import fi.spectrum.core.domain.order.Fee.{ERG, SPF}
import fi.spectrum.core.domain.order.Order.{Deposit, Lock, Redeem, Swap}
import fi.spectrum.core.domain.order.OrderOptics._
import fi.spectrum.core.domain.order.Redeemer.{ErgoTreeRedeemer, PublicKeyRedeemer}
import fi.spectrum.core.domain.order.{Order, PoolId, SwapParams}
import fi.spectrum.core.domain.transaction.Output
import fi.spectrum.core.domain.{ErgoTreeTemplate, PubKey}
import fi.spectrum.core.protocol.ErgoTreeSerializer
import fi.spectrum.parser.api.predefinedErgoTrees
import glass.classic.Optional
import org.ergoplatform.{ErgoAddressEncoder, P2PKAddress}

/** Tries to find output for off-chain operator.
  */
trait OffChainFeeParser {

  def parse(
    outputs: List[Output],
    order: Processed.Any,
    eval: Option[OrderEvaluation],
    poolId: PoolId
  ): Option[OffChainFee]
}

object OffChainFeeParser {

  implicit def make(implicit e: ErgoAddressEncoder): OffChainFeeParser = new Live

  final private class Live(implicit e: ErgoAddressEncoder) extends OffChainFeeParser {

    def parse(
      outputs: List[Output],
      order: Processed.Any,
      eval: Option[OrderEvaluation],
      poolId: PoolId
    ): Option[OffChainFee] =
      outputs
        .map { box =>
          val tree     = ErgoTreeSerializer.default.deserialize(box.ergoTree)
          val template = ErgoTreeTemplate.fromBytes(tree.template)
          val address  = e.fromProposition(tree).toOption

          def orderRedeemer = order.order.redeemer match {
            case ErgoTreeRedeemer(value) => value == box.ergoTree
            case PublicKeyRedeemer(value) =>
              val treePubKey = ErgoTreeSerializer.default.deserialize(value.ergoTree)
              e.fromProposition(treePubKey).toOption.map(e.toString) == address.map(e.toString)
          }

          val (feeOpt, needCalculate) = order.order match {
            case deposit: Deposit                                => deposit.fee.some -> false
            case redeem: Redeem                                  => redeem.fee.some  -> false
            case swap: Swap if swap.version.in(LegacyV1, V1, V2) => ERG(0).some      -> false
            case _: Swap                                         => SPF(0).some      -> true
            case _: Lock                                         => none             -> false
          }

          val pkOpt = address.collect { case address: P2PKAddress => PubKey.fromBytes(address.pubkeyBytes) }

          (feeOpt, pkOpt) match {
            case (Some(s @ SPF(_)), Some(pk)) if !orderRedeemer && !predefinedErgoTrees.contains(template) =>
              def fee: Option[SPF] =
                if (needCalculate) eval.flatMap(_.widen[SwapEvaluation]).flatMap { eval =>
                  Optional[Order, SwapParams].getOption(order.order).map { params =>
                    SPF(BigInt(params.dexFeePerTokenNum) / params.dexFeePerTokenDenom * eval.output.amount)
                  }
                }
                else s.some
              fee.map(OffChainFee(poolId, order.order.id, box.boxId, pk, _))
            case (Some(ERG(_)), Some(pk)) if !orderRedeemer && !predefinedErgoTrees.contains(template) =>
              OffChainFee(poolId, order.order.id, box.boxId, pk, ERG(box.value)).some
            case _ => none
          }
        }
        .collectFirst { case Some(value) => value }
  }
}
