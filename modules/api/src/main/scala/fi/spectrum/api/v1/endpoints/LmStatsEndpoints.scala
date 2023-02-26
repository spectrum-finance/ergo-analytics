package fi.spectrum.api.v1.endpoints

import fi.spectrum.api.v1.models.lm.{LMPoolStat, UserLmStats}
import fi.spectrum.common.http.{HttpError, baseEndpoint}
import fi.spectrum.core.domain.Address
import sttp.tapir._
import sttp.tapir.json.circe.jsonBody

class LmStatsEndpoints {
  val PathPrefix = "lm"
  val Group      = "lmStats"

  def endpoints: List[Endpoint[_, _, _, _, _]] = List(
    lmPoolsStatsE,
    lmUserStatsE
  )

  def lmPoolsStatsE: Endpoint[Unit, Unit, HttpError, List[LMPoolStat], Any] =
    baseEndpoint.get
      .in(PathPrefix / "pools" / "stats")
      .out(jsonBody[List[LMPoolStat]])
      .tag(Group)
      .name("LM pools stats")
      .description("Provides LM pools statistics")

  def lmUserStatsE: Endpoint[Unit, List[Address], HttpError, UserLmStats, Any] =
    baseEndpoint.post
      .in(PathPrefix / "user" / "stats")
      .in(jsonBody[List[Address]])
      .out(jsonBody[UserLmStats])
      .tag(Group)
      .name("LM stats by addresses")
      .description("Provides LM statistics by addresses")
}
