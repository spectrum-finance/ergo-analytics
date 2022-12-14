package fi.spectrum.parser.amm.order.v3

import fi.spectrum.core.domain.analytics.Version
import fi.spectrum.core.domain.order.Fee.SPF
import fi.spectrum.core.domain.order.Order.Deposit.DepositV3
import fi.spectrum.core.domain.order.Order.Redeem.RedeemV3
import fi.spectrum.core.domain.order.Order.Swap.SwapV3
import fi.spectrum.core.domain.order.Redeemer.ErgoTreeRedeemer
import fi.spectrum.core.domain.order._
import fi.spectrum.core.domain.transaction.Output
import fi.spectrum.core.domain.{AssetAmount, SErgoTree, TokenId}
import io.circe.parser.decode

object N2T {

  object swap {

    val outputSellNotY = decode[Output](
      s"""
         |{
         |    "boxId": "cd29cc599a8a53d28504f2752fc075286359d6b7f1e82582b7e6cd45468a6b29",
         |    "transactionId": "52cdef65d1a93e29774904b5457577e7ec351a6fded878f5ee63733020865495",
         |    "blockId": "274b42d256323aa61a1735f55ca15071a789704b9c66d5daba19151f4199e7e3",
         |    "value": 7260000,
         |    "index": 0,
         |    "globalIndex": 20758110,
         |    "creationHeight": 825489,
         |    "settlementHeight": 825491,
         |    "ergoTree": "19d70422040005f01504d00f010004040406040204000101040804000e2000000000000000000000000000000000000000000000000000000000000000000e2003030303030303030303030303030303030303030303030303030303030303030e20010101010101010101010101010101010101010101010101010101010101010105c00c010101010540055406010004020e2002020202020202020202020202020202020202020202020202020202020202020101040405e01204c80f06010105e01204c80f0e691005040004000e36100204a00b08cd0279be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798ea02d192a39a8cc7a701730073011001020402d19683030193a38cc7b2a57300000193c2b2a57301007473027303830108cdeeac93b1a573040500050005e2f85e0100d803d601b2a4730000d6027301d6037302d1ec730395ed92b1a4730493b1db630872017305d806d604db63087201d605b2a5730600d606db63087205d607b27206730700d6088c720702d6099573089d9c7e720306997e7208067e7202067e7309067e720806edededededed938cb27204730a0001730b93c27205730c938c720701730d9272097e730e0695730f7310d801d60a997e7202069d9c72097e7311067e7312069591720a7313d801d60bb27206731400ed938c720b017315927e8c720b0206720a7316909c9c7e8cb2720473170002067e7318067e7319069c9a7209731a9a9c7ec17201067e7203067e9c731b7e731c050690b0ada5d9010a639593c2720a731dc1720a731e731fd9010a599a8c720a018c720a0273207321",
         |    "address": "ynjTGtEt9xdN4LPAreFLJVwwdzZgP1tG5jqMBtvhF1Rr7bsMjBioWPLamCZBkytNPRuKCvzFq2Ghr76X2wVvzwPEiGUDEyykJDTbrChB8iNS8xARPwijRGZGymCG7E3xMNn7tCX2yZU3qT5a4ngG8RCJrvVjTef2kuU4jKARdXY8xYEJUDLBsUnssoT54ma2N3xzQRi4cAhA5Ap5AbM5YxV63GaBewRuEshjrTJH4dLDx6ApyVNmpVRwaZUuiLHFvdUtqcFsyakNyACaWTiJfPCWygFsBznUnMmfKB84eRjdmdL1jPB8TF4Heus2Hh82KVThdfWxAe1P5TVkovNUgLw7t4DyuTh6Mr9sMxzTPiidDriv1mKhrDGGru6w7uFhJ5fKZFQHskZ9ei2ybz6D9Vmz9tuUtcEWUGn5Jh8hjbYVHe9ckCCT2Sh56bV5DEx4hZ7Egnprt6ADdMU1sLoN4N5youbByQsZBmDxrbRmGEyMQaubBFJfEKGGk8EUjENvXgCiZ14iXhiyWQaoLFP1QzNrAJHeCtYNLq1XTAH1j2ciMw8CChJELXeAAd9nvUheizsg6kUqzsraCscg6FrEntCiC9ZtPkvcD8KEmtzj5bUDh6WNzMkGUuMtWVr3qto1Eu94DQDkzSP6dCZvRHRjribkHeYTQvhyjREEUzpS2a5zsAY1UxBsRJt45pcHFwanfYTg2kAJnhTCm9rE13kgyHcgZGnKpKwCJxEoYLXUiMRZ6icHW1eKCqjnAkT6tQik9fBWXZwkoKLmFVbbvfcT51a",
         |    "assets": [
         |        {
         |            "tokenId": "03faf2cb329f2e90d6d23b58d91bbb6c046aa143261cc21f52fbe2824bfcbf04",
         |            "index": 0,
         |            "amount": 1000,
         |            "name": "SigUSD",
         |            "decimals": 2,
         |            "type": "EIP-004"
         |        }
         |    ],
         |    "additionalRegisters": {},
         |    "spentTransactionId": "b2799d2dfa796a4e99f92ff48f85cdb80cb513333ab04ceb217e7f68f97190ad",
         |    "mainChain": true
         |}
         |""".stripMargin
    ).toOption.get

