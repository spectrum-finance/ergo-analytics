package fi.spectrum.parser.amm.order.legacy.v2

import fi.spectrum.core.domain.analytics.Version
import fi.spectrum.core.domain.order.Fee.ERG
import fi.spectrum.core.domain.order.Order.Deposit.AmmDeposit._
import fi.spectrum.core.domain.order.Redeemer.PublicKeyRedeemer
import fi.spectrum.core.domain.order.{AmmDepositParams, PoolId}
import fi.spectrum.core.domain.transaction.Output
import fi.spectrum.core.domain.{AssetAmount, PubKey, TokenId}
import io.circe.parser.decode

object N2T {

  object deposit {

    val output: Output = decode[Output](
      """
        |{
        |    "boxId": "8f02592ab29f6fa23521945cb4a6be442b7d82de4f7d44bdd81af3e60ebee0d6",
        |    "transactionId": "2bf9834c6946d30dee3620c1df7d8c660ecb8b8e85e0e2c4d6ddedf7da81051e",
        |    "blockId": "d06548a423ef41aef7b4a80f64552c1d383c4083845b4e617823efce43ae2653",
        |    "value": 4391252651,
        |    "index": 0,
        |    "globalIndex": 10079873,
        |    "creationHeight": 506880,
        |    "settlementHeight": 627904,
        |    "ergoTree": "19a3031408cd03ac924c5ce32ee142e2506df85b06075ceb43fc235c665c4f4b354bfb997f918204000596e28bd520040404060402040205feffffffffffffffff0104040400040004000e209916d75132593c8b07fe18bd8d583bda1652eed7565cf41a4738ddd90fc992ec040404020580bfd6060596e28bd5200580bfd60601000100d803d6017300d602b2a4730100d6037302eb027201d195ed93b1a4730393b1db630872027304d80bd604db63087202d605b2a5730500d606b27204730600d6077e9973078c72060206d6087ec1720206d6099d9c7e72030672077208d60ab27204730800d60b7e8c720a0206d60c9d9c7e8cb2db6308a773090002067207720bd60ddb63087205d60eb2720d730a00edededed938cb27204730b0001730c93c27205d0720195ed8f7209720c93b1720d730dd801d60fb2720d730e00eded92c172059999c1a7730f7310938c720f018c720a01927e8c720f02069d9c99720c7209720b720795927209720c927ec1720506997e99c1a7731106997e7203069d9c997209720c720872077312938c720e018c720601927e8c720e0206a17209720c7313",
        |    "address": "2DnYatBTAZF8xuhFwaPMU5p1JhwvCqrXnHGFcyvoyhckwtwwRRSTgD6UKBg44SmR3YXvmJXkaxkaWX4tUataHZzZUsAAjqFqe8P5u3gWNnen5xdHpWqydcRqF5XrMTHsXscXMJFSToy5VD1WpTvadHR9AjFNeZgHFJjU7qiYJ8vFzYcwryS4u7aRtFV4CnMRFg82tAy1Xj7iM6TQcwJMkng6hXTYU6gS3xh3jzJkWeZBkNLzLJeZRpPSBuERN4MvPWzFd6BTJ4w19u2ZibH6DSj4w3upF3o8eHY4DLADyEpxpNyXp9WFNQpxzc66btDWBUiH4D8HkD28KNRH4sVJSvYJf7stTY5KiasVcwz948wBG1mV5hXRJDK5yhLnTpEsWwf4KpPXAJhyoebRuFvduRXCihA8s7mJ2iwEYz2X3pAfT5fWjZ3Z3n2msv3rLTMRbrLeUj6FVCpKH8gTVGqQ8o3BAr8Xjo5gwGay1cWMZToRgiyLu6ZzxMAsGNxc5uV6XLfRy81c1fAwjBDbwUfTwAQMd8EZQLptjBChKiL2QcsFXnQsKSyd7zqfrdTcqGV6FjZwNwx",
        |    "assets": [
        |        {
        |            "tokenId": "03faf2cb329f2e90d6d23b58d91bbb6c046aa143261cc21f52fbe2824bfcbf04",
        |            "index": 0,
        |            "amount": 3426,
        |            "name": "SigUSD",
        |            "decimals": 2,
        |            "type": "EIP-004"
        |        }
        |    ],
        |    "additionalRegisters": {},
        |    "spentTransactionId": "db97e4753460b1a6b1a8144df49c320320a7f9d7ea0943a9e9cfdc11de548b7e",
        |    "mainChain": true
        |}
        |""".stripMargin
    ).toOption.get

    val deposit: AmmDepositLegacyV2 = AmmDepositLegacyV2(
      output,
      ERG(7000000),
      PoolId.unsafeFromString("9916d75132593c8b07fe18bd8d583bda1652eed7565cf41a4738ddd90fc992ec"),
      PublicKeyRedeemer(PubKey.unsafeFromString("03ac924c5ce32ee142e2506df85b06075ceb43fc235c665c4f4b354bfb997f9182")),
      AmmDepositParams(
        AssetAmount.native(4384192651L),
        AssetAmount(
          TokenId.unsafeFromString("03faf2cb329f2e90d6d23b58d91bbb6c046aa143261cc21f52fbe2824bfcbf04"),
          3426
        )
      ),
      Version.LegacyV2,

    )
  }
}
