package fi.spectrum.parser.amm.order.v3

import fi.spectrum.core.domain.analytics.Version
import fi.spectrum.core.domain.order.Fee.SPF
import fi.spectrum.core.domain.order.Order.Deposit.AmmDeposit._
import fi.spectrum.core.domain.order.Order.Redeem.AmmRedeem.RedeemV3
import fi.spectrum.core.domain.order.Order.Swap.SwapV3
import fi.spectrum.core.domain.order.Redeemer.{ErgoTreeRedeemer, PublicKeyRedeemer}
import fi.spectrum.core.domain.order._
import fi.spectrum.core.domain.transaction.Output
import fi.spectrum.core.domain.{AssetAmount, PubKey, SErgoTree, TokenId}
import io.circe.parser.decode

object N2T {

  object swap {

    val outputSellNotY = decode[Output](
      s"""
         |{
         |    "boxId": "4b8c85f4d083cd11653954217a02366c4b61004f84fba2df6d5787735225a7bd",
         |    "transactionId": "ea4cb7e08e4ef2cfa44412a73fdf7b8fff9310510b889a33fba2affe6636a9e7",
         |    "blockId": "23714bdd50e5177e6913484e51715a3daf9208b51b8f9e769ac6624ae3465610",
         |    "value": 90000400000,
         |    "index": 0,
         |    "globalIndex": 28301646,
         |    "creationHeight": 982911,
         |    "settlementHeight": 982914,
         |    "ergoTree": "199705210400058080a0f6f4acdbe01b0596ed98d0ad8ad4e01b058090d8c69e0504ca0f08cd029e04b6a9d6503b701a3c3dfb5e7b48132750b6858dd19b4921adc15681c19db6040404060402040001000588e01504000e2046463b61bae37a3f2f0963798d57279167d82e17f78ccd0ccedec7e49cbdbbd10e240008cd029e04b6a9d6503b701a3c3dfb5e7b48132750b6858dd19b4921adc15681c19db60e20089990451bb430f05a85f4ef3bcb6ebf852b3d6ee68d86d78658b9ccef20074f059a80cf99450101010105f015060100040404020e209a06d9e545a41fd51eeffc5e20d818073bf820c635e2a9d922269913e0de369d0101040406010104d00f0e691005040004000e36100204a00b08cd0279be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798ea02d192a39a8cc7a701730073011001020402d19683030193a38cc7b2a57300000193c2b2a57301007473027303830108cdeeac93b1a5730405000500058092f4010100d804d601b2a4730000d6027301d6037302d6049c73037e730405eb027305d195ed92b1a4730693b1db630872017307d806d605db63087201d606b2a5730800d607db63087206d608b27207730900d6098c720802d60a95730a9d9c7e997209730b067e7202067e7203067e720906edededededed938cb27205730c0001730d93c27206730e938c720801730f92720a7e7310069573117312d801d60b997e7313069d9c720a7e7203067e72020695ed91720b731492b172077315d801d60cb27207731600ed938c720c017317927e8c720c0206720b7318909c7e8cb2720573190002067e7204069c9a720a731a9a9c7ec17201067e731b067e72040690b0ada5d9010b639593c2720b731cc1720b731d731ed9010b599a8c720b018c720b02731f7320",
         |    "address": "3zMqFY8VVj3yBZiDotmE6ZmsvqqAPpucWbn2gkRVDRjty3titnYpWZTGENdX5k2aDfPXTyf1Eh5s693LrArVZiNjTUJjMR9yQxU2RaRbK47XyfGw94ttD7k7JmaBh7jfFqCfx5EX1dq3PQxyZZjsi5k6CJopvxFjFtTvz95afYE74VnDXJxFJevmzTbJhHScz7M6mLYL3eH1LtugRS4DebNsWpSCZHc4RAKBUfsteyYcr6h6uyXptHW6TQy7FfiWRmAjDqXZNc2x6y8qNQhZQucTuQdKjnA5VkiPb4AyonoygktiDsDzGWvZbMG7fsegkqFkHEryiprwihhU74xKebUnMZAUec1E8mckK5rvSQRJ7aVaYj1Pm75wE3gmTJ2y9MsULgBKUiMtetDDEvjZaZHS4n1os3SppeX4jeN8juZ1ePJ7tAs8KADi4eK7JV37o4eMaEfXEYNnDqUJqKksUCnZimic7QcbPaVqLKLCK3XKCueLphCMXq3EpvtsdxMF9uBptYswUizi1qBRzRZqETwrS6F8Dt3XFhN6GGdpHHhvBuNT1JGrzriLfNotSaHyK9JDvN7b2tzSZ6n6WesTLoQ8DFH8hPoPNfxvnZH5PAd8StvZK2h5eQ2rNRRaXevhyLvAkiCTqqqRBranuW1WsN9GRVsS1dh2SNyicQaeFKafBcWrULnVazB5uv7onw69H9e71zCQCruQL71b15DNxgZmznDG5U6wJaSVvZg98oCVd7aa1BjWit75epGSYcvACSz6J3agghjrpn3pbczSTpLTBwPCKubVqvhgC8FeW63Gn5C1wmJf2YnLRyTHimber7Wg8UUtWCavktj2hrHFa8Gzww1Kz491nXyy4cWti6CuwRvfc2XkHus9CQzPX9c8Jx8QmNz8swYi2X5MfRUv",
         |    "assets": [
         |        {
         |            "tokenId": "9a06d9e545a41fd51eeffc5e20d818073bf820c635e2a9d922269913e0de369d",
         |            "index": 0,
         |            "amount": 178180,
         |            "name": "SPF",
         |            "decimals": 6,
         |            "type": "EIP-004"
         |        }
         |    ],
         |    "additionalRegisters": {},
         |    "spentTransactionId": "dff21c0da1c2d24a6ed6b9d47bfc5845d36d81062b6ed1dd1712febd07938dd4",
         |    "mainChain": true
         |}
         |""".stripMargin
    ).toOption.get

