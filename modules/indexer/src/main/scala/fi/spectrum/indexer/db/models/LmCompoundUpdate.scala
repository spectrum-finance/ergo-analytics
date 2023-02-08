package fi.spectrum.indexer.db.models

import fi.spectrum.core.domain.BoxId
import fi.spectrum.core.domain.order.OrderId

final case class LmCompoundUpdate(info: TxInfo, poolStateId: Option[BoxId], orderId: OrderId)
