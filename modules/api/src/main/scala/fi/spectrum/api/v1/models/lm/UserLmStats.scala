package fi.spectrum.api.v1.models.lm

import derevo.circe.{decoder, encoder}
import derevo.derive
import fi.spectrum.api.v1.models.AssetAmountApi
import fi.spectrum.core.domain.{AssetAmount, HexString, TokenId}
import fi.spectrum.core.domain.order.PoolId
import sttp.tapir.Schema
import io.circe.syntax._

@derive(encoder, decoder)
final case class UserLmStats(userNextStakesReward: List[UserNextStakeReward], userInterests: List[UserCompoundResult])

object UserLmStats {

  implicit def userLmStatsSchema: Schema[UserLmStats] = Schema
    .derived[UserLmStats]
    .modify(_.userInterests)(_.description("Total collected reward by wallet for requested pool"))
    .modify(_.userNextStakesReward)(_.description("Next potential reward by wallet for requested pool"))
    .encodedExample(
      UserLmStats(
        UserNextStakeReward(
          PoolId(
            TokenId(HexString.unsafeFromString("9916d75132593c8b07fe18bd8d583bda1652eed7565cf41a4738ddd90fc992ec"))
          ),
          BigDecimal(250.76).toString()
        ) :: Nil,
        UserCompoundResult(
          PoolId(
            TokenId(HexString.unsafeFromString("9916d75132593c8b07fe18bd8d583bda1652eed7565cf41a4738ddd90fc992ec"))
          ),
          AssetAmountApi(
            TokenId(HexString.unsafeFromString("03faf2cb329f2e90d6d23b58d91bbb6c046aa143261cc21f52fbe2824bfcbf04")),
            s"${378294}"
          )
        ) :: Nil
      ).asJson
    )
}
