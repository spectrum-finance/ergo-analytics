package fi.spectrum.parser.amm.order.v1

import fi.spectrum.core.domain.analytics.Version
import fi.spectrum.core.domain.analytics.Version.LegacyV2
import fi.spectrum.core.domain.order.Fee.ERG
import fi.spectrum.core.domain.order.Order.Deposit.AmmDeposit._
import fi.spectrum.core.domain.order.Order.Redeem.AmmRedeem.RedeemV1
import fi.spectrum.core.domain.order.Order.Swap.{SwapLegacyV2, SwapV1}
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
        |    "boxId": "a20edaf87f627de0220b2446a02d96bc0771e1255dea895e717e3ac455b26ce5",
        |    "transactionId": "a6f75551aaf0ef0b8b09f67e90fb62de67fa23dce85d09e1dcb304219ce40b65",
        |    "blockId": "9a1fd2557576394940f0480e254aa0a662bd3bbcd32c639b136597c5aae7a2fc",
        |    "value": 7260000,
        |    "index": 0,
        |    "globalIndex": 28280656,
        |    "creationHeight": 982356,
        |    "settlementHeight": 982359,
        |    "ergoTree": "19b5031508cd03e605bd2d03dc22205f706a24abc0cec5a1c5b43019bcc90758a846f2dfb30f160400040404060402058080d0d88bdea2e30205cefbc0eab3f18ae302040004000e209916d75132593c8b07fe18bd8d583bda1652eed7565cf41a4738ddd90fc992ec05faf4ce9e5504c60f060101040404d00f04c60f0e691005040004000e36100204a00b08cd0279be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798ea02d192a39a8cc7a701730073011001020402d19683030193a38cc7b2a57300000193c2b2a57301007473027303830108cdeeac93b1a5730405000500058092f4010100d802d6017300d602b2a4730100eb027201d195ed92b1a4730293b1db630872027303d804d603db63087202d604b2a5730400d6059d9c7e99c17204c1a7067e7305067e730606d6068cb2db6308a773070002edededed938cb2720373080001730993c27204d072019272057e730a06909c9c7ec17202067e7206067e730b069c9a7205730c9a9c7e8cb27203730d0002067e730e067e9c72067e730f050690b0ada5d90107639593c272077310c1720773117312d90107599a8c7207018c72070273137314",
        |    "address": "Dxg1z3EWagGuWKTyZjZ3bZJmcFTndPCq5dNr3QGdgKFL9kUs5wuSoZZUXimzKkjqhgZrEYrScVPsGxa1zsp2c6DgQqWSuzP4CBrK3ALs9kL9ANYVE34PLREmk6LEHBq45g9mnwV9rv7poBv83disdv5aApBfkC1pZFE9d8uE6mngfK7nHqYtukHDpefFEiNUQzyfUp6AEaoH4MMbzFo136K6R7uSmcfMe7i6RrFStxDbHHdLowmCSCUBn2THV7PLzT2dZXpCZf2uUQdqLrabZeYW8ZSP6EaxEWFbeoiBJ3QooEfB2QUTWVrfnumoVPkaMzJsYPe7n6vg3b4FjSVAorCPEha6UyBLFv1zruRvRGmHTe9ski1ZtwduLwaz76bEVqhdoq8U6RGEZMfTSETFTvLaPmRPcvWzQZMvYtMhBDRycQpC6yKLCgMaAz2NWSpX3Jpbcx2dMNwFwBLsuHBRGtG2qS7ZwG4jLej5LYQ4wpAjf5qDRgukdK1suQcwVhR9iNvzd1u5dGcNtNGX9pc64HXJB7PmgjV8LSwdfsRKuwsKAnZ6hciLEfiyqCvjv5Su5J2VNVSSkmDAWTsMYJ7FARoyFjTvsFw",
        |    "assets": [
        |        {
        |            "tokenId": "03faf2cb329f2e90d6d23b58d91bbb6c046aa143261cc21f52fbe2824bfcbf04",
        |            "index": 0,
        |            "amount": 2092,
        |            "name": "SigUSD",
        |            "decimals": 2,
        |            "type": "EIP-004"
        |        }
        |    ],
        |    "additionalRegisters": {},
        |    "spentTransactionId": "4bf84ec3431c760bc4a455fbf2e9d4d109182eea52db343c562ae55f2868e844",
        |    "mainChain": true
        |}
        |""".stripMargin
    ).toOption.get

    val order = SwapV1(
      output,
      PoolId.unsafeFromString("9916d75132593c8b07fe18bd8d583bda1652eed7565cf41a4738ddd90fc992ec"),
      PublicKeyRedeemer(PubKey.unsafeFromString("03e605bd2d03dc22205f706a24abc0cec5a1c5b43019bcc90758a846f2dfb30f16")),
      SwapParams(
        AssetAmount(
          TokenId.unsafeFromString("03faf2cb329f2e90d6d23b58d91bbb6c046aa143261cc21f52fbe2824bfcbf04"),
          2092
        ),
        AssetAmount(
          TokenId.unsafeFromString("0000000000000000000000000000000000000000000000000000000000000000"),
          11440610621L
        ),
        52444753158425L,
        100000000000000000L
      ),
      2000000,
      Version.V1
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

    val expectedT2TDepositV1: AmmDepositV1 = AmmDepositV1(
      depositT2T,
      ERG(7000000),
      PoolId.unsafeFromString("f1fb942ebd039dc782fd9109acdb60aabea4dc7e75e9c813b6528c62692fc781"),
      PublicKeyRedeemer(PubKey.unsafeFromString("03c900eda9e1c2a199ec844c0515279fae0c764ba9dd174b4d9e43ec74c6bfff2e")),
      AmmDepositParams(
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
      Version.V1
    )
  }

  object redeem {

    val output: Output = decode[Output](
      """
        |{
        |    "boxId": "d2b378f2f4084a086601f55819146e60cb8fa915000258895c8a3b3dbf523eca",
        |    "transactionId": "5beff9513b033d893688c0614aa2287a2e145a018f196490d1f535991c833cac",
        |    "blockId": "7635bf75e024ae183bcdfa555622a890ebfb79bf733bfd22dd1c4736165bb837",
        |    "value": 6060000,
        |    "index": 0,
        |    "globalIndex": 28206253,
        |    "creationHeight": 980535,
        |    "settlementHeight": 980537,
        |    "ergoTree": "19dd031508cd032ab594a44a0e8eb9e060f30b12a4513456f10d7c5bdd4f711a49756f44ad925004000404040804020400040404020406040005feffffffffffffffff01040204000e20facd58505693bccbf7281492fba58f38594d9b4924f253789bf99c49e5d34bd305020580b6dc050e691005040004000e36100204a00b08cd0279be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798ea02d192a39a8cc7a701730073011001020402d19683030193a38cc7b2a57300000193c2b2a57301007473027303830108cdeeac93b1a5730405000500058092f4010100d802d6017300d602b2a4730100eb027201d195ed92b1a4730293b1db630872027303d809d603db63087202d604b2a5730400d605db63087204d606b27205730500d607b27203730600d608b27205730700d609b27203730800d60a7e8cb2db6308a77309000206d60b7e99730a8cb27203730b000206ededededededed93b27203730c008602730d730e93c27204d0720192c1720499c1a7730f938c7206018c720701938c7208018c720901927e8c720602069d9c720a7e8c72070206720b927e8c720802069d9c720a7e8c72090206720b90b0ada5d9010c639593c2720c7310c1720c73117312d9010c599a8c720c018c720c0273137314",
        |    "address": "3qtA2thJnsq6LnyUrG82Wbgv5ZrBpRAghinfyLAhwjgkyU8xKr6mSoFKUc8ErrqM3ByEnhgbLFC7K1b7qEunQTR9WiZXvEoSTxTedQg7ds3qG3RdCHmWd36ixuiLQ27kEN2LXad5TsB8YpcpEoSvijC7hCAaZdTCWj5F3q8G7ZBgSWvYkmL2NQDGMguPACiKqLTnXnTYsp1xDfZEqnRACfzGUyUKzf6NygARUucjZpow8VnZv8bgubFDqCz4nTCGGBhjRHEkerzD7LBJbLU9sKgVHyLGhtcDD66b2Rr2fDjTodgzHVrbjmAMj6abAPQwPU43H1hQedL99yyV9bDz7NZGjwvBvnJcBXz4RpiAAJFYwzkRJ4ffF3aSdkWiPfhoxeC5Z9awjrLD49535jnRsWS27Eu8Go45XgSiGysAFdpgpZPCMix5wFtwYg6kphRkKPg3JNVmfT1xcMYnxu7imhRp1iNxhJ6VsevW7EtbNSEDoDS8yQYTZbiq2Nys2sLMZmd2S3hi2YyATsKt5KsexPx5UYBcJZZTcLFQDaea1fjwiXs53uGv5H6w4KtaBqdQzR8sMitgYQSe473h6QbC8XZFMr8kT2Ga9yZae9mXdx4RSPE5W2QC6sSKNHTPnAnoFuaFz5nFHGQ5qMNkCWk2UA",
        |    "assets": [
        |        {
        |            "tokenId": "8e93957e2d2db47a7c8c2550a4d17e7b4f8b39e51ceb03ba20efed62d28ecf47",
        |            "index": 0,
        |            "amount": 693650,
        |            "name": "706fb118_c07bde48_LP",
        |            "decimals": 0,
        |            "type": "EIP-004"
        |        }
        |    ],
        |    "additionalRegisters": {},
        |    "spentTransactionId": "33f87e0d8ab3c64441c127ca93dea47035f2e973b919469424aa287ded6e7323",
        |    "mainChain": true
        |}
        |""".stripMargin
    ).toOption.get

    val order = RedeemV1(
      output,
      ERG(6000000),
      PoolId.unsafeFromString("facd58505693bccbf7281492fba58f38594d9b4924f253789bf99c49e5d34bd3"),
      PublicKeyRedeemer(
        PubKey.unsafeFromString("032ab594a44a0e8eb9e060f30b12a4513456f10d7c5bdd4f711a49756f44ad9250")
      ),
      RedeemParams(
        AssetAmount(
          TokenId.unsafeFromString("8e93957e2d2db47a7c8c2550a4d17e7b4f8b39e51ceb03ba20efed62d28ecf47"),
          693650
        )
      ),
      2000000,
      Version.V1
    )
  }

}
