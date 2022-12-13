package fi.spectrum.parser.amm.order.legacy.v2

import fi.spectrum.core.domain.{AssetAmount, PubKey, TokenId}
import fi.spectrum.core.domain.analytics.Version
import fi.spectrum.core.domain.order.Fee.ERG
import fi.spectrum.core.domain.order.Order.Deposit.{DepositLegacyV1, DepositLegacyV2}
import fi.spectrum.core.domain.order.{DepositParams, Operation, OrderType, PoolId}
import fi.spectrum.core.domain.order.Redeemer.PublicKeyRedeemer
import fi.spectrum.core.domain.transaction.Output
import io.circe.parser.decode
object T2T {

  object deposit {

    val output: Output = decode[Output](
      """
        |{
        |    "boxId": "552e6d056e7fca3bbb3a6c81ae27c483fff617f9ea882a894e1519beaec9de6c",
        |    "transactionId": "adac0745ad237793b1aa113ac0473059a58fbe6c06c1342e9968103b21ffe18b",
        |    "blockId": "0dac8114b9ef8e655cb28c627193fd12ccb05127de9200ebcfbe1fc07d952adf",
        |    "value": 7060000,
        |    "index": 0,
        |    "globalIndex": 9731300,
        |    "creationHeight": 506880,
        |    "settlementHeight": 621162,
        |    "ergoTree": "19c3031708cd0275f8c92f067d410cb192b972ff3283b12a1887def532057d84ecfc2644fcae660400040404080402040205feffffffffffffffff010404040004060402040004000e2065fa572bc4a7007e5a6450c9af2bfa1594e6dfb43b667027f1930eefddeac7bf05020580bfd6060404040204040402010101000100d803d6017300d602b2a4730100d603db6308a7eb027201d195ed93b1a4730293b1db630872027303d80cd604db63087202d605b2a5730400d606b27204730500d6077e9973068c72060206d608b27204730700d6097e8c72080206d60a9d9c7e8cb27203730800020672077209d60bb27204730900d60c7e8c720b0206d60d9d9c7e8cb27203730a0002067207720cd60edb63087205d60fb2720e730b00ededededed93b27204730c008602730d730e93c27205d0720192c1720599c1a7730f95ed8f720a720d93b1720e7310d801d610b2720e731100ed938c7210018c720b01927e8c721002069d9c99720d720a720c720795ed91720a720d93b1720e7312d801d610b2720e731300ed938c7210018c720801927e8c721002069d9c99720a720d720972079593720a720d73147315938c720f018c720601927e8c720f0206a1720a720d7316",
        |    "address": "N2o2aGFKuPzaX6A6S4h27JubynRzXpNq1vCLJwYZJVUEHiiMcMELzUM1RjjSFxYTAmUvYxY2SGi7Mk8saMNY37spoyXW6PuNZ8eg3mD2xhWWwEjdEbKL6otZrdXBRvPiZ5T934oELMJm1Wat6jwdJZTU4V2ozaBggis1cRX8d6FYg2kfQdxei6Y8RLf7qErHSEUJXuo9wkgpMPWmAMve727yH3v1SR9dm5PiECsB31DtkK1ycqPjjHSWQ14F9GQd8J24KpHPvapShKoE93Qyn2RFw2LmFvPJ9oAkEiYU5M9siM1baRYkyqYKa2eDL1TUENqhd9JzU2LqKfKaoeCVXFjuczQcVErd8jKcEyBP6v9CTrnDyqH3dPTBB2NN71oG5c3uP5gzZZ8RHWe2VJhZygH8Vvwg9TS3H3gqA4jCFAYdVAQ5qtvXwhD7TFyb5jda5nUERMwWzkcWktyhCNhgaHhNbq6aKedN2jgJdhhv2TSCe2PggqoERD9TrgcHNDuQVB9qyxQt2GdaAhPcpErCLTNPXSHSLaEMtyTkiHoF1BwnrtZgh5r5uKfxoRMDzjUWVb6T3Jwymw5eQMvp1ysgSvArShKsmaBY1kTAZm3w35mfFXbBo3",
        |    "assets": [
        |        {
        |            "tokenId": "36aba4b4a97b65be491cf9f5ca57b5408b0da8d0194f30ec8330d1e8946161c1",
        |            "index": 0,
        |            "amount": 36,
        |            "name": "Erdoge",
        |            "decimals": 0,
        |            "type": "EIP-004"
        |        },
        |        {
        |            "tokenId": "fbbaac7337d051c10fc3da0ccb864f4d32d40027551e1c3ea3ce361f39b91e40",
        |            "index": 1,
        |            "amount": 278984,
        |            "name": "kushti",
        |            "decimals": 0,
        |            "type": "EIP-004"
        |        }
        |    ],
        |    "additionalRegisters": {},
        |    "spentTransactionId": "28b3010bfeed889c7fa9ed7bf69579d25e3b98b3afebca7a4d4402ef226857b2",
        |    "mainChain": true
        |}
        |""".stripMargin
    ).toOption.get

    val deposit: DepositLegacyV2 = DepositLegacyV2(
      output,
      ERG(7000000),
      PoolId.unsafeFromString("65fa572bc4a7007e5a6450c9af2bfa1594e6dfb43b667027f1930eefddeac7bf"),
      PublicKeyRedeemer(PubKey.unsafeFromString("0275f8c92f067d410cb192b972ff3283b12a1887def532057d84ecfc2644fcae66")),
      DepositParams(
        AssetAmount(
          TokenId.unsafeFromString("36aba4b4a97b65be491cf9f5ca57b5408b0da8d0194f30ec8330d1e8946161c1"),
          36
        ),
        AssetAmount(
          TokenId.unsafeFromString("fbbaac7337d051c10fc3da0ccb864f4d32d40027551e1c3ea3ce361f39b91e40"),
          278984
        )
      ),
      Version.make.legacyV2,
      OrderType.make.amm,
      Operation.make.deposit
    )
  }
}
