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
         |    "boxId": "edb99cbfb9df93a78530bf595e5e4ab858e9417afa6b7dead2110fd8ad90ee9c",
         |    "transactionId": "61fd94e2edbb65366c1d881b22b823256cb283eb448a4a52e5fa439c7b980a87",
         |    "blockId": "8ed658e4527362f69120f7b6428ec9c93f837cc796518bdb4b8ee334281f8bd8",
         |    "value": 1007260000,
         |    "index": 0,
         |    "globalIndex": 19584094,
         |    "creationHeight": 803617,
         |    "settlementHeight": 803620,
         |    "ergoTree": "19fe031808cd03f563854608e2e9e5f406c3f4d6a8bd0b619ac4b911ba53bc6921cf9d903725cc04000580a8d6b907040404060402040004000e20b6b38cae74e4754ae70a7c4335a9150449e47cb7421394016ab73c5c22a1a9dc0e2094180232cc0d91447178a0a995e2c14c57fbf03b06d5d87d5f79226094f52ffc05c8e0b3e20e05e4d7bad197bce00a058080a0f6f4acdbe01b040404ca0f06010104d00f0580a8d6b90704ca0f0e691005040004000e36100204a00b08cd0279be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798ea02d192a39a8cc7a701730073011001020402d19683030193a38cc7b2a57300000193c2b2a57301007473027303830108cdeeac93b1a5730405000500058092f4010100d803d6017300d602b2a4730100d6037302eb027201d195ed93b1a4730393b1db630872027304d804d604db63087202d605b2a5730500d606b2db63087205730600d6077e8c72060206edededededed938cb2720473070001730893c27205d07201938c72060173099272077e730a06927ec172050699997ec1a7069d9c72077e730b067e730c067e720306909c9c7e8cb27204730d0002067e7203067e730e069c9a7207730f9a9c7ec17202067e7310067e9c73117e7312050690b0ada5d90108639593c272087313c1720873147315d90108599a8c7208018c72080273167317",
         |    "address": "4jDxre1SHe1NYCLCsrhnsnapp6yjTExscAKAV2gnmbgqD6L4i1SJN1LDWCWJQPWBFb2gGo2NXV1i2tH1b18158sohUWjfUCHgjwdpERXb9qnKUafKXaLZavb2SSQNcpuiTNuxsxy5c4nmFZ21T6fDB7k3HWVZj8ngoGc81eGFFyXF3yBKg33zNwd5N1SuNGM8dEjWQfEaQijJvmME9bdkK63KrufzUH3pTmyg2DirRPyCjAh53mTsQgVAuvuRxRAh1bu7euTzuMREopY4eJYRsx1YAz4ntZtDDe67x6nZk5qMemJdgvSvepTTFJoax6ok6GgRs5XeBWDCrJ191mqLP17grtH9hgu2DQD7FHCsxdu31hqmQ8869ET9H9h9L9oV4dA6HaHbuyPAJ47SgZeWSsqKkgDyfMZXm5E87o7aTLSMbvMJwZFzDNofwYSKJrbFbHQp95wAonyxxLWNfg7R2H3prmyCUApdQf9LmEQNsx9NQnFZFg7bEtGGkF8ZUyb1PK9X81kbw1wRBHB4vwQMDRbPuRqMNYWVmxtToc91CoiCv25EAvPKSoKKmNrANoXX6GGYsGYRQgUoRkVsZyTsEywzCiyc9fTmiX4G4YWuW5swyvz9eQRAVrCGrnQTQRmGjkXeRgcTbxY1uoEKyeqrEtLms5uM9XBSuAaeE8yHP6YZALcA995P9BKHBEvNRSR3QQ",
         |    "assets": [],
         |    "additionalRegisters": {}
         |}
         |""".stripMargin
    ).toOption.get

    val swapN2TSellOrder: SwapV1 = SwapV1(
      swapN2TSell,
      PoolId.unsafeFromString("b6b38cae74e4754ae70a7c4335a9150449e47cb7421394016ab73c5c22a1a9dc"),
      PublicKeyRedeemer(PubKey.unsafeFromString("03f563854608e2e9e5f406c3f4d6a8bd0b619ac4b911ba53bc6921cf9d903725cc")),
      SwapParams(
        AssetAmount.native(1000000000),
        AssetAmount(
          TokenId.unsafeFromString("94180232cc0d91447178a0a995e2c14c57fbf03b06d5d87d5f79226094f52ffc"),
          1982232612
        ),
        3026889964213746L,
        1000000000000000000L
      ),
      2000000,
      Version.V1
    )

    val swapN2TBuy: Output = decode[Output](
      s"""
         |{
         |    "boxId": "b2fc29c14555f803e6b1d30fc9a01fb7a60389abed9037575cf51db5da6276a4",
         |    "transactionId": "b7dbf4c112a9856b9452e755b1efa63898849a97cb37dc5100dfd39ba5028340",
         |    "blockId": "730e81329ca04fb6a156deb354372b9916acd7aa33d075c15e441332f4763e43",
         |    "value": 7260000,
         |    "index": 0,
         |    "globalIndex": 19584534,
         |    "creationHeight": 803622,
         |    "settlementHeight": 803625,
         |    "ergoTree": "19b5031508cd03a9be85b303314dc03101d332a643bd60fdab055b9187200b5c29d11c23508e0f0400040404060402058080a0f6f4acdbe01b05e88f9ece9694f7d21b040004000e209916d75132593c8b07fe18bd8d583bda1652eed7565cf41a4738ddd90fc992ec05e6e0f8c20b04c60f060101040404d00f04c60f0e691005040004000e36100204a00b08cd0279be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798ea02d192a39a8cc7a701730073011001020402d19683030193a38cc7b2a57300000193c2b2a57301007473027303830108cdeeac93b1a5730405000500058092f4010100d802d6017300d602b2a4730100eb027201d195ed93b1a4730293b1db630872027303d804d603db63087202d604b2a5730400d6059d9c7e99c17204c1a7067e7305067e730606d6068cb2db6308a773070002edededed938cb2720373080001730993c27204d072019272057e730a06909c9c7ec17202067e7206067e730b069c9a7205730c9a9c7e8cb27203730d0002067e730e067e9c72067e730f050690b0ada5d90107639593c272077310c1720773117312d90107599a8c7207018c72070273137314",
         |    "address": "Dxg1z3EWagB2DFmZDZC4YutyYoCSfsVZGSfPEWkubkd9UfNimrnxoR5PZ2mtiHWMoGev4BXFE8VdnRxr9ZKwPymCyzkdsYbtoKYJgSTiLTPHMbUz33R9kV7hbA4HhHWS2ax5fmJbK4da1Gy29Kqvu5AZvFqnVtKyHhx6qeTAK8WsTgxTo22bW7HnbseJ8Lp9Z81fJyoLvhCqxfkDeMSyQePVhcTwMnpBfRVR2LmYqXbPruD7GwXk2rf1g9p2AyoMhgUNwbUYn7t2tWPdft1DJA4YQaMvJ1iF3ShRQqHns98CHogv2XCeSw8Htg7tqx7sbBverPNzuhH81G34nLhJaiuEtC7DB7u8KsyDkaAAy63TyJZPjTdvzEf82smio3rg1sDGDficGaojV9vKPi1zrPhF2siZUKFTTeWEQ8fi7ZW28Ykf3J4YffecEEXXKX5eZ4st3VKKiEQmSjCXB2fPGh1Po9HrhzDgeHRWgCkXDdLbnS2UW63eZHqnUE5bkyXWmA6MXxfakwBLfbTdw2YM7m7H4ALyoDmP3bQ4hcTUF1XEjyJ146TCwBcEnnZ1USf9uVqUgW2LLAxDMKjJfSt98md2jYZQhKE",
         |    "assets": [
         |        {
         |            "tokenId": "03faf2cb329f2e90d6d23b58d91bbb6c046aa143261cc21f52fbe2824bfcbf04",
         |            "index": 0,
         |            "amount": 317,
         |            "name": "SigUSD",
         |            "decimals": 2,
         |            "type": "EIP-004"
         |        }
         |    ],
         |    "additionalRegisters": {},
         |    "spentTransactionId": "6408b73f5088c05106ddf2349363665038c1662fd5e06f2058b1ef89a93a28fd",
         |    "mainChain": true
         |}
         |""".stripMargin
    ).toOption.get

    val swapN2TBuyOrder: Swap = SwapV1(
      swapN2TBuy,
      PoolId.unsafeFromString("9916d75132593c8b07fe18bd8d583bda1652eed7565cf41a4738ddd90fc992ec"),
      PublicKeyRedeemer(PubKey.unsafeFromString("03a9be85b303314dc03101d332a643bd60fdab055b9187200b5c29d11c23508e0f")),
      SwapParams(
        AssetAmount(
          TokenId.unsafeFromString("03faf2cb329f2e90d6d23b58d91bbb6c046aa143261cc21f52fbe2824bfcbf04"),
          317
        ),
        AssetAmount.native(1546590259),
        3879501998078988L,
        1000000000000000000L
      ),
      2000000,
      Version.V1
    )
  }

  object deposit {

    val depositN2T: Output = decode[Output](
      """
        |{
        |    "boxId": "0014655fd62c0668e3646c49833cd02df630959b7b1390d36d396e5cd1960636",
        |    "transactionId": "f4f378be4978bc8b99f8feff7669d12f89ceb447535df3812af683853c6cbecb",
        |    "blockId": "8d7dc915bed488779ca54c8cef980344835f571466f119f5f2188cf87f1f0068",
        |    "value": 745118814,
        |    "index": 0,
        |    "globalIndex": 11871821,
        |    "creationHeight": 506880,
        |    "settlementHeight": 661647,
        |    "ergoTree": "19bd041808cd0295d0031c6b506aaeb1d146b21e1e19059a7a4392aceba9aaf045a85aebe9a21b040005fc91e9c005040404060402040205feffffffffffffffff0104040400040004000e201d5afc59838920bb5ef2a8f9d63825a55b1d48e269d7cecee335d637c3ff5f3f040404020580b6dc0505fc91e9c0050580b6dc0501000e691005040004000e36100204a00b08cd0279be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798ea02d192a39a8cc7a701730073011001020402d19683030193a38cc7b2a57300000193c2b2a57301007473027303830108cdeeac93b1a5730405000500058092f4010100d803d6017300d602b2a4730100d6037302eb027201d195ed93b1a4730393b1db630872027304d80bd604db63087202d605b2a5730500d606b27204730600d6077e9973078c72060206d6087ec1720206d6099d9c7e72030672077208d60ab27204730800d60b7e8c720a0206d60c9d9c7e8cb2db6308a773090002067207720bd60ddb63087205d60eb2720d730a00ededededed938cb27204730b0001730c93c27205d0720195ed8f7209720c93b1720d730dd801d60fb2720d730e00eded92c172059999c1a7730f7310938c720f018c720a01927e8c720f02069d9c99720c7209720b720795927209720c927ec1720506997e99c1a7731106997e7203069d9c997209720c720872077312938c720e018c720601927e8c720e0206a17209720c90b0ada5d9010f639593c2720f7313c1720f73147315d9010f599a8c720f018c720f0273167317",
        |    "address": "5KevXenwFgBWY8DXPxvBn61PpLJj3T9uhx5h89T6bfvHscrJSoci5QMvYFVEUGvhXWRgUo5as8qxgxBYg53Ut7CrL8YyDnqndYVdfAsv5wgJPugxtxQuRf9cPy8jNtFErYaL2fiuTFm2Ctgsrh2xdwHn2cTW9WmV3XW6nTsUdmyTKUfhHQkseDGGxdSS8HH9F5A15XuEAZaFdze215DU5E8GiaYYktt6U2GR51A84qN6Egw8JX3TT7uYYLQcYghf5sxqPcP6qC4XXTkqpUfQGWDdABDxBRennJMye3QVGW5m2YBUNgLK333hSSyHBpJweodDta2mv11xMCXzon2eZZ3bT4GN8YHrKie95EVJ194KQkc32E53ngFmY4aGgkCYeqQLiFK4Qt3Tg7QXCbGmjx6cRiY6xyzLH7BrshXx7nUw1m1kQ6rd1KCRccappKgT9Hm8AoYBAksKXpr1A1yVcqP8W7wyFk1wshyW9Mij2YHufbm2A4fBkGsh6LJaTpafoVxNn4ezXQWocuQK6iZXe8CG5LTKKxshrxGMRu4ibU4L4tUBXL3sZQy5cvQ9HgcKY78ypDEZ5Hjcoq4goQG8rwvfdAkdSk7JaDXTYhJPeA1Nfob8RVyQSDoH7PQArUzJPBPf2F3JecGkf52WJCGDPi8oYQ2CTJDEb3pgVzdhLHt1NHqQNdb64RBTxe8SVPAwQo3aFw5ZsLnGk2hDHi9T6MtbKxzTy6SeddBKMbsLJ6VWDzgWVPbCJ48vk3rLNJXy8yGjFQhgW2seM4gp2ituAQ4wK",
        |    "assets": [
        |        {
        |            "tokenId": "003bd19d0187117f130b62e1bcab0939929ff5c7709f843c5c4dd158949285d0",
        |            "index": 0,
        |            "amount": 935,
        |            "name": "SigRSV",
        |            "decimals": 0,
        |            "type": "EIP-004"
        |        }
        |    ],
        |    "additionalRegisters": {},
        |    "spentTransactionId": "325f0dc5c094afe51a8324c0652476417c7e6f088e9261b9477fe23727da1fc2",
        |    "mainChain": true
        |}
        |""".stripMargin
    ).toOption.get

    val expectedN2TDepositV1: AmmDepositV1 = AmmDepositV1(
      depositN2T,
      ERG(6000000),
      PoolId.unsafeFromString("1d5afc59838920bb5ef2a8f9d63825a55b1d48e269d7cecee335d637c3ff5f3f"),
      PublicKeyRedeemer(PubKey.unsafeFromString("0295d0031c6b506aaeb1d146b21e1e19059a7a4392aceba9aaf045a85aebe9a21b")),
      AmmDepositParams(
        AssetAmount.native(739058814),
        AssetAmount(
          TokenId.unsafeFromString("003bd19d0187117f130b62e1bcab0939929ff5c7709f843c5c4dd158949285d0"),
          935
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
        |    "boxId": "7aa665f23148a5914b1b8403377dd05bfc3d6f850e8be755d749efae1667c8ec",
        |    "transactionId": "bdb03cf07027fb906a80f0d5f646cf6822ec11b50cb2f6dcfa135cad544b7fb7",
        |    "blockId": "5c164e1f384146694d135772c19e431eacb5cc41113296a84fc5c8b41adcf801",
        |    "value": 7060000,
        |    "index": 0,
        |    "globalIndex": 11476531,
        |    "creationHeight": 506880,
        |    "settlementHeight": 653156,
        |    "ergoTree": "19b1031208cd029c27bdc7a3d17ac258d918f23227172e70f2495cdd9a41d4d06f7d83f34b5935040004040406040204000404040005feffffffffffffffff01040204000e201d5afc59838920bb5ef2a8f9d63825a55b1d48e269d7cecee335d637c3ff5f3f0580bfd6060e691005040004000e36100204a00b08cd0279be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798ea02d192a39a8cc7a701730073011001020402d19683030193a38cc7b2a57300000193c2b2a57301007473027303830108cdeeac93b1a5730405000500058092f4010100d802d6017300d602b2a4730100eb027201d195ed93b1a4730293b1db630872027303d806d603db63087202d604b2a5730400d605b2db63087204730500d606b27203730600d6077e8cb2db6308a77307000206d6087e9973088cb272037309000206ededededed938cb27203730a0001730b93c27204d07201938c7205018c720601927e9a99c17204c1a7730c069d9c72077ec17202067208927e8c720502069d9c72077e8c72060206720890b0ada5d90109639593c27209730dc17209730e730fd90109599a8c7209018c72090273107311",
        |    "address": "2ysN6BW5GJ6BvfNAqeXzkHUsou1CCoUZifMuY8siXbM2dAtmaBoQSwqjh3YwNqDL6H5DPBF6MNKSNvNXCXYB3JGBsTZLesrq21ZGB2J9S1AcGLHjSd9saqRrsoP9JDX5GjPxLfx1ainRYQYBLxaSeUoVkNeuvh1MzhRYBZiCTaZyC6FiJm9nygxufmgswxfu6yiX96bKwjMt3r5PHeGcvhSvExaHbDvTvd1nTYGfLz34P289yqqR4RshjMFE383rVDE6XhNiCYgWMohNnJbjP7zX4cNZveLJrjLPUvBksLGtzPASRcDWCmPAMaWDUJuTCcZNKSsnLW7hvEHuL4EzKk8LDpXZwU6USqxv68YtfScg78xHwATFJpkeg2jTfDHaEDVCxmGFUJEs6U11APcikebo6rcW36wJ23HYgQt4CBPkUjnXAHL5f25knv2DefHn2cNF2ziprLD3TZBJ1xLCBHo94AVRyC4o7RsdzX27chth215bVKG47E4PbyeT5CSkK85oFPPdd2TmCyGpE4DdWzLQ9W2gLAtmJwuyV3NeK1U7bTHA7aW9QJtXddtBaXDoH7tjTsTxHn5v4SmwgwxggYvwTZ",
        |    "assets": [
        |        {
        |            "tokenId": "fa6326a26334f5e933b96470b53b45083374f71912b0d7597f00c2c7ebeb5da6",
        |            "index": 0,
        |            "amount": 326116969,
        |            "name": null,
        |            "decimals": null,
        |            "type": null
        |        }
        |    ],
        |    "additionalRegisters": {},
        |    "spentTransactionId": "81864cfc69aaced4538799d12b4f28c847ce8773e1ecf8d4cc5e97ca0c7ddb33",
        |    "mainChain": true
        |}
        |""".stripMargin
    ).toOption.get

    val redeem: RedeemV1 =
      RedeemV1(
        output,
        ERG(7000000),
        PoolId.unsafeFromString("1d5afc59838920bb5ef2a8f9d63825a55b1d48e269d7cecee335d637c3ff5f3f"),
        PublicKeyRedeemer(
          PubKey.unsafeFromString("029c27bdc7a3d17ac258d918f23227172e70f2495cdd9a41d4d06f7d83f34b5935")
        ),
        RedeemParams(
          AssetAmount(
            TokenId.unsafeFromString("fa6326a26334f5e933b96470b53b45083374f71912b0d7597f00c2c7ebeb5da6"),
            326116969
          )
        ),
        2000000,
        Version.V1
      )

  }

}
