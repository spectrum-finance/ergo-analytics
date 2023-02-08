package fi.spectrum.api.v1.models.history

import derevo.circe.{decoder, encoder}
import derevo.derive
import fi.spectrum.core.domain.{Address, TokenId, TxId}
import sttp.tapir.Schema
import tofu.logging.derivation.loggable

@derive(encoder, decoder, loggable)
final case class HistoryApiQuery(
  addresses: List[Address],
  orderType: Option[OrderType],
  orderStatus: Option[OrderStatusApi],
  txId: Option[TxId],
  tokenIds: Option[List[TokenId]],
  tokenPair: Option[TokenPair]
)

object HistoryApiQuery {
  implicit val schema: Schema[HistoryApiQuery] = Schema.derived
}
