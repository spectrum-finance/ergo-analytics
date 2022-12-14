package fi.spectrum.indexer.models

import fi.spectrum.core.domain.order.OrderId

final case class UpdateState(info: TxInfo, orderId: OrderId)