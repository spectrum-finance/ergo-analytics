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

object T2T {

  object swap {

    val outputSpf = decode[Output](
      s"""
         |{
         |    "boxId": "2b07a640f0e6cf2d3cbbac97822dd8a74b8aa9321138ab071db185728a181c00",
         |    "transactionId": "2d979e63d7950265c53da145f987a02d4b135f8cbe441dd89a4adcc41718ffb6",
         |    "blockId": "2a4351a1d248ffced621d010c48dc6ba98f85a636f6b93ae1daa0bea6fdbeb09",
         |    "value": 400000,
         |    "index": 0,
         |    "globalIndex": 28280300,
         |    "creationHeight": 982347,
         |    "settlementHeight": 982349,
         |    "ergoTree": "19d2052304000e2003faf2cb329f2e90d6d23b58d91bbb6c046aa143261cc21f52fbe2824bfcbf0405808095e789c60405f6dc87c00304bc0f04d00f08cd025031ca056b8f9e910a9879059d5edf27a7322285832a60aed9655d5a0114a6030404040804020400010005c8e81505b5cbe4eceeaaee120404040606010104000e20d8c82a39215e41db9244a855b675b8efa71cd2eb3d98f008e951c79b0ff0e0150e240008cd025031ca056b8f9e910a9879059d5edf27a7322285832a60aed9655d5a0114a60305b0040100010105f015059c01060100040404020e209a06d9e545a41fd51eeffc5e20d818073bf820c635e2a9d922269913e0de369d01010e691005040004000e36100204a00b08cd0279be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798ea02d192a39a8cc7a701730073011001020402d19683030193a38cc7b2a57300000193c2b2a57301007473027303830108cdeeac93b1a5730405000500058092f4010100d805d601b2a4730000d6027301d6037302d6049c73037e730405d6057305eb027306d195ed92b1a4730793b1db630872017308d80ad606db63087201d607b2a5730900d608db63087207d609b27208730a00d60a8c720902d60b95730b9d9c7e99720a730c067e7203067e730d067e720a06d60cb27206730e00d60d7e8c720c0206d60e7e8cb27206730f000206d60f9a720b7310ededededededed938cb2720673110001731293c272077313938c720901720292720a731492c17207c1a79573157316d801d610997e7317069d9c720b7e7318067e72030695ed917210731992b17208731ad801d611b27208731b00ed938c721101731c927e8c721102067210731d95938c720c017202909c720d7e7204069c720f9a9c720e7e7205067e720406909c720e7e7204069c720f9a9c720d7e7205067e72040690b0ada5d90110639593c27210731ec17210731f7320d90110599a8c7210018c72100273217322",
         |    "address": "XjLvPNqZqd8npV3rPWRcR8vneV6FSdFrahJerkgMkCc5kszp4RgRocMsUezeta8LTeYrv2uS4XFsQc74LCtJiqPQzvucbstBf6UkSou1RuiFsCH7D6ac3NiVUijo1JHy6S1iCCwD4T4CpWR1KSrk7WjEhdrckhYFZFgjhWDWLmYePWhxZgWhVjrpLNQsszEd8a56cDEKCmNk7WRXUAMzkPXBZFacYEkarVigm5WobDFM898n4Bzj1exUEHceAyX1JrohnDoqxmnfThvRqtnVs4bxz3LDdmCrtc31NAEWov4oi9p3bKCv1hJvsS36DoQHPswh3FiaL4nwhJf3FwbNpquhwbsnVGPeCg2muj4c57od2xJSnnV5vL9ECBmec2L1n3yqqiY2ceUBXThkhHX1pNDKsrP4CfjtEUbjxGnXGfwcekueYmVpPUccnQWLXLKKFY9nDS1dy7rMNh6Nqdrp9rDyc3eigsD1ZZf5aVTBzoMjZX1WzGTrAbz4QcXqYnR9nJgHFfLhi5gNQ5TiFUPhqBp4FKrU6KJZRUHqxypnvSwyPCteEFh8KvQe46dAHe7HD5G7XCqnDw8FPCWzBvCT2B3b8S6RUh9V5ZMY2L6VKse71EvRr3XwHig5rEdHfXVhWCW2WXLAUdNWgxXZj4FHubF2btCaTWsecCx2rYcbWohnuCsbKrFFTraT2Q81Lo5KRkDikQkpdVFD2c1BpvqYpyCaF7EZaU1ShYvvtzGHsXbcJPrfX9ir8znKvGKryCD6mncbLTuafjVRgtwynJphYMtrEQJm5h292244RJZr7b2euYjnQzz2JHiRXXeXqiksFfehw8mH73pZKvqb2tJfFUJtSqxtPqX971D11NypA6bPuKuewd1CXnEfXKHNyXY3NekoLffGfnXwmvL29f1KunkEoan7z767vJXueKL6DprwVmmqrsMYou8juqAcUK2QMj8NcC3sdX81pNNgymtvL2pzPBonBaZvDhkp",
         |    "assets": [
         |        {
         |            "tokenId": "e8b20745ee9d18817305f32eb21015831a48f02d40980de6e849f886dca7f807",
         |            "index": 0,
         |            "amount": 469825339,
         |            "name": "Flux",
         |            "decimals": 8,
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
         |    "spentTransactionId": "2599cf5a8c993cc23d9f2c89c30cee85185fc4a0b626ade0212d96eda109d708",
         |    "mainChain": true
         |}
         |""".stripMargin
    ).toOption.get