    val swapSellNotY = SwapV3(
      outputSellNotY,
      PoolId.unsafeFromString("46463b61bae37a3f2f0963798d57279167d82e17f78ccd0ccedec7e49cbdbbd1"),
      PublicKeyRedeemer(
        PubKey.unsafeFromString(
          "029e04b6a9d6503b701a3c3dfb5e7b48132750b6858dd19b4921adc15681c19db6"
        )
      ),
      SwapParams(
        AssetAmount(
          TokenId.unsafeFromString("0000000000000000000000000000000000000000000000000000000000000000"),
          90000000000L
        ),
        AssetAmount(
          TokenId.unsafeFromString("089990451bb430f05a85f4ef3bcb6ebf852b3d6ee68d86d78658b9ccef20074f"),
          9287884813L
        ),
        15986847704245L,
        1000000000000000000L
      ),
      2000000,
      178180,
      Version.V3
    )

    val outputSellSpf = decode[Output](
      s"""
         |{
         |    "boxId": "a1f9f348c33d356002b4f919f3c17ebf42a4c20214823a2f4d1017e89353fbab",
         |    "transactionId": "9c5f82be3aefe85e488ce0b550c0526b4f3cfde58e85f831179c52cf30b03087",
         |    "blockId": "c06af4137428486c3a03486b7e68528d4a053230db637e5b116610b952fad62b",
         |    "value": 150000400000,
         |    "index": 0,
         |    "globalIndex": 28301486,
         |    "creationHeight": 982907,
         |    "settlementHeight": 982909,
         |    "ergoTree": "199305210400058080b4ccd4dfc60305ddf0e6ebdf87820e0580f092cbdd0804c60f08cd029e04b6a9d6503b701a3c3dfb5e7b48132750b6858dd19b4921adc15681c19db6040404060402040001000588e01504000e209916d75132593c8b07fe18bd8d583bda1652eed7565cf41a4738ddd90fc992ec0e240008cd029e04b6a9d6503b701a3c3dfb5e7b48132750b6858dd19b4921adc15681c19db60e2003faf2cb329f2e90d6d23b58d91bbb6c046aa143261cc21f52fbe2824bfcbf040594d5030101010105f015060100040404020e209a06d9e545a41fd51eeffc5e20d818073bf820c635e2a9d922269913e0de369d0101040406010104d00f0e691005040004000e36100204a00b08cd0279be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798ea02d192a39a8cc7a701730073011001020402d19683030193a38cc7b2a57300000193c2b2a57301007473027303830108cdeeac93b1a5730405000500058092f4010100d804d601b2a4730000d6027301d6037302d6049c73037e730405eb027305d195ed92b1a4730693b1db630872017307d806d605db63087201d606b2a5730800d607db63087206d608b27207730900d6098c720802d60a95730a9d9c7e997209730b067e7202067e7203067e720906edededededed938cb27205730c0001730d93c27206730e938c720801730f92720a7e7310069573117312d801d60b997e7313069d9c720a7e7203067e72020695ed91720b731492b172077315d801d60cb27207731600ed938c720c017317927e8c720c0206720b7318909c7e8cb2720573190002067e7204069c9a720a731a9a9c7ec17201067e731b067e72040690b0ada5d9010b639593c2720b731cc1720b731d731ed9010b599a8c720b018c720b02731f7320",
         |    "address": "TVYqK6vZhdD2e6rtmgWrGV1mDrGAVyn49BdYhhvSuL2LpBpjgLZXP9Yx5YFjfrEPwYGU2tBSZPsw6hkvc6UASbu3oALgzYdtBbbqxieLT6jff9bmBgJrU144jGEubG51C6jZB2F7Tof4LJv3YDBVmfMXGdJbBLtYsVEn9HNgRK26gNUJBjHSvGEcgySAPyA69hW7kpgc6pS1kGQiFzJjWsUMQ2xJ4KMikKRJ52ASBgXJAzn1uuXzQpBEsBmDD7hn8y6nj4ToXT4pt6DzKhYDbiZBKs7ckpZJDXj3McJgPZ3TtbUK3nUsq4HjP85P3JZKinL9deaR1DTeN38zKvUCNu3RXZQNmNTpkqZH6HXBppeoqju6tCdDAWxtkwgZFkSm8U3oLL5Vrzr3ffFbJQfLUuwR5E5xskbzq5CU22XQwdmV4ePVWrwxUtifE9EhtS9rmc8htEueDhU7fkFB9RwbQ4mEc8FvURPCpGF6Gem2nEGwAVNNbBuWR5SGuHmk9t6cEwWyhhqg4kpiTS4U4wZu7c2uDoEcKBxZQZ6kGorar5P9ZLeMA4NyGC94BcM5n3JfFzvNZkxrwrCWYix2tgpWEgFoQomGMB1zQHE2yLsG9T6E61BZ8V82Gxfrwu5VsjpezqswyzZNS3iKUwJkSj4cjzJiUdvQtWDnbZMwucsXBbadYpWdkePFZrRoB4v5cX8yKaem5cibxb5br3tB4GejkHrWEeyejo17K3bqGsq61XD9ddtsMaqC56HyC7L33CaEQ9JLnbvbhXEuvGiQBEaHAz9yPYvjkEXvKxBcXzB4yxBN2sinFzesYftvPQQiraFFH4aLQ2Lgqj7ETGtAijz1yxQcojXbC2t5RoyJF7SEWVs4erHNfXxNCGZmjj9NipqftuD4gpNENyoZQK",
         |    "assets": [
         |        {
         |            "tokenId": "9a06d9e545a41fd51eeffc5e20d818073bf820c635e2a9d922269913e0de369d",
         |            "index": 0,
         |            "amount": 178180,
         |            "name": "SPF",
         |            "decimals": 6,
         |            "type": "EIP-004"
         |        }
         |    ],
         |    "additionalRegisters": {},
         |    "spentTransactionId": "6f3bac8b460f24fe2d7e92055030284d143d1fdac91a306525084a0b67809057",
         |    "mainChain": true
         |}
         |""".stripMargin
    ).toOption.get

