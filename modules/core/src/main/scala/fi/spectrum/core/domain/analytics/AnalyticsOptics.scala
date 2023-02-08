package fi.spectrum.core.domain.analytics

import fi.spectrum.core.domain.analytics.OrderEvaluation._
import glass.Subset
import glass.macros.GenSubset

object AnalyticsOptics {

  implicit val redeemEvalPrism: Subset[OrderEvaluation, RedeemEvaluation] =
    GenSubset[OrderEvaluation, RedeemEvaluation]

  implicit val swapEvalPrism: Subset[OrderEvaluation, SwapEvaluation] =
    GenSubset[OrderEvaluation, SwapEvaluation]

  implicit val depositEvalPrism: Subset[OrderEvaluation, AmmDepositEvaluation] =
    GenSubset[OrderEvaluation, AmmDepositEvaluation]

}
