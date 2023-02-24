package fi.spectrum.api.v1.models.lm

import derevo.circe.{decoder, encoder}
import derevo.derive
import fi.spectrum.core.domain.order.PoolId
import sttp.tapir.Schema

@derive(encoder, decoder)
final case class UserNextStakeReward(poolId: PoolId, nextReward: BigDecimal)

object UserNextStakeReward {
  implicit val userNextStakeRewardSchema: Schema[UserNextStakeReward] = Schema.derived
}
