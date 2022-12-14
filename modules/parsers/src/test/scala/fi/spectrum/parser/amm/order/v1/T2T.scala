package fi.spectrum.parser.amm.order.v1

import fi.spectrum.core.domain.analytics.Version
import fi.spectrum.core.domain.order.Fee.ERG
import fi.spectrum.core.domain.order.Order.Deposit.DepositV1
import fi.spectrum.core.domain.order.Order.Redeem.RedeemV1
import fi.spectrum.core.domain.order.Order.Swap.SwapV1
import fi.spectrum.core.domain.order.Redeemer.PublicKeyRedeemer
import fi.spectrum.core.domain.order._
import fi.spectrum.core.domain.transaction.Output
import fi.spectrum.core.domain.{AssetAmount, PubKey, TokenId}
import io.circe.parser.decode

object T2T {

  object swap {

    val output: Output = decode[Output](
      """
        |{
        |    "boxId": "e05930095a04bbc3fd12c6815196fab1a7a162e6ee5c5a7bf326710d9c05b4e5",
        |    "transactionId": "a89e0135e55d5481664bfb0f52f4e585f0d4a77a7a177e11af19c573fc8496a4",
        |    "blockId": "ff0c25367fd954e2b15fafff893915bfb1ce99d334ad2ceffe75bddf57700b8b",
        |    "value": 7260000,
        |    "index": 0,
        |    "globalIndex": 19585963,
        |    "creationHeight": 803634,
        |    "settlementHeight": 803637,
        |    "ergoTree": "19b8041708cd0318970b0466076d3e610a70c6f232879c96156140587e448a88be5c1f5ae520b804000e20d71693c49a84fbbecd4908c94813b46514b18b67a99952dc1e6e4791556de41304c80f04d00f040404080402040004040400040606010104000e20f9b9fdc5ed4cfed538e25146a929201d2622f1e63d7f8fd653f36e43267fe2a305bcf40105cac48ffab4f1cf0d05808095e789c6040e691005040004000e36100204a00b08cd0279be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798ea02d192a39a8cc7a701730073011001020402d19683030193a38cc7b2a57300000193c2b2a57301007473027303830108cdeeac93b1a5730405000500058092f4010100d805d6017300d602b2a4730100d6037302d6047303d6057304eb027201d195ed93b1a4730593b1db630872027306d80ad606db63087202d607b2a5730700d608b2db63087207730800d6098c720802d60a7e720906d60bb27206730900d60c7e8c720b0206d60d7e8cb2db6308a7730a000206d60e7e8cb27206730b000206d60f9a720a730cedededededed938cb27206730d0001730e93c27207d07201938c7208017203927209730f927ec1720706997ec1a7069d9c720a7e7310067e73110695938c720b017203909c9c720c720d7e7204069c720f9a9c720e7e7205069c720d7e720406909c9c720e720d7e7204069c720f9a9c720c7e7205069c720d7e72040690b0ada5d90110639593c272107312c1721073137314d90110599a8c7210018c72100273157316",
        |    "address": "9gLcPJmGH41CZ3aLCpHnEE6SuHxaPagNAPav6EDUVswTXRwF7m7RZnyCR42Mwv7FHNMevdvDBmr3oD1jrQwS9MyAMqjNsfwoqh3xuh6WDnJuEvmwycDerhrNjKrRJTtuMdWMA6XF8MsE6L6Kywc9dz68VqFSwvfFiG2W5hMczboBsyxQzdtuC2tcVWWyNfUSPBEGkbqD69Jfj8PpJ5aCwGDftAJgkpAhQ69VzWHVFF8Tj5PN5WoQ7EbLRhLBzYPkYX5UPM6CtNGGpPLChJLcBK9HPnNTQEsoW1oVrGjkYLveWjeVtmJrCBgSdRXfvGc9dWipkXgbZ2oTaFSvn7ZHbZ7v6DzfLjn6ETLYeD6n1LEesU8K9Ehj1ue3y5SvJA2J7dzgJDSApmv7mHZN4U9WwySBPVKLdp2oRzYPNzsENfYm3QCdYG9F1QFdbdQB9QvBCGfLkgmoatboCZVrdMeSDV4utF46FEhXRzR3APE31qaACyEw2MC6aYKyhutCp814WpgeQNkeZwGFkeGL84TxNEVSLvUERSLmaFwR1r1F7hUtX53eG5WffkAmEFuq9BjACeJMafyEwiRtABaZapucqY4PAQ45Bo6EU9xUyJj61mwGygbRguHL3y1F81Wsff3F7r5C7e6C5FrYtRRyZ9Ms1i2THuq3otJwbdX6AqVGgXYr1KutZoGKTzmLz5fUyJrpSkeELFfiWsk9eCRiWZKM1x45DaZz2HKVdFvrwR46DrDV6fSuF9DhcTqoGNeegjyKTiHPvaeXVrbXgAaAUg",
        |    "assets": [
        |        {
        |            "tokenId": "03faf2cb329f2e90d6d23b58d91bbb6c046aa143261cc21f52fbe2824bfcbf04",
        |            "index": 0,
        |            "amount": 1004,
        |            "name": "SigUSD",
        |            "decimals": 2,
        |            "type": "EIP-004"
        |        }
        |    ],
        |    "additionalRegisters": {},
        |    "spentTransactionId": "2d4d388cdd5701de08fd4fc51218999a684a13adf25585b1a30439113e270b06",
        |    "mainChain": true
        |}
        |""".stripMargin
    ).toOption.get