    val swapSpf = SwapV3(
      outputSpf,
      PoolId.unsafeFromString("d8c82a39215e41db9244a855b675b8efa71cd2eb3d98f008e951c79b0ff0e015"),
      PublicKeyRedeemer(
        PubKey.unsafeFromString(
          "025031ca056b8f9e910a9879059d5edf27a7322285832a60aed9655d5a0114a603"
        )
      ),
      SwapParams(
        AssetAmount(
          TokenId.unsafeFromString("e8b20745ee9d18817305f32eb21015831a48f02d40980de6e849f886dca7f807"),
          469825339
        ),
        AssetAmount(
          TokenId.unsafeFromString("03faf2cb329f2e90d6d23b58d91bbb6c046aa143261cc21f52fbe2824bfcbf04"),
          280
        ),
        5319178571428571L,
        10000000000000L
      ),
      2000000,
      178724,
      Version.V3
    )

    val outputNoSpf = decode[Output](
      s"""
         |{
         |    "boxId": "7647887d83baf394828a82cc3dd9b3a7467d53aec224699dd9f28f7ec656f2e9",
         |    "transactionId": "a6001a775af989663a3361c3d1cbabc8f094ca2dfbbaedf61abd78d01ea09349",
         |    "blockId": "3e997243794924576440a128ce74a595e38d06bb80f73adeb9f820ed87ea01f2",
         |    "value": 400000,
         |    "index": 0,
         |    "globalIndex": 28261716,
         |    "creationHeight": 981833,
         |    "settlementHeight": 981835,
         |    "ergoTree": "19cf052304000e20003bd19d0187117f130b62e1bcab0939929ff5c7709f843c5c4dd158949285d0058080d287e2bc2d05ec0404c60f04d00f08cd025031ca056b8f9e910a9879059d5edf27a7322285832a60aed9655d5a0114a603040404080402040001000580c315059d81ecb4bebec50d0404040606010104000e20080e453271ff4d2f85f97569b09755e67537bf2a1bc1cd09411b459cf901b9020e240008cd025031ca056b8f9e910a9879059d5edf27a7322285832a60aed9655d5a0114a60305d83a0100010105f015059c01060100040404020e209a06d9e545a41fd51eeffc5e20d818073bf820c635e2a9d922269913e0de369d01010e691005040004000e36100204a00b08cd0279be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798ea02d192a39a8cc7a701730073011001020402d19683030193a38cc7b2a57300000193c2b2a57301007473027303830108cdeeac93b1a5730405000500058092f4010100d805d601b2a4730000d6027301d6037302d6049c73037e730405d6057305eb027306d195ed92b1a4730793b1db630872017308d80ad606db63087201d607b2a5730900d608db63087207d609b27208730a00d60a8c720902d60b95730b9d9c7e99720a730c067e7203067e730d067e720a06d60cb27206730e00d60d7e8c720c0206d60e7e8cb27206730f000206d60f9a720b7310ededededededed938cb2720673110001731293c272077313938c720901720292720a731492c17207c1a79573157316d801d610997e7317069d9c720b7e7318067e72030695ed917210731992b17208731ad801d611b27208731b00ed938c721101731c927e8c721102067210731d95938c720c017202909c720d7e7204069c720f9a9c720e7e7205067e720406909c720e7e7204069c720f9a9c720d7e7205067e72040690b0ada5d90110639593c27210731ec17210731f7320d90110599a8c7210018c72100273217322",
         |    "address": "MjCexemzjm4NfZHfR1HRKTad7SygbVEMppuuaL5mjzYi7uxR89mAgiP96Qi2TE2bX9cGc8fvQe8kE7YpEnZjSvjvdAh4DS8hoBoL8gS1WE5qx6AYz2KetGfBdXaSqo1gYHuPuejXgL6CrDpTNeHdogdLEBCMwa6N6TteDfM7oh3r8LJN4acYTxLmtfimh6gsFekZv4yyxHdmCKXajt1ZixDB6ieQKsBt3hsuC9zZLf43twogZJeuUH9CHZAwRzBLECuZScSkca5CM1pwFhfNRvnWUYAdv6zQr2oUiSBYGq6gmWvFMWUNpvkJ5j4EQkAHiLyM9jn9fzTZ1attAHqBvgyzbUegG5LcWtzPsuxQc9Uzs52QxWddco3W2J7PKteaFTHEvssLXGLaaZi8sMqgcx37p6u1D32WWBB2Ltkqf5mjCRWiRP9cudhjzzyBcQFHc45LRnw31Ex2x7oMH9kPPudnMAExmxEeFLmqXtAKHT219UJyze1fntbkg59PC8V8Y1QsXwBse6xMuK4Q9Umzi8vwK6vJwMLqVHnohX9n4AKp3cvPDxHiUQL2qsfgpfLWXzs4FogqWGTyspticcTtTtUmbtTKgoHwu4fjghqPMA5AQWn4WQLiM4hCqv3rNL8no6d7UrvDCYAb6Epa5n9u1RSux1YdfqjG1C97x2TXSTRJ51NV2kiwwzMaALpThwMk8koy5wxG1kGPhHzoaPZSG2MR6P2msimcXmjSX5ZmYW3AsFReZKMpMkG89z8hoeb132Qx5RjJPTP4BVsKxuXZBqmzjpDZUQfjSQWtqgwPS3mBZfJ4mJsYmf52hPpLRY1KP7kBwvNFctnXSsZkUpfLST5U5S6KD4VZPfdWcWEmb4aiw7Zj6ExxzQc4fwwkuHv4SfxY2wHRwmbmPxC9G5TsRoXyjraTZr3KJqu7sQa78DUc2rbfxBA8xAGS4yvf6LNDM1xKfVKqPssWJPUim6SsXERpDrbRBm5R",
         |    "assets": [
         |        {
         |            "tokenId": "03faf2cb329f2e90d6d23b58d91bbb6c046aa143261cc21f52fbe2824bfcbf04",
         |            "index": 0,
         |            "amount": 310,
         |            "name": "SigUSD",
         |            "decimals": 2,
         |            "type": "EIP-004"
         |        },
         |        {
         |            "tokenId": "9a06d9e545a41fd51eeffc5e20d818073bf820c635e2a9d922269913e0de369d",
         |            "index": 1,
         |            "amount": 176320,
         |            "name": "SPF",
         |            "decimals": 6,
         |            "type": "EIP-004"
         |        }
         |    ],
         |    "additionalRegisters": {},
         |    "spentTransactionId": "e92f214e65ffc7d96896d2eba45fd6a00a0046129b2550a6815915306c80ce4b",
         |    "mainChain": true
         |}
         |""".stripMargin
    ).toOption.get

