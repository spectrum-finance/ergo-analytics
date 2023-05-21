package fi.spectrum.parser.amm.order.v1

import fi.spectrum.core.domain.analytics.Version
import fi.spectrum.core.domain.analytics.Version.V1
import fi.spectrum.core.domain.order.Fee.ERG
import fi.spectrum.core.domain.order.Order.Deposit.AmmDeposit._
import fi.spectrum.core.domain.order.Order.Redeem.AmmRedeem.RedeemV1
import fi.spectrum.core.domain.order.Order.Swap
import fi.spectrum.core.domain.{AssetAmount, PubKey, TokenId}
import fi.spectrum.core.domain.order.Order.Swap.SwapV1

import fi.spectrum.core.domain.order.{AmmDepositParams, PoolId, RedeemParams, SwapParams}
import fi.spectrum.core.domain.order.Redeemer.PublicKeyRedeemer
import fi.spectrum.core.domain.transaction.Output
import io.circe.parser.decode

object N2T {

  object swap {

    val swapN2TSell: Output = decode[Output](
      s"""
         |{
         |    "boxId": "fa6f95b9d5b75f370747aaa9e798732e74c519ac25a193688871ef55e814baf3",
         |    "transactionId": "53b44b334e3e20697768f2e66bc2c817b3a7c0c86904d9cb79a2a0e2ce95a408",
         |    "blockId": "30d1860f45ec321b6aac3426b6ccd2cdcf260225725588884bad9acd60df1d6d",
         |    "value": 7307260000,
         |    "index": 0,
         |    "globalIndex": 28279282,
         |    "creationHeight": 982319,
         |    "settlementHeight": 982321,
         |    "ergoTree": "19fe031808cd02b3dcf362d632cafa9d8d0ae64d75a1ad7fcc938122433f57ad3fcd842e586a8004000580a4e9b136040404060402040004000e20f40afb6f877c40a30c8637dd5362227285738174151ce66d6684bc1b727ab6cf0e209a06d9e545a41fd51eeffc5e20d818073bf820c635e2a9d922269913e0de369d0598f8efa40105e6e1e7bbad9caa0c058080d0d88bdea2e302040404ca0f06010104d00f0580a4e9b13604ca0f0e691005040004000e36100204a00b08cd0279be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798ea02d192a39a8cc7a701730073011001020402d19683030193a38cc7b2a57300000193c2b2a57301007473027303830108cdeeac93b1a5730405000500058092f4010100d803d6017300d602b2a4730100d6037302eb027201d195ed92b1a4730393b1db630872027304d804d604db63087202d605b2a5730500d606b2db63087205730600d6077e8c72060206edededededed938cb2720473070001730893c27205d07201938c72060173099272077e730a06927ec172050699997ec1a7069d9c72077e730b067e730c067e720306909c9c7e8cb27204730d0002067e7203067e730e069c9a7207730f9a9c7ec17202067e7310067e9c73117e7312050690b0ada5d90108639593c272087313c1720873147315d90108599a8c7208018c72080273167317",
         |    "address": "4jDxre1SHdrM4C1nVJN7PbqPGE8sz7tU9svUo61c5RU7drwvCKjg8mPLbqLUe92SseTBagRYT94eUMpvDnx42x55ViYujPCm7BcAg1BkZKMUsCSH7mZ5HK4eCVkdhLJK7ETEhP658necK9zEvEJ8DeRU5KEGSzZk3c7PATauBhvMxEfJp3J8t41AkqXTS59fv4HFX16zQaYkNFktBKQrNa25dG2GgYH4kjk1Lqgsx1DJ3dZFCpmzR8risonFmDFcT1DhUEh3vpJMMZk6xKiTLvgrNopRLoPqQk65RGhQqxUDjJqHHLJXXa3BqbrAcLsN7tSy85dCmyu3P6MqPN6TbVcpWit5NWLG7XzV8Fkh92hRBGTPW68YzE1Gzz2tnt3tnq2GhtLbza5PuXaMfqi1AthHrLm55mZhB9GCJ73VG8WU6sBiV1kAfhj2VkyDpwWsDAZ33s8KUC9thXeReUrYojUEVFrSzk31a9zkx73RouDsYzHcv7hSYBCFrBWLHawbxyP8cB9YtYCQs6XUL253sPRvmD3cmsVLmmchWNw249EgRicxkX6hAhGnVEaXC4zna9owBYDCUzTuCfHSfz39yEwZa9nM7cG8cggyWQ3sYYs44Bt3adrHwKrWpLKvpkyS9a8BwsoFCb62nLhbUPRHqbXNjivf24epeeaGgTEkKbwfxr8PcScUkMgZaNL3jmczdds",
         |    "assets": [],
         |    "additionalRegisters": {},
         |    "spentTransactionId": "14ad9a359e99654188eeb64eb1c597e704cb8278a0c9d05cb597cabf0cbbb0c2",
         |    "mainChain": true
         |}
         |""".stripMargin
    ).toOption.get