    val swapSellNotY = SwapV3(
      outputSellNotY,
      PoolId.unsafeFromString("0000000000000000000000000000000000000000000000000000000000000000"),
      ErgoTreeRedeemer(
        SErgoTree.unsafeFromString(
          "0303030303030303030303030303030303030303030303030303030303030303"
        )
      ),
      SwapParams(
        AssetAmount(
          TokenId.unsafeFromString("0000000000000000000000000000000000000000000000000000000000000000"),
          1200
        ),
        AssetAmount(
          TokenId.unsafeFromString("0101010101010101010101010101010101010101010101010101010101010101"),
          800
        ),
        32,
        42
      ),
      777777,
      1400,
      Version.V3,
      OrderType.AMM,
      Operation.Swap
    )

    val outputSellSpf = decode[Output](
      s"""
         |{
         |    "boxId": "cd29cc599a8a53d28504f2752fc075286359d6b7f1e82582b7e6cd45468a6b29",
         |    "transactionId": "52cdef65d1a93e29774904b5457577e7ec351a6fded878f5ee63733020865495",
         |    "blockId": "274b42d256323aa61a1735f55ca15071a789704b9c66d5daba19151f4199e7e3",
         |    "value": 7260000,
         |    "index": 0,
         |    "globalIndex": 20758110,
         |    "creationHeight": 825489,
         |    "settlementHeight": 825491,
         |    "ergoTree": "19d70422040005f01504d00f010004040406040204000101040804000e2000000000000000000000000000000000000000000000000000000000000000000e2003030303030303030303030303030303030303030303030303030303030303030e20010101010101010101010101010101010101010101010101010101010101010105c00c010101010540055406010004020e2002020202020202020202020202020202020202020202020202020202020202020101040405e01204c80f06010105e01204c80f0e691005040004000e36100204a00b08cd0279be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798ea02d192a39a8cc7a701730073011001020402d19683030193a38cc7b2a57300000193c2b2a57301007473027303830108cdeeac93b1a573040500050005e2f85e0100d803d601b2a4730000d6027301d6037302d1ec730395ed92b1a4730493b1db630872017305d806d604db63087201d605b2a5730600d606db63087205d607b27206730700d6088c720702d6099573089d9c7e720306997e7208067e7202067e7309067e720806edededededed938cb27204730a0001730b93c27205730c938c720701730d9272097e730e0695730f7310d801d60a997e7202069d9c72097e7311067e7312069591720a7313d801d60bb27206731400ed938c720b017315927e8c720b0206720a7316909c9c7e8cb2720473170002067e7318067e7319069c9a7209731a9a9c7ec17201067e7203067e9c731b7e731c050690b0ada5d9010a639593c2720a731dc1720a731e731fd9010a599a8c720a018c720a0273207321",
         |    "address": "ynjTGtEt9xdN4LPAreFLJVwwdzZgP1tG5jqMBtvhF1Rr7bsMjBioWPLamCZBkytNPRuKCvzFq2Ghr76X2wVvzwPEiGUDEyykJDTbrChB8iNS8xARPwijRGZGymCG7E3xMNn7tCX2yZU3qT5a4ngG8RCJrvVjTef2kuU4jKARdXY8xYEJUDLBsUnssoT54ma2N3xzQRi4cAhA5Ap5AbM5YxV63GaBewRuEshjrTJH4dLDx6ApyVNmpVRwaZUuiLHFvdUtqcFsyakNyACaWTiJfPCWygFsBznUnMmfKB84eRjdmdL1jPB8TF4Heus2Hh82KVThdfWxAe1P5TVkovNUgLw7t4DyuTh6Mr9sMxzTPiidDriv1mKhrDGGru6w7uFhJ5fKZFQHskZ9ei2ybz6D9Vmz9tuUtcEWUGn5Jh8hjbYVHe9ckCCT2Sh56bV5DEx4hZ7Egnprt6ADdMU1sLoN4N5youbByQsZBmDxrbRmGEyMQaubBFJfEKGGk8EUjENvXgCiZ14iXhiyWQaoLFP1QzNrAJHeCtYNLq1XTAH1j2ciMw8CChJELXeAAd9nvUheizsg6kUqzsraCscg6FrEntCiC9ZtPkvcD8KEmtzj5bUDh6WNzMkGUuMtWVr3qto1Eu94DQDkzSP6dCZvRHRjribkHeYTQvhyjREEUzpS2a5zsAY1UxBsRJt45pcHFwanfYTg2kAJnhTCm9rE13kgyHcgZGnKpKwCJxEoYLXUiMRZ6icHW1eKCqjnAkT6tQik9fBWXZwkoKLmFVbbvfcT51a",
         |    "assets": [
         |        {
         |            "tokenId": "03faf2cb329f2e90d6d23b58d91bbb6c046aa143261cc21f52fbe2824bfcbf04",
         |            "index": 0,
         |            "amount": 1000,
         |            "name": "SigUSD",
         |            "decimals": 2,
         |            "type": "EIP-004"
         |        }
         |    ],
         |    "additionalRegisters": {},
         |    "spentTransactionId": "b2799d2dfa796a4e99f92ff48f85cdb80cb513333ab04ceb217e7f68f97190ad",
         |    "mainChain": true
         |}
         |""".stripMargin
    ).toOption.get