    val order = SwapV1(
      output,
      PoolId.unsafeFromString("f9b9fdc5ed4cfed538e25146a929201d2622f1e63d7f8fd653f36e43267fe2a3"),
      PublicKeyRedeemer(PubKey.unsafeFromString("0318970b0466076d3e610a70c6f232879c96156140587e448a88be5c1f5ae520b8")),
      SwapParams(
        AssetAmount(
          TokenId.unsafeFromString("03faf2cb329f2e90d6d23b58d91bbb6c046aa143261cc21f52fbe2824bfcbf04"),
          1004
        ),
        AssetAmount(
          TokenId.unsafeFromString("d71693c49a84fbbecd4908c94813b46514b18b67a99952dc1e6e4791556de413"),
          15646
        ),
        3834845967020325L,
        10000000000000L
      ),
      2000000,
      Version.V1,
      OrderType.AMM,
      Operation.Swap
    )
  }

  object deposit {

    val depositT2T: Output = decode[Output](
      """
        |{
        |    "boxId": "3b8f662b0286d7e5442585c9fd0f2fba35f7cccf2ed02b0c6a95fbc327318b6c",
        |    "transactionId": "46bf4f19d7a537274b1124ff242cea0351f30a38f2ba87b9454c5a9c49d3ed8b",
        |    "blockId": "186076620f5f7c69be24adc4a82671b71513d4ae550b62ca6de64628dc9d3ada",
        |    "value": 7060000,
        |    "index": 0,
        |    "globalIndex": 10156876,
        |    "creationHeight": 508928,
        |    "settlementHeight": 629205,
        |    "ergoTree": "19dd041b08cd03c900eda9e1c2a199ec844c0515279fae0c764ba9dd174b4d9e43ec74c6bfff2e0400040404080402040205feffffffffffffffff010404040004060402040004000e20f1fb942ebd039dc782fd9109acdb60aabea4dc7e75e9c813b6528c62692fc78105020580bfd6060404040204040402010101000e691005040004000e36100204a00b08cd0279be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798ea02d192a39a8cc7a701730073011001020402d19683030193a38cc7b2a57300000193c2b2a57301007473027303830108cdeeac93b1a5730405000500058092f4010100d803d6017300d602b2a4730100d603db6308a7eb027201d195ed93b1a4730293b1db630872027303d80cd604db63087202d605b2a5730400d606b27204730500d6077e9973068c72060206d608b27204730700d6097e8c72080206d60a9d9c7e8cb27203730800020672077209d60bb27204730900d60c7e8c720b0206d60d9d9c7e8cb27203730a0002067207720cd60edb63087205d60fb2720e730b00edededededed93b27204730c008602730d730e93c27205d0720192c1720599c1a7730f95ed8f720a720d93b1720e7310d801d610b2720e731100ed938c7210018c720b01927e8c721002069d9c99720d720a720c720795ed91720a720d93b1720e7312d801d610b2720e731300ed938c7210018c720801927e8c721002069d9c99720a720d720972079593720a720d73147315938c720f018c720601927e8c720f0206a1720a720d90b0ada5d90110639593c272107316c1721073177318d90110599a8c7210018c7210027319731a",
        |    "address": "2HUM2ZSbd7qnLqgcaNjh6de8XjR78GXzDS5m53Dx6QuUc5ksvR4DTZt1bzecZnN6yzkMHw4vuF8fGYjQrgZr3qTQ6tJyVByqxVo1m42bDW4UV9gc96AjWKhnnuLQe62Y7SGgK3jRRVaRdoLTSv4gkjYTG4zHBfRUmcxj31nj55t5pf4ZCQ2GztUvnKjkc1Dg1Lf6v4bYnXvuBRx39eTfK1CvgZQWYSqGYDGGWjxUYc9DF9Bj64881cwBgQT52QfgggSBJ2xAwVfvopAQ5DHRh2dVU9BnUEH9xH7TfM1VkVLor4ksdhJ5BrHwwedPtJv5yyo2KuyNJejWi59JY3AQn79u5GpLw6M9r8Ffney2rusB9ER4H8VtnUU1JGxEktQeSUYtz2oxnMjK5xGenrBSC9ze7Z8k8uMqRgXGDABcHPQepwLRhrbLLsyhSbzF4RfFgVAPM5xgKwDMPNhj8rkPcbqgz7zmzG2E2v6EfJ5PQ88FST5jyKzjyygHA6cXhx7CquyVMQf6jNfVAcedtSco1cHPvhe16V9PPtvq729tCKNeMnxPPhKT97PeVLpe2T9eYcrqFFCySRXX5RKimp7wwNyVavvU2tRxLjDbYZiPFZHyv7mcRzXQij1JFKLWxCY4fbzfojNxn8wTL5WKAQNvyTfGHzdSUmxo4GFBctbjWEsbouvBeDUJf49HQFT1Crap5CrAoC1Akdvrjw1LjPR5KVfMjQjUF15g2QNr88NBwRo8uP6CU6LwnfSzKhapKcPcwkbwffU763L35g7Ud4EzVYUWcKUS6E6JPRcvWQrSFfT8u9SACBoca2cSfy2J65JADZXrM",
        |    "assets": [
        |        {
        |            "tokenId": "ef802b475c06189fdbf844153cdc1d449a5ba87cce13d11bb47b5a539f27f12b",
        |            "index": 0,
        |            "amount": 200000000000,
        |            "name": "WT_ERG",
        |            "decimals": 9,
        |            "type": "EIP-004"
        |        },
        |        {
        |            "tokenId": "30974274078845f263b4f21787e33cc99e9ec19a17ad85a5bc6da2cca91c5a2e",
        |            "index": 1,
        |            "amount": 96138352753,
        |            "name": "WT_ADA",
        |            "decimals": 8,
        |            "type": "EIP-004"
        |        }
        |    ],
        |    "additionalRegisters": {},
        |    "spentTransactionId": "106426da7ad745ae5c25322f79794cb480e15f76e8e4abc423d443bb997b9740",
        |    "mainChain": true
        |}
        |""".stripMargin
    ).toOption.get

