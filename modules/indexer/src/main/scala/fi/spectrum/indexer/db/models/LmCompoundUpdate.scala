package fi.spectrum.indexer.db.models

import fi.spectrum.core.domain.{AssetAmount, BoxId}
import fi.spectrum.core.domain.order.OrderId

final case class LmCompoundUpdate(interest: Option[AssetAmount], info: TxInfo, poolStateId: Option[BoxId], orderId: OrderId)
