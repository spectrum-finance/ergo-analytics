package fi.spectrum.api.v1.models.lm

import derevo.circe.{decoder, encoder}
import derevo.derive
import fi.spectrum.core.domain.order.PoolId
import sttp.tapir.Schema

@derive(encoder, decoder)
final case class LMPoolStat(poolId: PoolId, compoundedReward: String, yearProfit: Option[String])

object LMPoolStat {
  implicit val lmPoolStatsSchema: Schema[LMPoolStat] = Schema.derived
}
