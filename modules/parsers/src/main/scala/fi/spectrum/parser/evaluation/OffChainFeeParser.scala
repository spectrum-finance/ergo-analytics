package fi.spectrum.parser.evaluation

import cats.syntax.option._
import fi.spectrum.core.domain.analytics.OffChainOperatorFee
import fi.spectrum.core.domain.order.Fee.{ERG, SPF}
import fi.spectrum.core.domain.order.Redeemer.{ErgoTreeRedeemer, PublicKeyRedeemer}
import fi.spectrum.core.domain.order.{Order, PoolId}
import fi.spectrum.core.domain.transaction.Output
import fi.spectrum.core.domain.{ErgoTreeTemplate, PubKey}
import fi.spectrum.core.protocol.ErgoTreeSerializer
import fi.spectrum.parser.constants.predefinedErgoTrees
import org.ergoplatform.{ErgoAddressEncoder, P2PKAddress}

trait OffChainFeeParser {
  def parse(outputs: List[Output], order: Order.Any, poolId: PoolId): Option[OffChainOperatorFee]
}

object OffChainFeeParser {

  def make(implicit e: ErgoAddressEncoder): OffChainFeeParser = new Live

  final private class Live(implicit e: ErgoAddressEncoder) extends OffChainFeeParser {

    def parse(outputs: List[Output], order: Order.Any, poolId: PoolId): Option[OffChainOperatorFee] =
      outputs
        .map { box =>
          val tree     = ErgoTreeSerializer.default.deserialize(box.ergoTree)
          val template = ErgoTreeTemplate.fromBytes(tree.template)
          val address  = e.fromProposition(tree).toOption

          def orderRedeemer = order.redeemer match {
            case ErgoTreeRedeemer(value) => value == box.ergoTree
            case PublicKeyRedeemer(value) =>
              val treePubKey = ErgoTreeSerializer.default.deserialize(value.ergoTree)
              e.fromProposition(treePubKey).toOption.map(e.toString) == address.map(e.toString)
          }

          val feeOpt = order match {
            case deposit: Order.Deposit[_, _] => deposit.fee.some
            case redeem: Order.Redeem[_, _]   => redeem.fee.some
            case swap: Order.Swap[_, _]       => swap.fee.some
            case _: Order.Lock[_]             => none
          }

          val pkOpt = address.collect { case address: P2PKAddress =>
            PubKey.fromBytes(address.pubkeyBytes)
          }

          (feeOpt, pkOpt) match {
            case (Some(SPF(_, tokenId)), Some(pk)) if !orderRedeemer && !predefinedErgoTrees.contains(template) =>
              box.assets.find(_.tokenId == tokenId) match {
                case Some(value) =>
                  OffChainOperatorFee(poolId, order.id, box.boxId, pk, SPF(value.amount, value.tokenId)).some
                case None => none
              }
            case (Some(ERG(_)), Some(pk)) if !orderRedeemer && !predefinedErgoTrees.contains(template) =>
              OffChainOperatorFee(poolId, order.id, box.boxId, pk, ERG(box.value)).some
            case _ => none
          }
        }
        .collectFirst { case Some(value) => value }
  }
}