    val swapNoSpf = SwapV3(
      outputNoSpf,
      PoolId.unsafeFromString("080e453271ff4d2f85f97569b09755e67537bf2a1bc1cd09411b459cf901b902"),
      PublicKeyRedeemer(
        PubKey.unsafeFromString(
          "025031ca056b8f9e910a9879059d5edf27a7322285832a60aed9655d5a0114a603"
        )
      ),
      SwapParams(
        AssetAmount(
          TokenId.unsafeFromString("03faf2cb329f2e90d6d23b58d91bbb6c046aa143261cc21f52fbe2824bfcbf04"),
          310
        ),
        AssetAmount(
          TokenId.unsafeFromString("003bd19d0187117f130b62e1bcab0939929ff5c7709f843c5c4dd158949285d0"),
          3756
        ),
        3911980830670927L,
        100000000000000L
      ),
      2000000,
      176320,
      Version.V3
    )
  }

  object deposit {

    val outputSpf = decode[Output](
      s"""
         |{
         |    "boxId": "e48c5e856ee274a6175800e471e3a135e9af0027de04ea44b77fd07fc7b9908c",
         |    "transactionId": "5c8355957ce18e492cd36406ee442b82022bfe879933f83a4bc4f27a940dc9be",
         |    "blockId": "f031cc3cfb4bf6fd7bb21024ebf176ebdf6d096c22fa7829a4cf440553ae81be",
         |    "value": 400000,
         |    "index": 0,
         |    "globalIndex": 26967066,
         |    "creationHeight": 949420,
         |    "settlementHeight": 949422,
         |    "ergoTree": "19e3041a040008cd03b196b978d77488fba3138876a40a40b9a046c2fbb5ecfa13d4ecf8f1eec52aec040404080402040205feffffffffffffffff0104040590a2c60504060504040004000e20bfb483069f2809349a9378e8a746cbdcb44c66f94d0e839fd14158e8829eea680e240008cd03b196b978d77488fba3138876a40a40b9a046c2fbb5ecfa13d4ecf8f1eec52aec0404040204040402010101000e691005040004000e36100204a00b08cd0279be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798ea02d192a39a8cc7a701730073011001020402d19683030193a38cc7b2a57300000193c2b2a57301007473027303830108cdeeac93b1a5730405000500058092f4010100d801d601b2a4730000eb027301d195ed92b1a4730293b1db630872017303d80cd602db63087201d603b2a5730400d604b27202730500d6057e9973068c72040206d606b27202730700d6077e8c72060206d6089d9c7e73080672057207d609b27202730900d60a7e8c72090206d60b9d9c7e730a067205720ad60cdb63087203d60db2720c730b00edededededed938cb27202730c0001730d93c27203730e92c17203c1a795ed8f7208720b93b1720c730fd801d60eb2720c731000ed938c720e018c720901927e8c720e02069d9c99720b7208720a720595ed917208720b93b1720c7311d801d60eb2720c731200ed938c720e018c720601927e8c720e02069d9c997208720b7207720595937208720b73137314938c720d018c720401927e8c720d0206a17208720b90b0ada5d9010e639593c2720e7315c1720e73167317d9010e599a8c720e018c720e0273187319",
         |    "address": "3pgwev33aP9MzhGLC5gwnKTL5e8gQNon3iFvVWTe5tkHX6C3j9rfGQ5qJYaoSUVoh14FGUkaZce6HrByVr82EABLkByViVSc6zwyJWdyeRcCw4AjFoZGHxjmNbJFdoMpw5dPBwEe3J4QD92ZGZMeX3sVDpyH19mg3rQFEbc49LpZVw3RMzmYngWYSt3BteihHkw1XuEfbeT9UmT8h1XjAbNP9GXce3MsiZTN7SGyB5s4BsoFTqoShLjmW7gKhsxPBH2pmnCHNoBWRoLTdAnTsU7xVmdmVsB3xn5DJsuLBif14eZhvc8uPWX58cLg8b9fa61L7jhKubT7TGvU3xAhu2NUxhSh2oVDi6LFX8D7Lqpr68jzLMQyvaLsWj8JkUUDBNmy9zHxoV3YQY4zVLyPQmWiyYBew39Qkix1BzMjCgBqyxyxLuGTPd1LmXkqKHAuqrsLGkZdvdWi1vFpHwccfys8WArzy7LLati1F3TekRr52ifYovgzABqhm7W4VDNBYjLkuJxu1myYSJW6ZVhwmCADMW6QuAeeKk6Qvtzs3CLUjxWY8kU5BfW2iUBfzX3vuZ6iDdkgaXY4ESdvrXtCSUV3urTN2BP7rqnukEzfjyyJ2YMuscGUrnk5gbF1uCFDomryYEXnaZ83TWQTRNdY7VmKxSRk6dD71bA475aSSsyVoJP3anneXBbCJ5g7M59f2f9g4WuAQhSBTvPpdm36GTZrG3ku9fSfQvCb81mVu4uTM54sfoMAET3UzP7nCzcN6AUFaBzmQ7oe7b2uq6eqvbp425s5ozohHh6M8rie3RpbsYR3eFgcSaD4FwLSP9BfrtVcMZ2wTt3VF",
         |    "assets": [
         |        {
         |            "tokenId": "472c3d4ecaa08fb7392ff041ee2e6af75f4a558810a74b28600549d5392810e8",
         |            "index": 0,
         |            "amount": 5818504,
         |            "name": "NETA",
         |            "decimals": 6,
         |            "type": "EIP-004"
         |        },
         |        {
         |            "tokenId": "03faf2cb329f2e90d6d23b58d91bbb6c046aa143261cc21f52fbe2824bfcbf04",
         |            "index": 1,
         |            "amount": 2,
         |            "name": "SigUSD",
         |            "decimals": 2,
         |            "type": "EIP-004"
         |        },
         |        {
         |            "tokenId": "9a06d9e545a41fd51eeffc5e20d818073bf820c635e2a9d922269913e0de369d",
         |            "index": 2,
         |            "amount": 135031,
         |            "name": "SPF",
         |            "decimals": 6,
         |            "type": "EIP-004"
         |        }
         |    ],
         |    "additionalRegisters": {},
         |    "spentTransactionId": "d7d423d6848da0df7018ff34bc4000e3dedfa4434204f9c963451e5f17b005e4",
         |    "mainChain": true
         |}
         |""".stripMargin
    ).toOption.get