    val swapSellSpf = SwapV3(
      outputSellSpf,
      PoolId.unsafeFromString("0000000000000000000000000000000000000000000000000000000000000000"),
      ErgoTreeRedeemer(
        SErgoTree.unsafeFromString(
          "0303030303030303030303030303030303030303030303030303030303030303"
        )
      ),
      SwapParams(
        AssetAmount(
          TokenId.unsafeFromString("0000000000000000000000000000000000000000000000000000000000000000"),
          1200
        ),
        AssetAmount(
          TokenId.unsafeFromString("0101010101010101010101010101010101010101010101010101010101010101"),
          800
        ),
        32,
        42
      ),
      777777,
      1400,
      Version.V3,
      OrderType.AMM,
      Operation.Swap
    )

    val outputBuyNoSpf = decode[Output](
      s"""
         |{
         |    "boxId": "cd29cc599a8a53d28504f2752fc075286359d6b7f1e82582b7e6cd45468a6b29",
         |    "transactionId": "52cdef65d1a93e29774904b5457577e7ec351a6fded878f5ee63733020865495",
         |    "blockId": "274b42d256323aa61a1735f55ca15071a789704b9c66d5daba19151f4199e7e3",
         |    "value": 7260000,
         |    "index": 0,
         |    "globalIndex": 20758110,
         |    "creationHeight": 825489,
         |    "settlementHeight": 825491,
         |    "ergoTree": "1993041b04000e200202020202020202020202020202020202020202020202020202020202020202040004c80f010004040406040205f0150542055405f01504000e2000000000000000000000000000000000000000000000000000000000000000000e20030303030303030303030303030303030303030303030303030303030303030305c00c0500040001010502040404d00f0e691005040004000e36100204a00b08cd0279be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798ea02d192a39a8cc7a701730073011001020402d19683030193a38cc7b2a57300000193c2b2a57301007473027303830108cdeeac93b1a573040500050005e2f85e0100d806d601b2a4730000d6027301d603db6308a7d604b27203730200d6058c720402d6067303d1ec730495ed92b1a4730593b1db630872017306d807d607db63087201d608b2a5730700d60999c17208c1a7d60a9973089d9c72097309730ad60b7203d60c7204d60d7e95948c720c0172027205997205730b06ededededed938cb27207730c0001730d93c27208730e927209730f9591720a7310d801d60eb2db63087208731100ed938c720e017202928c720e02720a7312909c9c7ec1720106720d7e7206069c7e9a72097313069a9c7e8cb2720773140002067e7315069c720d7e72060690b0ada5d9010e639593c2720e7316c1720e73177318d9010e599a8c720e018c720e027319731a",
         |    "address": "ynjTGtEt9xdN4LPAreFLJVwwdzZgP1tG5jqMBtvhF1Rr7bsMjBioWPLamCZBkytNPRuKCvzFq2Ghr76X2wVvzwPEiGUDEyykJDTbrChB8iNS8xARPwijRGZGymCG7E3xMNn7tCX2yZU3qT5a4ngG8RCJrvVjTef2kuU4jKARdXY8xYEJUDLBsUnssoT54ma2N3xzQRi4cAhA5Ap5AbM5YxV63GaBewRuEshjrTJH4dLDx6ApyVNmpVRwaZUuiLHFvdUtqcFsyakNyACaWTiJfPCWygFsBznUnMmfKB84eRjdmdL1jPB8TF4Heus2Hh82KVThdfWxAe1P5TVkovNUgLw7t4DyuTh6Mr9sMxzTPiidDriv1mKhrDGGru6w7uFhJ5fKZFQHskZ9ei2ybz6D9Vmz9tuUtcEWUGn5Jh8hjbYVHe9ckCCT2Sh56bV5DEx4hZ7Egnprt6ADdMU1sLoN4N5youbByQsZBmDxrbRmGEyMQaubBFJfEKGGk8EUjENvXgCiZ14iXhiyWQaoLFP1QzNrAJHeCtYNLq1XTAH1j2ciMw8CChJELXeAAd9nvUheizsg6kUqzsraCscg6FrEntCiC9ZtPkvcD8KEmtzj5bUDh6WNzMkGUuMtWVr3qto1Eu94DQDkzSP6dCZvRHRjribkHeYTQvhyjREEUzpS2a5zsAY1UxBsRJt45pcHFwanfYTg2kAJnhTCm9rE13kgyHcgZGnKpKwCJxEoYLXUiMRZ6icHW1eKCqjnAkT6tQik9fBWXZwkoKLmFVbbvfcT51a",
         |    "assets": [
         |        {
         |            "tokenId": "03faf2cb329f2e90d6d23b58d91bbb6c046aa143261cc21f52fbe2824bfcbf04",
         |            "index": 0,
         |            "amount": 1000,
         |            "name": "SigUSD",
         |            "decimals": 2,
         |            "type": "EIP-004"
         |        }
         |    ],
         |    "additionalRegisters": {},
         |    "spentTransactionId": "b2799d2dfa796a4e99f92ff48f85cdb80cb513333ab04ceb217e7f68f97190ad",
         |    "mainChain": true
         |}
         |""".stripMargin
    ).toOption.get