    val swapN2TSellOrder: SwapV1 = SwapV1(
      swapN2TSell,
      PoolId.unsafeFromString("f40afb6f877c40a30c8637dd5362227285738174151ce66d6684bc1b727ab6cf"),
      PublicKeyRedeemer(PubKey.unsafeFromString("02b3dcf362d632cafa9d8d0ae64d75a1ad7fcc938122433f57ad3fcd842e586a80")),
      SwapParams(
        AssetAmount.native(7300000000L),
        AssetAmount(
          TokenId.unsafeFromString("9a06d9e545a41fd51eeffc5e20d818073bf820c635e2a9d922269913e0de369d"),
          172883468
        ),
        3470545836111987L,
        100000000000000000L
      ),
      2000000,
      Version.V1
    )

    val swapN2TBuy: Output = decode[Output](
      s"""
         |{
         |    "boxId": "f588ae44ae93835d26846c6d4d950912e4e2ef0ffa1b644ab38d037ec416a0e3",
         |    "transactionId": "7ee2d87a42a4cdb891d838571873f1dc124731da23bdd17a306c6bc2f53c7f95",
         |    "blockId": "3a6251893234f404c144c03c0bdd288fbfa1db6d405e450a741a944b1b115e66",
         |    "value": 7260000,
         |    "index": 0,
         |    "globalIndex": 28279398,
         |    "creationHeight": 982326,
         |    "settlementHeight": 982328,
         |    "ergoTree": "19b5031508cd038948e9e824eee9f4f9e2047e8cde4d8d5845d70b1450878a6ea7cbbac75bcf800400040404060402058080d0d88bdea2e3020596ebf292959780e302040004000e20666be5df835a48b99c40a395a8aa3ea6ce39ede2cd77c02921d629b9baad820005dcc3bbea3a04ca0f060101040404d00f04ca0f0e691005040004000e36100204a00b08cd0279be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798ea02d192a39a8cc7a701730073011001020402d19683030193a38cc7b2a57300000193c2b2a57301007473027303830108cdeeac93b1a5730405000500058092f4010100d802d6017300d602b2a4730100eb027201d195ed92b1a4730293b1db630872027303d804d603db63087202d604b2a5730400d6059d9c7e99c17204c1a7067e7305067e730606d6068cb2db6308a773070002edededed938cb2720373080001730993c27204d072019272057e730a06909c9c7ec17202067e7206067e730b069c9a7205730c9a9c7e8cb27203730d0002067e730e067e9c72067e730f050690b0ada5d90107639593c272077310c1720773117312d90107599a8c7207018c72070273137314",
         |    "address": "Dxg1z3EWag7rRk72uTCoyVGFg2BrgAUrawZKYaVVNrMSuVSSrHx9pD7aGwT7i5jR3sAUepDzVx3XHwsfNL7HGKxEqRErqB2DSWHcSKx5ap5ne1A9G8YgoENTynW5Cpv1H4wmDcvHD21VMY1jxRe1KQMY2mPHZbw79XHm3g4pbEfLWCwkmvcHcqRxW75bKvagAQCq4pdbmJKk757uWrxS4zASGV5waZYZJz1oeJ9xxpmgfn4aFm8wSjb7PsdMBkwnaemkYHmguCVcnzVy8gxreDMbco3FJ5ZLoBbuLhw5RbWknSDDzJc4afV8nBKp8Ag8uf93nA8GQ5uqBMfi2DeJd7eeSKy3aHDJDhRRae8ofygecm4hQv18GmmihChXDErakhwLVgVat8Lz5gCmfYuNUTBaFtmu6bCzg9VovmegTerfPDwQoNhiKJtpGY2Dw7SozMJvRi1hFpFuyDLaxTFSpXa6yC7MAVbHk2BtSZu89hGhL4iciHgm2ynM3WZFaaAAsU47voKkppEh2St4TmKcnXdqkLHGFUQASv3VkiyvoBbnmyjNQwKCLQZmUKmatcobf9W87C2PE9tb5DhbBHMioJVA4ycbujL",
         |    "assets": [
         |        {
         |            "tokenId": "1fd6e032e8476c4aa54c18c1a308dce83940e8f4a28f576440513ed7326ad489",
         |            "index": 0,
         |            "amount": 30912561,
         |            "name": "Paideia",
         |            "decimals": 4,
         |            "type": "EIP-004"
         |        }
         |    ],
         |    "additionalRegisters": {},
         |    "spentTransactionId": "8834cbde71c70499c68c2810454fb296299cf117a1802dfd80a74f9b0e69ec85",
         |    "mainChain": true
         |}
         |""".stripMargin
    ).toOption.get

