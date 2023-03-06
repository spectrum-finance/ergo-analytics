package fi.spectrum.api.v1.models.lm

import derevo.circe.{decoder, encoder}
import derevo.derive
import fi.spectrum.core.domain.{HexString, TokenId}
import fi.spectrum.core.domain.order.PoolId
import sttp.tapir.Schema
import io.circe.syntax._

@derive(encoder, decoder)
final case class LMPoolStat(poolId: PoolId, compoundedReward: BigDecimal, yearProfit: Option[BigDecimal])

object LMPoolStat {

  implicit def lmPoolStatsSchema: Schema[LMPoolStat] = Schema
    .derived[LMPoolStat]
    .modify(_.poolId)(_.description("LM program pool id"))
    .modify(_.compoundedReward)(_.description("LM program completion percent"))
    .modify(_.yearProfit)(_.description("Year APR"))
    .encodedExample(
      LMPoolStat(
        PoolId(
          TokenId(HexString.unsafeFromString("9916d75132593c8b07fe18bd8d583bda1652eed7565cf41a4738ddd90fc992ec"))
        ),
        BigDecimal(75.0),
        Some(BigDecimal(3700))
      ).asJson
    )
}