    val swapBuyNoSpf = SwapV3(
      outputBuyNoSpf,
      PoolId.unsafeFromString("0000000000000000000000000000000000000000000000000000000000000000"),
      ErgoTreeRedeemer(
        SErgoTree.unsafeFromString(
          "0303030303030303030303030303030303030303030303030303030303030303"
        )
      ),
      SwapParams(
        AssetAmount(
          TokenId.unsafeFromString("03faf2cb329f2e90d6d23b58d91bbb6c046aa143261cc21f52fbe2824bfcbf04"),
          1000
        ),
        AssetAmount.native(800),
        9,
        42
      ),
      777777,
      1400,
      Version.V3,
      OrderType.AMM,
      Operation.Swap
    )

    val outputBuySpf = decode[Output](
      s"""
         |{
         |    "boxId": "cd29cc599a8a53d28504f2752fc075286359d6b7f1e82582b7e6cd45468a6b29",
         |    "transactionId": "52cdef65d1a93e29774904b5457577e7ec351a6fded878f5ee63733020865495",
         |    "blockId": "274b42d256323aa61a1735f55ca15071a789704b9c66d5daba19151f4199e7e3",
         |    "value": 7260000,
         |    "index": 0,
         |    "globalIndex": 20758110,
         |    "creationHeight": 825489,
         |    "settlementHeight": 825491,
         |    "ergoTree": "1993041b04000e200202020202020202020202020202020202020202020202020202020202020202040004c80f010004040406040205f0150542055405f01504000e2000000000000000000000000000000000000000000000000000000000000000000e20030303030303030303030303030303030303030303030303030303030303030305c00c0500040001010502040404d00f0e691005040004000e36100204a00b08cd0279be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798ea02d192a39a8cc7a701730073011001020402d19683030193a38cc7b2a57300000193c2b2a57301007473027303830108cdeeac93b1a573040500050005e2f85e0100d806d601b2a4730000d6027301d603db6308a7d604b27203730200d6058c720402d6067303d1ec730495ed92b1a4730593b1db630872017306d807d607db63087201d608b2a5730700d60999c17208c1a7d60a9973089d9c72097309730ad60b7203d60c7204d60d7e95948c720c0172027205997205730b06ededededed938cb27207730c0001730d93c27208730e927209730f9591720a7310d801d60eb2db63087208731100ed938c720e017202928c720e02720a7312909c9c7ec1720106720d7e7206069c7e9a72097313069a9c7e8cb2720773140002067e7315069c720d7e72060690b0ada5d9010e639593c2720e7316c1720e73177318d9010e599a8c720e018c720e027319731a",
         |    "address": "ynjTGtEt9xdN4LPAreFLJVwwdzZgP1tG5jqMBtvhF1Rr7bsMjBioWPLamCZBkytNPRuKCvzFq2Ghr76X2wVvzwPEiGUDEyykJDTbrChB8iNS8xARPwijRGZGymCG7E3xMNn7tCX2yZU3qT5a4ngG8RCJrvVjTef2kuU4jKARdXY8xYEJUDLBsUnssoT54ma2N3xzQRi4cAhA5Ap5AbM5YxV63GaBewRuEshjrTJH4dLDx6ApyVNmpVRwaZUuiLHFvdUtqcFsyakNyACaWTiJfPCWygFsBznUnMmfKB84eRjdmdL1jPB8TF4Heus2Hh82KVThdfWxAe1P5TVkovNUgLw7t4DyuTh6Mr9sMxzTPiidDriv1mKhrDGGru6w7uFhJ5fKZFQHskZ9ei2ybz6D9Vmz9tuUtcEWUGn5Jh8hjbYVHe9ckCCT2Sh56bV5DEx4hZ7Egnprt6ADdMU1sLoN4N5youbByQsZBmDxrbRmGEyMQaubBFJfEKGGk8EUjENvXgCiZ14iXhiyWQaoLFP1QzNrAJHeCtYNLq1XTAH1j2ciMw8CChJELXeAAd9nvUheizsg6kUqzsraCscg6FrEntCiC9ZtPkvcD8KEmtzj5bUDh6WNzMkGUuMtWVr3qto1Eu94DQDkzSP6dCZvRHRjribkHeYTQvhyjREEUzpS2a5zsAY1UxBsRJt45pcHFwanfYTg2kAJnhTCm9rE13kgyHcgZGnKpKwCJxEoYLXUiMRZ6icHW1eKCqjnAkT6tQik9fBWXZwkoKLmFVbbvfcT51a",
         |    "assets": [
         |        {
         |            "tokenId": "0202020202020202020202020202020202020202020202020202020202020202",
         |            "index": 0,
         |            "amount": 1000,
         |            "name": "SigUSD",
         |            "decimals": 2,
         |            "type": "EIP-004"
         |        }
         |    ],
         |    "additionalRegisters": {},
         |    "spentTransactionId": "b2799d2dfa796a4e99f92ff48f85cdb80cb513333ab04ceb217e7f68f97190ad",
         |    "mainChain": true
         |}
         |""".stripMargin
    ).toOption.get

