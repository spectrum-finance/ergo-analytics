package fi.spectrum.parser.evaluation

import fi.spectrum.core.domain.{AssetAmount, TokenId}
import fi.spectrum.core.domain.analytics.OrderEvaluation.{DepositEvaluation, RedeemEvaluation, SwapEvaluation}
import fi.spectrum.core.domain.transaction.Output

object OrderEval {

  object swap {

    val eval = SwapEvaluation(
      AssetAmount(
        TokenId.unsafeFromString("30974274078845f263b4f21787e33cc99e9ec19a17ad85a5bc6da2cca91c5a2e"),
        17077241659L
      )
    )
  }

  object redeem {
    val eval = RedeemEvaluation(
      AssetAmount(
        TokenId.unsafeFromString("ef802b475c06189fdbf844153cdc1d449a5ba87cce13d11bb47b5a539f27f12b"),
        33796097282L
      ),
      AssetAmount(
        TokenId.unsafeFromString("30974274078845f263b4f21787e33cc99e9ec19a17ad85a5bc6da2cca91c5a2e"),
        64049721480L
      )
    )
  }

  object deposit {
    val eval = DepositEvaluation(
      AssetAmount(
        TokenId.unsafeFromString("303f39026572bcb4060b51fafc93787a236bb243744babaa99fceb833d61e198"),
        3604314
      )
    )
  }
}
