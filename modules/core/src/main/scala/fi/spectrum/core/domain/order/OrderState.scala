package fi.spectrum.core.domain.order

import derevo.circe.{decoder, encoder}
import derevo.derive
import fi.spectrum.core.domain.TxId

@derive(encoder, decoder)
final case class OrderState(txId: TxId, timestamp: Long, status: OrderStatus)

object OrderState {}
