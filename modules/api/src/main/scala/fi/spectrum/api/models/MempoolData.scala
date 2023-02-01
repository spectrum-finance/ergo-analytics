package fi.spectrum.api.models

import derevo.circe.{decoder, encoder}
import derevo.derive
import fi.spectrum.core.domain.Address
import fi.spectrum.core.domain.analytics.Processed
import sttp.tapir.Schema

@derive(encoder, decoder)
final case class MempoolData(address: Address, orders: List[Processed.Any])

object MempoolData {
  implicit def schema: Schema[MempoolData] = Schema.derived
}