    val swapSellSpf = SwapV3(
      outputSellSpf,
      PoolId.unsafeFromString("9916d75132593c8b07fe18bd8d583bda1652eed7565cf41a4738ddd90fc992ec"),
      PublicKeyRedeemer(
        PubKey.unsafeFromString(
          "029e04b6a9d6503b701a3c3dfb5e7b48132750b6858dd19b4921adc15681c19db6"
        )
      ),
      SwapParams(
        AssetAmount(
          TokenId.unsafeFromString("0000000000000000000000000000000000000000000000000000000000000000"),
          150000000000L
        ),
        AssetAmount(
          TokenId.unsafeFromString("03faf2cb329f2e90d6d23b58d91bbb6c046aa143261cc21f52fbe2824bfcbf04"),
          30026
        ),
        4945180843269167L,
        1000000000000000L
      ),
      2000000,
      178180,
      Version.V3
    )

    val outputBuyNoSpf = decode[Output](
      s"""
         |{
         |    "boxId": "51a706d115b7ed003636214a6d30f5f28c915c96212a93c856c0e01a51fb36fa",
         |    "transactionId": "5333ed00b7f124dbf7ec9f781d5a0afb6656adbd079a63b8f681f5f043e461f1",
         |    "blockId": "f2678a4cb75da544b09565d3e619da2a3a0d1fe95a26a7788090e56021752d30",
         |    "value": 400000,
         |    "index": 0,
         |    "globalIndex": 28288533,
         |    "creationHeight": 982487,
         |    "settlementHeight": 982489,
         |    "ergoTree": "19a4041a04000580c38ee00104ca0f08cd025031ca056b8f9e910a9879059d5edf27a7322285832a60aed9655d5a0114a60304040406040205bae115058080a0f6f4acdbe01b05b2c0ae9e8cbf0704000e20f40afb6f877c40a30c8637dd5362227285738174151ce66d6684bc1b727ab6cf0e240008cd025031ca056b8f9e910a9879059d5edf27a7322285832a60aed9655d5a0114a60305faf5aa964306010004000e209a06d9e545a41fd51eeffc5e20d818073bf820c635e2a9d922269913e0de369d01010502040404d00f0e691005040004000e36100204a00b08cd0279be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798ea02d192a39a8cc7a701730073011001020402d19683030193a38cc7b2a57300000193c2b2a57301007473027303830108cdeeac93b1a5730405000500058092f4010100d802d601b2a4730000d6029c73017e730205eb027303d195ed92b1a4730493b1db630872017305d804d603db63087201d604b2a5730600d60599c17204c1a7d606997e7307069d9c7e7205067e7308067e730906ededededed938cb27203730a0001730b93c27204730c927205730d95917206730ed801d607b2db63087204730f00ed938c7207017310927e8c7207020672067311909c7ec17201067e7202069c7e9a72057312069a9c7e8cb2720373130002067e7314067e72020690b0ada5d90107639593c272077315c1720773167317d90107599a8c7207018c72070273187319",
         |    "address": "3S7gJFgEymiqUNMDKzAvAAAFfAhVKbo5U4wfZrRoTrs3Eowgk5oDks353SoTFaKA6EuSUBqB5fk17xyFV68mRBSSyGNTGYBHdKYSyvwPuCiX3k7ob73kw9zVHgSk2s8XVtQtNgC54d4Vag4LKMkjuw1rQTVEfzHKWcrEA4nsdTsC9YGCm4H2aMiCo33ksGtn2RKcQMHSfaCnwSxa7xa8VoJ9BTVcvZzG81Vn55dLQZTZMoztZ4gAKNFdyfwJfbxYYTpbMQSxHjR4L9fxB32bxKNDWfmA8ZbpUFovuPZc3Jfip8iBF1WcW4NtdSXFb8HhwaLj1uGyYRuYhBa2e7bBCB7q7eaYYajKFy8F3T8Tg56TZkW4nF84NhbPEiC1beLLsJAoV8aKfpg2GUgDycbLvSHYQW5cs5VtNKeT8rvfbvP9KLsMokb9DHFt84LEX98cshbEwh8pNLcbRaLPKxb2ytYkNn95BaS7P6SvXXtfoGu2U6QGyRt6K7829unzcYMN4QcX6Qtzcj3sPpuQ6g9UrF5CFRXvbBZcWHbLtRkQuaSnojbMwJEXx3u6XHXKqY5z5mKtBrq2cy6atu72Z4xXUodhAPYA8LV73gcp9NCDxWWZRLhT1dMYuh6BTz7RJMZoz9WjoycP9dnGmdUJXkXr4EfcXYc5RCoErfVqpqfcL48bA44SBxoabQW7oMweH2b5r7eEio4GCdobEYVqeb9VHffE6iBk2pvM9UhNs6JCAF83ahx27XtDGe6",
         |    "assets": [
         |        {
         |            "tokenId": "9a06d9e545a41fd51eeffc5e20d818073bf820c635e2a9d922269913e0de369d",
         |            "index": 0,
         |            "amount": 235178269,
         |            "name": "SPF",
         |            "decimals": 6,
         |            "type": "EIP-004"
         |        }
         |    ],
         |    "additionalRegisters": {},
         |    "spentTransactionId": "218be185d21681b05a457d99e5d65b4a7f46aab39fe66eb2e38baf5678a06d10",
         |    "mainChain": true
         |}
         |""".stripMargin
    ).toOption.get