    val swapN2TBuyOrder: Swap = SwapV1(
      swapN2TBuy,
      PoolId.unsafeFromString("666be5df835a48b99c40a395a8aa3ea6ce39ede2cd77c02921d629b9baad8200"),
      PublicKeyRedeemer(PubKey.unsafeFromString("038948e9e824eee9f4f9e2047e8cde4d8d5845d70b1450878a6ea7cbbac75bcf80")),
      SwapParams(
        AssetAmount(
          TokenId.unsafeFromString("1fd6e032e8476c4aa54c18c1a308dce83940e8f4a28f576440513ed7326ad489"),
          30912561
        ),
        AssetAmount.native(7896264942L),
        75985292338485L,
        100000000000000000L
      ),
      2000000,
      Version.V1
    )
  }

  object deposit {

    val depositN2T: Output = decode[Output](
      """
        |{
        |    "boxId": "fcb6f77e27e3289dd93f05d4ac0e4e333cb657ff18e3215dbe8443ffd9a8024b",
        |    "transactionId": "ef8ba9d6bdae3f5a5afd696d62aab118f6b4ffe1e4b0be86afe1a755b4de3d29",
        |    "blockId": "6d31bd072c044aebceb4feacac385fd5931cb16aabb9cad68fc5753cfb5ab8e3",
        |    "value": 3319036391,
        |    "index": 0,
        |    "globalIndex": 28289125,
        |    "creationHeight": 982501,
        |    "settlementHeight": 982503,
        |    "ergoTree": "19bd041808cd02d54811cfc9a3e85dbc14ea1e6113109bc21495c95deeab909946b0a3fa2076db0400058e88c0d718040404060402040205feffffffffffffffff0104040400040004000e20f40afb6f877c40a30c8637dd5362227285738174151ce66d6684bc1b727ab6cf040404020580b6dc05058e88c0d7180580b6dc0501000e691005040004000e36100204a00b08cd0279be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798ea02d192a39a8cc7a701730073011001020402d19683030193a38cc7b2a57300000193c2b2a57301007473027303830108cdeeac93b1a5730405000500058092f4010100d803d6017300d602b2a4730100d6037302eb027201d195ed92b1a4730393b1db630872027304d80bd604db63087202d605b2a5730500d606b27204730600d6077e9973078c72060206d6087ec1720206d6099d9c7e72030672077208d60ab27204730800d60b7e8c720a0206d60c9d9c7e8cb2db6308a773090002067207720bd60ddb63087205d60eb2720d730a00ededededed938cb27204730b0001730c93c27205d0720195ed8f7209720c93b1720d730dd801d60fb2720d730e00eded92c172059999c1a7730f7310938c720f018c720a01927e8c720f02069d9c99720c7209720b720795927209720c927ec1720506997e99c1a7731106997e7203069d9c997209720c720872077312938c720e018c720601927e8c720e0206a17209720c90b0ada5d9010f639593c2720f7313c1720f73147315d9010f599a8c720f018c720f0273167317",
        |    "address": "5KevXenwFgDaN9wNR8c1PtmFzAqPhBW5e1vVMJFhiv6YMnoaDNM72CDUDwrtToWpRzsxGGaLtjmtCoHiVnDHcwos6drxyiTTYPfi44S7fE6Lej6oUH2SRkhKju2zh1BjXbZHc23gc4Too6i2nQBVdPDngc8ajnB3N7iC49jq7NcfnKaJVD43Gtwj7XqKfUAFpaxpyS81QPie2ksoL7nTnv9aupXYNhPDLHRT5ahCd9eWRtbogVJKJ1Ku2g8VeGRCdtaLvH3Vbvp4ifeAMEXnG7x1VUTwcXeHwPpfBs268hYKBST7sMzW5xsH99TQM1qyuj6r1L8BJkhqdJYRM1zcmqQbrM5ndyCXMcyXSo1pY2gstmNn9bMMqFqiQNMxCTpW2vgx3i48XC2VaujzvXckZjsWJt2QW7zAmp9WhvMoR4JwtAPGiwGuZb3rYW8WDdcpn7Um9KPS3qygJH9Edggtp71kTmWJd7Zwa9VxwRxCTFMGJP3QknQmCXi9tXiDRBEquk9nuLTrv2D99QJJ5f4MHRhi1QrtrUV9yvXioX8R84NSkunDqqZhdFNeqpmfEVeyjRmRcE325SuNqq1XSTHzZvqWYKAeYixpQzYL3hLUPAs46MbFGcumjQD1Uhk2rwDYtJ7XKiWpPtzxtqiwqN68rg8JhemKJ1JsBgzT2so8nsyeAsDehShZgrMA2C4SSuXBRnESGduRHEz67yc3JtVTsdxRst5QNoZTzVKKk3EouqCwnSautAvQ7aEX5pCpmSTszqXBYtY4JFd47NKxVheRapeu9",
        |    "assets": [
        |        {
        |            "tokenId": "9a06d9e545a41fd51eeffc5e20d818073bf820c635e2a9d922269913e0de369d",
        |            "index": 0,
        |            "amount": 80528299,
        |            "name": "SPF",
        |            "decimals": 6,
        |            "type": "EIP-004"
        |        }
        |    ],
        |    "additionalRegisters": {},
        |    "spentTransactionId": "ed4070fefa076e994fe90e0289ab4922ca898834bb61e2796bfe86c0fa55ddb8",
        |    "mainChain": true
        |}
        |""".stripMargin
    ).toOption.get

