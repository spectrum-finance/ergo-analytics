package fi.spectrum.api.models

import derevo.derive
import fi.spectrum.core.domain.AssetAmount
import tofu.logging.derivation.loggable

@derive(loggable)
case class RegisterDeposit(inX: AssetAmount, inY: AssetAmount, info: TxInfo)