    val swapBuyNoSpf = SwapV3(
      outputBuyNoSpf,
      PoolId.unsafeFromString("f40afb6f877c40a30c8637dd5362227285738174151ce66d6684bc1b727ab6cf"),
      PublicKeyRedeemer(
        PubKey.unsafeFromString(
          "025031ca056b8f9e910a9879059d5edf27a7322285832a60aed9655d5a0114a603"
        )
      ),
      SwapParams(
        AssetAmount(
          TokenId.unsafeFromString("9a06d9e545a41fd51eeffc5e20d818073bf820c635e2a9d922269913e0de369d"),
          235000000
        ),
        AssetAmount.native(9016008061L),
        16477136998425L,
        1000000000000000000L
      ),
      2000000,
      178269,
      Version.V3
    )

    val outputBuySpf = decode[Output](
      s"""
         |{
         |    "boxId": "b7aaef19dd8f4d62b91122691cf16bc31f163ca07928079b61b2ede34fd68646",
         |    "transactionId": "015ba6ef78308878ce504092a711fb86351aa890604caa02929681df9598f299",
         |    "blockId": "dfae20ddeada3dbddf658ac565c4c8a9142da4b1319a4f4ea35ed793869b248e",
         |    "value": 400000,
         |    "index": 0,
         |    "globalIndex": 28278493,
         |    "creationHeight": 982300,
         |    "settlementHeight": 982302,
         |    "ergoTree": "19a3041a040005fccf8a0104c80f08cd0204c0adc32d09ae48f00ca495fc783ea7ee3c5f1e4d92186c77decee7e8fd00f804040406040205c8e815058080d0d88bdea2e30205e6cd99ddf61104000e20d7868533f26db1b1728c1f85c2326a3c0327b57ddab14e41a2b77a5d4c20f4b20e240008cd0204c0adc32d09ae48f00ca495fc783ea7ee3c5f1e4d92186c77decee7e8fd00f805808787a5e80206010004000e209a06d9e545a41fd51eeffc5e20d818073bf820c635e2a9d922269913e0de369d01010502040404d00f0e691005040004000e36100204a00b08cd0279be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798ea02d192a39a8cc7a701730073011001020402d19683030193a38cc7b2a57300000193c2b2a57301007473027303830108cdeeac93b1a5730405000500058092f4010100d802d601b2a4730000d6029c73017e730205eb027303d195ed92b1a4730493b1db630872017305d804d603db63087201d604b2a5730600d60599c17204c1a7d606997e7307069d9c7e7205067e7308067e730906ededededed938cb27203730a0001730b93c27204730c927205730d95917206730ed801d607b2db63087204730f00ed938c7207017310927e8c7207020672067311909c7ec17201067e7202069c7e9a72057312069a9c7e8cb2720373130002067e7314067e72020690b0ada5d90107639593c272077315c1720773167317d90107599a8c7207018c72070273187319",
         |    "address": "YyLFSXrJzwxkf7JAEgrxixib39rXWt4UP1Bv2XFukNobVsmtqvjoqAf2cSYJawN5iwPJ1w4WZZMHLuBR8Sb7NeGsZ8yuedewrk3p8anHs672T37Lu8et1j7kASpih8N9kmM22Sc7dwtyQvow7WsQAHvJTTXKRpU4JesNNZ2YpxrTySieJGkNwauQ8qfF7CAyzYr6Q3rNiTxj5cWFmNKHN82tnuXFf1gto7NbUayL1T7avoVn1qRutwKomi5LBtzxHedKagiij5zqDPsriszCAuiirqCevBQHw9uqZXNYHhqzJG52tpuqBdMybVbKuHoUKeaqdydeeDnDyKDBNVavPjXXk1J1c36VETzSvTyy4dSdTeMSdGB2A7MYihwvfyMMhyJ9Nnp9JN3rjM7uqufDUwD1WXBAeoqng3bmCGMLU1HHb1YxJaAt6jWxm8DadnirwV31Q9Ck6NutEJYeM4UKg6nPqky2nkG4KzjDT8Sv2MTyMUVedCB651JbSvpCeeZkEZzu2ZNCzYHLDWoV8BczhtMdpp1Lb37CT25N52FafmkXasHz7mYMyqitv7yy746xYSaWNMYvnQQ3TroTPaeBaVGoqJbuuMpHRf1k1hTtXjrVzA5i9fdS81GYBS9iWz2sVRQcdXVCAzQokEHSJBmNdxVWULnGdvSvz81T98i51jpPUNPqKMeet9WMSRFfB1oamPyFKgrxmeTpMx5KeswBeq7Eso6Nkg5tZPNssB2jGAB9XFjVV5m1o",
         |    "assets": [
         |        {
         |            "tokenId": "d71693c49a84fbbecd4908c94813b46514b18b67a99952dc1e6e4791556de413",
         |            "index": 0,
         |            "amount": 1135614,
         |            "name": "ergopad",
         |            "decimals": 2,
         |            "type": "EIP-004"
         |        },
         |        {
         |            "tokenId": "9a06d9e545a41fd51eeffc5e20d818073bf820c635e2a9d922269913e0de369d",
         |            "index": 1,
         |            "amount": 178724,
         |            "name": "SPF",
         |            "decimals": 6,
         |            "type": "EIP-004"
         |        }
         |    ],
         |    "additionalRegisters": {},
         |    "spentTransactionId": "3de3a42741c222996e5dd7a5e8e67e9a7b0b17817977bd65672f1450c1e48d9e",
         |    "mainChain": true
         |}
         |""".stripMargin
    ).toOption.get

