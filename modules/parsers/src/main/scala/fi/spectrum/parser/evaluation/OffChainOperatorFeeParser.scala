package fi.spectrum.parser.evaluation

import cats.syntax.option._
import fi.spectrum.core.domain.analytics.OffChainOperatorFee
import fi.spectrum.core.domain.order.Fee.ERG
import fi.spectrum.core.domain.order.Redeemer.{ErgoTreeRedeemer, PublicKeyRedeemer}
import fi.spectrum.core.domain.order.{Order, PoolId}
import fi.spectrum.core.domain.transaction.Output
import fi.spectrum.core.domain.{ErgoTreeTemplate, PubKey}
import fi.spectrum.core.protocol.ErgoTreeSerializer
import fi.spectrum.parser.constants.predefinedErgoTrees
import org.ergoplatform.{ErgoAddressEncoder, P2PKAddress}

trait OffChainOperatorFeeParser {
  def parse(box: Output, order: Order.Any, poolId: PoolId): Option[OffChainOperatorFee]
}

object OffChainOperatorFeeParser {

  def make(implicit e: ErgoAddressEncoder): OffChainOperatorFeeParser = new Live

  final private class Live(implicit e: ErgoAddressEncoder) extends OffChainOperatorFeeParser {

    def parse(box: Output, order: Order.Any, poolId: PoolId): Option[OffChainOperatorFee] = {
      val tree     = ErgoTreeSerializer.default.deserialize(box.ergoTree)
      val template = ErgoTreeTemplate.fromBytes(tree.template)
      val address  = e.fromProposition(tree).toOption

      def orderRedeemer = order.redeemer match {
        case ErgoTreeRedeemer(value) => value == box.ergoTree
        case PublicKeyRedeemer(value) =>
          val treePubKey = ErgoTreeSerializer.default.deserialize(value.ergoTree)
          e.fromProposition(treePubKey).toOption.map(e.toString) == address.map(e.toString)
      }

      address.collect { case address: P2PKAddress =>
        PubKey.fromBytes(address.pubkeyBytes)
      } match {
        case Some(pk) if !orderRedeemer && !predefinedErgoTrees.contains(template) =>
          OffChainOperatorFee(poolId, order.id, box.boxId, pk, ERG(box.value)).some
        case _ => none
      }

    }
  }
}
