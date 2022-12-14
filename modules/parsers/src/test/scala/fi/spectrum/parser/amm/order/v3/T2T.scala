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

object T2T {

  object swap {

    val outputSpf = decode[Output](
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
         |    "ergoTree": "19c4052204000e20010101010101010101010101010101010101010101010101010101010101010105f01504d00f0e200202020202020202020202020202020202020202020202020202020202020202040004c80f0100040404080402040001010408040405f015040606010104000e2000000000000000000000000000000000000000000000000000000000000000000e20030303030303030303030303030303030303030303030303030303030303030305c00c0101010105160518060100040201010e691005040004000e36100204a00b08cd0279be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798ea02d192a39a8cc7a701730073011001020402d19683030193a38cc7b2a57300000193c2b2a57301007473027303830108cdeeac93b1a573040500050005e2f85e0100d809d601b2a4730000d6027301d6037302d6047303d6057304d606db6308a7d607b27206730500d6088c720702d6097306d1ec730795ed92b1a4730893b1db630872017309d80dd60adb63087201d60bb2a5730a00d60cdb6308720bd60db2720c730b00d60e8c720d02d60f95730c9d9c7e720406997e720e067e7203067e730d067e720e06d610b2720a730e00d6117e8c72100206d6127206d6137207d6147e95948c72130172057208997208730f06d6157e8cb2720a7310000206d6169a720f7311edededededed938cb2720a73120001731393c2720b7314938c720d01720292720e73159573167317d801d617997e7203069d9c720f7e7318067e73190695917217731ad801d618b2720c731b00ed938c7218017205927e8c721802067217731c95938c7210017202909c9c721172147e7209069c72169a9c72157e7204069c72147e720906909c9c721572147e7209069c72169a9c72117e7204069c72147e72090690b0ada5d90117639593c27217731dc17217731e731fd90117599a8c7217018c72170273207321",
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

    val swapSpf = SwapV3(
      outputSpf,
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
        AssetAmount(
          TokenId.unsafeFromString("0101010101010101010101010101010101010101010101010101010101010101"),
          800
        ),
        11,
        12
      ),
      777777,
      1400,
      Version.V3,
      OrderType.AMM,
      Operation.Swap
    )

    val outputNoSpf = decode[Output](
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
         |    "ergoTree": "19c4052204000e20010101010101010101010101010101010101010101010101010101010101010105f01504d00f0e200202020202020202020202020202020202020202020202020202020202020202040004c80f0100040404080402040001010408040405f015040606010104000e2000000000000000000000000000000000000000000000000000000000000000000e20030303030303030303030303030303030303030303030303030303030303030305c00c0101010105160518060100040201010e691005040004000e36100204a00b08cd0279be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798ea02d192a39a8cc7a701730073011001020402d19683030193a38cc7b2a57300000193c2b2a57301007473027303830108cdeeac93b1a573040500050005e2f85e0100d809d601b2a4730000d6027301d6037302d6047303d6057304d606db6308a7d607b27206730500d6088c720702d6097306d1ec730795ed92b1a4730893b1db630872017309d80dd60adb63087201d60bb2a5730a00d60cdb6308720bd60db2720c730b00d60e8c720d02d60f95730c9d9c7e720406997e720e067e7203067e730d067e720e06d610b2720a730e00d6117e8c72100206d6127206d6137207d6147e95948c72130172057208997208730f06d6157e8cb2720a7310000206d6169a720f7311edededededed938cb2720a73120001731393c2720b7314938c720d01720292720e73159573167317d801d617997e7203069d9c720f7e7318067e73190695917217731ad801d618b2720c731b00ed938c7218017205927e8c721802067217731c95938c7210017202909c9c721172147e7209069c72169a9c72157e7204069c72147e720906909c9c721572147e7209069c72169a9c72117e7204069c72147e72090690b0ada5d90117639593c27217731dc17217731e731fd90117599a8c7217018c72170273207321",
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

    val swapNoSpf = SwapV3(
      outputNoSpf,
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
        AssetAmount(
          TokenId.unsafeFromString("0101010101010101010101010101010101010101010101010101010101010101"),
          800
        ),
        11,
        12
      ),
      777777,
      1400,
      Version.V3,
      OrderType.AMM,
      Operation.Swap
    )
  }

  object deposit {