    val swapBuySpf = SwapV3(
      outputBuySpf,
      PoolId.unsafeFromString("d7868533f26db1b1728c1f85c2326a3c0327b57ddab14e41a2b77a5d4c20f4b2"),
      PublicKeyRedeemer(
        PubKey.unsafeFromString("0204c0adc32d09ae48f00ca495fc783ea7ee3c5f1e4d92186c77decee7e8fd00f8")
      ),
      SwapParams(
        AssetAmount(
          TokenId.unsafeFromString("d71693c49a84fbbecd4908c94813b46514b18b67a99952dc1e6e4791556de413"),
          1135614
        ),
        AssetAmount.native(48357237184L),
        307993195379L,
        100000000000000000L
      ),
      2000000,
      178724,
      Version.V3
    )
  }

  object deposit {

    val outputSpfY = decode[Output](
      s"""
         |{
         |    "boxId": "fa2a51578c9fbf3e4f3d9520f1242a9c7020c02b70775c37c2cee963fd9b1c1b",
         |    "transactionId": "638887e23e7e6824b2ad36e8f98ad73817312c811572af8f215e46038827928b",
         |    "blockId": "b0837dfba5f90ec6981258e8f8f0bf9e488655b2d651fb9c26b062d1fc446c51",
         |    "value": 41984305472,
         |    "index": 0,
         |    "globalIndex": 28641415,
         |    "creationHeight": 992011,
         |    "settlementHeight": 992013,
         |    "ergoTree": "19c6041704000580bbfce6b80208cd0215673d844f85d763aac8c0dd4ea83fa1f11438fa5d683e7e6b1ac95586203ea2040404060402040205feffffffffffffffff01040405ca6d040004000e209916d75132593c8b07fe18bd8d583bda1652eed7565cf41a4738ddd90fc992ec0e240008cd0215673d844f85d763aac8c0dd4ea83fa1f11438fa5d683e7e6b1ac95586203ea20404040205c0b80201000e691005040004000e36100204a00b08cd0279be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798ea02d192a39a8cc7a701730073011001020402d19683030193a38cc7b2a57300000193c2b2a57301007473027303830108cdeeac93b1a5730405000500058092f4010100d802d601b2a4730000d6027301eb027302d195ed92b1a4730393b1db630872017304d80bd603db63087201d604b2a5730500d605b27203730600d6067e9973078c72050206d6077ec1720106d6089d9c7e72020672067207d609b27203730800d60a7e8c72090206d60b9d9c7e7309067206720ad60cdb63087204d60db2720c730a00ededededed938cb27203730b0001730c93c27204730d95ed8f7208720b93b1720c730ed801d60eb2720c730f00eded92c1720499c1a77310938c720e018c720901927e8c720e02069d9c99720b7208720a720695927208720b927ec1720406997ec1a706997e7202069d9c997208720b720772067311938c720d018c720501927e8c720d0206a17208720b90b0ada5d9010e639593c2720e7312c1720e73137314d9010e599a8c720e018c720e0273157316",
         |    "address": "F5oaa6ywhFqGYcDxPWF3mXRh5tuHUF4o9ygXc9ixQtGoxeQPAU4gDnY5H1nkLz8NnThPeoddgPJ9dNFKRmTNLQ4dhN6526dsFXxA4S7av5DGjyK47YNipLdyfpSMrdLWJPsqAnny2VCJ6WuVyQdySrXt1qBPc74PRY6mcy2npQmWR13WYhQZotQYThCRezZbTWVQowEFCLjw5wUbDHc2vbckQKr9cVBR9QwvXe2BxdKE9UoUpc8gKUDfKgkyDJzedTXHDbpxW7yTiY2YnPrPJdKZKBCF96LBu8f4yBDfxXW47AUaM4V3L22qRWTPHL2kur3zS3hZpaiCJifbAvhFW5cux7FNYjCTmZ9eoF7fbL3T4RvHWJMxNTyhTAE29Bi25EAAeGjjJhQLoNCqvRLU2o2CyuZA5BZm1LyCNso2o8htFJcizkgJQZ6WPoKVdusp6K1HVbWswAWcaWUh4FtYn1AmhETmBUim8munFwgN8eu2MoUch15LsngHjYjMVd67M1ds1oSgVmKXZJJrmtzqqr6AfFJVafTxoBYwrzZmT2Rj1zniCRG1zkBvHEBEU4ECRct67MFszURvsEqcA6UN7ooKcdJXcPJjMNVwyJ1QVMWVMHqt1gDA4gfbABmhbBJ9gzsTgypkJMSsDt8tX5vKkXa27kDCQDNhRwGVFoH25Tghcg478YSdYdT5KECWfhfCF2PXMAUqLzegdAQVxSE98bAf48Nc4NjpiTrHLXDZQp3P8vweok8n7P5aaJQBRmVsxCrfqjv268GMZJYkioGNVqu7wpgLC9rpJBFZF",
         |    "assets": [
         |        {
         |            "tokenId": "03faf2cb329f2e90d6d23b58d91bbb6c046aa143261cc21f52fbe2824bfcbf04",
         |            "index": 0,
         |            "amount": 7013,
         |            "name": "SigUSD",
         |            "decimals": 2,
         |            "type": "EIP-004"
         |        },
         |        {
         |            "tokenId": "9a06d9e545a41fd51eeffc5e20d818073bf820c635e2a9d922269913e0de369d",
         |            "index": 1,
         |            "amount": 152650,
         |            "name": "SPF",
         |            "decimals": 6,
         |            "type": "EIP-004"
         |        }
         |    ],
         |    "additionalRegisters": {},
         |    "spentTransactionId": "10e2d101a5063c97edbe5e47bc86b19c7707aee87e886193271da96f610b363b",
         |    "mainChain": true
         |}
         |""".stripMargin
    ).toOption.get

