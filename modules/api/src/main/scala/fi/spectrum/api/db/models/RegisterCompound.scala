package fi.spectrum.api.db.models

import derevo.derive
import tofu.logging.derivation.loggable

@derive(loggable)
final case class RegisterCompound(info: TxInfo)