    val swapBuySpf = SwapV3(
      outputBuySpf,
      PoolId.unsafeFromString("0000000000000000000000000000000000000000000000000000000000000000"),
      ErgoTreeRedeemer(
        SErgoTree.unsafeFromString(
          "0303030303030303030303030303030303030303030303030303030303030303"
        )
      ),
      SwapParams(
        AssetAmount(
          TokenId.unsafeFromString("0202020202020202020202020202020202020202020202020202020202020202"),
          -400
        ),
        AssetAmount.native(800),
        9,
        42
      ),
      777777,
      1400,
      Version.V3,
      OrderType.AMM,
      Operation.Swap
    )
  }

  object deposit {

    val outputSpfY = decode[Output](
      s"""
         |{
         |    "boxId": "cd29cc599a8a53d28504f2752fc075286359d6b7f1e82582b7e6cd45468a6b29",
         |    "transactionId": "52cdef65d1a93e29774904b5457577e7ec351a6fded878f5ee63733020865495",
         |    "blockId": "274b42d256323aa61a1735f55ca15071a789704b9c66d5daba19151f4199e7e3",
         |    "value": 7260000,
         |    "index": 0,
         |    "globalIndex": 20758110,
         |    "creationHeight": 825489,
         |    "settlementHeight": 825491,
         |    "ergoTree": "19b40419040005f2c0010100040404060402040205feffffffffffffffff0104000404010105f601040004000e2000000000000000000000000000000000000000000000000000000000000000000e2003030303030303030303030303030303030303030303030303030303030303030404040205f2c00101000e691005040004000e36100204a00b08cd0279be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798ea02d192a39a8cc7a701730073011001020402d19683030193a38cc7b2a57300000193c2b2a57301007473027303830108cdeeac93b1a573040500050005e2f85e0100d802d601b2a4730000d6027301d1ec730295ed92b1a4730393b1db630872017304d80cd603db63087201d604b2a5730500d605b27203730600d6067e9973078c72050206d6077ec1720106d6089d9c7e72020672067207d6098cb2db6308a773080002d60ab27203730900d60b7e8c720a0206d60c9d9c7e95730a997209730b7209067206720bd60ddb63087204d60eb2720d730c00ededededed938cb27203730d0001730e93c27204730f95ed8f7208720c93b1720d7310d801d60fb2720d731100eded92c1720499c1a77312938c720f018c720a01927e8c720f02069d9c99720c7208720b720695927208720c927ec1720406997ec1a706997e7202069d9c997208720c720772067313938c720e018c720501927e8c720e0206a17208720c90b0ada5d9010f639593c2720f7314c1720f73157316d9010f599a8c720f018c720f0273177318",
         |    "address": "ynjTGtEt9xdN4LPAreFLJVwwdzZgP1tG5jqMBtvhF1Rr7bsMjBioWPLamCZBkytNPRuKCvzFq2Ghr76X2wVvzwPEiGUDEyykJDTbrChB8iNS8xARPwijRGZGymCG7E3xMNn7tCX2yZU3qT5a4ngG8RCJrvVjTef2kuU4jKARdXY8xYEJUDLBsUnssoT54ma2N3xzQRi4cAhA5Ap5AbM5YxV63GaBewRuEshjrTJH4dLDx6ApyVNmpVRwaZUuiLHFvdUtqcFsyakNyACaWTiJfPCWygFsBznUnMmfKB84eRjdmdL1jPB8TF4Heus2Hh82KVThdfWxAe1P5TVkovNUgLw7t4DyuTh6Mr9sMxzTPiidDriv1mKhrDGGru6w7uFhJ5fKZFQHskZ9ei2ybz6D9Vmz9tuUtcEWUGn5Jh8hjbYVHe9ckCCT2Sh56bV5DEx4hZ7Egnprt6ADdMU1sLoN4N5youbByQsZBmDxrbRmGEyMQaubBFJfEKGGk8EUjENvXgCiZ14iXhiyWQaoLFP1QzNrAJHeCtYNLq1XTAH1j2ciMw8CChJELXeAAd9nvUheizsg6kUqzsraCscg6FrEntCiC9ZtPkvcD8KEmtzj5bUDh6WNzMkGUuMtWVr3qto1Eu94DQDkzSP6dCZvRHRjribkHeYTQvhyjREEUzpS2a5zsAY1UxBsRJt45pcHFwanfYTg2kAJnhTCm9rE13kgyHcgZGnKpKwCJxEoYLXUiMRZ6icHW1eKCqjnAkT6tQik9fBWXZwkoKLmFVbbvfcT51a",
         |    "assets": [
         |        {
         |            "tokenId": "03faf2cb329f2e90d6d23b58d91bbb6c046aa143261cc21f52fbe2824bfcbf04",
         |            "index": 0,
         |            "amount": 1000,
         |            "name": "SigUSD",
         |            "decimals": 2,
         |            "type": "EIP-004"
         |        }
         |    ],
         |    "additionalRegisters": {},
         |    "spentTransactionId": "b2799d2dfa796a4e99f92ff48f85cdb80cb513333ab04ceb217e7f68f97190ad",
         |    "mainChain": true
         |}
         |""".stripMargin
    ).toOption.get

