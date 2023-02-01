package fi.spectrum.api.v1.models.amm

import derevo.circe.{decoder, encoder}
import derevo.derive
import fi.spectrum.core.domain.TxId
import sttp.tapir.Schema

@derive(encoder, decoder)
final case class TxData(id: TxId, ts: Long)

object TxData {
  implicit val schema: Schema[TxData] = Schema.derived
}
