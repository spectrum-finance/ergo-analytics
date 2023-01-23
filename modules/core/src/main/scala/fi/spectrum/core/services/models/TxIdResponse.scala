package fi.spectrum.core.services.models

import derevo.circe.decoder
import derevo.derive
import fi.spectrum.core.domain.TxId

@derive(decoder)
final case class TxIdResponse(id: TxId)
