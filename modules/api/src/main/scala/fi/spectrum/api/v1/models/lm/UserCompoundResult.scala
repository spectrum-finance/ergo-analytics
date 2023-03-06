package fi.spectrum.api.v1.models.lm

import derevo.circe.{decoder, encoder}
import derevo.derive
import fi.spectrum.api.v1.models.AssetAmountApi
import fi.spectrum.core.domain.AssetAmount
import fi.spectrum.core.domain.order.PoolId
import sttp.tapir.Schema

@derive(encoder, decoder)
final case class UserCompoundResult(poolId: PoolId, reward: AssetAmountApi)

object UserCompoundResult {

  implicit def userCompoundResultSchema: Schema[UserCompoundResult] = Schema
    .derived[UserCompoundResult]
    .modify(_.poolId)(_.description("Id of corresponding pool"))
}
