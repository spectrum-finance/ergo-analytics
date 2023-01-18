package fi.spectrum.indexer.db.models

import fi.spectrum.core.domain.TxId

final case class TxInfo(id: TxId, timestamp: Long)
