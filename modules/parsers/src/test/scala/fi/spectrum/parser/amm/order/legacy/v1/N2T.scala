package fi.spectrum.parser.amm.order.legacy.v1

import fi.spectrum.core.domain.analytics.Version
import fi.spectrum.core.domain.order.Fee.ERG
import fi.spectrum.core.domain.order.Order.Redeem.AmmRedeem.RedeemLegacyV1
import fi.spectrum.core.domain.order.Order.Swap.SwapLegacyV1
import fi.spectrum.core.domain.order.Redeemer.PublicKeyRedeemer
import fi.spectrum.core.domain.order._
import fi.spectrum.core.domain.transaction.Output
import fi.spectrum.core.domain.{AssetAmount, PubKey, TokenId}
import io.circe.parser.decode

object N2T {

  object swap {

    val outputSell = decode[Output](
      """
        |{
        |    "boxId": "620d1bfb8fe29ea00f9755f0766813ddc51efc8f1c5adf6c5f3e903a46e2bb04",
        |    "transactionId": "24a287909cf383536767bc5a7334c6f2b523a3c34d5ff0192b0eefc8055469a7",
        |    "blockId": "1747286104d9b02c6478fc85eca156c246966cabd0088c8eb0a65f7c5a299ce9",
        |    "value": 528000000,
        |    "index": 0,
        |    "globalIndex": 6864950,
        |    "creationHeight": 506880,
        |    "settlementHeight": 568520,
        |    "ergoTree": "19de021408cd03ffb21db743415c5d84fb2b55c0f7b3297ef19a6c05fb7b8642598269d53e03eb0400058094ebdc03040404060402040004000e209916d75132593c8b07fe18bd8d583bda1652eed7565cf41a4738ddd90fc992ec0e2003faf2cb329f2e90d6d23b58d91bbb6c046aa143261cc21f52fbe2824bfcbf0405b00e05d890bfc7f9adf6390580c0a8ca9a3a040404c60f06010104d00f058094ebdc0304c60f0100d803d6017300d602b2a4730100d6037302eb027201d195ed93b1a4730393b1db630872027304d804d604db63087202d605b2a5730500d606b2db63087205730600d6077e8c72060206ededededed938cb2720473070001730893c27205d07201938c72060173099272077e730a06927ec172050699997ec1a7069d9c72077e730b067e730c067e720306909c9c7e8cb27204730d0002067e7203067e730e069c9a7207730f9a9c7ec17202067e7310067e9c73117e731205067313",
        |    "address": "UnHAL6aBQbqpBSTfhRHH4P4sFRVQzNyFS4QH91DDXJ9a8Y58PTeEodzifvPWco8L5TQNmZTKZU1tpXJBNDuKsPX2Xe2TBVzYhg64REnpfB3Dj6zQgVGqhRd5GxCf1B2vtXnQMDaDtqYCjnNjoAfE1D3yzyg5iCyERGwMdA7cF4eoWqa5KkAsujvpeLF5PSnFfX5QABV1RCUp56RQw3dy1dDrvBMxhcwjjG8rw3W1nrFE7u359Vq9n7HmDQPXNnLaXwZXYxvv291aJZKUe7zShNT8uYvRNvARGcgSTCFWvjZMuhW4DJ5UQKa7pA2Jcvvt4iTWjPZYiYgXwvoW4MpcUtKShE65B49gC7JrZ5rsvoNfvwdTvH8qwhYRzFGLHiSorAA3n8kQKTZE1md3YeRHegYA6Rpv7rfNgFMbF31bQDuguXEvLeT8ofyaxHzKhPP6tg7FSCtNd8xUvBNid1AfexjoUfFiRhgSCEDo73Es",
        |    "assets": [],
        |    "additionalRegisters": {},
        |    "spentTransactionId": "5fb09330bf82d227331072b9611e25185a7ef0c2b50640d6d3446e24fb657cc8",
        |    "mainChain": true
        |}
        |""".stripMargin
    ).toOption.get

