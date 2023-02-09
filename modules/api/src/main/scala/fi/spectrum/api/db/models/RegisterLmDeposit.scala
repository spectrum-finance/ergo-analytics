package fi.spectrum.api.db.models

import derevo.derive
import fi.spectrum.core.domain.AssetAmount
import tofu.logging.derivation.loggable

@derive(loggable)
final case class RegisterLmDeposit(epochs: Int, in: AssetAmount, info: TxInfo)
