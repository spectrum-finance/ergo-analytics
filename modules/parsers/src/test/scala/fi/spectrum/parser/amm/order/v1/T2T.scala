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
        |    "boxId": "4dd85a4d3461ac2a66bc9f6d9edbe45a2bafba3337925683abc0ec2775ade7e7",
        |    "transactionId": "b5acea53c9f9e505831cc24bd62a8925e07bf7c1e96ef09acb5e3d07f01fe3c7",
        |    "blockId": "5c6d249f16a528db514da331eabb3151240a24fd103dc69e4948594503c19283",
        |    "value": 7260000,
        |    "index": 0,
        |    "globalIndex": 28618891,
        |    "creationHeight": 991447,
        |    "settlementHeight": 991449,
        |    "ergoTree": "19b6041708cd038facdb201507dd1ae78b7979dcf822deed0e3e2e0bd1e3dd4e0f7d9ed165cd5904000e200cd8c9f416e5b1ca9f986a7f10a84191dfb85941619e49e53c0dc30ebf83324b04c60f04d00f040404080402040004040400040606010104000e200e7ae6bcddac78ec42e218775e3dc2e87226cb09692d23242b4e13e435a4b58805e40705e2c0f2a48ff1e62a0580c0a8ca9a3a0e691005040004000e36100204a00b08cd0279be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798ea02d192a39a8cc7a701730073011001020402d19683030193a38cc7b2a57300000193c2b2a57301007473027303830108cdeeac93b1a5730405000500058092f4010100d805d6017300d602b2a4730100d6037302d6047303d6057304eb027201d195ed92b1a4730593b1db630872027306d80ad606db63087202d607b2a5730700d608b2db63087207730800d6098c720802d60a7e720906d60bb27206730900d60c7e8c720b0206d60d7e8cb2db6308a7730a000206d60e7e8cb27206730b000206d60f9a720a730cedededededed938cb27206730d0001730e93c27207d07201938c7208017203927209730f927ec1720706997ec1a7069d9c720a7e7310067e73110695938c720b017203909c9c720c720d7e7204069c720f9a9c720e7e7205069c720d7e720406909c9c720e720d7e7204069c720f9a9c720c7e7205069c720d7e72040690b0ada5d90110639593c272107312c1721073137314d90110599a8c7210018c72100273157316",
        |    "address": "SqWZwLTsYLTTkP5GcZZhPSadEGudcXMrU7idDmbzSo9c8jrtjEfXBEbs3FQHRFKnUzZopxrUbmGpib9CSLKr6fxzrB686x1Tpt5wQNoJxCB1whdS6tFed1Frb8A61kVx7wVDctYN3cgnFRGB6hdwtQuGHTyt2V2CCyjxXW7XR77VHSSAyeWn43wefDAc3TooEwjE97UyMEqygkR396VYAKmgRQtkfetMDBc8wZUr2sTiKQx5qLDvUjodZaYkKLY3JQs3xjak1rLpSinYLqtprwnyMHZgZtNejgSV3tyD9D21ixing63WN6QQC7FTwK8ivnuJHu1mafzVLtPtkhJwgA93TFNqYLwwtwNzUh6H22NhTYdQ7x35nqFi2fKgzvMbpCWMDMksszWRsUr2nM1HLmj1gi4rym4gYaqeHUhDmdyLgGzniS4i9TRrG8qYp1be6h8m5ZepFfVQkdJBHFTdNDCkgKUNpuEaKqDjv6J8TfH88KfhPsqUsoof1HwWx9qzSWcFomRRdoNbdFZSwDA4Q8iu7bwX4cPidYwHGd2iBaHMmxdk7YabEjtatp4GhmTQyc2qkYd68Kwhis7cVGxVVMemseP75Uwfh5KkKwGU5dRGTRV3uSSZTp5F2syiV8EfqDHtASZJC2FC8h8t7FWrZ3j6FULVZtHTC22S2h7tiEyymM3pin8Ah8kzjhRRJjeNG6GLZQuvpqjqjzLLBT5PCbNEyAF4phnZNo9LAtxd8McuHD7PKUzbCn88F6Ezc7DmejEDfzemx4uJSf4",
        |    "assets": [
        |        {
        |            "tokenId": "0779ec04f2fae64e87418a1ad917639d4668f78484f45df962b0dec14a2591d2",
        |            "index": 0,
        |            "amount": 1000,
        |            "name": "Mi Goreng ",
        |            "decimals": 0,
        |            "type": "EIP-004"
        |        }
        |    ],
        |    "additionalRegisters": {},
        |    "spentTransactionId": "d1dfe839c7fbd7b0368ee8b1559063c8b762018923b5ad86596aa665a670cdd7",
        |    "mainChain": true
        |}
        |""".stripMargin
    ).toOption.get

    val order = SwapV1(
      output,
      PoolId.unsafeFromString("0e7ae6bcddac78ec42e218775e3dc2e87226cb09692d23242b4e13e435a4b588"),
      PublicKeyRedeemer(PubKey.unsafeFromString("038facdb201507dd1ae78b7979dcf822deed0e3e2e0bd1e3dd4e0f7d9ed165cd59")),
      SwapParams(
        AssetAmount(
          TokenId.unsafeFromString("0779ec04f2fae64e87418a1ad917639d4668f78484f45df962b0dec14a2591d2"),
          1000
        ),
        AssetAmount(
          TokenId.unsafeFromString("0cd8c9f416e5b1ca9f986a7f10a84191dfb85941619e49e53c0dc30ebf83324b"),
          498
        ),
        12048192771084337L,
        1000000000000L
      ),
      2000000,
      Version.V1
    )
  }

  object deposit {

    val depositT2T: Output = decode[Output](
      """
        |{
        |    "boxId": "2a0a6d752699c41288ec695c7580a61159c9a014def9ff9850e5b4a4ff5deefe",
        |    "transactionId": "c5675624f3d4c69ef0281a7e3d172a32d31e12587304ffc36742212737abecce",
        |    "blockId": "90aacbc5820c0d044ffe9f34a412b6f00cd1d68cb1d7fa3b03499b965bafe86c",
        |    "value": 6060000,
        |    "index": 0,
        |    "globalIndex": 28614154,
        |    "creationHeight": 991312,
        |    "settlementHeight": 991315,
        |    "ergoTree": "19dd041b08cd03abaf717045fcf5ad0a4058b9895e6d5343bf61a12884618a278b0b81ea6309710400040404080402040205feffffffffffffffff010404040004060402040004000e20facd58505693bccbf7281492fba58f38594d9b4924f253789bf99c49e5d34bd305020580b6dc050404040204040402010101000e691005040004000e36100204a00b08cd0279be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798ea02d192a39a8cc7a701730073011001020402d19683030193a38cc7b2a57300000193c2b2a57301007473027303830108cdeeac93b1a5730405000500058092f4010100d803d6017300d602b2a4730100d603db6308a7eb027201d195ed92b1a4730293b1db630872027303d80cd604db63087202d605b2a5730400d606b27204730500d6077e9973068c72060206d608b27204730700d6097e8c72080206d60a9d9c7e8cb27203730800020672077209d60bb27204730900d60c7e8c720b0206d60d9d9c7e8cb27203730a0002067207720cd60edb63087205d60fb2720e730b00edededededed93b27204730c008602730d730e93c27205d0720192c1720599c1a7730f95ed8f720a720d93b1720e7310d801d610b2720e731100ed938c7210018c720b01927e8c721002069d9c99720d720a720c720795ed91720a720d93b1720e7312d801d610b2720e731300ed938c7210018c720801927e8c721002069d9c99720a720d720972079593720a720d73147315938c720f018c720601927e8c720f0206a1720a720d90b0ada5d90110639593c272107316c1721073177318d90110599a8c7210018c7210027319731a",
        |    "address": "2HUM2ZSbd7qVu9APa19mNfdoddaushbEMcUqdASCkN52MpHaeagGVJVWYUBtsSSgSStnTscKwUaKKcD6FEEbJSkVcCrZ5NJ6mXJeEtyNjuJd8ZpvMhyqtgeuwGnCEMPGvmdKs4wbQ9gDXjRGNkdDSDxv8Nw6UEvnZNxEzF5xri1BAaVvaxEeyy23xwAb3Ua9tNRbXQ64zAJ6dhp2up3Y8z3gX5vx7etRY1L3ymVPaaPahzry9LzPmhybK5MQMNJy9KAUWTHL4VNNvRyLkVhke5hVPKsTqH1JumFnB8bz6wEQdBxCsyjvEkk2dC1ReGHLYqjbGSqTuaFEVSD2Fjuz2bXBERupCtnqxQ5kqNbam9deChWZWoQSRnrog9E2L5VfX9XhdKEPqjFs8uz8mg9cpgehEWVvdWmVzmyqLLJ16MmbymZ9aasna9k7HVY5whU8nwAHZX94gkgJuXTfpghpw3veGoGyFZnbCBHStq6NPmoC19cprD6W7pZg4mix1QcAdYXvofG8Zkuj7VgUo17b9CyhvHC1f25HVsF6gzUHmJ6L2BcEjEdm8WEkAdnjgGXRgb2XpJGZ7wKAUvF99PQwGf3CBLpv91sUDSdTh64MtGwvUUpw7NBLPzmoQ863YmUkVgqZP8CNsY4FeCUWNDkidjdHefK6T3cLBvczX4SrWkQ1xSio8NDL4cnoMakZQZhfFGXq9hSDuFPDPe7QEJGpTyBm1QmyvEkwqEe1go6xWjZygi4TGJz5zhbbJoFeVuGJGTvjxnefweBem2BwuZHh3QkjU91FGE8vPDnLpsfpsrJyfcbLbkNEfNyLUyroDYQhLJ6UC",
        |    "assets": [
        |        {
        |            "tokenId": "706fb118782c421b0b00e01e5403efb64b68e31e01048dc398f3a143eaa05b26",
        |            "index": 0,
        |            "amount": 1394638966,
        |            "name": "tERG",
        |            "decimals": 8,
        |            "type": "EIP-004"
        |        },
        |        {
        |            "tokenId": "c07bde48465708577d0e0b401300245987d297de2a6004116c825ba09dd26b7d",
        |            "index": 1,
        |            "amount": 8500,
        |            "name": "tSigRSV",
        |            "decimals": 0,
        |            "type": "EIP-004"
        |        }
        |    ],
        |    "additionalRegisters": {},
        |    "spentTransactionId": "ebf52980545672a961286b115c1767b49b52b4cff0ed32db6e5cd8e57041a355",
        |    "mainChain": true
        |}
        |""".stripMargin
    ).toOption.get

    val expectedT2TDepositV1: AmmDepositV1 = AmmDepositV1(
      depositT2T,
      ERG(6000000),
      PoolId.unsafeFromString("facd58505693bccbf7281492fba58f38594d9b4924f253789bf99c49e5d34bd3"),
      PublicKeyRedeemer(PubKey.unsafeFromString("03abaf717045fcf5ad0a4058b9895e6d5343bf61a12884618a278b0b81ea630971")),
      AmmDepositParams(
        AssetAmount(
          TokenId.unsafeFromString("706fb118782c421b0b00e01e5403efb64b68e31e01048dc398f3a143eaa05b26"),
          1394638966
        ),
        AssetAmount(
          TokenId.unsafeFromString("c07bde48465708577d0e0b401300245987d297de2a6004116c825ba09dd26b7d"),
          8500
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
