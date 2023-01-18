package fi.spectrum.parser.evaluation

import cats.syntax.option._
import fi.spectrum.core.domain.analytics.OffChainFee
import fi.spectrum.core.domain.analytics.Version.{LegacyV1, V1, V2}
import fi.spectrum.core.domain.order.Fee.{ERG, SPF}
import fi.spectrum.core.domain.order.Order.{Deposit, Lock, Redeem, Swap}
import fi.spectrum.core.domain.order.Redeemer.{ErgoTreeRedeemer, PublicKeyRedeemer}
import fi.spectrum.core.domain.order.{Order, PoolId}
import fi.spectrum.core.domain.transaction.Output
import fi.spectrum.core.domain.{ErgoTreeTemplate, PubKey, TokenId}
import fi.spectrum.core.protocol.ErgoTreeSerializer
import fi.spectrum.parser.api.predefinedErgoTrees
import org.ergoplatform.{ErgoAddressEncoder, P2PKAddress}

/** Tries to find output for off-chain operator.
  */
trait OffChainFeeParser {
  def parse(outputs: List[Output], order: Order, poolId: PoolId): Option[OffChainFee]
}

object OffChainFeeParser {

  def make(spf: TokenId)(implicit e: ErgoAddressEncoder): OffChainFeeParser = new Live(spf)

  final private class Live(spf: TokenId)(implicit e: ErgoAddressEncoder) extends OffChainFeeParser {

    def parse(outputs: List[Output], order: Order, poolId: PoolId): Option[OffChainFee] =
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
            case deposit: Deposit                                => deposit.fee.some
            case redeem: Redeem                                  => redeem.fee.some
            case swap: Swap if swap.version.in(LegacyV1, V1, V2) => ERG(0).some
            case _: Swap                                         => SPF(0).some
            case _: Lock                                         => none
          }

          val pkOpt = address.collect { case address: P2PKAddress => PubKey.fromBytes(address.pubkeyBytes) }

          (feeOpt, pkOpt) match {
            case (Some(SPF(_)), Some(pk)) if !orderRedeemer && !predefinedErgoTrees.contains(template) =>
              box.assets.find(_.tokenId == spf) match {
                case Some(value) =>
                  OffChainFee(poolId, order.id, box.boxId, pk, SPF(value.amount)).some
                case None => none
              }
            case (Some(ERG(_)), Some(pk)) if !orderRedeemer && !predefinedErgoTrees.contains(template) =>
              OffChainFee(poolId, order.id, box.boxId, pk, ERG(box.value)).some
            case _ => none
          }
        }
        .collectFirst { case Some(value) => value }
  }
}
