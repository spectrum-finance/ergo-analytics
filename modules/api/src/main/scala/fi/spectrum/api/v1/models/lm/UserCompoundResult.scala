package fi.spectrum.api.v1.models.lm

import derevo.circe.{decoder, encoder}
import derevo.derive
import fi.spectrum.core.domain.AssetAmount
import fi.spectrum.core.domain.order.PoolId
import sttp.tapir.Schema

@derive(encoder, decoder)
final case class UserCompoundResult(poolId: PoolId, reward: AssetAmount)

object UserCompoundResult {
  implicit val userCompoundResultSchema: Schema[UserCompoundResult] = Schema.derived
}