    val expectedT2TDepositV1: DepositV1 = DepositV1(
      depositT2T,
      ERG(7000000),
      PoolId.unsafeFromString("f1fb942ebd039dc782fd9109acdb60aabea4dc7e75e9c813b6528c62692fc781"),
      PublicKeyRedeemer(PubKey.unsafeFromString("03c900eda9e1c2a199ec844c0515279fae0c764ba9dd174b4d9e43ec74c6bfff2e")),
      DepositParams(
        AssetAmount(
          TokenId.unsafeFromString("ef802b475c06189fdbf844153cdc1d449a5ba87cce13d11bb47b5a539f27f12b"),
          200000000000L
        ),
        AssetAmount(
          TokenId.unsafeFromString("30974274078845f263b4f21787e33cc99e9ec19a17ad85a5bc6da2cca91c5a2e"),
          96138352753L
        )
      ),
      2000000,
      Version.V1,
      OrderType.AMM,
      Operation.Deposit
    )
  }

  object redeem {

    val output: Output = decode[Output](
      """
        |{
        |    "boxId": "18bd4c926a623cd4036e38a99875c631415f2251e1d9e2ed31b5207277033382",
        |    "transactionId": "271dedaf5ebcf6bd446878ffb24eaf451e592d2167abcaaccd2dcb9cc07dfdde",
        |    "blockId": "e1d632125f9b3364ce2b0c124501f333e2c37972087a2a75772cceff55b62857",
        |    "value": 7060000,
        |    "index": 0,
        |    "globalIndex": 10698236,
        |    "creationHeight": 506880,
        |    "settlementHeight": 638167,
        |    "ergoTree": "19dd031508cd02b98e70d0b730df7898a2f839c1dfa2b9dcc53eb4f2b77881256f075f103d487d04000404040804020400040404020406040005feffffffffffffffff01040204000e2065fa572bc4a7007e5a6450c9af2bfa1594e6dfb43b667027f1930eefddeac7bf05020580bfd6060e691005040004000e36100204a00b08cd0279be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798ea02d192a39a8cc7a701730073011001020402d19683030193a38cc7b2a57300000193c2b2a57301007473027303830108cdeeac93b1a5730405000500058092f4010100d802d6017300d602b2a4730100eb027201d195ed93b1a4730293b1db630872027303d809d603db63087202d604b2a5730400d605db63087204d606b27205730500d607b27203730600d608b27205730700d609b27203730800d60a7e8cb2db6308a77309000206d60b7e99730a8cb27203730b000206ededededededed93b27203730c008602730d730e93c27204d0720192c1720499c1a7730f938c7206018c720701938c7208018c720901927e8c720602069d9c720a7e8c72070206720b927e8c720802069d9c720a7e8c72090206720b90b0ada5d9010c639593c2720c7310c1720c73117312d9010c599a8c720c018c720c0273137314",
        |    "address": "3qtA2thJnsnfqUUNCUamvSkDAjTH2PakDj4XBbQk6o51B211KBWMUs5rpVQrgYi3gX1qkP4GEFpAiDCinaB7PmJTN4gZNJuuaAfHGuMn8CURELns1jj2bArWhGc3kGAjaUwaAe1APp7iBbrMGCXXSuBbJ7ZS3znai4G8FdHb1sg6kqMZxWRNuebypEvsvL7oHoEQJf9SQA7VZK9RuejpPMQdDTrF4pBsZj5dxfcm3XonRtdc8YLxnQmgeJWMTWdLVRizipdebyrux3NH6EpyQommHxLqbJoC8gatDLto5USihWYrGgd68PLYcnj433qwTnnZSEZkjKs7X4fKzRQwiLYdkZ5Yk2Bu7vZktSuzoKx8msDLWPhraiWw1MCyTgcgVonBDXXpXEDHBafmX8YKNp7UP3v7XyEsWbB5NLkZjAaTqhDvLkLTcb3ZN8DhA6uCVMFBGgDpspX1eGM8sWKyYEyaq2G7JqD8r23XnxRQdSWR23d3eACDWWoYBrCiKhfUVsuZ4VP5y3x3xBMpfzR9ZpEFdbXJxUgcZsKZgEA91eFpPugCgNZ7s5j4ibvpcdVhveKTGkcA1v8YUd7MdWyYWMvoJ8mbCgRAFSLwRoAZBEqAXbGR7jdBQUBDveie9KMrE3JQjMqW3rkgncR4EUHGcN",
        |    "assets": [
        |        {
        |            "tokenId": "e0588d273c8183865cff31b3bfa766bc7b178e2362b45497b67e79662e3615b7",
        |            "index": 0,
        |            "amount": 700,
        |            "name": null,
        |            "decimals": null,
        |            "type": null
        |        }
        |    ],
        |    "additionalRegisters": {},
        |    "spentTransactionId": "bda9c192413a3c589f8bec2dcf392d73508692baf48732d925d38815ba857e71",
        |    "mainChain": true
        |}
        |""".stripMargin
    ).toOption.get

    val order = RedeemV1(
      output,
      ERG(7000000),
      PoolId.unsafeFromString("65fa572bc4a7007e5a6450c9af2bfa1594e6dfb43b667027f1930eefddeac7bf"),
      PublicKeyRedeemer(
        PubKey.unsafeFromString("02b98e70d0b730df7898a2f839c1dfa2b9dcc53eb4f2b77881256f075f103d487d")
      ),
      RedeemParams(
        AssetAmount(
          TokenId.unsafeFromString("e0588d273c8183865cff31b3bfa766bc7b178e2362b45497b67e79662e3615b7"),
          700
        )
      ),
      2000000,
      Version.V1,
      OrderType.AMM,
      Operation.Redeem
    )
  }

}
