package fi.spectrum.indexer.db.models

import fi.spectrum.core.domain.analytics.OffChainFee
import fi.spectrum.core.domain.order.{Fee, OrderId, PoolId}
import fi.spectrum.core.domain.{BoxId, PubKey}
import fi.spectrum.indexer.classes.ToDB

case class OffChainFeeDB(poolId: PoolId, orderId: OrderId, outputId: BoxId, pubKey: PubKey, fee: Fee)

object OffChainFeeDB {

  implicit val toDB: ToDB[OffChainFee, OffChainFeeDB] = fee =>
    OffChainFeeDB(fee.poolId, fee.orderId, fee.outputId, fee.pubKey, fee.fee)
}
