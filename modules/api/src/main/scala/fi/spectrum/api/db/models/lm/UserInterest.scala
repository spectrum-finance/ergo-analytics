package fi.spectrum.api.db.models.lm

import fi.spectrum.core.domain.AssetAmount
import fi.spectrum.core.domain.order.PoolId

final case class UserInterest(poolId: PoolId, reward: AssetAmount)
