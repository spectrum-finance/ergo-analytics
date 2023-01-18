package fi.spectrum.parser

import fi.spectrum.core.domain.ErgoTreeTemplate
import fi.spectrum.parser.templates.{Lock, N2T, T2T}

object api {

  private val ergoBaseOutput: ErgoTreeTemplate =
    ErgoTreeTemplate.unsafeFromString("d19683030193a38cc7b2a57300000193c2b2a57301007473027303830108cdeeac93b1a57304")

  val predefinedErgoTrees: List[ErgoTreeTemplate] =
    List(
      N2T.depositV3,
      N2T.redeemV3,
      N2T.swapSellV3,
      N2T.swapBuyV3,
      T2T.depositV3,
      T2T.redeemV3,
      T2T.swapV3,
      N2T.swapSellV2,
      N2T.swapBuyV2,
      T2T.swapV2,
      N2T.depositV1,
      N2T.redeemV1,
      N2T.swapSellV1,
      N2T.swapBuyV1,
      T2T.depositV1,
      T2T.redeemV1,
      T2T.swapV1,
      N2T.depositLegacyV1,
      N2T.redeemLegacyV1,
      N2T.swapSellLegacyV1,
      N2T.swapBuyLegacyV1,
      T2T.depositLegacyV1,
      T2T.redeemLegacyV1,
      T2T.swapLegacyV1,
      N2T.depositLegacyV2,
      T2T.depositLegacyV2,
      Lock.lockV1,
      T2T.pool,
      N2T.pool,
      ergoBaseOutput
    )

}