    val outputSpf = decode[Output](
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
         |    "ergoTree": "19f2041d08cd02217daf90deb73bdf8b6709bb42093fdfaff6573fd47b630e2d3fdd4a8193a74d04000404040804020400040205feffffffffffffffff010404010105f60104020406010005f601040004000e2000000000000000000000000000000000000000000000000000000000000000000404040204040402010101000e691005040004000e36100204a00b08cd0279be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798ea02d192a39a8cc7a701730073011001020402d19683030193a38cc7b2a57300000193c2b2a57301007473027303830108cdeeac93b1a573040500050005e2f85e0100d802d6017300d602b2a4730100eb027201d195ed92b1a4730293b1db630872027303d80fd603db63087202d604b2a5730400d605db6308a7d6068cb2720573050002d607b27203730600d6087e9973078c72070206d609b27203730800d60a7e8c72090206d60b9d9c7e957309997206730a7206067208720ad60c8cb27205730b0002d60db27203730c00d60e7e8c720d0206d60f9d9c7e95730d99720c730e720c067208720ed610db63087204d611b27210730f00edededededed938cb2720373100001731193c27204d0720192c17204c1a795ed8f720b720f93b172107312d801d612b27210731300ed938c7212018c720d01927e8c721202069d9c99720f720b720e720895ed91720b720f93b172107314d801d612b27210731500ed938c7212018c720901927e8c721202069d9c99720b720f720a72089593720b720f73167317938c7211018c720701927e8c72110206a1720b720f90b0ada5d90112639593c272127318c172127319731ad90112599a8c7212018c721202731b731c",
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
         |            "tokenId": "01faf2cb329f2e90d6d23b58d91bbb6c046aa143261cc21f52fbe2824bfcbf04",
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

    val depositSpf: DepositV3 = DepositV3(
      outputSpf,
      SPF(123),
      PoolId.unsafeFromString("0000000000000000000000000000000000000000000000000000000000000000"),
      ErgoTreeRedeemer(
        SErgoTree.unsafeFromString("0000000000000000000000000000000000000000000000000000000000000000")
      ),
      DepositParams(
        AssetAmount(
          TokenId.unsafeFromString("03faf2cb329f2e90d6d23b58d91bbb6c046aa143261cc21f52fbe2824bfcbf04"),
          877
        ),
        AssetAmount(
          TokenId.unsafeFromString("01faf2cb329f2e90d6d23b58d91bbb6c046aa143261cc21f52fbe2824bfcbf04"),
          1001
        )
      ),
      777777,
      Version.V3,
      OrderType.AMM,
      Operation.Deposit
    )

    val outputSpfIsX = decode[Output](
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
         |    "ergoTree": "19f2041d08cd02217daf90deb73bdf8b6709bb42093fdfaff6573fd47b630e2d3fdd4a8193a74d04000404040804020400040205feffffffffffffffff010404010005f60104020406010105f601040004000e2000000000000000000000000000000000000000000000000000000000000000000404040204040402010101000e691005040004000e36100204a00b08cd0279be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798ea02d192a39a8cc7a701730073011001020402d19683030193a38cc7b2a57300000193c2b2a57301007473027303830108cdeeac93b1a573040500050005e2f85e0100d802d6017300d602b2a4730100eb027201d195ed92b1a4730293b1db630872027303d80fd603db63087202d604b2a5730400d605db6308a7d6068cb2720573050002d607b27203730600d6087e9973078c72070206d609b27203730800d60a7e8c72090206d60b9d9c7e957309997206730a7206067208720ad60c8cb27205730b0002d60db27203730c00d60e7e8c720d0206d60f9d9c7e95730d99720c730e720c067208720ed610db63087204d611b27210730f00edededededed938cb2720373100001731193c27204d0720192c17204c1a795ed8f720b720f93b172107312d801d612b27210731300ed938c7212018c720d01927e8c721202069d9c99720f720b720e720895ed91720b720f93b172107314d801d612b27210731500ed938c7212018c720901927e8c721202069d9c99720b720f720a72089593720b720f73167317938c7211018c720701927e8c72110206a1720b720f90b0ada5d90112639593c272127318c172127319731ad90112599a8c7212018c721202731b731c",
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
         |            "tokenId": "01faf2cb329f2e90d6d23b58d91bbb6c046aa143261cc21f52fbe2824bfcbf04",
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

    val depositSpfIsX: DepositV3 = DepositV3(
      outputSpfIsX,
      SPF(123),
      PoolId.unsafeFromString("0000000000000000000000000000000000000000000000000000000000000000"),
      ErgoTreeRedeemer(
        SErgoTree.unsafeFromString("0000000000000000000000000000000000000000000000000000000000000000")
      ),
      DepositParams(
        AssetAmount(
          TokenId.unsafeFromString("03faf2cb329f2e90d6d23b58d91bbb6c046aa143261cc21f52fbe2824bfcbf04"),
          1000
        ),
        AssetAmount(
          TokenId.unsafeFromString("01faf2cb329f2e90d6d23b58d91bbb6c046aa143261cc21f52fbe2824bfcbf04"),
          878
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
         |    "ergoTree": "19c40314040001000404040804020400040404020406040005feffffffffffffffff01040204000e2000000000000000000000000000000000000000000000000000000000000000000e2003030303030303030303030303030303030303030303030303030303030303030e691005040004000e36100204a00b08cd0279be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798ea02d192a39a8cc7a701730073011001020402d19683030193a38cc7b2a57300000193c2b2a57301007473027303830108cdeeac93b1a573040500050005e2f85e0100d801d601b2a4730000d1ec730195ed92b1a4730293b1db630872017303d809d602db63087201d603b2a5730400d604db63087203d605b27204730500d606b27202730600d607b27204730700d608b27202730800d6097e8cb2db6308a77309000206d60a7e99730a8cb27202730b000206edededededed938cb27202730c0001730d93c27203730e938c7205018c720601938c7207018c720801927e8c720502069d9c72097e8c72060206720a927e8c720702069d9c72097e8c72080206720a90b0ada5d9010b639593c2720b730fc1720b73107311d9010b599a8c720b018c720b0273127313",
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
