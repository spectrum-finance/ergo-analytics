package fi.spectrum.parser.evaluation

import cats.syntax.option._
import fi.spectrum.core.domain.analytics.OrderEvaluation._
import fi.spectrum.core.domain.analytics.Version.{LegacyV1, LegacyV2, V1, V2}
import fi.spectrum.core.domain.analytics.{OffChainFee, OrderEvaluation, Processed}
import fi.spectrum.core.domain.order.Fee.{ERG, SPF}
import fi.spectrum.core.domain.order.Order.Deposit.{AmmDeposit, LmDeposit}
import fi.spectrum.core.domain.order.Order.Redeem.{AmmRedeem, LmRedeem}
import fi.spectrum.core.domain.order.Order.{Compound, Lock, Swap}
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
            case deposit: AmmDeposit                                       => deposit.fee.some -> false
            case redeem: AmmRedeem                                         => redeem.fee.some  -> false
            case swap: Swap if swap.version.in(LegacyV2, LegacyV1, V1, V2) => ERG(0).some      -> true
            case _: Swap                                                   => SPF(0).some      -> true
            case _: Lock                                                   => none             -> false
            case _: LmDeposit                                              => none             -> true
            case _: LmRedeem                                               => none             -> true
            case _: Compound                                               => none             -> true
          }

          val pkOpt = address.collect { case address: P2PKAddress => PubKey.fromBytes(address.pubkeyBytes) }

          (feeOpt, pkOpt) match {
            case (Some(s @ SPF(_)), Some(pk)) if !orderRedeemer && !predefinedErgoTrees.contains(template) =>
              def fee: Option[SPF] =
                if (needCalculate) eval.flatMap(_.widen[SwapEvaluation]).flatMap { eval =>
                  Optional[Order, SwapParams].getOption(order.order).map { params =>
                    val fee = BigDecimal(params.dexFeePerTokenNum) / params.dexFeePerTokenDenom * eval.output.amount
                    SPF(fee.toLong)
                  }
                }
                else s.some

              fee.map(OffChainFee(poolId, order.order.id, box.boxId, pk, _))
            case (Some(ERG(erg)), Some(pk)) if !orderRedeemer && !predefinedErgoTrees.contains(template) =>
              def fee: Option[ERG] =
                if (needCalculate) eval.flatMap(_.widen[SwapEvaluation]).flatMap { eval =>
                  Optional[Order, SwapParams].getOption(order.order).map { params =>
                    val fee = BigDecimal(params.dexFeePerTokenNum) / params.dexFeePerTokenDenom * eval.output.amount
                    ERG(fee.toLong)
                  }
                }
                else ERG(erg).some
              fee.map(OffChainFee(poolId, order.order.id, box.boxId, pk, _))
            case (_, Some(pk)) if needCalculate && !orderRedeemer && !predefinedErgoTrees.contains(template) =>
              def validDeposit: Boolean = order.wined[LmDeposit].exists {
                _.evaluation.flatMap(_.widen[LmDepositCompoundEvaluation]).map(_.boxId).contains(box.boxId)
              }

              def validCompound: Boolean = order.wined[Compound].exists {
                _.evaluation.flatMap(_.widen[LmDepositCompoundEvaluation]).map(_.boxId).contains(box.boxId)
              }

              def validRedeem: Boolean = order.wined[LmRedeem].exists {
                _.evaluation.flatMap(_.widen[LmRedeemEvaluation]).map(_.boxId).contains(box.boxId)
              }

              if (!validDeposit && !validCompound && !validRedeem) none
//                OffChainFee(poolId, order.order.id, box.boxId, pk, ERG(box.value)).some
              else none
            case _ => none
          }
        }
        .collectFirst { case Some(value) => value }
  }
}
