package fi.spectrum.indexer.models

import fi.spectrum.core.domain.TxId

final case class TxInfo(id: TxId, timestamp: Long)
