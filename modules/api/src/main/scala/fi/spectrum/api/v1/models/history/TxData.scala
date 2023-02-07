package fi.spectrum.api.v1.models.history

import derevo.circe.{decoder, encoder}
import derevo.derive
import fi.spectrum.core.domain.TxId
import sttp.tapir.Schema
import tofu.logging.derivation.{loggable, show}

@derive(encoder, decoder, show, loggable)
final case class TxData(id: TxId, ts: Long)

object TxData {
  implicit val schema: Schema[TxData] = Schema.derived
}
