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
         |    "boxId": "374f037640bf0dbf6e34244871937e75aaf110de9254ffcf09aa06291c11d375",
         |    "transactionId": "3da3aae612feee5ecf8d0af01e1205bdf25c01b09e5774517c8abd9387f81c03",
         |    "blockId": "258667b9c516bc0610017e92b3690c137d7aec673f6b6ad98d2c90fd2c31d780",
         |    "value": 12323164,
         |    "index": 0,
         |    "globalIndex": 26670124,
         |    "creationHeight": 942994,
         |    "settlementHeight": 942996,
         |    "ergoTree": "19c30417040005f8fbd80b08cd03b196b978d77488fba3138876a40a40b9a046c2fbb5ecfa13d4ecf8f1eec52aec040404060402040205feffffffffffffffff0104040504040004000e209916d75132593c8b07fe18bd8d583bda1652eed7565cf41a4738ddd90fc992ec0e240008cd03b196b978d77488fba3138876a40a40b9a046c2fbb5ecfa13d4ecf8f1eec52aec0404040205c0b80201000e691005040004000e36100204a00b08cd0279be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798ea02d192a39a8cc7a701730073011001020402d19683030193a38cc7b2a57300000193c2b2a57301007473027303830108cdeeac93b1a5730405000500058092f4010100d802d601b2a4730000d6027301eb027302d195ed92b1a4730393b1db630872017304d80bd603db63087201d604b2a5730500d605b27203730600d6067e9973078c72050206d6077ec1720106d6089d9c7e72020672067207d609b27203730800d60a7e8c72090206d60b9d9c7e7309067206720ad60cdb63087204d60db2720c730a00ededededed938cb27203730b0001730c93c27204730d95ed8f7208720b93b1720c730ed801d60eb2720c730f00eded92c1720499c1a77310938c720e018c720901927e8c720e02069d9c99720b7208720a720695927208720b927ec1720406997ec1a706997e7202069d9c997208720b720772067311938c720d018c720501927e8c720d0206a17208720b90b0ada5d9010e639593c2720e7312c1720e73137314d9010e599a8c720e018c720e0273157316",
         |    "address": "AVwRjyvectUWbmNGFBDonnfZue2PmZwhZtn3jBssdMdzBvnRw6moHaPGRAkesgkmfgs4cWU11Jxg1NQzNYzFE2VNwf4kVfxZfo7d2xD4YKEfP2oMMNnPyWVhTRt5vbCE4gEpbbcCDhY68dmx4Bx9bTd8eQQxJG2NzmSpuzU4qzVhrrqwg3Mq1sbc6zdADqzs2k8GQQt38GxG5QQLhJSnZEAB8TvEiMZGEKqPMATqY9HETxr13959bHLTyn2SvDdqDVKF7ZgpjEWmiqoikTaSLTGwfMopBXrE3bWTY26tFaL698zuPZ9zK6Ruz6kK3B2MCbFxemzxoiaoYnLfAjyjzczajp2ZP5eTasE76Ly2GtnAYnpfFa4VUAaSnCY2CQ2doSGwNCGd3DZE6e1btpewLUV33ZWAYj5NhxnrnXGPg1V9gz31HfbPiDMGdQcip41R99GmJLXfY3CoxLkJLPVYBXTvaRyNZHziE6JFHeMn41yTgKDBy9zoQLXsEn9v8vRJX9N44Ftc3wMR7hcfxgE2AdaH591ZFjEyV6FzKD6g53mGxfDNBaiqcLmvoE1Q26XyYzoac5weU9T39BbJaeETbZiJ81WH5CrcQmfZyYkRzypRKCD7cHnJ4MXQojo7Ff5gF5EP3Ta15eoFgcUDLKWCQTxirz5nBA3tqsZQuSD2A4FkQG1UZ3d27JJM7vjy5k1NJ6w3Ty4YaTUX2e2zm16sDN6L58oTSp21aeFcc952vX6FtbdYWUr68ifBux9ZCoS2sb7ZpacNz3NQvr8YnWY51QGPATZG4qagz",
         |    "assets": [
         |        {
         |            "tokenId": "03faf2cb329f2e90d6d23b58d91bbb6c046aa143261cc21f52fbe2824bfcbf04",
         |            "index": 0,
         |            "amount": 2,
         |            "name": "SigUSD",
         |            "decimals": 2,
         |            "type": "EIP-004"
         |        },
         |        {
         |            "tokenId": "003bd19d0187117f130b62e1bcab0939929ff5c7709f843c5c4dd158949285d0",
         |            "index": 1,
         |            "amount": 15,
         |            "name": "SigRSV",
         |            "decimals": 0,
         |            "type": "EIP-004"
         |        }
         |    ],
         |    "additionalRegisters": {},
         |    "spentTransactionId": "e3023afd988ec2e10a4355edf346f7a6cfbd8d7ed9bd99c51702190b58163caa",
         |    "mainChain": true
         |}
         |""".stripMargin
    ).toOption.get

    val depositSpf: AmmDepositV3 = AmmDepositV3(
      outputSpf,
      SPF(123),
      PoolId.unsafeFromString("0000000000000000000000000000000000000000000000000000000000000000"),
      ErgoTreeRedeemer(
        SErgoTree.unsafeFromString("0000000000000000000000000000000000000000000000000000000000000000")
      ),
      AmmDepositParams(
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
      Version.V3
    )

    val outputSpfIsX = decode[Output](
      s"""
         |{
         |    "boxId": "6d2158c595abda513c2f8cf9fe92ea0542d3b936bbf372f03dcda568f6296c60",
         |    "transactionId": "d098f935fe6c686cffe3c58895e768f31ae35bf439b13d48b457874c8c0e00c4",
         |    "blockId": "f687cea76d2bfac06027c153db294bd2a368594b8180c95c8f008943e5e232c6",
         |    "value": 400000,
         |    "index": 0,
         |    "globalIndex": 28289311,
         |    "creationHeight": 982507,
         |    "settlementHeight": 982509,
         |    "ergoTree": "19e8041a040008cd03b221b94e0cbfacb79502a646c8a7ba08717e913db1583e12ecd12d2cb18b2156040404080402040205feffffffffffffffff0104040580a4a7da06040605c8b79cbc01040004000e2091578c60081a4f6c7d5cb3e5e587b2dc7b51712c0d4f66561eb134934964fa980e240008cd03b221b94e0cbfacb79502a646c8a7ba08717e913db1583e12ecd12d2cb18b21560404040204040402010101000e691005040004000e36100204a00b08cd0279be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798ea02d192a39a8cc7a701730073011001020402d19683030193a38cc7b2a57300000193c2b2a57301007473027303830108cdeeac93b1a5730405000500058092f4010100d801d601b2a4730000eb027301d195ed92b1a4730293b1db630872017303d80cd602db63087201d603b2a5730400d604b27202730500d6057e9973068c72040206d606b27202730700d6077e8c72060206d6089d9c7e73080672057207d609b27202730900d60a7e8c72090206d60b9d9c7e730a067205720ad60cdb63087203d60db2720c730b00edededededed938cb27202730c0001730d93c27203730e92c17203c1a795ed8f7208720b93b1720c730fd801d60eb2720c731000ed938c720e018c720901927e8c720e02069d9c99720b7208720a720595ed917208720b93b1720c7311d801d60eb2720c731200ed938c720e018c720601927e8c720e02069d9c997208720b7207720595937208720b73137314938c720d018c720401927e8c720d0206a17208720b90b0ada5d9010e639593c2720e7315c1720e73167317d9010e599a8c720e018c720e0273187319",
         |    "address": "2QWphCN3bp6iGUoLqHPk4tqSVCyj772KgY9hMpJ4GeSj1mJEoAU8hnr6kgUGkp6d6KsqHLKoFnij5JNLzphD7mH9J6YswAnLGSosuMtZFAszrnn3zhhEyHDerW7YC9yPHg2YUWuzMrP7jeESohq36bQ8zUiiXRVirF4rZsS9hTrVmukSkKK6iXKPJdPt7MKBuoxXVrFBmtMG4AsQT8ihBcsoFQH355R3wKvKvn2UVkrXvzUjbFjVquacKja52oZbeGYUr4jS5V2YerzWhrWVn5CJrzHzgP7kxgxSKfbdiDm5CV61dmiUJgyoncjfbUBKsYZbzKd844NdCYGnZ7tSnouWD2YZosPe8nQx9MDqruh7nWS7srNbRfnZ29T4x2AK5R7ZGMYjP26RjaBSuNqD8zyNLBbLWaz9scWXJtiVzebexSdKF1TtjKeUDoA76id6oQ9eoWq9QeXXYawMz5Z9ggm6jbt4bFjoNMGm9A9StsEeDzfQJqjG9Tc4dX8PV7LSnT1j87oua8e1q6vRKUgTPCvSj4MaLoYnPEvCcJtuJQVWjTmeMJJKVxFf6wjZ4ptLSBV8389f8NTFo5pXaxgzbyP7xuFdzeVVEZe8LC94KK8yvMDu4129jtxhWDro1nsmY24gQr71wy7M5RXphWcW7K63hXX9YvUfhegpbCxLK7PndAt9xA4A28MFWSuRwb2h4d8vXLX8EL1gQMdU5BXyANbxhfXUdHP32KgJv1CrjApGpfD1HfGV3toMuytbG8L6q8uNXnnx4KFFGSRXWkXbTjoJrtyFuULhS47fvxbLnVdcSe4Vuu2z76CYGbBLPHhz9qRghuk5oD22ZHXeUyHQ",
         |    "assets": [
         |        {
         |            "tokenId": "706fb118782c421b0b00e01e5403efb64b68e31e01048dc398f3a143eaa05b26",
         |            "index": 0,
         |            "amount": 900000000,
         |            "name": "tERG",
         |            "decimals": 8,
         |            "type": "EIP-004"
         |        },
         |        {
         |            "tokenId": "50eab1afc495420c17c0df5154584cf09d9167263fffc99c25e3e0ae4b26fe00",
         |            "index": 1,
         |            "amount": 197365220,
         |            "name": "tSPF",
         |            "decimals": 6,
         |            "type": "EIP-004"
         |        },
         |        {
         |            "tokenId": "9a06d9e545a41fd51eeffc5e20d818073bf820c635e2a9d922269913e0de369d",
         |            "index": 2,
         |            "amount": 148613,
         |            "name": "SPF",
         |            "decimals": 6,
         |            "type": "EIP-004"
         |        }
         |    ],
         |    "additionalRegisters": {},
         |    "spentTransactionId": "2f93ffc2c893c24e0d0804f81d6624f08cc678a3cf2708a38a8a16430dd631d3",
         |    "mainChain": true
         |}
         |""".stripMargin
    ).toOption.get

    val depositSpfIsX: AmmDepositV3 = AmmDepositV3(
      outputSpfIsX,
      SPF(123),
      PoolId.unsafeFromString("0000000000000000000000000000000000000000000000000000000000000000"),
      ErgoTreeRedeemer(
        SErgoTree.unsafeFromString("0000000000000000000000000000000000000000000000000000000000000000")
      ),
      AmmDepositParams(
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
