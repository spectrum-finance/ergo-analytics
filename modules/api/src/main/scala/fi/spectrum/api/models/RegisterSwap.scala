package fi.spectrum.api.models

import derevo.derive
import fi.spectrum.core.domain.AssetAmount
import tofu.logging.derivation.loggable

@derive(loggable)
case class RegisterSwap(base: AssetAmount, minQuote: AssetAmount, info: TxInfo)
