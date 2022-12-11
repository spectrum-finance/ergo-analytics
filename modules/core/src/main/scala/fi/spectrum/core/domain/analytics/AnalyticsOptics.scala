package fi.spectrum.core.domain.analytics

import fi.spectrum.core.domain.analytics.OrderEvaluation.{RedeemEvaluation, SwapEvaluation}
import glass.macros.GenSubset

object AnalyticsOptics {

  implicit val redeemEvalPrism = GenSubset[OrderEvaluation, RedeemEvaluation]
  implicit val swapEvalPrism = GenSubset[OrderEvaluation, SwapEvaluation]

}
