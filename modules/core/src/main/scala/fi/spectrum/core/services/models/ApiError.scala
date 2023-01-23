package fi.spectrum.core.services.models

import derevo.circe.decoder
import derevo.derive

@derive(decoder)
final case class ApiError(status: Int, reason: String)
