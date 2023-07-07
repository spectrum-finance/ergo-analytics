package fi.spectrum.api.v1.models.history

import derevo.circe.{decoder, encoder}
import derevo.derive
import fi.spectrum.core.domain.PubKey
import sttp.tapir.Schema
import tofu.logging.derivation.loggable

@derive(loggable, encoder, decoder)
final case class AddressesHistoryResponse(addresses: List[PubKey], total: Long)

object AddressesHistoryResponse {

  implicit val schema: Schema[AddressesHistoryResponse] =
    Schema
      .derived[AddressesHistoryResponse]
      .modify(_.addresses)(_.description("Address list"))
      .modify(_.total)(_.description("Total addresses num"))
}