    val depositSpf: AmmDepositV3 = AmmDepositV3(
      outputSpf,
      SPF(135031),
      PoolId.unsafeFromString("bfb483069f2809349a9378e8a746cbdcb44c66f94d0e839fd14158e8829eea68"),
      PublicKeyRedeemer(
        PubKey.unsafeFromString("03b196b978d77488fba3138876a40a40b9a046c2fbb5ecfa13d4ecf8f1eec52aec")
      ),
      AmmDepositParams(
        AssetAmount(
          TokenId.unsafeFromString("472c3d4ecaa08fb7392ff041ee2e6af75f4a558810a74b28600549d5392810e8"),
          5818504
        ),
        AssetAmount(
          TokenId.unsafeFromString("03faf2cb329f2e90d6d23b58d91bbb6c046aa143261cc21f52fbe2824bfcbf04"),
          2
        )
      ),
      2000000,
      Version.V3
    )

    val outputSpfIsX = decode[Output](
      s"""
         |{
         |    "boxId": "4774de4f9dab4d60fcf654c09faa0decfd08dd94ac4c58b1a04ac1e7de3abdaa",
         |    "transactionId": "e3d6c3d3775776b4660d29343db62fea55faafec32f53122dbfd08137071f9be",
         |    "blockId": "b7f55bee53d2930ed703a6805069a123fb9dc391cf61e27443c3970ce2419d2d",
         |    "value": 400000,
         |    "index": 0,
         |    "globalIndex": 26939118,
         |    "creationHeight": 948753,
         |    "settlementHeight": 948755,
         |    "ergoTree": "19e0041a040008cd03b196b978d77488fba3138876a40a40b9a046c2fbb5ecfa13d4ecf8f1eec52aec040404080402040205feffffffffffffffff010404050204060536040004000e20080e453271ff4d2f85f97569b09755e67537bf2a1bc1cd09411b459cf901b9020e240008cd03b196b978d77488fba3138876a40a40b9a046c2fbb5ecfa13d4ecf8f1eec52aec0404040204040402010101000e691005040004000e36100204a00b08cd0279be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798ea02d192a39a8cc7a701730073011001020402d19683030193a38cc7b2a57300000193c2b2a57301007473027303830108cdeeac93b1a5730405000500058092f4010100d801d601b2a4730000eb027301d195ed92b1a4730293b1db630872017303d80cd602db63087201d603b2a5730400d604b27202730500d6057e9973068c72040206d606b27202730700d6077e8c72060206d6089d9c7e73080672057207d609b27202730900d60a7e8c72090206d60b9d9c7e730a067205720ad60cdb63087203d60db2720c730b00edededededed938cb27202730c0001730d93c27203730e92c17203c1a795ed8f7208720b93b1720c730fd801d60eb2720c731000ed938c720e018c720901927e8c720e02069d9c99720b7208720a720595ed917208720b93b1720c7311d801d60eb2720c731200ed938c720e018c720601927e8c720e02069d9c997208720b7207720595937208720b73137314938c720d018c720401927e8c720d0206a17208720b90b0ada5d9010e639593c2720e7315c1720e73167317d9010e599a8c720e018c720e0273187319",
         |    "address": "2uQihBXBJDVbjpuuS1HEue3WtBhs9VepJMcevcLgHVxyprbLjnudeeQEKCQkAgX22t9sNJuH4UTvdDJe9jbbxR9sWoYPSEsJbMVXzo5RJFFxWRQePbiutcNHE1PZtGQ87NWhKKSA4TevwjeL8neoQDzaHL4tS8GdrVo5pdJsDfm5a4faip5x8HpNAjYFCgE3aRVjRbZA7giPKquZANr9CUqsXcZuHqw4EAjjs8Ht9DDWt3ovHTLzRFYAuRZhfepUNW5roxTrHBwWAFSAt8w5Mhs2cxue3jma9pc7t3964y4UiTPsPNAdq2nuwhDyqiCB1c5UWUJ1BW7qdnsmJCTNC5vvptLjzKQLHWSLwEP69GYpFv2mzon2M4iBSWLqH4ndX6wA4gBdbrqy9dB2bKgBG68whkw94QGVBgT7uQCukh6jSZ6CKNzbPVi7oHDvbKf4GreMQNKgGCkjb33qiQJSfKnCCeMSwonx5THBLFGBp3xHwq6mHgDnV4pZJxJndCQUzY25gHvq5AfJSxGKHWjrZzUSMVr1LP8sqQEdDs8osGbHxVLYhrXbY633CKXQM4PGvttk7e675koyznpQoZ51ffDPWUQv6yesyffZ9U3sffr2C9JJWi761vkeksutGSJX7fE8DoZq3H2mNam7m2xHfrNv318kZGGwGMSxbwFHzf9QCgs83KhmGst1fvhzF7kLiP9MveKSnyhakf3kvZun4a4Fd1uxU3djyiDWartapzzNPeZgVf4R5jyF6WbtywZ1uykaXbpTVYmuFsBYew2rpekGYkNknfKHBuQPDfJ541v2EoNe3sw6cteDzVyasy1v21Mf4BTgq",
         |    "assets": [
         |        {
         |            "tokenId": "03faf2cb329f2e90d6d23b58d91bbb6c046aa143261cc21f52fbe2824bfcbf04",
         |            "index": 0,
         |            "amount": 1,
         |            "name": "SigUSD",
         |            "decimals": 2,
         |            "type": "EIP-004"
         |        },
         |        {
         |            "tokenId": "003bd19d0187117f130b62e1bcab0939929ff5c7709f843c5c4dd158949285d0",
         |            "index": 1,
         |            "amount": 27,
         |            "name": "SigRSV",
         |            "decimals": 0,
         |            "type": "EIP-004"
         |        },
         |        {
         |            "tokenId": "9a06d9e545a41fd51eeffc5e20d818073bf820c635e2a9d922269913e0de369d",
         |            "index": 2,
         |            "amount": 134568,
         |            "name": "SPF",
         |            "decimals": 6,
         |            "type": "EIP-004"
         |        }
         |    ],
         |    "additionalRegisters": {},
         |    "spentTransactionId": "394e6eca34ba5427e4f70cdf9432ef3b557c91c1eca386059511cb9289d90b3e",
         |    "mainChain": true
         |}
         |""".stripMargin
    ).toOption.get

