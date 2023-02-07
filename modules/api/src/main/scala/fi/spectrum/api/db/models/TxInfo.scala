package fi.spectrum.api.db.models

import derevo.derive
import fi.spectrum.core.domain.TxId
import tofu.logging.derivation.loggable

@derive(loggable)
final case class TxInfo(id: TxId, timestamp: Long)