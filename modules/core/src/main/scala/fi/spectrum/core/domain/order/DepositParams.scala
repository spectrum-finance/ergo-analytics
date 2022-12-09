package fi.spectrum.core.domain.order

import derevo.circe.{decoder, encoder}
import derevo.derive
import fi.spectrum.core.domain.AssetAmount

@derive(encoder, decoder)
final case class DepositParams(inX: AssetAmount, inY: AssetAmount)
