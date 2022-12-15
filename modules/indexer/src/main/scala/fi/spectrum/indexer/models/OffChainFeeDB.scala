package fi.spectrum.indexer.models

import fi.spectrum.core.domain.analytics.OffChainFee
import fi.spectrum.core.domain.{BoxId, PubKey}
import fi.spectrum.core.domain.order.{Fee, Order, OrderId, PoolId}
import fi.spectrum.indexer.classes.ToSchema

case class OffChainFeeDB(poolId: PoolId, orderId: OrderId, outputId: BoxId, pubKey: PubKey, fee: Fee)

object OffChainFeeDB {

  implicit val toSchema: ToSchema[OffChainFee, OffChainFeeDB] = fee =>
    OffChainFeeDB(fee.poolId, fee.orderId, fee.outputId, fee.pubKey, fee.fee)
}
