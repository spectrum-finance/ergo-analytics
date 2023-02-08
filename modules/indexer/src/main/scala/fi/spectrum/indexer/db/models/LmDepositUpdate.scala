package fi.spectrum.indexer.db.models

import fi.spectrum.core.domain.AssetAmount
import fi.spectrum.core.domain.order.OrderId

final case class LmDepositUpdate(lp: AssetAmount, compoundId: OrderId)