    val depositSpfY: DepositV3 = DepositV3(
      outputSpfY,
      SPF(123),
      PoolId.unsafeFromString("0000000000000000000000000000000000000000000000000000000000000000"),
      ErgoTreeRedeemer(
        SErgoTree.unsafeFromString("0303030303030303030303030303030303030303030303030303030303030303")
      ),
      DepositParams(
        AssetAmount.native(12345),
        AssetAmount(
          TokenId.unsafeFromString("03faf2cb329f2e90d6d23b58d91bbb6c046aa143261cc21f52fbe2824bfcbf04"),
          877
        )
      ),
      777777,
      Version.V3,
      OrderType.AMM,
      Operation.Deposit
    )

    val outputSpfNotY = decode[Output](
      s"""
         |{
         |    "boxId": "cd29cc599a8a53d28504f2752fc075286359d6b7f1e82582b7e6cd45468a6b29",
         |    "transactionId": "52cdef65d1a93e29774904b5457577e7ec351a6fded878f5ee63733020865495",
         |    "blockId": "274b42d256323aa61a1735f55ca15071a789704b9c66d5daba19151f4199e7e3",
         |    "value": 7260000,
         |    "index": 0,
         |    "globalIndex": 20758110,
         |    "creationHeight": 825489,
         |    "settlementHeight": 825491,
         |    "ergoTree": "19b40419040005f2c0010100040404060402040205feffffffffffffffff0104000404010005f601040004000e2000000000000000000000000000000000000000000000000000000000000000000e2003030303030303030303030303030303030303030303030303030303030303030404040205f2c00101000e691005040004000e36100204a00b08cd0279be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798ea02d192a39a8cc7a701730073011001020402d19683030193a38cc7b2a57300000193c2b2a57301007473027303830108cdeeac93b1a573040500050005e2f85e0100d802d601b2a4730000d6027301d1ec730295ed92b1a4730393b1db630872017304d80cd603db63087201d604b2a5730500d605b27203730600d6067e9973078c72050206d6077ec1720106d6089d9c7e72020672067207d6098cb2db6308a773080002d60ab27203730900d60b7e8c720a0206d60c9d9c7e95730a997209730b7209067206720bd60ddb63087204d60eb2720d730c00ededededed938cb27203730d0001730e93c27204730f95ed8f7208720c93b1720d7310d801d60fb2720d731100eded92c1720499c1a77312938c720f018c720a01927e8c720f02069d9c99720c7208720b720695927208720c927ec1720406997ec1a706997e7202069d9c997208720c720772067313938c720e018c720501927e8c720e0206a17208720c90b0ada5d9010f639593c2720f7314c1720f73157316d9010f599a8c720f018c720f0273177318",
         |    "address": "ynjTGtEt9xdN4LPAreFLJVwwdzZgP1tG5jqMBtvhF1Rr7bsMjBioWPLamCZBkytNPRuKCvzFq2Ghr76X2wVvzwPEiGUDEyykJDTbrChB8iNS8xARPwijRGZGymCG7E3xMNn7tCX2yZU3qT5a4ngG8RCJrvVjTef2kuU4jKARdXY8xYEJUDLBsUnssoT54ma2N3xzQRi4cAhA5Ap5AbM5YxV63GaBewRuEshjrTJH4dLDx6ApyVNmpVRwaZUuiLHFvdUtqcFsyakNyACaWTiJfPCWygFsBznUnMmfKB84eRjdmdL1jPB8TF4Heus2Hh82KVThdfWxAe1P5TVkovNUgLw7t4DyuTh6Mr9sMxzTPiidDriv1mKhrDGGru6w7uFhJ5fKZFQHskZ9ei2ybz6D9Vmz9tuUtcEWUGn5Jh8hjbYVHe9ckCCT2Sh56bV5DEx4hZ7Egnprt6ADdMU1sLoN4N5youbByQsZBmDxrbRmGEyMQaubBFJfEKGGk8EUjENvXgCiZ14iXhiyWQaoLFP1QzNrAJHeCtYNLq1XTAH1j2ciMw8CChJELXeAAd9nvUheizsg6kUqzsraCscg6FrEntCiC9ZtPkvcD8KEmtzj5bUDh6WNzMkGUuMtWVr3qto1Eu94DQDkzSP6dCZvRHRjribkHeYTQvhyjREEUzpS2a5zsAY1UxBsRJt45pcHFwanfYTg2kAJnhTCm9rE13kgyHcgZGnKpKwCJxEoYLXUiMRZ6icHW1eKCqjnAkT6tQik9fBWXZwkoKLmFVbbvfcT51a",
         |    "assets": [
         |        {
         |            "tokenId": "03faf2cb329f2e90d6d23b58d91bbb6c046aa143261cc21f52fbe2824bfcbf04",
         |            "index": 0,
         |            "amount": 1000,
         |            "name": "SigUSD",
         |            "decimals": 2,
         |            "type": "EIP-004"
         |        }
         |    ],
         |    "additionalRegisters": {},
         |    "spentTransactionId": "b2799d2dfa796a4e99f92ff48f85cdb80cb513333ab04ceb217e7f68f97190ad",
         |    "mainChain": true
         |}
         |""".stripMargin
    ).toOption.get

