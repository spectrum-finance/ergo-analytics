package fi.spectrum.core.domain.order

import derevo.circe.{decoder, encoder}
import derevo.derive
import fi.spectrum.core.domain.AssetAmount
import tofu.logging.derivation.{loggable, show}

@derive(encoder, decoder, loggable)
final case class RedeemParams(lp: AssetAmount)
