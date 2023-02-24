package fi.spectrum.api.v1.models.lm

import derevo.circe.{decoder, encoder}
import derevo.derive
import sttp.tapir.Schema

@derive(encoder, decoder)
final case class UserLmStats(userNextStakesReward: List[UserNextStakeReward], userInterests: List[UserCompoundResult])

object UserLmStats {
  implicit val userLmStatsSchema: Schema[UserLmStats] = Schema.derived
}