    val sellOrder: SwapLegacyV1 = SwapLegacyV1(
      outputSell,
      PoolId.unsafeFromString("9916d75132593c8b07fe18bd8d583bda1652eed7565cf41a4738ddd90fc992ec"),
      PublicKeyRedeemer(PubKey.unsafeFromString("03ffb21db743415c5d84fb2b55c0f7b3297ef19a6c05fb7b8642598269d53e03eb")),
      SwapParams(
        AssetAmount.native(500000000),
        AssetAmount(
          TokenId.unsafeFromString("03faf2cb329f2e90d6d23b58d91bbb6c046aa143261cc21f52fbe2824bfcbf04"),
          920
        ),
        16304347826086956L,
        1000000000000L
      ),
      Version.LegacyV1
    )

    val outputBuy = decode[Output](
      """
        |{
        |    "boxId": "67c7b719ca64750101243e0d93ceb9ba4ee7caceff1cdab328c3ff4e29de3625",
        |    "transactionId": "f552ab81fff7a3bf55c1c908db9f26fcbb1c2b27a2dc039bb29fa90a797b91f6",
        |    "blockId": "c2a2903351db9260379dd51a7881d3c4978784cf30985658d582d972dfd18395",
        |    "value": 25400000,
        |    "index": 0,
        |    "globalIndex": 6945879,
        |    "creationHeight": 506880,
        |    "settlementHeight": 570135,
        |    "ergoTree": "199b021108cd02737cc36e895b7e53ea41295efdb42d31ae9d9dbd661c2a403766fd862f4813f00400040404060402058080a0f6f4acdbe01b05e6cbd0bfe0eca98a1b040004000e201d5afc59838920bb5ef2a8f9d63825a55b1d48e269d7cecee335d637c3ff5f3f05feeadf9a0504c60f060101040404d00f04c60f0100d802d6017300d602b2a4730100eb027201d195ed93b1a4730293b1db630872027303d804d603db63087202d604b2a5730400d6059d9c7e99c17204c1a7067e7305067e730606d6068cb2db6308a773070002ededed938cb2720373080001730993c27204d072019272057e730a06909c9c7ec17202067e7206067e730b069c9a7205730c9a9c7e8cb27203730d0002067e730e067e9c72067e730f05067310",
        |    "address": "4fGuNEr4vX8BfAbJXVaHn4PpcPwmWXPduLYzmfYrTJjJoYR5qnXyAaPuhW1tfF62cV76SCerHKk9AcEiy3eSKjZpTout2RvRJuMhWBRugg3Eb69ibAN7yXBkJ7K22nmXKoEA5q3d8xFWmZF4uyU1Rcd6nPwpfNY9Tw341H9BxnJs9PUJMNnkwj4XDYzn1Mg9y383Psvpd1eYM4Z7viuRBj5TCXg12AdJta5X9XLaGxotBA41tgmrpBE6VKYAu5uJsjNxATQC7QCpRxHYvQt5coUtje4CP4sFt1nDSs9AaVocKDkQNEPS1u791Doq5DgUVnNbJKyvsKruCFqsDYzCoPRVLVkG389evVE6kJugeVUdzzKwR2RQARtr4CycrQFqu6ZY8P3WHM7RA",
        |    "assets": [
        |        {
        |            "tokenId": "003bd19d0187117f130b62e1bcab0939929ff5c7709f843c5c4dd158949285d0",
        |            "index": 0,
        |            "amount": 554,
        |            "name": "SigRSV",
        |            "decimals": 0,
        |            "type": "EIP-004"
        |        }
        |    ],
        |    "additionalRegisters": {},
        |    "spentTransactionId": "de028966b90da65b0a95a85af6aa50cf599dbf920ac44e3d44be05be5a747b4a",
        |    "mainChain": true
        |}
        |""".stripMargin
    ).toOption.get