    val depositSpfIsX: AmmDepositV3 = AmmDepositV3(
      outputSpfIsX,
      SPF(134568),
      PoolId.unsafeFromString("080e453271ff4d2f85f97569b09755e67537bf2a1bc1cd09411b459cf901b902"),
      PublicKeyRedeemer(
        PubKey.unsafeFromString("03b196b978d77488fba3138876a40a40b9a046c2fbb5ecfa13d4ecf8f1eec52aec")
      ),
      AmmDepositParams(
        AssetAmount(
          TokenId.unsafeFromString("03faf2cb329f2e90d6d23b58d91bbb6c046aa143261cc21f52fbe2824bfcbf04"),
          1
        ),
        AssetAmount(
          TokenId.unsafeFromString("003bd19d0187117f130b62e1bcab0939929ff5c7709f843c5c4dd158949285d0"),
          27
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
         |    "boxId": "c5b2fe08a73564216d38dc7423f13f43e1ed0e7a2e03e72e1960f9209029d7c8",
         |    "transactionId": "f671b9b9a3ec7357cb118bc45147df5010ecb92596fc261ba797f7014fa45291",
         |    "blockId": "46e3535415e6b4df00f3add6ee599530353aa79b0cc5cab60c515138560e963b",
         |    "value": 400000,
         |    "index": 0,
         |    "globalIndex": 28025970,
         |    "creationHeight": 975770,
         |    "settlementHeight": 975772,
         |    "ergoTree": "19eb0314040008cd02d4b86f73bc65b8ff943bf54c5211c9160b9695f359d3e71f92abb988726ca6310404040804020400040404020406040005feffffffffffffffff01040204000e201f54d04bc509883023d3b7550232f687fb6210305416ee0706b701825acb6dd20e240008cd02d4b86f73bc65b8ff943bf54c5211c9160b9695f359d3e71f92abb988726ca6310e691005040004000e36100204a00b08cd0279be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798ea02d192a39a8cc7a701730073011001020402d19683030193a38cc7b2a57300000193c2b2a57301007473027303830108cdeeac93b1a5730405000500058092f4010100d801d601b2a4730000eb027301d195ed92b1a4730293b1db630872017303d809d602db63087201d603b2a5730400d604db63087203d605b27204730500d606b27202730600d607b27204730700d608b27202730800d6097e8cb2db6308a77309000206d60a7e99730a8cb27202730b000206edededededed938cb27202730c0001730d93c27203730e938c7205018c720601938c7207018c720801927e8c720502069d9c72097e8c72060206720a927e8c720702069d9c72097e8c72080206720a90b0ada5d9010b639593c2720b730fc1720b73107311d9010b599a8c720b018c720b0273127313",
         |    "address": "5cZpjmfCNTqeecfAF3X3SqxvvEEPmmd6tuWLSB8DB3s68fPiXZzKU2U9D2Z9VWeSguUWkiTpMWTQLgq9c4r4ynt2Sjy9hrqraYuqvpAqK1L78YGCbgNV9MLtTGghxSyXcirVfNWLdN4VoS8sZCgwKeWGymf2hGagcefwXNB5tKAq3Msg8U4dX6xcWWuMubz8THTTkjRVaaGZuGQ1xsSHuBDLE9Ju5whb4t3PzXMzC2F3BsFSHncHVoTtErPYPh1DuNqpdqPz8433L98z9dSYgEwnQEBAZJqeBqf8D8uQChzyzhv3HMjNXHtYm4xRjukSE5kd1uhNeHEJuzdqt78gypabKFQC3VfB63KMDmZvpKuqPNbeT6q3nVV3jJWuFYohWE58wBeFLrRyTTTq5ccM1C2WBMTrL8a1uJ135wqYtqn7BxpoLdVLsKsgC2KYP1BVtoah2Z2Q2uuN5X9d71JEYnjMw3qN7HMei4ARdPcq9enM2GrfVJh8535oKmNrJrgFLryERxdkyveL3pykTDBLei7QwpZn3E4b7JgWXYxXFkCxnWzUePMASdZnkBi3VDJpjGbTD8GajdphriDh7cKkcVrjEDyuuRcncwAdZRT3gFBSb3kmyeJ4UwH6bJ4GNTCzd35a9fv2nPSK6AEDDCo2AqjzRS4nbFFzhdjdyEtjf",
         |    "assets": [
         |        {
         |            "tokenId": "04928901c08363208a29e334af73cd428f3d92d3cce8e379bcd0aee6231a421d",
         |            "index": 0,
         |            "amount": 54725090908,
         |            "name": "ef802b47_30974274_LP",
         |            "decimals": 0,
         |            "type": "EIP-004"
         |        },
         |        {
         |            "tokenId": "9a06d9e545a41fd51eeffc5e20d818073bf820c635e2a9d922269913e0de369d",
         |            "index": 1,
         |            "amount": 145917,
         |            "name": "SPF",
         |            "decimals": 6,
         |            "type": "EIP-004"
         |        }
         |    ],
         |    "additionalRegisters": {},
         |    "spentTransactionId": "5c389a60b2e613fe7480bb2f359b4fbc14e478cc2b4d2b991687786b04a7cdf3",
         |    "mainChain": true
         |}
         |""".stripMargin
    ).toOption.get

    val order = RedeemV3(
      output,
      SPF(145917),
      PoolId.unsafeFromString("1f54d04bc509883023d3b7550232f687fb6210305416ee0706b701825acb6dd2"),
      PublicKeyRedeemer(
        PubKey.unsafeFromString("02d4b86f73bc65b8ff943bf54c5211c9160b9695f359d3e71f92abb988726ca631")
      ),
      RedeemParams(
        AssetAmount(
          TokenId.unsafeFromString("04928901c08363208a29e334af73cd428f3d92d3cce8e379bcd0aee6231a421d"),
          54725090908L
        )
      ),
      2000000,
      Version.V3
    )
  }
}