    val expectedN2TDepositV1: AmmDepositV1 = AmmDepositV1(
      depositN2T,
      ERG(6000000),
      PoolId.unsafeFromString("f40afb6f877c40a30c8637dd5362227285738174151ce66d6684bc1b727ab6cf"),
      PublicKeyRedeemer(PubKey.unsafeFromString("02d54811cfc9a3e85dbc14ea1e6113109bc21495c95deeab909946b0a3fa2076db")),
      AmmDepositParams(
        AssetAmount.native(3312976391L),
        AssetAmount(
          TokenId.unsafeFromString("9a06d9e545a41fd51eeffc5e20d818073bf820c635e2a9d922269913e0de369d"),
          80528299
        )
      ),
      2000000,
      Version.V1
    )
  }

  object redeem {

    val output: Output = decode[Output](
      """
        |{
        |    "boxId": "79d61f8f5b5627766a60ea27d68a1a6f83347c6dbd12487e914d19b1742a1951",
        |    "transactionId": "ae431359b282a9ae4beeb0f1ec45abe666a95defa6e78addea973dfebfee4f9e",
        |    "blockId": "b412d83f8d86a781b59b1f41fe69bd8b1abcb17f9582aa20ea9ea526865f6383",
        |    "value": 6060000,
        |    "index": 0,
        |    "globalIndex": 28302592,
        |    "creationHeight": 982958,
        |    "settlementHeight": 982961,
        |    "ergoTree": "19b1031208cd02c5cade949960ca2119f1ff2f08a5211410071c74e544f2fdf90edb549e36efd9040004040406040204000404040005feffffffffffffffff01040204000e2004f468174eddbc68bce3f0965dd14bc6ed1443f5a405ec7f7f9925d999370b970580b6dc050e691005040004000e36100204a00b08cd0279be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798ea02d192a39a8cc7a701730073011001020402d19683030193a38cc7b2a57300000193c2b2a57301007473027303830108cdeeac93b1a5730405000500058092f4010100d802d6017300d602b2a4730100eb027201d195ed92b1a4730293b1db630872027303d806d603db63087202d604b2a5730400d605b2db63087204730500d606b27203730600d6077e8cb2db6308a77307000206d6087e9973088cb272037309000206ededededed938cb27203730a0001730b93c27204d07201938c7205018c720601927e9a99c17204c1a7730c069d9c72077ec17202067208927e8c720502069d9c72077e8c72060206720890b0ada5d90109639593c27209730dc17209730e730fd90109599a8c7209018c72090273107311",
        |    "address": "2ysN6BW5GJ6oxEsXnjJvfDG16nYJJbxDQ8FtqbTT3hKUBoZV681MXu1ZL28smb7ZizwBrQkgo49zWHyEaNYTHZ81zyuVoaVhpcSUGR2LGcy81wrGanAojbzHTRbELVYmaqFjzV8oNFEmbZLGHdojSRihLS5jdBgR6GCuvNeWCmD6FW94Fc3vsb6LUPVKPcjetL5sfvFnSHFEwYkfT5EXNBd2Vtoe7JoCzxRsUKaiYGbUGnEnMX8Z5AJmsBphP9h3eeCmskrfkuGfGC4w5Md4Qyd8nmiL1zxsPvtUgbVDbRTcZETdPWmRmgp3df5k3SazKxCJUD21gswmoBw5nNBun1bEP7Roj3Xhv5GBvLyQU1u36dxZU7E3w8mL6izjGAPW17gmmb3ynVfLcAVetr8YGCdksAZcUnZr23X7WmGrV9wBvC6tFYFtWpgCdoLoGmxvQhpmeLyVPBeGrHg79uqcQmk3h8gPWgzqwgaxV3nmv4cXrsJsowFgJPE3LugATbCMsRWqLBpW12bfMnvu5fchpYhfNzc7qD17sumkcYkz9z7FTTD8GgdH1o3GdjK96EgC9aEnhoXpMW4MsS1e9J3ehaPkcG",
        |    "assets": [
        |        {
        |            "tokenId": "e7021bda9872a7eb2aa69dd704e6a997dae9d1b40d1ff7a50e426ef78c6f6f87",
        |            "index": 0,
        |            "amount": 3820260697,
        |            "name": "Ergo_ErgoPOS_LP",
        |            "decimals": 0,
        |            "type": "EIP-004"
        |        }
        |    ],
        |    "additionalRegisters": {},
        |    "spentTransactionId": "7deb7c7cdd51aaa4a82ed0da2ec90751cb7ee48d09382f60810dcd10c48e4348",
        |    "mainChain": true
        |}
        |""".stripMargin
    ).toOption.get

    val redeem: RedeemV1 =
      RedeemV1(
        output,
        ERG(6000000),
        PoolId.unsafeFromString("04f468174eddbc68bce3f0965dd14bc6ed1443f5a405ec7f7f9925d999370b97"),
        PublicKeyRedeemer(
          PubKey.unsafeFromString("02c5cade949960ca2119f1ff2f08a5211410071c74e544f2fdf90edb549e36efd9")
        ),
        RedeemParams(
          AssetAmount(
            TokenId.unsafeFromString("e7021bda9872a7eb2aa69dd704e6a997dae9d1b40d1ff7a50e426ef78c6f6f87"),
            3820260697L
          )
        ),
        2000000,
        Version.V1
      )

  }

}