    val depositSpfY: AmmDepositV3 = AmmDepositV3(
      outputSpfY,
      SPF(152650),
      PoolId.unsafeFromString("9916d75132593c8b07fe18bd8d583bda1652eed7565cf41a4738ddd90fc992ec"),
      PublicKeyRedeemer(
        PubKey.unsafeFromString("0215673d844f85d763aac8c0dd4ea83fa1f11438fa5d683e7e6b1ac95586203ea2")
      ),
      AmmDepositParams(
        AssetAmount.native(41983905472L),
        AssetAmount(
          TokenId.unsafeFromString("03faf2cb329f2e90d6d23b58d91bbb6c046aa143261cc21f52fbe2824bfcbf04"),
          7013
        )
      ),
      2000000,
      Version.V3
    )

    val outputSpfNotY = decode[Output](
      s"""
         |{
         |    "boxId": "4d690b4a03b40335b35431be4f68f3de6d64b073c79761591eed73a25f4befe1",
         |    "transactionId": "e30b6870304d6931eb77c9413a2f240b5c855980f7cb289f80f895a10f193f6d",
         |    "blockId": "314ad7528b71f3578e9ddab0a18dcf005056a4cc862965094c5383802f82f3e2",
         |    "value": 500400000,
         |    "index": 0,
         |    "globalIndex": 28632198,
         |    "creationHeight": 991715,
         |    "settlementHeight": 991718,
         |    "ergoTree": "19c504170400058094ebdc0308cd02790df2ef284bbec57fb8c39a58919fa6d08ea566b3a500f20255d9ebe2ec38f8040404060402040205feffffffffffffffff01040405aa10040004000e201d5afc59838920bb5ef2a8f9d63825a55b1d48e269d7cecee335d637c3ff5f3f0e240008cd02790df2ef284bbec57fb8c39a58919fa6d08ea566b3a500f20255d9ebe2ec38f80404040205c0b80201000e691005040004000e36100204a00b08cd0279be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798ea02d192a39a8cc7a701730073011001020402d19683030193a38cc7b2a57300000193c2b2a57301007473027303830108cdeeac93b1a5730405000500058092f4010100d802d601b2a4730000d6027301eb027302d195ed92b1a4730393b1db630872017304d80bd603db63087201d604b2a5730500d605b27203730600d6067e9973078c72050206d6077ec1720106d6089d9c7e72020672067207d609b27203730800d60a7e8c72090206d60b9d9c7e7309067206720ad60cdb63087204d60db2720c730a00ededededed938cb27203730b0001730c93c27204730d95ed8f7208720b93b1720c730ed801d60eb2720c730f00eded92c1720499c1a77310938c720e018c720901927e8c720e02069d9c99720b7208720a720695927208720b927ec1720406997ec1a706997e7202069d9c997208720b720772067311938c720d018c720501927e8c720d0206a17208720b90b0ada5d9010e639593c2720e7312c1720e73137314d9010e599a8c720e018c720e0273157316",
         |    "address": "4C4FWeFQvWP1sHao7jQ4xqRrZuZSQdUsi4rkK4XRPy9kuMbZbzodjebWgMkeHUVBMz3p7oujwdnzELaNCGLuLECppr8XA87VhHa8iYP6qF4JuvFuaNexNUMrx4Woo5PK56YviG4d4GA8SXtWFcoRvB1nXjRMBGRKgtCW8AvXq6GQHQ8E7nY76WfTvSPm6sQGvi5zwZ2G5QsuvKTBTcNQyh3Xoa519vwfjohdnj3GBUwoTJVj9wEY96oqoWGkms5GRWdpfam2Kn1sNN4NJQqWLEe2DXrRtbxfFBqoLCX5jhCKn2VFbazzTFLwfs7KfQA1jqs9GzThtZHdSfdiq4JCWHJ4zTiMykCcgKPbe6QUbHTkg67vhMzgQt89A7BaDSBmcHCGqd8aYLhWoJzfXkqVQhSzB9VCEfDeZ27vUhBWQwPixeJ9M3FGW3ZhNYLAEuvqB6vAUzJRpgAz56resoFc2TG43Tw6EsdK8YeHNpKbdxoc6YjvtgedHNe25s5fwyaMoRhc7CocRQwGcnn2dkAXhFcZUAwP6bwpgfR7FPgFkBuHWdZDsV3wxc16ZgNsjxhXttreMV8gTrS6h2aEY8zk9Q2vQK4r1osGMEdcBSYDu9zpGdnZvzLkd52hGkcSQMnFVKub8ziKz7e6RmJQUUzH8nLyUVtMAxHStUXHtg3CEpzJvQSxWYf1rTJHBQ7fXG6bWZeojt9CDZETWqGWpzoaijFH8v821Quy4Bhz3AwuccFQGvQfBzYTaeMdhxvykS7MEeJYyHL1xwGkoDJfAmvbve5TdHfMNZpv4mz7",
         |    "assets": [
         |        {
         |            "tokenId": "003bd19d0187117f130b62e1bcab0939929ff5c7709f843c5c4dd158949285d0",
         |            "index": 0,
         |            "amount": 1045,
         |            "name": "SigRSV",
         |            "decimals": 0,
         |            "type": "EIP-004"
         |        },
         |        {
         |            "tokenId": "9a06d9e545a41fd51eeffc5e20d818073bf820c635e2a9d922269913e0de369d",
         |            "index": 1,
         |            "amount": 152508,
         |            "name": "SPF",
         |            "decimals": 6,
         |            "type": "EIP-004"
         |        }
         |    ],
         |    "additionalRegisters": {},
         |    "spentTransactionId": "565009e770aeec4a9e0333f9fc9ad95f22e32c53ff3a2b614e144dd2496e5b06",
         |    "mainChain": true
         |}
         |""".stripMargin
    ).toOption.get