    val buyOrder: SwapLegacyV1 = SwapLegacyV1(
      outputBuy,
      PoolId.unsafeFromString("1d5afc59838920bb5ef2a8f9d63825a55b1d48e269d7cecee335d637c3ff5f3f"),
      PublicKeyRedeemer(PubKey.unsafeFromString("02737cc36e895b7e53ea41295efdb42d31ae9d9dbd661c2a403766fd862f4813f0")),
      SwapParams(
        AssetAmount(
          TokenId.unsafeFromString("003bd19d0187117f130b62e1bcab0939929ff5c7709f843c5c4dd158949285d0"),
          554
        ),
        AssetAmount.native(699136703),
        24315702389894413L,
        1000000000000000000L
      ),
      Version.LegacyV1
    )
  }

  object redeem {

    val output: Output = decode[Output](
      """
        |{
        |    "boxId": "6870c9f9fb410c668fe86da6e8ee973a3ce3533be517a8203544c59911d78884",
        |    "transactionId": "f02bd18a886cdf49929ac76cc138ec4d0edd10f52ba1ac6bdf2b4a22a3db7e96",
        |    "blockId": "87e66ad4d0c4ec9c9bb017c3e6c195b38a710d0208082162add69cef835ce984",
        |    "value": 20000000,
        |    "index": 0,
        |    "globalIndex": 6778211,
        |    "creationHeight": 506880,
        |    "settlementHeight": 566815,
        |    "ergoTree": "1997020e08cd03325247ab5630b1f1bea28437400940ff5761f3153b76b4a991f2be9a9a7ed198040004040406040204000404040005feffffffffffffffff01040204000e209916d75132593c8b07fe18bd8d583bda1652eed7565cf41a4738ddd90fc992ec0580dac4090100d802d6017300d602b2a4730100eb027201d195ed93b1a4730293b1db630872027303d806d603db63087202d604b2a5730400d605b2db63087204730500d606b27203730600d6077e8cb2db6308a77307000206d6087e9973088cb272037309000206edededed938cb27203730a0001730b93c27204d07201938c7205018c720601927e9a99c17204c1a7730c069d9c72077ec17202067208927e8c720502069d9c72077e8c720602067208730d",
        |    "address": "ZSU7trPRbj7LVDnLZwMG3n2nBY49n3aStBFeP4vfV16mseWaNQxLn9hhgxRKn5jmWVJ7iresc3p9GmUWq984tZ42DyA7cfxnADFdv46yiXTyjBCDuVmYxwembefBd1wzeKKdbjpsfo7ngN9ig8ioqLmDVLrrA3po5SLtwnoNaYU5bZ59nsPddET5nuFDfem22bPaUy5LT2gnttJgvFLxwppdtYB9Ly1AdU7VWvDB42hrL1tQzhNDMpEVhHkfftDnVTJ1JfnN3CwWSvkeLB7Wzgu71Q6Ft5kwBSgy1wWzb6hFTPBuS3CD5PKtbVMXTD8FbSGe5UEvbP9QnTPBEyNQSsCurroGtnLi83uDFy9CnX7jskQgbjXJPjjaKj61nW8WRYF3JZJ",
        |    "assets": [
        |        {
        |            "tokenId": "303f39026572bcb4060b51fafc93787a236bb243744babaa99fceb833d61e198",
        |            "index": 0,
        |            "amount": 1000,
        |            "name": null,
        |            "decimals": null,
        |            "type": null
        |        }
        |    ],
        |    "additionalRegisters": {},
        |    "spentTransactionId": "8e200d2ddb85ebe74eee1f448fcb25b63650b2a7bdc3ddc840dfe73ec3673330",
        |    "mainChain": true
        |}
        |""".stripMargin
    ).toOption.get

    val redeem: RedeemLegacyV1 =
      RedeemLegacyV1(
        output,
        ERG(10000000),
        PoolId.unsafeFromString("9916d75132593c8b07fe18bd8d583bda1652eed7565cf41a4738ddd90fc992ec"),
        PublicKeyRedeemer(
          PubKey.unsafeFromString("03325247ab5630b1f1bea28437400940ff5761f3153b76b4a991f2be9a9a7ed198")
        ),
        RedeemParams(
          AssetAmount(
            TokenId.unsafeFromString("303f39026572bcb4060b51fafc93787a236bb243744babaa99fceb833d61e198"),
            1000
          )
        ),
        Version.LegacyV1
      )

  }

}
