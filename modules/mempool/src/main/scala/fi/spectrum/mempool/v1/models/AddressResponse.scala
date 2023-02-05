package fi.spectrum.mempool.v1.models

import derevo.circe.{decoder, encoder}
import derevo.derive
import fi.spectrum.core.domain.Address
import fi.spectrum.core.domain.analytics.Processed
import sttp.tapir.Schema

@derive(encoder, decoder)
final case class AddressResponse(address: Address, orders: List[Processed.Any])

object AddressResponse {
  implicit def schema: Schema[AddressResponse] = Schema.derived
}
