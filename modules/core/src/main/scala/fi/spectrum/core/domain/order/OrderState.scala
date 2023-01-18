package fi.spectrum.core.domain.order

import derevo.circe.{decoder, encoder}
import derevo.derive
import fi.spectrum.core.domain.TxId
import glass.macros.ClassyOptics
import tofu.logging.derivation.loggable

/** Keeps information about the transaction, which brings to order new status
  * @param txId - transaction id
  * @param timestamp - transaction timestamp
  * @param status - order status
  */

@ClassyOptics
@derive(encoder, decoder, loggable)
final case class OrderState(txId: TxId, timestamp: Long, status: OrderStatus)

object OrderState