    val depositSpfNotY: AmmDepositV3 = AmmDepositV3(
      outputSpfNotY,
      SPF(152508),
      PoolId.unsafeFromString("1d5afc59838920bb5ef2a8f9d63825a55b1d48e269d7cecee335d637c3ff5f3f"),
      PublicKeyRedeemer(
        PubKey.unsafeFromString("02790df2ef284bbec57fb8c39a58919fa6d08ea566b3a500f20255d9ebe2ec38f8")
      ),
      AmmDepositParams(
        AssetAmount.native(500000000),
        AssetAmount(
          TokenId.unsafeFromString("003bd19d0187117f130b62e1bcab0939929ff5c7709f843c5c4dd158949285d0"),
          1045
        )
      ),
      2000000,
      Version.V3
    )
  }

  object redeem {

    val output = decode[Output](
      s"""
         |{
         |    "boxId": "22eb7ded7dc155c67cb2eaaafd31b80f3ef344b628293721b3f71e2159297820",
         |    "transactionId": "9e6fdef21d6ca158d6cde9f570dac0738b7dbee0c3c571849c5c725db0a2ccf0",
         |    "blockId": "75e92f73f121c5037ad165f3e087aa49e3d905cd4c9ff80fa251263419e132e2",
         |    "value": 400000,
         |    "index": 0,
         |    "globalIndex": 27699095,
         |    "creationHeight": 966543,
         |    "settlementHeight": 966545,
         |    "ergoTree": "19ca0312040008cd02d4b86f73bc65b8ff943bf54c5211c9160b9695f359d3e71f92abb988726ca63104040406040204000404040005feffffffffffffffff01040204000e20ea6d2ceff1565ac062cb260f558f1ed492dcd805dcd83fd1ecaf0e0407c8f1ff0e240008cd02d4b86f73bc65b8ff943bf54c5211c9160b9695f359d3e71f92abb988726ca6310e691005040004000e36100204a00b08cd0279be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798ea02d192a39a8cc7a701730073011001020402d19683030193a38cc7b2a57300000193c2b2a57301007473027303830108cdeeac93b1a5730405000500058092f4010100d801d601b2a4730000eb027301d195ed92b1a4730293b1db630872017303d806d602db63087201d603b2a5730400d604b2db63087203730500d605b27202730600d6067e8cb2db6308a77307000206d6077e9973088cb272027309000206ededededed938cb27202730a0001730b93c27203730c938c7204018c720501927e99c17203c1a7069d9c72067ec17201067207927e8c720402069d9c72067e8c72050206720790b0ada5d90108639593c27208730dc17208730e730fd90108599a8c7208018c72080273107311",
         |    "address": "4X2w8UZHL14TJfy3iRH6AuzBUoiqrTEmFhRhfhhLWHozt69WkMmVuSwwzabMtynk3qzfu6U8Vbmwnrq2PfszttKhLobPWjVQGUABdqPXYZmviCJmJTyvGvwbTVKEk37kAxBKWiNbGXPujGTeuM7w1DR2VQCCnLNj9dEn294vFJYUBhiCNUfYcec8GJGwB8tYx7fGHDFELT94ouZz5DVwW6UJdiiWcHdLcAvekXajT1AtHdZ6sysHoq7v6KJpcZnd7BqJKFYLPEvwsx6oxY9JXtt3cFYUzH9TP3Adph7gi4ZH9nMWGvRYJnvHgdpjoEbCzG1jyxsS2wxD8K94WtRGe3W4bvBf3b9tVy6FaWUPEEd6LhfS5AYY2HFXGSMaU6CvjnSe6HWyyg1YcivzwBijqYD59YsV7kBhKr55DLVoX5Rqz2aH7YQaEZBdQ15mV8nWXUwRFkGDJyYWSwmb32z1wfqByQRfXHD65ZSeTg8o46nXke1BPGnFVaCdMWTRm3bAq6T797YdgGRWUrdKkw1SatU52HPWDUK49uBv7ZHYkFBRQ9P6hk8xgix4Aaxbe3R7sJhiYZg9xqUKmfgXv1ZmaPEANcWPj91ytgLgUYBU1gM4UZqyZCKuv5tNjdze",
         |    "assets": [
         |        {
         |            "tokenId": "bc1813b933672629e639d91fa66578890dc5bbab064ee4e516cad2c5fa72019c",
         |            "index": 0,
         |            "amount": 122963,
         |            "name": null,
         |            "decimals": null,
         |            "type": null
         |        },
         |        {
         |            "tokenId": "9a06d9e545a41fd51eeffc5e20d818073bf820c635e2a9d922269913e0de369d",
         |            "index": 1,
         |            "amount": 145471,
         |            "name": "SPF",
         |            "decimals": 6,
         |            "type": "EIP-004"
         |        }
         |    ],
         |    "additionalRegisters": {},
         |    "spentTransactionId": "1a47131360fcbc9388fd69dcee8a79ec0ced7933356a32f1f16a66845960edc3",
         |    "mainChain": true
         |}
         |""".stripMargin
    ).toOption.get

    val order = RedeemV3(
      output,
      SPF(145471),
      PoolId.unsafeFromString("ea6d2ceff1565ac062cb260f558f1ed492dcd805dcd83fd1ecaf0e0407c8f1ff"),
      PublicKeyRedeemer(
        PubKey.unsafeFromString("02d4b86f73bc65b8ff943bf54c5211c9160b9695f359d3e71f92abb988726ca631")
      ),
      RedeemParams(
        AssetAmount(
          TokenId.unsafeFromString("bc1813b933672629e639d91fa66578890dc5bbab064ee4e516cad2c5fa72019c"),
          122963
        )
      ),
      2000000,
      Version.V3
    )
  }

}