    val depositSpfNotY: DepositV3 = DepositV3(
      outputSpfNotY,
      SPF(123),
      PoolId.unsafeFromString("0000000000000000000000000000000000000000000000000000000000000000"),
      ErgoTreeRedeemer(
        SErgoTree.unsafeFromString("0303030303030303030303030303030303030303030303030303030303030303")
      ),
      DepositParams(
        AssetAmount.native(12345),
        AssetAmount(
          TokenId.unsafeFromString("03faf2cb329f2e90d6d23b58d91bbb6c046aa143261cc21f52fbe2824bfcbf04"),
          1000
        )
      ),
      777777,
      Version.V3,
      OrderType.AMM,
      Operation.Deposit
    )
  }

  object redeem {

    val output = decode[Output](
      s"""
             |{
             |    "boxId": "cd29cc599a8a53d28504f2752fc075286359d6b7f1e82582b7e6cd45468a6b29",
             |    "transactionId": "52cdef65d1a93e29774904b5457577e7ec351a6fded878f5ee63733020865495",
             |    "blockId": "274b42d256323aa61a1735f55ca15071a789704b9c66d5daba19151f4199e7e3",
             |    "value": 7260000,
             |    "index": 0,
             |    "globalIndex": 20758110,
             |    "creationHeight": 825489,
             |    "settlementHeight": 825491,
             |    "ergoTree": "19a303120400010004040406040204000404040005feffffffffffffffff01040204000e2000000000000000000000000000000000000000000000000000000000000000000e2003030303030303030303030303030303030303030303030303030303030303030e691005040004000e36100204a00b08cd0279be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798ea02d192a39a8cc7a701730073011001020402d19683030193a38cc7b2a57300000193c2b2a57301007473027303830108cdeeac93b1a573040500050005e2f85e0100d801d601b2a4730000d1ec730195ed92b1a4730293b1db630872017303d806d602db63087201d603b2a5730400d604b2db63087203730500d605b27202730600d6067e8cb2db6308a77307000206d6077e9973088cb272027309000206ededededed938cb27202730a0001730b93c27203730c938c7204018c720501927e99c17203c1a7069d9c72067ec17201067207927e8c720402069d9c72067e8c72050206720790b0ada5d90108639593c27208730dc17208730e730fd90108599a8c7208018c72080273107311",
             |    "address": "ynjTGtEt9xdN4LPAreFLJVwwdzZgP1tG5jqMBtvhF1Rr7bsMjBioWPLamCZBkytNPRuKCvzFq2Ghr76X2wVvzwPEiGUDEyykJDTbrChB8iNS8xARPwijRGZGymCG7E3xMNn7tCX2yZU3qT5a4ngG8RCJrvVjTef2kuU4jKARdXY8xYEJUDLBsUnssoT54ma2N3xzQRi4cAhA5Ap5AbM5YxV63GaBewRuEshjrTJH4dLDx6ApyVNmpVRwaZUuiLHFvdUtqcFsyakNyACaWTiJfPCWygFsBznUnMmfKB84eRjdmdL1jPB8TF4Heus2Hh82KVThdfWxAe1P5TVkovNUgLw7t4DyuTh6Mr9sMxzTPiidDriv1mKhrDGGru6w7uFhJ5fKZFQHskZ9ei2ybz6D9Vmz9tuUtcEWUGn5Jh8hjbYVHe9ckCCT2Sh56bV5DEx4hZ7Egnprt6ADdMU1sLoN4N5youbByQsZBmDxrbRmGEyMQaubBFJfEKGGk8EUjENvXgCiZ14iXhiyWQaoLFP1QzNrAJHeCtYNLq1XTAH1j2ciMw8CChJELXeAAd9nvUheizsg6kUqzsraCscg6FrEntCiC9ZtPkvcD8KEmtzj5bUDh6WNzMkGUuMtWVr3qto1Eu94DQDkzSP6dCZvRHRjribkHeYTQvhyjREEUzpS2a5zsAY1UxBsRJt45pcHFwanfYTg2kAJnhTCm9rE13kgyHcgZGnKpKwCJxEoYLXUiMRZ6icHW1eKCqjnAkT6tQik9fBWXZwkoKLmFVbbvfcT51a",
             |    "assets": [
             |        {
             |            "tokenId": "03faf2cb329f2e90d6d23b58d91bbb6c046aa143261cc21f52fbe2824bfcbf04",
             |            "index": 0,
             |            "amount": 1000,
             |            "name": "SigUSD",
             |            "decimals": 2,
             |            "type": "EIP-004"
             |        },
             |        {
             |            "tokenId": "02faf2cb329f2e90d6d23b58d91bbb6c046aa143261cc21f52fbe2824bfcbf04",
             |            "index": 0,
             |            "amount": 1001,
             |            "name": "SigUSD",
             |            "decimals": 2,
             |            "type": "EIP-004"
             |        }
             |    ],
             |    "additionalRegisters": {},
             |    "spentTransactionId": "b2799d2dfa796a4e99f92ff48f85cdb80cb513333ab04ceb217e7f68f97190ad",
             |    "mainChain": true
             |}
             |""".stripMargin
    ).toOption.get

    val order = RedeemV3(
      output,
      SPF(1001),
      PoolId.unsafeFromString("0000000000000000000000000000000000000000000000000000000000000000"),
      ErgoTreeRedeemer(
        SErgoTree.unsafeFromString("0303030303030303030303030303030303030303030303030303030303030303")
      ),
      RedeemParams(
        AssetAmount(
          TokenId.unsafeFromString("03faf2cb329f2e90d6d23b58d91bbb6c046aa143261cc21f52fbe2824bfcbf04"),
          1000
        )
      ),
      777777,
      Version.V3,
      OrderType.AMM,
      Operation.Redeem
    )
  }

}
